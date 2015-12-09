package com.ciscospark;

/**
 * Copyright (c) 2015 Cisco Systems, Inc. See LICENSE file.
 */
public class NotAuthenticatedException extends SparkException {
    public NotAuthenticatedException() {
    }

    public NotAuthenticatedException(String message) {
        super(message);
    }

    public NotAuthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAuthenticatedException(Throwable cause) {
        super(cause);
    }
}
