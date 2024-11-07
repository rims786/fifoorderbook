package com.scila.codetest.fifoorderbook.api;

import com.scila.codetest.fifoorderbook.enums.OrderOperation;
import com.scila.codetest.fifoorderbook.enums.OrderSide;

import java.util.logging.Logger;

/**
 * Represents an order in the order book system.
 * This interface defines the structure and behavior of an order.
 */
public interface Order {
    Logger LOGGER = Logger.getLogger(Order.class.getName());

    /**
     * Gets the timestamp when the order was created.
     *
     * @return The timestamp in milliseconds.
     */
    long timestampMs();

    /**
     * Gets the unique identifier of the order.
     * This ID is used to track the order lifecycle (e.g., add - update - cancel).
     *
     * @return The order ID as a String.
     */
    String orderId();

    /**
     * Gets the price at which the order will match.
     *
     * @return The price of the order.
     */
    double price();

    /**
     * Gets the initial volume of the order at insertion.
     *
     * @return The initial volume of the order.
     */
    double initialVolume();

    /**
     * Gets the current volume of the order after matching.
     *
     * @return The current volume of the order.
     */
    double currentVolume();

    /**
     * Gets the side of the order (bid or ask).
     *
     * @return The OrderSide enum value.
     */
    OrderSide orderSide();

    /**
     * Gets the operation type of the order (add, update, or cancel).
     *
     * @return The OrderOperation enum value.
     */
    OrderOperation orderOperation();

    /**
     * Logs the details of the order.
     * This default method can be used to log order information consistently across implementations.
     */
    default void logOrderDetails() {
        LOGGER.info(String.format("Order Details - ID: %s, Price: %.2f, Initial Volume: %.2f, Current Volume: %.2f, Side: %s, Operation: %s",
                orderId(), price(), initialVolume(), currentVolume(), orderSide(), orderOperation()));
    }
}

