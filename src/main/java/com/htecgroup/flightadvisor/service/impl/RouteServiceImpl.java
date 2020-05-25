package com.htecgroup.flightadvisor.service.impl;

import com.htecgroup.flightadvisor.repository.AirportRepository;
import com.htecgroup.flightadvisor.service.FileService;
import com.htecgroup.flightadvisor.service.RouteService;
import com.htecgroup.flightadvisor.service.batch.RouteBatchService;
import com.htecgroup.flightadvisor.service.dto.ImportResponse;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class RouteServiceImpl implements RouteService {
    private final FileService fileService;
    private final RouteBatchService routeBatchService;
    private final AirportRepository airportRepository;

    public RouteServiceImpl(
            FileService fileService, RouteBatchService routeBatchService, AirportRepository airportRepository
    ) {
        this.fileService = fileService;
        this.routeBatchService = routeBatchService;
        this.airportRepository = airportRepository;
    }

    @Override
    public ImportResponse importRoutes(MultipartFile multipartFile) {
        String fileName = String.format(
                "routes-%s.csv",
                UUID.randomUUID()
                        .toString()
        );
        File file = fileService.saveMultipartFile(fileName, multipartFile);
        var airports = IteratorUtils.toList(airportRepository.findAll()
                .iterator());
        return routeBatchService.startImportJob(file, airports);
    }

}
