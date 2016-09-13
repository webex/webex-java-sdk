package com.ciscospark;

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

    public NotAuthenticatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
