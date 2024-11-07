package com.scila.codetest.fifoorderbook;

import com.scila.codetest.fifoorderbook.api.Orderbook;
import com.scila.codetest.fifoorderbook.enums.OrderSide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.scila.codetest.fifoorderbook.FifoOrderbookTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.logging.Logger;

/**
 * Test class for FifoOrderbook implementation.
 * This class contains unit tests to verify the correct functionality of the FifoOrderbook class.
 */
class FifoOrderbookTest {
    private static final Logger LOGGER = Logger.getLogger(FifoOrderbookTest.class.getName());
    private Orderbook orderBook;

    /**
     * Set up method to initialize the orderBook before each test.
     */
    @BeforeEach
    void init() {
        this.orderBook = new FifoOrderbook("");
        LOGGER.info("Initialized new FifoOrderbook for testing");
    }

    /**
     * Test adding orders to the order book.
     */
    @Test
    void addOrderToOrderBook() {
        LOGGER.info("Starting addOrderToOrderBook test");
        orderBook.updateOrderBook(buildAddOrder("1", 100, 10, OrderSide.BID, 1));
        orderBook.updateOrderBook(buildAddOrder("2", 100, 10, OrderSide.BID, 2));
        orderBook.updateOrderBook(buildAddOrder("3", 101, 10, OrderSide.ASK, 3));

        assertEquals(3, orderBook.totalNumberOfActiveOrders());
        assertEquals(2, orderBook.totalNumberOfBidOrders());
        assertEquals(1, orderBook.totalNumberOfAskOrders());
        LOGGER.info("Completed addOrderToOrderBook test");
    }

    /**
     * Test updating an order in the order book.
     */
    @Test
    void updateOrder() {
        LOGGER.info("Starting updateOrder test");
        orderBook.updateOrderBook(buildAddOrder("1", 100, 10, OrderSide.ASK, 1));
        assertEquals(100, orderBook.getBestAskOrder().get().price());
        orderBook.updateOrderBook(buildUpdateOrder("1", 99, 10, 5, OrderSide.ASK, 2));
        assertEquals(99, orderBook.getBestAskOrder().get().price());
        assertEquals(1, orderBook.totalNumberOfAskPriceLevels());
        LOGGER.info("Completed updateOrder test");
    }

    /**
     * Test updating an order in the order book and verifying all related statistics.
     */
    @Test
    void updateOrderInOrderBook() {
        LOGGER.info("Starting updateOrderInOrderBook test");
        orderBook.updateOrderBook(buildAddOrder("1", 100, 10, OrderSide.BID, 1));
        assertEquals(1, orderBook.totalNumberOfPriceLevels());
        assertEquals(1, orderBook.totalNumberOfBidPriceLevels());
        assertEquals(0, orderBook.totalNumberOfAskPriceLevels());
        assertEquals(10, orderBook.getOrder("1").currentVolume());
        assertEquals(100, orderBook.getOrder("1").price());
        assertEquals(10, orderBook.totalBidVolume());
        assertEquals(0, orderBook.totalAskVolume());
        assertEquals(10, orderBook.totalOrderVolume());

        orderBook.updateOrderBook(buildUpdateOrder("1", 99, 10, 9, OrderSide.BID, 2));
        assertEquals(1, orderBook.totalNumberOfPriceLevels());
        assertEquals(1, orderBook.totalNumberOfBidPriceLevels());
        assertEquals(0, orderBook.totalNumberOfAskPriceLevels());
        assertEquals(9, orderBook.getOrder("1").currentVolume());
        assertEquals(99, orderBook.getOrder("1").price());
        assertEquals(9, orderBook.totalBidVolume());
        assertEquals(0, orderBook.totalAskVolume());
        assertEquals(9, orderBook.totalOrderVolume());
        LOGGER.info("Completed updateOrderInOrderBook test");
    }

    /**
     * Test the best price after adding multiple orders.
     */
    @Test
    void bestPriceAfterAddOrders() {
        LOGGER.info("Starting bestPriceAfterAddOrders test");
        orderBook.updateOrderBook(buildAddOrder("1", 100, 10, OrderSide.BID, 1));
        orderBook.updateOrderBook(buildAddOrder("2", 102, 10, OrderSide.ASK, 2));
        assertEquals(100, orderBook.getBestBidPrice());
        assertEquals(102, orderBook.getBestAskPrice());

        orderBook.updateOrderBook(buildAddOrder("3", 100.5, 10, OrderSide.BID, 3));
        orderBook.updateOrderBook(buildAddOrder("4", 103, 10, OrderSide.ASK, 3));
        assertEquals(100.5, orderBook.getBestBidPrice());
        assertEquals(102, orderBook.getBestAskPrice());
        LOGGER.info("Completed bestPriceAfterAddOrders test");
    }

