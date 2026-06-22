package com.example.demo.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class StorageService {

    private final Path rootLocation;

    public StorageService(@Value("${storage.location}") String storageLocation) {
        this.rootLocation = Paths.get(storageLocation).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            StringBuilder sb = new StringBuilder("Could not initialize storage directory: ");
            sb.append(rootLocation.toAbsolutePath());
            throw new RuntimeException(sb.toString(), e);
        }
    }

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Failed to store empty file.");
        }

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (originalFilename.contains("..")) {
            throw new IllegalArgumentException("Cannot store file with relative path outside current directory: " + originalFilename);
        }

        // Generate a unique filename using StringBuilder to prevent name collisions
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
        }

        String baseName = dotIndex > 0 ? originalFilename.substring(0, dotIndex) : originalFilename;
        
        StringBuilder uniqueNameBuilder = new StringBuilder();
        uniqueNameBuilder.append(baseName)
                         .append("_")
                         .append(UUID.randomUUID().toString().substring(0, 8))
                         .append(extension);
        
        String uniqueFilename = uniqueNameBuilder.toString();

        try {
            Path destinationFile = this.rootLocation.resolve(Paths.get(uniqueFilename))
                    .normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new IllegalArgumentException("Cannot store file outside designated directory.");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            
            return uniqueFilename;
        } catch (IOException e) {
            StringBuilder sb = new StringBuilder("Failed to store file ");
            sb.append(originalFilename);
            throw new RuntimeException(sb.toString(), e);
        }
    }

    public Resource loadAsResource(String filename) throws FileNotFoundException {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty.");
        }

        try {
            Path file = rootLocation.resolve(filename).normalize();
            
            // Check path traversal vulnerability
            if (!file.toAbsolutePath().startsWith(rootLocation.toAbsolutePath())) {
                throw new IllegalArgumentException("Access denied. Attempted path traversal.");
            }

            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                StringBuilder sb = new StringBuilder("Could not read file: ");
                sb.append(filename);
                throw new FileNotFoundException(sb.toString());
            }
        } catch (MalformedURLException e) {
            StringBuilder sb = new StringBuilder("Could not read file: ");
            sb.append(filename);
            throw new FileNotFoundException(sb.toString());
        }
    }
}
