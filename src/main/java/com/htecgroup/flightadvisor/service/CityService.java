package com.htecgroup.flightadvisor.service;

import com.htecgroup.flightadvisor.security.IsAdmin;
import com.htecgroup.flightadvisor.service.dto.CityDto;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface CityService {

    /**
     * Create a city.
     *
     * @param cityDto {@link CityDto} object describing the city that needs to be created.
     * @return {@link CityDto} created city object
     */
    @IsAdmin
    CityDto createCity(CityDto cityDto);

    /**
     * List all cities whose name contains cityName parameter and attack numberOfComments comments to each city
     * @param numberOfComments Number of maximum comments returned for each city
     * @param cityName Part of the city name that needs to match
     * @return A list of cities that match the criteria
     */
    // @formatter:off
    @PreAuthorize("isAuthenticated()") // NOSONAR
    List<CityDto> listCities(Integer numberOfComments, String cityName);
    // @formatter:on
}
