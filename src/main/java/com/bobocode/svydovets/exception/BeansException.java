package com.bobocode.svydovets.exception;

/**
 * Exception can be thrown when something goes wrong while working with Beans.
 */
public class BeansException extends RuntimeException {

    public BeansException() {
    }

    /**
     * Create a new exception {@code BeansException}.
     * @param message the detailed message
     */
    public BeansException(String message) {
        super(message);
    }
}
