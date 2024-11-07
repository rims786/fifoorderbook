package com.scila.codetest.fifoorderbook;

import com.scila.codetest.fifoorderbook.api.Order;
import com.scila.codetest.fifoorderbook.api.Orderbook;
import com.scila.codetest.fifoorderbook.enums.OrderSide;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Logger;

/**
 * Implementation of the OrderBook interface using a FIFO (First-In-First-Out) approach.
 * This class manages orders for both BID and ASK sides of the order book.
 */
public class FifoOrderbook implements Orderbook {
    private static final Logger LOGGER = Logger.getLogger(FifoOrderbook.class.getName());

    private final String orderBookId;
    private final Map<OrderSide, ConcurrentSkipListMap<Double, LinkedList<Order>>> orderBook;
    private final Map<String, Order> orderMap;

    /**
     * Initializes the FifoOrderbook with
     * a given ID and creates empty ConcurrentHashMaps
     * for orderBook and orderMap.
     *
     * @param orderBookId The unique identifier for this order book.
     */
    public FifoOrderbook(String orderBookId) {

        this.orderBookId = orderBookId;
        this.orderBook = new EnumMap<>(OrderSide.class);
        this.orderBook.put(OrderSide.BID, new ConcurrentSkipListMap<>(Collections.reverseOrder()));
        this.orderBook.put(OrderSide.ASK, new ConcurrentSkipListMap<>());
        this.orderMap = new ConcurrentHashMap<>();
        LOGGER.info("FifoOrderbook created with ID: " + orderBookId);
    }

    /**
     * Updates the order book with the given order.
     * Entry point for all order operations.
     * Validates the order and calls the appropriate method based on the order operation.
     *
     * @param order The order to be processed.
     */
    @Override
    public synchronized void updateOrderBook(Order order) {
        // Entry point for all order operations.
        // Validates the order and calls the appropriate method based on the order operation.
        LOGGER.info("Updating order book: " + order);

        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        try {
            switch (order.orderOperation()) {
                case ADD:
                    addOrder(order);
                    break;
                case UPDATE:
                    updateOrder(order);
                    break;
                case CANCEL:
                    cancelOrder(order);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid order operation");
            }
        } catch (Exception e) {
            LOGGER.severe("Error processing order: " + e.getMessage());
            throw new RuntimeException("Failed to process order", e);
        }
    }

    /**
     * Adds a new order to both orderMap and orderBook,
     * creating new data structures if necessary.
     *
     * @param order The order to be added.
     */
    private void addOrder(Order order) {
        validateOrder(order);
        orderMap.put(order.orderId(), order);
        orderBook.get(order.orderSide())
                .computeIfAbsent(order.price(), k -> new LinkedList<>())
                .addLast(order);
        LOGGER.info("Order added: " + order.orderId());
    }

    /**
     * Updates an existing order by first cancelling it
     * and then adding the new version.
     *
     * @param newOrder The updated order.
     */
    private void updateOrder(Order newOrder) {
        validateOrder(newOrder);
        Order oldOrder = orderMap.get(newOrder.orderId());
        if (oldOrder == null) {
            throw new IllegalArgumentException("Order not found for update: " + newOrder.orderId());
        }
        cancelOrder(oldOrder);
        addOrder(newOrder);
        LOGGER.info("Order updated: " + newOrder.orderId());
    }

    /**
     * Cancels an existing order in the order book.
     * Removes an order from both orderMap and orderBook,
     * cleaning up empty price levels if necessary.
     *
     * @param order The order to be cancelled.
     */
    private void cancelOrder(Order order) {
        validateOrder(order);
        orderMap.remove(order.orderId());
        ConcurrentSkipListMap<Double, LinkedList<Order>> sideBook = orderBook.get(order.orderSide());
        LinkedList<Order> ordersAtPrice = sideBook.get(order.price());
        if (ordersAtPrice != null) {
            ordersAtPrice.removeIf(o -> o.orderId().equals(order.orderId()));
            if (ordersAtPrice.isEmpty()) {
                sideBook.remove(order.price());
            }
        }
        LOGGER.info("Order cancelled: " + order.orderId());
    }

