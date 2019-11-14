package com.bbva.intranet.not.core.exceptions;

public class TransactionStoppedException extends Exception {

    private transient Throwable cause;
    private transient String message;

    public TransactionStoppedException() {
        super();
    }

    public TransactionStoppedException(final String message) {
        this.message = message;
    }

    public TransactionStoppedException(final String message, final Throwable cause) {
        this.message = message;
        this.cause = cause;
    }

    public TransactionStoppedException(final String message, final Throwable cause, Integer code) {
        this.message = message;
        this.cause = cause;
    }

    public TransactionStoppedException(final String message, Integer code) {
        this.message = message;
    }

    @Override
    public Throwable getCause() {
        return this.cause;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
