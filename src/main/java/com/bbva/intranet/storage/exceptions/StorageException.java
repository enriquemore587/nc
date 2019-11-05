package com.bbva.intranet.storage.exceptions;

public abstract class StorageException extends Exception {
    private String message;
    private int statusCode;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public StorageException(String message) {
        this.message = message;
    }
    public StorageException(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

}
