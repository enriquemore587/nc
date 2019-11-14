package com.bbva.intranet.not.core.exceptions;

public class NoRecordFoundException extends Exception {

    private transient Throwable cause;
    private transient String message;

    public static final String RECORD_NO_FOUND_MESSAGE = "No record found by the criteria chose.";

    public NoRecordFoundException() {
        super();
    }

    public NoRecordFoundException(final String message) {
        this.message = message;
    }

    public NoRecordFoundException(final String message, final Throwable cause) {
        this.message = message;
        this.cause = cause;
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