    /**
     * Validates the order parameters.
     * Checks if the order is valid by ensuring it's not null,
     * has a non-empty ID, and has positive price and volume.
     *
     * @param order The order to be validated.
     */
    private void validateOrder(Order order) {
        if (order.price() <= 0 || order.initialVolume() <= 0 || order.currentVolume() < 0) {
            throw new IllegalArgumentException("Invalid order parameters");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOrderBookId() {
        return orderBookId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Order getOrder(String orderId) {
        Order order = orderMap.get(orderId);
        LOGGER.info("Retrieved order: " + orderId);
        return order;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double totalOrderVolume() {
        double total = totalBidVolume() + totalAskVolume();
        LOGGER.info("Total order volume: " + total);
        return total;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double totalBidVolume() {
        double total = calculateTotalVolume(OrderSide.BID);
        LOGGER.info("Total bid volume: " + total);
        return total;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double totalAskVolume() {
        double total = calculateTotalVolume(OrderSide.ASK);
        LOGGER.info("Total ask volume: " + total);
        return total;
    }

    /**
     * Calculates the total volume for a given side of the order book.
     *
     * @param side The side of the order book (BID or ASK).
     * @return The total volume for the given side.
     */
    private double calculateTotalVolume(OrderSide side) {
        return orderBook.get(side).values().stream()
                .flatMap(Collection::stream)
                .mapToDouble(Order::currentVolume)
                .sum();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getBestBidPrice() {
        double price = orderBook.get(OrderSide.BID).isEmpty() ? 0 : orderBook.get(OrderSide.BID).firstKey();
        LOGGER.info("Best bid price: " + price);
        return price;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getBestAskPrice() {
        double price = orderBook.get(OrderSide.ASK).isEmpty() ? 0 : orderBook.get(OrderSide.ASK).firstKey();
        LOGGER.info("Best ask price: " + price);
        return price;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long totalNumberOfBidOrders() {
        long count = countOrders(OrderSide.BID);
        LOGGER.info("Total number of bid orders: " + count);
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long totalNumberOfAskOrders() {
        long count = countOrders(OrderSide.ASK);
        LOGGER.info("Total number of ask orders: " + count);
        return count;
    }

    /**
     * Counts the number of orders for a given side of the order book.
     *
     * @param side The side of the order book (BID or ASK).
     * @return The number of orders for the given side.
     */
    private long countOrders(OrderSide side) {
        return orderBook.get(side).values().stream()
                .mapToLong(Collection::size)
                .sum();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long totalNumberOfActiveOrders() {
        long total = totalNumberOfBidOrders() + totalNumberOfAskOrders();
        LOGGER.info("Total number of active orders: " + total);
        return total;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double totalVolumeAtPriceLevel(int priceLevel) {
        double total = totalBidVolumeAtPriceLevel(priceLevel) + totalAskVolumeAtPriceLevel(priceLevel);
        LOGGER.info("Total volume at price level " + priceLevel + ": " + total);
        return total;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double totalBidVolumeAtPriceLevel(int priceLevel) {
        double volume = getVolumeAtPriceLevel(OrderSide.BID, priceLevel);
        LOGGER.info("Total bid volume at price level " + priceLevel + ": " + volume);
        return volume;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double totalAskVolumeAtPriceLevel(int priceLevel) {
        double volume = getVolumeAtPriceLevel(OrderSide.ASK, priceLevel);
        LOGGER.info("Total ask volume at price level " + priceLevel + ": " + volume);
        return volume;
    }

    /**
     * Gets the volume at a specific price level for a given side of the order book.
     *
     * @param side The side of the order book (BID or ASK).
     * @param priceLevel The price level to check.
     * @return The volume at the specified price level.
     */
    private double getVolumeAtPriceLevel(OrderSide side, int priceLevel) {
        if (priceLevel <= 0 || priceLevel > orderBook.get(side).size()) {
            return 0;
        }
        return orderBook.get(side).values().stream()
                .skip(priceLevel - 1)
                .findFirst()
                .map(orders -> orders.stream().mapToDouble(Order::currentVolume).sum())
                .orElse(0.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int totalNumberOfPriceLevels() {
        int total = totalNumberOfBidPriceLevels() + totalNumberOfAskPriceLevels();
        LOGGER.info("Total number of price levels: " + total);
        return total;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int totalNumberOfBidPriceLevels() {
        int levels = orderBook.get(OrderSide.BID).size();
        LOGGER.info("Total number of bid price levels: " + levels);
        return levels;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int totalNumberOfAskPriceLevels() {
        int levels = orderBook.get(OrderSide.ASK).size();
        LOGGER.info("Total number of ask price levels: " + levels);
        return levels;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getAskPriceAtPriceLevel(int priceLevel) {
        double price = getPriceAtPriceLevel(OrderSide.ASK, priceLevel);
        LOGGER.info("Ask price at price level " + priceLevel + ": " + price);
        return price;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getBidPriceAtPriceLevel(int priceLevel) {
        double price = getPriceAtPriceLevel(OrderSide.BID, priceLevel);
        LOGGER.info("Bid price at price level " + priceLevel + ": " + price);
        return price;
    }

    /**
     * Gets the price at a specific price level for a given side of the order book.
     *
     * @param side The side of the order book (BID or ASK).
     * @param priceLevel The price level to check.
     * @return The price at the specified price level.
     */
    private double getPriceAtPriceLevel(OrderSide side, int priceLevel) {
        if (priceLevel <= 0 || priceLevel > orderBook.get(side).size()) {
            return 0;
        }
        return orderBook.get(side).keySet().stream()
                .skip(priceLevel - 1)
                .findFirst()
                .orElse(0.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Order> getBestBidOrder() {
        Optional<Order> order = getBestOrder(OrderSide.BID);
        LOGGER.info("Best bid order: " + (order.isPresent() ? order.get().orderId() : "None"));
        return order;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Order> getBestAskOrder() {
        Optional<Order> order = getBestOrder(OrderSide.ASK);
        LOGGER.info("Best ask order: " + (order.isPresent() ? order.get().orderId() : "None"));
        return order;
    }

    /**
     * Gets the best order for a given side of the order book.
     *
     * @param side The side of the order book (BID or ASK).
     * @return An Optional containing the best order, or an empty Optional if no orders exist.
     */
    private Optional<Order> getBestOrder(OrderSide side) {
        return orderBook.get(side).values().stream()
                .findFirst()
                .flatMap(orders -> orders.stream().findFirst());
    }
}
