package com.htecgroup.flightadvisor.config;

public class ErrorConstants {

    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String BAD_CREDENTIALS = "BAD_CREDENTIALS";
    public static final String CONSTRAINT_VALIDATION = "CONSTRAINT_VALIDATION";
    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String CSV_PARSE_FAILED = "CSV_PARSE_FAILED";
    public static final String CANNOT_START_JOB = "CANNOT_START_JOB";
    public static final String FILE_UPLOAD_FAILED = "FILE_UPLOAD_FAILED";
    public static final Object USERNAME_ALREADY_TAKEN = "USERNAME_ALREADY_TAKEN";

    private ErrorConstants() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }
}
