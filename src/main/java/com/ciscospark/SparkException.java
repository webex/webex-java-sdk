package com.ciscospark;

/**
 * Copyright (c) 2015 Cisco Systems, Inc. See LICENSE file.
 */
public class SparkException extends RuntimeException {
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
}
