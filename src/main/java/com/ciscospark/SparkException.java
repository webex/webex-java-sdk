package com.ciscospark;

import java.net.MalformedURLException;

/**
 * Created on 11/24/15.
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

    public SparkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
