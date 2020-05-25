package com.htecgroup.flightadvisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.htecgroup.flightadvisor.domain.ApplicationUser;
import com.htecgroup.flightadvisor.domain.City;
import com.htecgroup.flightadvisor.domain.Comment;
import com.htecgroup.flightadvisor.repository.CityRepository;
import com.htecgroup.flightadvisor.repository.CommentRepository;
import com.htecgroup.flightadvisor.repository.UserRepository;
import com.htecgroup.flightadvisor.security.AuthoritiesConstants;
import com.htecgroup.flightadvisor.service.dto.CommentDto;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class CommentControllerTest extends BaseIntegrationTest {
    @Autowired
    private MockMvc restAccountMockMvc;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private City belgrade = City.builder()
            .country("Serbia")
            .comments(new HashSet<>())
            .description("Capital of Serbia")
            .name("Belgrade")
            .build();

    private ApplicationUser user = ApplicationUser.builder()
            .firstName("Luka")
            .lastName("Klacar")
            .username(TEST_USER_LOGIN)
            .password("$2a$10$R9/XnsSQ5HzJsudcygmt9OeD.byqQ5cUPKCQEraVwRLj5mVwDb9Lm")
            .build();

    private List<Comment> belgradeComments = Arrays.asList(Comment.builder()
                    .author(user)
                    .city(belgrade)
                    .text("Comment 1")
                    .build(),
            Comment.builder()
                    .author(user)
                    .city(belgrade)
                    .text("Comment 2")
                    .build(),
            Comment.builder()
                    .author(user)
                    .city(belgrade)
                    .text("Comment 3")
                    .build(),
            Comment.builder()
                    .author(user)
                    .city(belgrade)
                    .text("Comment 4")
                    .build(),
            Comment.builder()
                    .author(user)
                    .city(belgrade)
                    .text("Comment 5")
                    .build()
    );

    private final CommentDto comment = CommentDto.builder()
            .text("Awesome city")
            .build();

    @BeforeEach
    @Transactional
    void setUp() {
        cityRepository.deleteAll();
        userRepository.deleteAll();
        commentRepository.deleteAll();
        belgrade = cityRepository.save(belgrade);
        user = userRepository.save(user);
        belgradeComments = commentRepository.saveAll(belgradeComments);
    }

    @Test
    @Transactional
    @WithMockUser(value = TEST_USER_LOGIN, authorities = {AuthoritiesConstants.USER})
    void createComment() throws Exception {
        var commentsBeforeCreate = commentRepository.findAll();

        var result = restAccountMockMvc.perform(post(String.format("/api/cities/%d/comments",
                belgrade.getId()
        )).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        var response = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CommentDto.class);

        Assert.assertEquals(commentsBeforeCreate.size() + 1,
                commentRepository.findAll()
                        .size()
        );

        Assert.assertEquals(comment.getText(), response.getText());
        Assert.assertEquals(user.getUsername(), response.getAuthor());
    }

    @Test
    @Transactional
    @WithMockUser(value = TEST_USER_LOGIN, authorities = {AuthoritiesConstants.USER})
    void createCommentNonExistingCity() throws Exception {
        restAccountMockMvc.perform(post(String.format("/api/cities/%d/comments",
                100L
        )).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andReturn();
    }

    @Test
    @Transactional
    @WithMockUser(value = TEST_USER_LOGIN, authorities = {AuthoritiesConstants.USER})
    void deleteComment() throws Exception {
        var commentsBeforeDelete = commentRepository.findAll();
        var commentToDelete = commentsBeforeDelete.get(0);

        restAccountMockMvc.perform(delete(String.format(String.format("/api/comments/%d", commentToDelete.getId()),
                belgrade.getId()
        )))
                .andExpect(status().isNoContent())
                .andReturn();

        var commentsAfterDelete = commentRepository.findAll();

        Assert.assertEquals(commentsBeforeDelete.size() - 1, commentsAfterDelete.size());
    }

    @Test
    @Transactional
    @WithMockUser(value = TEST_USER_LOGIN, authorities = {AuthoritiesConstants.USER})
    void updateComment() throws Exception {
        restAccountMockMvc.perform(put(String.format("/api/comments/%d",
                belgradeComments.get(0)
                        .getId()
        )).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CommentDto.builder()
                        .text("Updated comment")
                        .build())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        var comment = commentRepository.findById(belgradeComments.get(0).getId()).orElse(null);

        Assert.assertNotNull(comment);
        Assert.assertEquals("Updated comment", comment.getText());
    }
}
