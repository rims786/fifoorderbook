package com.scila.codetest.fifoorderbook.exception;

import java.util.logging.Logger;

/**
 * Custom exception class for order book related errors.
 * This exception is thrown when there are issues with order processing or management.
 */
public class OrderBookException extends RuntimeException {

    private static final Logger LOGGER = Logger.getLogger(OrderBookException.class.getName());

    /**
     * Constructs a new OrderBookException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public OrderBookException(String message) {
        super(message);
        logException(message);
    }

    /**
     * Constructs a new OrderBookException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     * @param cause the cause (which is saved for later retrieval by the getCause() method)
     */
    public OrderBookException(String message, Throwable cause) {
        super(message, cause);
        logException(message, cause);
    }

    /**
     * Logs the exception message.
     *
     * @param message the detail message to be logged
     */
    private void logException(String message) {
        LOGGER.severe("OrderBookException occurred: " + message);
    }

    /**
     * Logs the exception message and its cause.
     *
     * @param message the detail message to be logged
     * @param cause the cause of the exception
     */
    private void logException(String message, Throwable cause) {
        LOGGER.severe("OrderBookException occurred: " + message + ". Caused by: " + cause.toString());
    }
}