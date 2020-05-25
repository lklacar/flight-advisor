package com.htecgroup.flightadvisor.controller;

import com.htecgroup.flightadvisor.service.CityService;
import com.htecgroup.flightadvisor.service.dto.CityDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/cities")
public class CityController {
    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping
    public ResponseEntity<CityDto> createCity(@Valid @RequestBody CityDto city) {
        log.debug("Create city");
        CityDto response = cityService.createCity(city);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public Object listCities(
            @RequestParam(value = "commentCount", required = false, defaultValue = "2147483647") Integer commentCount,
            @RequestParam(value = "cityName", required = false, defaultValue = "") String cityName
    ) {
        log.debug("Create city");
        List<CityDto> cities = cityService.listCities(commentCount, cityName);
        return ResponseEntity.status(HttpStatus.OK)
                .body(cities);
    }

}