    /**
     * Test cancelling orders in the order book.
     */
    @Test
    void cancelOrdersInOrderBook() {
        LOGGER.info("Starting cancelOrdersInOrderBook test");
        orderBook.updateOrderBook(buildAddOrder("1", 100, 10, OrderSide.BID, 1));
        orderBook.updateOrderBook(buildAddOrder("2", 100, 10, OrderSide.BID, 2));
        orderBook.updateOrderBook(buildAddOrder("3", 101, 10, OrderSide.ASK, 3));

        assertEquals(3, orderBook.totalNumberOfActiveOrders());
        assertEquals(2, orderBook.totalNumberOfBidOrders());
        assertEquals(1, orderBook.totalNumberOfAskOrders());

        orderBook.updateOrderBook(buildCancelOrder("1", 100, 10, OrderSide.BID, 4));
        orderBook.updateOrderBook(buildCancelOrder("3", 101, 10, OrderSide.ASK, 5));
        assertEquals(1, orderBook.totalNumberOfActiveOrders());
        assertEquals(1, orderBook.totalNumberOfBidOrders());
        assertEquals(0, orderBook.totalNumberOfAskOrders());
        LOGGER.info("Completed cancelOrdersInOrderBook test");
    }

    /**
     * Test the best price after cancelling orders.
     */
    @Test
    void bestPriceAfterCancelOrders() {
        LOGGER.info("Starting bestPriceAfterCancelOrders test");
        orderBook.updateOrderBook(buildAddOrder("1", 99, 10, OrderSide.BID, 1));
        orderBook.updateOrderBook(buildAddOrder("2", 100, 10, OrderSide.BID, 2));
        orderBook.updateOrderBook(buildAddOrder("3", 101, 10, OrderSide.ASK, 3));
        orderBook.updateOrderBook(buildAddOrder("4", 103, 10, OrderSide.ASK, 3));
        assertEquals(100, orderBook.getBestBidPrice());
        assertEquals(101, orderBook.getBestAskPrice());

        orderBook.updateOrderBook(buildCancelOrder("2", 100, 10, OrderSide.BID, 4));
        orderBook.updateOrderBook(buildCancelOrder("3", 101, 10, OrderSide.ASK, 5));
        assertEquals(99, orderBook.getBestBidPrice());
        assertEquals(103, orderBook.getBestAskPrice());
        LOGGER.info("Completed bestPriceAfterCancelOrders test");
    }

    /**
     * Test volume calculations in the order book.
     */
    @Test
    void volumeInOrderBook() {
        LOGGER.info("Starting volumeInOrderBook test");
        orderBook.updateOrderBook(buildAddOrder("1", 100, 11, OrderSide.BID, 1));
        orderBook.updateOrderBook(buildAddOrder("2", 102, 13, OrderSide.ASK, 2));
        orderBook.updateOrderBook(buildAddOrder("3", 99, 12, OrderSide.BID, 3));
        orderBook.updateOrderBook(buildAddOrder("4", 103, 10, OrderSide.ASK, 4));
        assertEquals(46, orderBook.totalOrderVolume());
        assertEquals(23, orderBook.totalBidVolume());
        assertEquals(23, orderBook.totalAskVolume());

        orderBook.updateOrderBook(buildCancelOrder("1", 100, 11, OrderSide.BID, 5));
        orderBook.updateOrderBook(buildCancelOrder("2", 102, 13, OrderSide.ASK, 6));
        assertEquals(22, orderBook.totalOrderVolume());
        assertEquals(12, orderBook.totalBidVolume());
        assertEquals(10, orderBook.totalAskVolume());
        LOGGER.info("Completed volumeInOrderBook test");
    }

