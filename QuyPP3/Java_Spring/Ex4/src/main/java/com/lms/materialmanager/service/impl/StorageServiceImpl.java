package com.lms.materialmanager.service.impl;

import com.lms.materialmanager.service.StorageService;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class StorageServiceImpl implements StorageService {

    private final Path rootLocation = Paths.get("uploads/materials");
    
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList("pdf", "docx", "pptx", "zip", "txt"));
    private static final Set<String> BLOCKED_EXTENSIONS = new HashSet<>(Arrays.asList("exe", "bat", "cmd", "sh"));

    @Override
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage folder uploads/materials", e);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot store empty file.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.contains("..")) {
            throw new IllegalArgumentException("Cannot store file with relative path outside current directory.");
        }

        // Validate size just in case (though Spring's multipart configuration will also handle this)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("Maximum file size is 10MB.");
        }

        // Validate extension
        String extension = getFileExtension(originalFilename).toLowerCase();
        if (BLOCKED_EXTENSIONS.contains(extension) || !ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Unsupported file format.");
        }

        // Generate unique stored file name: 8 characters from UUID + hyphen + original name
        String prefix = UUID.randomUUID().toString().substring(0, 8);
        String storedFileName = prefix + "-" + originalFilename;

        try {
            Path destinationFile = this.rootLocation.resolve(Paths.get(storedFileName)).normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new IllegalArgumentException("Cannot store file outside current directory.");
            }
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            return storedFileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + originalFilename, e);
        }
    }

    @Override
    public Resource loadFileAsResource(String storedFileName) {
        try {
            Path file = rootLocation.resolve(storedFileName).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Requested file does not exist.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Requested file does not exist.", e);
        }
    }

    @Override
    public void deleteFile(String storedFileName) {
        try {
            Path file = rootLocation.resolve(storedFileName).normalize();
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete physical file: " + storedFileName, e);
        }
    }

    private String getFileExtension(String filename) {
        return java.util.Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(f.lastIndexOf(".") + 1))
                .orElse("");
    }
}
