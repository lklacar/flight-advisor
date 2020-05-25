package com.htecgroup.flightadvisor.exception;

public class UploadFileException extends RuntimeException {

    public UploadFileException() {
        super();
    }

    public UploadFileException(String message) {
        super(message);
    }

    public UploadFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public UploadFileException(Throwable cause) {
        super(cause);
    }

    protected UploadFileException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
