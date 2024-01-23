package com.debts.debtsappbackend.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class FileService {
    public String uploadFile(MultipartFile file, List<String> errors) {
        log.info("File: " + file);
        String fileName = _formatFileName(file);
        try {
            // Save the file to the server
            String currentWorkingDir = System.getProperty("user.dir");
            Path path = Paths.get(currentWorkingDir + "\\src\\main\\java\\com\\debts\\debtsappbackend\\uploads\\" + fileName);
            log.info("PATH: " + path);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            log.error("ERROR: ", e);
            errors.add(e.getMessage());
            return "";
        }
    }

    private String _getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HHmmss");
        return now.format(formatter);
    }

    private String _formatFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        log.info("Original filename: " + originalFilename);
        log.info("Extension: " + extension);
        log.info("Filename without extension: " + FilenameUtils.removeExtension(originalFilename));
        return FilenameUtils.removeExtension(originalFilename) + _getCurrentDateTime() + "." + extension;
    }
}
