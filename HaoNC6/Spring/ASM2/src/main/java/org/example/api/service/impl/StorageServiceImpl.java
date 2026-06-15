package org.example.api.service.impl;

import jakarta.annotation.PostConstruct;
import org.example.api.exception.StorageException;
import org.example.api.exception.StorageFileNotFoundException;
import org.example.api.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;

/**
 * Implementation of StorageService that saves files to the local filesystem.
 * Upload directory is configurable via application.properties.
 */
@Service
public class StorageServiceImpl implements StorageService {

    private final Path uploadDir;

    public StorageServiceImpl(@Value("${app.storage.upload-dir:./uploads}") String uploadDirPath) {
        this.uploadDir = Paths.get(uploadDirPath).toAbsolutePath().normalize();
    }

    /**
     * Creates the upload directory on application startup if it doesn't exist.
     */
    @Override
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage directory: " + uploadDir, e);
        }
    }

    /**
     * Stores the file, prefixing the original filename with a timestamp
     * to avoid naming collisions.
     *
     * @return the stored filename (timestamp_originalname)
     */
    @Override
    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new StorageException("Cannot store an empty file.");
        }

        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "unknown"
        );

        // Guard against directory traversal attacks
        if (originalFilename.contains("..")) {
            throw new StorageException(
                    "Cannot store file with relative path outside current directory: " + originalFilename);
        }

        // Prefix with timestamp to avoid filename collisions
        String storedFilename = Instant.now().toEpochMilli() + "_" + originalFilename;
        Path targetPath = uploadDir.resolve(storedFilename);

        try {
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file: " + originalFilename, e);
        }

        return storedFilename;
    }

    /**
     * Loads a file from the upload directory as a downloadable Resource.
     *
     * @param filename the stored filename
     * @return a URL Resource pointing to the file
     */
    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path filePath = uploadDir.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }
}
