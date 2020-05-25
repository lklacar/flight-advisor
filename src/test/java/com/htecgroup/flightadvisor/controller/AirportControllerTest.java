package com.htecgroup.flightadvisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.htecgroup.flightadvisor.domain.City;
import com.htecgroup.flightadvisor.repository.AirportRepository;
import com.htecgroup.flightadvisor.repository.CityRepository;
import com.htecgroup.flightadvisor.security.AuthoritiesConstants;
import com.htecgroup.flightadvisor.service.dto.ImportResponse;
import org.apache.commons.collections4.IteratorUtils;
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

import java.util.HashSet;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AirportControllerTest extends BaseIntegrationTest {
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

    private final MockMultipartFile nonExistentCityAirportFile = new MockMultipartFile(
            "file",
            "airports.txt",
            "text/plain",
            "1,\"Goroka Airport\",\"Goroka\",\"Papua New Guinea\",\"GKA\",\"AYGA\",-6.081689834590001,145.391998291,5282,10,\"U\",\"Pacific/Port_Moresby\",\"airport\",\"OurAirports\"".getBytes()
    );

    private final MockMultipartFile existingCityAirportFile = new MockMultipartFile(
            "file",
            "airports.txt",
            "text/plain",
            "1739,\"Belgrade Nikola Tesla Airport\",\"Belgrade\",\"Serbia\",\"BEG\",\"LYBE\",44.8184013367,20.3090991974,335,1,\"E\",\"Europe/Belgrade\",\"airport\",\"OurAirports\"".getBytes()
    );

    @BeforeEach
    public void setup() {
        cityRepository.deleteAll();
        airportRepository.deleteAll();
        belgrade = cityRepository.save(belgrade);
    }

    @Test
    @WithMockUser(value = TEST_USER_LOGIN, authorities = {AuthoritiesConstants.ADMIN})
    void importExistingAirportsAdmin() throws Exception {
        var airportsBeforeImport = IteratorUtils.toList(airportRepository.findAll()
                .iterator());

        var result = restAccountMockMvc.perform(MockMvcRequestBuilders.multipart("/api/airports/import")
                .file(existingCityAirportFile))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        var response = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ImportResponse.class);

        Assert.assertEquals(true, response.getSuccess());
        Assert.assertEquals(Integer.valueOf("1"), response.getTotalRecords());
        Assert.assertEquals(Integer.valueOf("1"), response.getWriteCount());

        var airportsAfterImport = IteratorUtils.toList(airportRepository.findAll()
                .iterator());
        var importedAirport = airportsAfterImport.get(0);

        Assert.assertEquals(airportsBeforeImport.size() + 1, airportsAfterImport.size());
        Assert.assertEquals(belgrade.getId(), importedAirport.getCityId());

        Assert.assertEquals(belgrade.getName(), importedAirport.getCity());
        Assert.assertEquals(belgrade.getCountry(), importedAirport.getCountry());
        Assert.assertEquals("Belgrade Nikola Tesla Airport", importedAirport.getName());

    }

    @Test
    @WithMockUser(value = TEST_USER_LOGIN, authorities = {AuthoritiesConstants.ADMIN})
    void importNonExistingAirportsAdmin() throws Exception {
        var result = restAccountMockMvc.perform(MockMvcRequestBuilders.multipart("/api/airports/import")
                .file(nonExistentCityAirportFile))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        var response = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ImportResponse.class);

        Assert.assertEquals(true, response.getSuccess());
        Assert.assertEquals(Integer.valueOf("1"), response.getTotalRecords());
        Assert.assertEquals(Integer.valueOf("0"), response.getWriteCount());
    }

    @Test
    void importAirportsAnonymous() throws Exception {
        restAccountMockMvc.perform(MockMvcRequestBuilders.multipart("/api/airports/import")
                .file(nonExistentCityAirportFile))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andReturn();
    }

    @Test
    @WithMockUser(value = TEST_USER_LOGIN, authorities = {AuthoritiesConstants.USER})
    void importAirportsUser() throws Exception {
        restAccountMockMvc.perform(MockMvcRequestBuilders.multipart("/api/airports/import")
                .file(nonExistentCityAirportFile))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andReturn();
    }
}
