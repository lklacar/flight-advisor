package com.htecgroup.flightadvisor.service;

import com.htecgroup.flightadvisor.service.dto.ImportResponse;
import org.springframework.web.multipart.MultipartFile;

public interface RouteService {

    /**
     * Import routes from the multipart file provided.
     * The file must be a CSV format file.
     *
     * @param file CSV multipart file
     * @return Import response
     */
    ImportResponse importRoutes(MultipartFile file);
}
