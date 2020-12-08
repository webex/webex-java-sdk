package com.ciscospark;

/**
 * Copyright (c) 2015 Cisco Systems, Inc. See LICENSE file.
 */
public class SparkException extends RuntimeException {
    private int status;
    public SparkException() {
    }

    public SparkException(String message) {
        super(message);
    }

    public SparkException(String message, Throwable cause) {
        super(message, cause);
    }

    public SparkException(Throwable cause) {
        super(cause);
    }

    public SparkException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }
}
