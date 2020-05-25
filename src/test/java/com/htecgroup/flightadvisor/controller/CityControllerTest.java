package com.htecgroup.flightadvisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.htecgroup.flightadvisor.domain.ApplicationUser;
import com.htecgroup.flightadvisor.domain.City;
import com.htecgroup.flightadvisor.domain.Comment;
import com.htecgroup.flightadvisor.repository.CityRepository;
import com.htecgroup.flightadvisor.repository.CommentRepository;
import com.htecgroup.flightadvisor.repository.UserRepository;
import com.htecgroup.flightadvisor.security.AuthoritiesConstants;
import com.htecgroup.flightadvisor.service.dto.CityDto;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class CityControllerTest extends BaseIntegrationTest {
    @Autowired
    private MockMvc restAccountMockMvc;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private City belgrade = City.builder()
            .country("Serbia")
            .comments(new HashSet<>())
            .description("Capital of Serbia")
            .name("Belgrade")
            .build();

    private City stockholm = City.builder()
            .country("Stockholm")
            .comments(new HashSet<>())
            .description("Capital of Sweden")
            .name("Sweden")
            .build();

    private final CityDto newYork = CityDto.builder()
            .country("USA")
            .description("City in the USA")
            .name("New York")
            .build();

    private ApplicationUser user = ApplicationUser.builder()
            .firstName("Luka")
            .lastName("Klacar")
            .username("Test")
            .password("$2a$10$R9/XnsSQ5HzJsudcygmt9OeD.byqQ5cUPKCQEraVwRLj5mVwDb9Lm")
            .build();

    private List<Comment> belgradeComments = Arrays.asList(
            Comment.builder().author(user).city(belgrade).text("Comment 1").build(),
            Comment.builder().author(user).city(belgrade).text("Comment 2").build(),
            Comment.builder().author(user).city(belgrade).text("Comment 3").build(),
            Comment.builder().author(user).city(belgrade).text("Comment 4").build(),
            Comment.builder().author(user).city(belgrade).text("Comment 5").build()
    );

    @BeforeEach
    @Transactional
    public void setup() {
        cityRepository.deleteAll();
        userRepository.deleteAll();
        commentRepository.deleteAll();
        user = userRepository.save(user);
        belgrade = cityRepository.save(belgrade);
        stockholm = cityRepository.save(stockholm);
        belgradeComments = commentRepository.saveAll(belgradeComments);
    }

    @Test
    @Transactional
    @WithMockUser(value = TEST_USER_LOGIN, authorities = {AuthoritiesConstants.USER})
    void listCities() throws Exception {
        MvcResult result = restAccountMockMvc.perform(get("/api/cities"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        var response = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CityDto[].class);

        var responseBelgrade = response[0];
        var responseStockholm = response[1];

        Assert.assertEquals(cityRepository.findAll()
                .size(), response.length);

        Assert.assertEquals(belgrade.getId(), responseBelgrade.getId());
        Assert.assertEquals(belgrade.getCountry(), responseBelgrade.getCountry());
        Assert.assertEquals(belgrade.getName(), responseBelgrade.getName());
        Assert.assertEquals(belgrade.getDescription(), responseBelgrade.getDescription());

        Assert.assertEquals(stockholm.getId(), responseStockholm.getId());
        Assert.assertEquals(stockholm.getCountry(), responseStockholm.getCountry());
        Assert.assertEquals(stockholm.getName(), responseStockholm.getName());
        Assert.assertEquals(stockholm.getDescription(), responseStockholm.getDescription());

    }

    @Test
    @Transactional
    @WithMockUser(value = TEST_USER_LOGIN, authorities = {AuthoritiesConstants.USER})
    void listCitiesLimitComments() throws Exception {
        MvcResult result = restAccountMockMvc.perform(get("/api/cities?commentCount=2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        var response = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CityDto[].class);

        var responseBelgrade = response[0];

        Assert.assertEquals(2, responseBelgrade.getComments().size());
    }

    @Test
    @Transactional
    void listCitiesAnonymous() throws Exception {
        restAccountMockMvc.perform(get("/api/cities"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andReturn();
    }

    @Test
    @Transactional
    void createCityAnonymous() throws Exception {
        restAccountMockMvc.perform(post("/api/cities").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newYork)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andReturn();
    }

    @Test
    @Transactional
    @WithMockUser(value = TEST_USER_LOGIN, authorities = {AuthoritiesConstants.USER})
    void createCityUser() throws Exception {
        restAccountMockMvc.perform(post("/api/cities").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newYork)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andReturn();
    }

    @Test
    @Transactional
    @WithMockUser(value = TEST_USER_LOGIN, authorities = {AuthoritiesConstants.ADMIN})
    void createDuplicateCity() throws Exception {
        restAccountMockMvc.perform(post("/api/cities").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newYork)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        restAccountMockMvc.perform(post("/api/cities").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newYork)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andReturn();
    }

    @Test
    @Transactional
    @WithMockUser(value = TEST_USER_LOGIN, authorities = {AuthoritiesConstants.ADMIN})
    void createCityAdmin() throws Exception {
        var citiesBeforeCreate = cityRepository.findAll();

        var result = restAccountMockMvc.perform(post("/api/cities").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newYork)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        var response = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CityDto.class);

        Assert.assertEquals(
                citiesBeforeCreate.size() + 1,
                cityRepository.findAll()
                        .size()
        );

        Assert.assertNotNull(response.getId());
        Assert.assertEquals(newYork.getCountry(), response.getCountry());
        Assert.assertEquals(newYork.getName(), response.getName());
        Assert.assertEquals(newYork.getDescription(), response.getDescription());

        var createdCity = cityRepository.findById(response.getId())
                .orElse(null);

        Assert.assertNotNull(createdCity);
        Assert.assertEquals(createdCity.getId(), response.getId());
        Assert.assertEquals(createdCity.getCountry(), response.getCountry());
        Assert.assertEquals(createdCity.getName(), response.getName());
        Assert.assertEquals(createdCity.getDescription(), response.getDescription());
    }
}
