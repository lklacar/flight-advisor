package com.htecgroup.flightadvisor.config;

public final class Constants {

    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";
    public static final String SYSTEM_ACCOUNT = "system";

    private Constants() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }
}
