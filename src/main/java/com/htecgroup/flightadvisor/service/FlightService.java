package com.htecgroup.flightadvisor.service;

import java.util.Map;

public interface FlightService {

    /**
     * Returns an object describing the cheapest route from city with id fromCity to a city with id toCity
     *
     * @param fromCity Id of the source city
     * @param toCity   Id of the destination city
     * @return Object describing the cheapest route
     */
    Map<String, Object> findCheapestRoutes(Long fromCity, Long toCity);

}
