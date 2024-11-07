package com.scila.codetest.fifoorderbook.api;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * Represents an order book in the trading system.
 * This interface defines the structure and behavior of an order book.
 */
public interface Orderbook {
    Logger LOGGER = Logger.getLogger(Orderbook.class.getName());

    /**
     * Updates the order book with the given order.
     * This method handles add, update, and cancel operations.
     *
     * @param order The order to update the order book with.
     */
    void updateOrderBook(Order order);

    /**
     * Gets the unique ID of the order book.
     *
     * @return The order book ID as a String.
     */
    String getOrderBookId();

    /**
     * Retrieves an order from the order book based on its ID.
     *
     * @param orderId The ID of the order to retrieve.
     * @return The Order object if found, null otherwise.
     */
    Order getOrder(String orderId);

    /**
     * Calculates the total volume in the order book, aggregating all volume on both bid and ask sides.
     *
     * @return The total order volume.
     */
    double totalOrderVolume();

    /**
     * Calculates the total bid volume in the order book, aggregating all volume in each bid price level.
     *
     * @return The total bid volume.
     */
    double totalBidVolume();

    /**
     * Calculates the total ask volume in the order book, aggregating all volume in each ask price level.
     *
     * @return The total ask volume.
     */
    double totalAskVolume();

    /**
     * Gets the highest bid price in the order book.
     *
     * @return The best bid price, or 0 if no bids exist.
     */
    double getBestBidPrice();

    /**
     * Gets the lowest ask price in the order book.
     *
     * @return The best ask price, or 0 if no asks exist.
     */
    double getBestAskPrice();

    /**
     * Counts the total number of bid orders over all price levels in the order book.
     *
     * @return The total number of bid orders.
     */
    long totalNumberOfBidOrders();

    /**
     * Counts the total number of ask orders over all price levels in the order book.
     *
     * @return The total number of ask orders.
     */
    long totalNumberOfAskOrders();

    /**
     * Counts the total number of orders over all price levels in the order book, aggregating both bid and ask.
     *
     * @return The total number of active orders.
     */
    long totalNumberOfActiveOrders();

    /**
     * Calculates the total volume at a specific price level, aggregating bid and ask.
     *
     * @param priceLevel The price level to check.
     * @return The total volume at the specified price level.
     */
    double totalVolumeAtPriceLevel(int priceLevel);

    /**
     * Calculates the total bid volume at a specific price level.
     *
     * @param priceLevel The price level to check.
     * @return The total bid volume at the specified price level, or 0 if no volume exists.
     */
    double totalBidVolumeAtPriceLevel(int priceLevel);

    /**
     * Calculates the total ask volume at a specific price level.
     *
     * @param priceLevel The price level to check.
     * @return The total ask volume at the specified price level, or 0 if no volume exists.
     */
    double totalAskVolumeAtPriceLevel(int priceLevel);

    /**
     * Counts the total number of price levels, aggregating bid and ask.
     *
     * @return The total number of price levels.
     */
    int totalNumberOfPriceLevels();

    /**
     * Counts the total number of bid price levels.
     *
     * @return The total number of bid price levels.
     */
    int totalNumberOfBidPriceLevels();

    /**
     * Counts the total number of ask price levels.
     *
     * @return The total number of ask price levels.
     */
    int totalNumberOfAskPriceLevels();

    /**
     * Gets the ask price at a specific price level.
     *
     * @param priceLevel The price level to check.
     * @return The ask price at the specified price level, or 0 if no price exists.
     */
    double getAskPriceAtPriceLevel(int priceLevel);

    /**
     * Gets the bid price at a specific price level.
     *
     * @param priceLevel The price level to check.
     * @return The bid price at the specified price level, or 0 if no price exists.
     */
    double getBidPriceAtPriceLevel(int priceLevel);

    /**
     * Gets the best bid order.
     *
     * @return An Optional containing the best bid Order, or an empty Optional if no bid orders exist.
     */
    Optional<Order> getBestBidOrder();

    /**
     * Gets the best ask order.
     *
     * @return An Optional containing the best ask Order, or an empty Optional if no ask orders exist.
     */
    Optional<Order> getBestAskOrder();

    /**
     * Logs the current state of the order book.
     * This default method can be used to log order book information consistently across implementations.
     */
    default void logOrderBookState() {
        LOGGER.info(String.format("Order Book State - ID: %s, Total Orders: %d, Bid Orders: %d, Ask Orders: %d, Best Bid: %.2f, Best Ask: %.2f",
                getOrderBookId(), totalNumberOfActiveOrders(), totalNumberOfBidOrders(), totalNumberOfAskOrders(),
                getBestBidPrice(), getBestAskPrice()));
    }
}