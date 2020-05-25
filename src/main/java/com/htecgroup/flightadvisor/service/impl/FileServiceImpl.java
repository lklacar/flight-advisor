package com.htecgroup.flightadvisor.service.impl;

import com.htecgroup.flightadvisor.config.ApplicationProperties;
import com.htecgroup.flightadvisor.exception.UploadFileException;
import com.htecgroup.flightadvisor.service.FileService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {

    private final ApplicationProperties applicationProperties;

    public FileServiceImpl(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public File saveMultipartFile(String fileName, MultipartFile multipartFile) {
        try {
            Path path = Paths.get(applicationProperties.getUploadDirectory(), fileName);
            File file = path.toFile();
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
            return file;
        } catch (IOException e) {
            throw new UploadFileException(e);
        }
    }
}
