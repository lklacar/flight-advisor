package com.htecgroup.flightadvisor.service.batch;

import com.htecgroup.flightadvisor.domain.Airport;
import com.htecgroup.flightadvisor.domain.City;
import com.htecgroup.flightadvisor.service.dto.ImportResponse;

import java.io.File;
import java.util.List;

public interface AirportBatchService {

    /**
     * Starts a job that imports airports from the CSV file and uses pre-cached cities and airports values.
     *
     * @param file     Pointer to a {@link File} containing the CSV data that needs to be imported.
     * @param cities   Pre-cached list of all cities at the point of job start
     * @param airports Pre-cached list of all airports at the point of job start
     * @return ImportResponse describing the import action result
     */
    ImportResponse startImportJob(File file, List<City> cities, List<Airport> airports);

}
