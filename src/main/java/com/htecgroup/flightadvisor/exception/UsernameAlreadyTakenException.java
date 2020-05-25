package com.htecgroup.flightadvisor.exception;

public class UsernameAlreadyTakenException extends RuntimeException {

    public UsernameAlreadyTakenException() {
    }

    public UsernameAlreadyTakenException(String message) {
        super(message);
    }

    public UsernameAlreadyTakenException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameAlreadyTakenException(Throwable cause) {
        super(cause);
    }

    public UsernameAlreadyTakenException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
