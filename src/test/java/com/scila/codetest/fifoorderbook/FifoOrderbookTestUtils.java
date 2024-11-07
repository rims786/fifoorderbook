package com.scila.codetest.fifoorderbook;

import com.scila.codetest.fifoorderbook.api.Order;
import com.scila.codetest.fifoorderbook.enums.OrderOperation;
import com.scila.codetest.fifoorderbook.enums.OrderSide;
import com.scila.codetest.fifoorderbook.model.SimpleOrder;

import java.util.logging.Logger;

/**
 * Utility class for creating test orders in the FifoOrderBook test suite.
 * This class provides static methods to build various types of orders for testing purposes.
 */
public class FifoOrderbookTestUtils {

    private static final Logger LOGGER = Logger.getLogger(FifoOrderbookTestUtils.class.getName());

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private FifoOrderbookTestUtils() {
        throw new AssertionError("FifoOrderbookTestUtils is a utility class and should not be instantiated");
    }

    /**
     * Builds an ADD order for testing purposes.
     *
     * @param orderId     The unique identifier for the order.
     * @param price       The price of the order.
     * @param volume      The volume of the order.
     * @param orderSide   The side of the order (BID or ASK).
     * @param timestampMs The timestamp of the order in milliseconds.
     * @return A new Order object representing an ADD operation.
     */
    public static Order buildAddOrder(String orderId, double price, double volume, OrderSide orderSide, long timestampMs) {
        Order order = new SimpleOrder(timestampMs, orderId, price, volume, volume, orderSide, OrderOperation.ADD);
        LOGGER.info("Built ADD order: " + order);
        return order;
    }

    /**
     * Builds an UPDATE order for testing purposes.
     *
     * @param orderId        The unique identifier for the order.
     * @param price          The updated price of the order.
     * @param initialVolume  The initial volume of the order.
     * @param volume         The updated volume of the order.
     * @param orderSide      The side of the order (BID or ASK).
     * @param timestampMs    The timestamp of the order in milliseconds.
     * @return A new Order object representing an UPDATE operation.
     */
    public static Order buildUpdateOrder(String orderId, double price, double initialVolume, double volume, OrderSide orderSide, long timestampMs) {
        Order order = new SimpleOrder(timestampMs, orderId, price, initialVolume, volume, orderSide, OrderOperation.UPDATE);
        LOGGER.info("Built UPDATE order: " + order);
        return order;
    }

    /**
     * Builds a CANCEL order for testing purposes.
     *
     * @param orderId     The unique identifier for the order to be cancelled.
     * @param price       The price of the order to be cancelled.
     * @param volume      The volume of the order to be cancelled.
     * @param orderSide   The side of the order (BID or ASK).
     * @param timestampMs The timestamp of the cancellation in milliseconds.
     * @return A new Order object representing a CANCEL operation.
     */
    public static Order buildCancelOrder(String orderId, double price, double volume, OrderSide orderSide, long timestampMs) {
        Order order = new SimpleOrder(timestampMs, orderId, price, volume, volume, orderSide, OrderOperation.CANCEL);
        LOGGER.info("Built CANCEL order: " + order);
        return order;
    }
}
