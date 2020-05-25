package com.htecgroup.flightadvisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.htecgroup.flightadvisor.domain.ApplicationUser;
import com.htecgroup.flightadvisor.domain.Authority;
import com.htecgroup.flightadvisor.repository.AuthorityRepository;
import com.htecgroup.flightadvisor.repository.UserRepository;
import com.htecgroup.flightadvisor.service.dto.AuthenticateRequest;
import com.htecgroup.flightadvisor.service.dto.AuthenticateResponse;
import com.htecgroup.flightadvisor.service.dto.RegisterUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthenticationControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc restAccountMockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final RegisterUserRequest registerUserRequest = RegisterUserRequest.builder()
            .firstName("Luka")
            .lastName("Klacar")
            .password("password")
            .username("luka")
            .build();

    private ApplicationUser existingUser = ApplicationUser.builder()
            .firstName("Existing")
            .lastName("User")
            .username("existing")
            .password("$2a$10$R9/XnsSQ5HzJsudcygmt9OeD.byqQ5cUPKCQEraVwRLj5mVwDb9Lm")
            .build();

    private Authority authority = Authority.builder().name("USER").build();

    @BeforeEach
    @Transactional
    void setUp() {
        userRepository.deleteAll();
        authority = authorityRepository.save(authority);
        existingUser.setAuthorities(new HashSet<>(Collections.singletonList(authority)));
        existingUser = userRepository.save(existingUser);
    }

    @Test
    @Transactional
    void registerUser() throws Exception {
        restAccountMockMvc.perform(post("/api/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerUserRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    @Transactional
    void registerUserInvalidRequest() throws Exception {
        restAccountMockMvc.perform(post("/api/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(RegisterUserRequest.builder()
                        .password("")
                        .username("")
                        .build())))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Transactional
    void registerUserTakeUsername() throws Exception {
        restAccountMockMvc.perform(post("/api/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(RegisterUserRequest.builder()
                        .firstName("Test")
                        .lastName("Test")
                        .username("existing")
                        .password("password")
                        .build())))
                .andDo(print())
                .andExpect(status().isConflict())
                .andReturn();
    }

    @Test
    @Transactional
    void authorize() throws Exception {
        restAccountMockMvc.perform(post("/api/authenticate").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AuthenticateRequest.builder()
                        .username("existing")
                        .password("password")
                        .build())))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Transactional
    void authorizeInvalidUser() throws Exception {
        restAccountMockMvc.perform(post("/api/authenticate").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AuthenticateRequest.builder()
                        .username("invalid")
                        .password("invalid")
                        .build())))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @Transactional
    void testJwt() throws Exception {
        var response = restAccountMockMvc.perform(post("/api/authenticate").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AuthenticateRequest.builder()
                        .username("existing")
                        .password("password")
                        .build())))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        var res = objectMapper.readValue(response.getResponse().getContentAsString(), AuthenticateResponse.class);

        restAccountMockMvc.perform(get("/api/cities").contentType(MediaType.APPLICATION_JSON)
                .header(
                        "Authorization",
                        "Bearer " + res.getIdToken()
                ))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
}
