package com.htecgroup.flightadvisor.controller;

import com.htecgroup.flightadvisor.service.RouteService;
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
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping("/import")
    public ResponseEntity<ImportResponse> importRoutes(@RequestParam MultipartFile file) {
        var res = routeService.importRoutes(file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(res);
    }
}
