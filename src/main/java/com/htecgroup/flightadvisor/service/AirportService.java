package com.htecgroup.flightadvisor.service;

import com.htecgroup.flightadvisor.security.IsAdmin;
import com.htecgroup.flightadvisor.service.dto.ImportResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AirportService {

    /**
     * Import airports from the multipart file provided.
     * The file must be a CSV format file.
     *
     * @param multipartFile CSV multipart file
     * @return Import response
     */
    @IsAdmin
    ImportResponse importAirports(MultipartFile multipartFile);
}
