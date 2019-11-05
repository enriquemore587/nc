package com.bbva.intranet.senders.exceptions;

public class SenderException extends Exception {

    private String message;
    private Integer code;

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public SenderException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public SenderException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

}
