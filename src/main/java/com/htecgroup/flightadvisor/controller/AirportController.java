package com.htecgroup.flightadvisor.controller;

import com.htecgroup.flightadvisor.service.AirportService;
import com.htecgroup.flightadvisor.service.dto.ImportResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/airports")
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @PostMapping("/import")
    public ResponseEntity<ImportResponse> importAirports(@RequestParam MultipartFile file) {
        var res = airportService.importAirports(file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(res);
    }
}
