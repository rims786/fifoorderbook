package com.scila.codetest.fifoorderbook.enums;

import java.util.logging.Logger;

/**
 * Represents the side of an order in the order book system.
 */
public enum OrderSide {
    /**
     * Represents a bid order (buy order).
     */
    BID,

    /**
     * Represents an ask order (sell order).
     */
    ASK;

    private static final Logger LOGGER = Logger.getLogger(OrderSide.class.getName());

    /**
     * Returns the string representation of the order side.
     *
     * @return A string representation of the order side.
     */
    @Override
    public String toString() {
        String sideName = name().toLowerCase();
        LOGGER.fine("OrderSide toString called: " + sideName);
        return sideName;
    }

    /**
     * Logs the use of this OrderSide.
     */
    public void logUse() {
        LOGGER.info("OrderSide used: " + this.name());
    }
}
