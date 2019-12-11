package com.bbva.intranet.not.core.exceptions;

public class NotCoreException extends Exception {

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
    public NotCoreException(String message) {
        super(message);
        this.message = message;
    }

    public static class TransactionStoppedException {
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {

        return code;
    }
}
