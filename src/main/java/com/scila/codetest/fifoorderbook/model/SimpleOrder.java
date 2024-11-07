package com.scila.codetest.fifoorderbook.model;

import com.scila.codetest.fifoorderbook.api.Order;
import com.scila.codetest.fifoorderbook.enums.OrderOperation;
import com.scila.codetest.fifoorderbook.enums.OrderSide;

import java.util.logging.Logger;

/**
 * Represents a simple order in the order book system.
 * This record implements the Order interface and provides a basic structure for order information.
 */
public record SimpleOrder(
        long timestampMs,
        String orderId,
        double price,
        double initialVolume,
        double currentVolume,
        OrderSide orderSide,
        OrderOperation orderOperation
) implements Order {

    private static final Logger LOGGER = Logger.getLogger(SimpleOrder.class.getName());

    /**
     * Constructs a new SimpleOrder with the given parameters.
     * This constructor also logs the creation of the order.
     *
     * @param timestampMs    The timestamp of the order in milliseconds.
     * @param orderId        The unique identifier for the order.
     * @param price          The price of the order.
     * @param initialVolume  The initial volume of the order.
     * @param currentVolume  The current volume of the order.
     * @param orderSide      The side of the order (BID or ASK).
     * @param orderOperation The operation type of the order (ADD, UPDATE, or CANCEL).
     */
    public SimpleOrder {
        // Validate input parameters
        if (timestampMs <= 0) {
            throw new IllegalArgumentException("Timestamp must be positive");
        }
        if (orderId == null || orderId.isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        if (initialVolume <= 0) {
            throw new IllegalArgumentException("Initial volume must be positive");
        }
        if (currentVolume < 0) {
            throw new IllegalArgumentException("Current volume cannot be negative");
        }
        if (orderSide == null) {
            throw new IllegalArgumentException("Order side cannot be null");
        }
        if (orderOperation == null) {
            throw new IllegalArgumentException("Order operation cannot be null");
        }

        LOGGER.info("Created SimpleOrder: " + this.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logOrderDetails() {
        LOGGER.info(String.format("SimpleOrder Details - ID: %s, Price: %.2f, Initial Volume: %.2f, Current Volume: %.2f, Side: %s, Operation: %s",
                orderId, price, initialVolume, currentVolume, orderSide, orderOperation));
    }

    /**
     * Returns a string representation of the SimpleOrder.
     *
     * @return A string containing all the order details.
     */
    @Override
    public String toString() {
        return "SimpleOrder{" +
                "timestampMs=" + timestampMs +
                ", orderId='" + orderId + '\'' +
                ", price=" + price +
                ", initialVolume=" + initialVolume +
                ", currentVolume=" + currentVolume +
                ", orderSide=" + orderSide +
                ", orderOperation=" + orderOperation +
                '}';
    }
}
