package com.htecgroup.flightadvisor.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileService {

    /**
     * Saves a {@link MultipartFile} to the disk with a given fileName
     *
     * @param fileName      Name of the file to save
     * @param multipartFile Multipart file containing the data
     * @return {@link File} Pointer to the saved file
     */
    File saveMultipartFile(String fileName, MultipartFile multipartFile);
}