    /**
     * Test volume calculations at specific price levels.
     */
    @Test
    void volumeAtPriceLevel() {
        LOGGER.info("Starting volumeAtPriceLevel test");
        orderBook.updateOrderBook(buildAddOrder("1", 100, 11, OrderSide.BID, 1));
        orderBook.updateOrderBook(buildAddOrder("2", 102, 13, OrderSide.ASK, 2));
        orderBook.updateOrderBook(buildAddOrder("3", 99, 12, OrderSide.BID, 3));
        orderBook.updateOrderBook(buildAddOrder("4", 103, 10, OrderSide.ASK, 4));
        orderBook.updateOrderBook(buildAddOrder("5", 100, 10, OrderSide.BID, 5));
        assertEquals(34, orderBook.totalVolumeAtPriceLevel(1));
        assertEquals(21, orderBook.totalBidVolumeAtPriceLevel(1));
        assertEquals(13, orderBook.totalAskVolumeAtPriceLevel(1));
        assertEquals(22, orderBook.totalVolumeAtPriceLevel(2));
        assertEquals(12, orderBook.totalBidVolumeAtPriceLevel(2));
        assertEquals(10, orderBook.totalAskVolumeAtPriceLevel(2));

        orderBook.updateOrderBook(buildCancelOrder("1", 100, 11, OrderSide.BID, 5));
        orderBook.updateOrderBook(buildCancelOrder("2", 102, 13, OrderSide.ASK, 6));
        assertEquals(20, orderBook.totalVolumeAtPriceLevel(1));
        assertEquals(10, orderBook.totalBidVolumeAtPriceLevel(1));
        assertEquals(10, orderBook.totalAskVolumeAtPriceLevel(1));
        assertEquals(12, orderBook.totalVolumeAtPriceLevel(2));
        assertEquals(12, orderBook.totalBidVolumeAtPriceLevel(2));
        assertEquals(0, orderBook.totalAskVolumeAtPriceLevel(2));
        LOGGER.info("Completed volumeAtPriceLevel test");
    }

    /**
     * Test price retrieval at specific price levels.
     */
    @Test
    void priceAtPriceLevel() {
        LOGGER.info("Starting priceAtPriceLevel test");
        orderBook.updateOrderBook(buildAddOrder("1", 100, 11, OrderSide.BID, 1));
        orderBook.updateOrderBook(buildAddOrder("2", 102, 13, OrderSide.ASK, 2));
        orderBook.updateOrderBook(buildAddOrder("3", 99, 12, OrderSide.BID, 3));
        orderBook.updateOrderBook(buildAddOrder("4", 103, 10, OrderSide.ASK, 4));
        orderBook.updateOrderBook(buildAddOrder("5", 100, 10, OrderSide.BID, 5));
        assertEquals(100, orderBook.getBidPriceAtPriceLevel(1));
        assertEquals(102, orderBook.getAskPriceAtPriceLevel(1));
        assertEquals(99, orderBook.getBidPriceAtPriceLevel(2));
        assertEquals(103, orderBook.getAskPriceAtPriceLevel(2));

        orderBook.updateOrderBook(buildCancelOrder("1", 100, 11, OrderSide.BID, 5));
        orderBook.updateOrderBook(buildCancelOrder("2", 102, 13, OrderSide.ASK, 6));
        assertEquals(100, orderBook.getBidPriceAtPriceLevel(1));
        assertEquals(103, orderBook.getAskPriceAtPriceLevel(1));
        assertEquals(99, orderBook.getBidPriceAtPriceLevel(2));
        assertEquals(0, orderBook.getAskPriceAtPriceLevel(2));
        LOGGER.info("Completed priceAtPriceLevel test");
    }

    /**
     * Test the First-In-First-Out (FIFO) principle of the order book.
     */
    @Test
    void firstInFirstOut() {
        LOGGER.info("Starting firstInFirstOut test");
        orderBook.updateOrderBook(buildAddOrder("0", 99, 11, OrderSide.BID, 1));
        orderBook.updateOrderBook(buildAddOrder("1", 100, 11, OrderSide.BID, 1));
        orderBook.updateOrderBook(buildAddOrder("2", 102, 13, OrderSide.ASK, 2));
        orderBook.updateOrderBook(buildAddOrder("3", 99, 12, OrderSide.BID, 3));
        orderBook.updateOrderBook(buildAddOrder("4", 101, 10, OrderSide.ASK, 4));
        orderBook.updateOrderBook(buildAddOrder("5", 100, 10, OrderSide.BID, 5));
        assertEquals("1", orderBook.getBestBidOrder().get().orderId());
        assertEquals("4", orderBook.getBestAskOrder().get().orderId());
        LOGGER.info("Completed firstInFirstOut test");
    }
}


