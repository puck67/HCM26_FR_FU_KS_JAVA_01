package com.example.lms.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService {

    private final Path storageLocation = Paths.get("./uploads");

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(storageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage directory", e);
        }
    }

    public String store(MultipartFile file) {
        String fileName = Paths.get(file.getOriginalFilename()).getFileName().toString();
        try {
            Path target = storageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException(new StringBuilder("Failed to store file: ").append(fileName).toString(), e);
        }
    }

    public Resource load(String filename) {
        try {
            Path file = storageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            throw new RuntimeException(new StringBuilder("File not found or not readable: ").append(filename).toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(new StringBuilder("Could not read file: ").append(filename).toString(), e);
        }
    }
}
