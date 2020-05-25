package com.htecgroup.flightadvisor.config;

public class ProfileConstants {

    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String SPRING_PROFILE_TEST = "test";

    private ProfileConstants() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }
}
