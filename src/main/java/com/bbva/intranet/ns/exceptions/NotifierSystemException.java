package com.bbva.intranet.ns.exceptions;

public class NotifierSystemException extends Exception {

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
    public NotifierSystemException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public NotifierSystemException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

}
