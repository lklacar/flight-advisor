package com.htecgroup.flightadvisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.htecgroup.flightadvisor.domain.Airport;
import com.htecgroup.flightadvisor.domain.City;
import com.htecgroup.flightadvisor.repository.AirportRepository;
import com.htecgroup.flightadvisor.repository.CityRepository;
import com.htecgroup.flightadvisor.security.AuthoritiesConstants;
import com.htecgroup.flightadvisor.service.dto.ImportResponse;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class RouteControllerTest extends BaseIntegrationTest {
    @Autowired
    private MockMvc restAccountMockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private AirportRepository airportRepository;

    private City belgrade = City.builder()
            .country("Serbia")
            .comments(new HashSet<>())
            .description("Capital of Serbia")
            .name("Belgrade")
            .build();

    private City stockholm = City.builder()
            .country("Sweden")
            .comments(new HashSet<>())
            .description("Capital of Sweden")
            .name("Stockholm")
            .build();

    private Airport belgradeAirport = Airport
            .builder()
            .airportId("001")
            .city("Belgrade")
            .country("Serbia")
            .cityId(1L)
            .name("Belgrade Airport")
            .build();

    private Airport stockholmAirport = Airport
            .builder()
            .airportId("002")
            .city("Stockholm")
            .country("Sweden")
            .cityId(2L)
            .name("Stockholm Airport")
            .build();

    private final MockMultipartFile existingAirportsRouteFile = new MockMultipartFile(
            "file",
            "routes.txt",
            "text/plain",
            "111,AIR,001,001,002,002,Y,0,AAA,50".getBytes()
    );

    private final MockMultipartFile nonExistingAirportRouteFile = new MockMultipartFile(
            "file",
            "routes.txt",
            "text/plain",
            "111,AIR,001,001,005,005,Y,0,AAA,50".getBytes()
    );

    @BeforeEach
    @Transactional
    public void setup() {
        cityRepository.deleteAll();
        airportRepository.deleteAll();
        airportRepository.deleteAll();
        belgrade = cityRepository.save(belgrade);
        stockholm = cityRepository.save(stockholm);
        belgradeAirport = airportRepository.save(belgradeAirport);
        stockholmAirport = airportRepository.save(stockholmAirport);
    }

    @Test
    @WithMockUser(value = TEST_USER_LOGIN, authorities = {AuthoritiesConstants.ADMIN})
    void importRoutesForExistingAirports() throws Exception {
        var result = restAccountMockMvc.perform(MockMvcRequestBuilders.multipart("/api/routes/import")
                .file(existingAirportsRouteFile))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        var response = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ImportResponse.class);

        Assert.assertEquals(Integer.valueOf(1), response.getTotalRecords());
        Assert.assertEquals(Integer.valueOf(1), response.getWriteCount());
        Assert.assertEquals(true, response.getSuccess());
    }

    @Test
    @WithMockUser(value = TEST_USER_LOGIN, authorities = {AuthoritiesConstants.ADMIN})
    void importRoutesForNonExistingAirports() throws Exception {
        var result = restAccountMockMvc.perform(MockMvcRequestBuilders.multipart("/api/routes/import")
                .file(nonExistingAirportRouteFile))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        var response = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ImportResponse.class);

        Assert.assertEquals(Integer.valueOf(1), response.getTotalRecords());
        Assert.assertEquals(Integer.valueOf(0), response.getWriteCount());
        Assert.assertEquals(true, response.getSuccess());
    }
}
