package com.htecgroup.flightadvisor.controller;

import com.htecgroup.flightadvisor.config.ProfileConstants;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles(ProfileConstants.SPRING_PROFILE_TEST)
public abstract class BaseIntegrationTest {
    public static final String TEST_USER_LOGIN = "test";
}
