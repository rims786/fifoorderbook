package com.scila.codetest.fifoorderbook.enums;

import java.util.logging.Logger;

/**
 * Represents the types of operations that can be performed on an order in the order book system.
 */
public enum OrderOperation {
    /**
     * Represents the operation of adding a new order to the order book.
     */
    ADD,

    /**
     * Represents the operation of updating an existing order in the order book.
     */
    UPDATE,

    /**
     * Represents the operation of cancelling an existing order in the order book.
     */
    CANCEL;

    private static final Logger LOGGER = Logger.getLogger(OrderOperation.class.getName());

    /**
     * Returns the string representation of the order operation.
     *
     * @return A string representation of the order operation.
     */
    @Override
    public String toString() {
        String operationName = name().toLowerCase();
        LOGGER.fine("OrderOperation toString called: " + operationName);
        return operationName;
    }

    /**
     * Logs the use of this OrderOperation.
     */
    public void logUse() {
        LOGGER.info("OrderOperation used: " + this.name());
    }
}
