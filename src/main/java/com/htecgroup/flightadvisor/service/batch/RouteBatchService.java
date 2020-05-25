package com.htecgroup.flightadvisor.service.batch;

import com.htecgroup.flightadvisor.domain.Airport;
import com.htecgroup.flightadvisor.service.dto.ImportResponse;

import java.io.File;
import java.util.List;

public interface RouteBatchService {

    /**
     * Starts a job that imports airports from the CSV file and uses pre-cached airports values.
     *
     * @param file     Pointer to a {@link File} containing the CSV data that needs to be imported.
     * @param airportList Pre-cached list of all airports at the point of job start
     * @return ImportResponse describing the import action result
     */
    ImportResponse startImportJob(File file, List<Airport> airportList);
}
