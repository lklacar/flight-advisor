package com.htecgroup.flightadvisor.controller;

import com.htecgroup.flightadvisor.service.FlightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/flights")
public class FlightController {
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/{fromCityId}/{toCityId}")
    public ResponseEntity<Map<String, Object>> findCheapestPath(
            @PathVariable Long fromCityId,
            @PathVariable Long toCityId
    ) {
        var res = flightService.findCheapestRoutes(fromCityId, toCityId);
        return ResponseEntity.ok(res);
    }
}
