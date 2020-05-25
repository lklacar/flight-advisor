package com.htecgroup.flightadvisor.exception;

public class StartJobException extends RuntimeException {
    public StartJobException() {
        super();
    }

    public StartJobException(String message) {
        super(message);
    }

    public StartJobException(String message, Throwable cause) {
        super(message, cause);
    }

    public StartJobException(Throwable cause) {
        super(cause);
    }

    protected StartJobException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
