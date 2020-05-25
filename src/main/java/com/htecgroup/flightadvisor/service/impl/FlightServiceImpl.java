package com.htecgroup.flightadvisor.service.impl;

import com.htecgroup.flightadvisor.exception.NotFoundException;
import com.htecgroup.flightadvisor.repository.AirportRepository;
import com.htecgroup.flightadvisor.service.FlightService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Map;

@Service
public class FlightServiceImpl implements FlightService {
    private final AirportRepository airportRepository;

    public FlightServiceImpl(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Override
    public Map<String, Object> findCheapestRoutes(Long fromCityId, Long toCityId) {
        var res = airportRepository.findCheapestFlights(fromCityId, toCityId);
        return res.stream()
                .min(Comparator.comparingDouble(flight -> (Double) flight.get("totalPrice")))
                .orElseThrow(() -> new NotFoundException("Flights not found"));
    }
}
