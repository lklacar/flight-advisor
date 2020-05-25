package com.htecgroup.flightadvisor.service.impl;

import com.htecgroup.flightadvisor.domain.Airport;
import com.htecgroup.flightadvisor.domain.City;
import com.htecgroup.flightadvisor.repository.AirportRepository;
import com.htecgroup.flightadvisor.repository.CityRepository;
import com.htecgroup.flightadvisor.service.AirportService;
import com.htecgroup.flightadvisor.service.FileService;
import com.htecgroup.flightadvisor.service.batch.AirportBatchService;
import com.htecgroup.flightadvisor.service.dto.ImportResponse;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class AirportServiceImpl implements AirportService {
    private final FileService fileService;
    private final AirportBatchService airportBatchService;
    private final CityRepository cityRepository;
    private final AirportRepository airportRepository;

    public AirportServiceImpl(
            FileService fileService,
            AirportBatchService airportBatchService,
            CityRepository cityRepository,
            AirportRepository airportRepository
    ) {
        this.fileService = fileService;
        this.airportBatchService = airportBatchService;
        this.cityRepository = cityRepository;
        this.airportRepository = airportRepository;
    }

    @Override
    public ImportResponse importAirports(MultipartFile multipartFile) {
        String fileName = String.format(
                "airports-%s.csv",
                UUID.randomUUID()
                        .toString()
        );
        File file = fileService.saveMultipartFile(fileName, multipartFile);

        List<City> cities = cityRepository.findAll();
        List<Airport> airports = IteratorUtils.toList(airportRepository.findAll()
                .iterator());
        return airportBatchService.startImportJob(file, cities, airports);
    }

}
