package com.example.ASM2.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService {

    // Default root upload location (./uploads)
    private final Path rootLocation = Paths.get("./uploads");

    /**
     * Task 2.3: Initialize the storage directory on startup.
     */
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location directory", e);
        }
    }

    /**
     * Task 2.1: Store an uploaded file in the default directory.
     * Returns the name of the stored file (which might be changed to prevent collisions).
     */
    public String store(MultipartFile file) {
        return store(file, rootLocation);
    }

    /**
     * Generic/Reusable: Store an uploaded file in a specific subfolder.
     */
    public String store(MultipartFile file, String subFolder) {
        Path targetLocation = rootLocation.resolve(subFolder);
        try {
            Files.createDirectories(targetLocation);
        } catch (IOException e) {
            throw new StorageException("Could not create subfolder directory: " + subFolder, e);
        }
        return store(file, targetLocation);
    }


    private String store(MultipartFile file, Path targetDirectory) {
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file.");
        }

        // Clean filename path to prevent directory traversal security issues
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (filename.contains("..")) {
            throw new StorageException("Cannot store file with relative path outside current directory: " + filename);
        }

        try {
            // Split basename and extension to append counter if file already exists
            String baseName = filename;
            String extension = "";
            int dotIndex = filename.lastIndexOf('.');
            if (dotIndex > 0) {
                baseName = filename.substring(0, dotIndex);
                extension = filename.substring(dotIndex);
            }

            Path destinationFile = targetDirectory.resolve(filename);
            int count = 1;

            // Reusable collision-avoidance: if file exists, rename to: name_1.ext, name_2.ext,...
            while (Files.exists(destinationFile)) {
                filename = baseName + "_" + count + extension;
                destinationFile = targetDirectory.resolve(filename);
                count++;
            }

            // Copy file content, replacing existing file if any (we already checked existence though)
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    /**
     * Task 2.2: Load file as Path by filename.
     */
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }


    public Path load(String filename, String subFolder) {
        return rootLocation.resolve(subFolder).resolve(filename);
    }

    /**
     * Task 2.2: Load file as Resource by filename for downloads.
     */
    public Resource loadAsResource(String filename) {
        return loadAsResource(filename, rootLocation);
    }


    public Resource loadAsResource(String filename, String subFolder) {
        return loadAsResource(filename, rootLocation.resolve(subFolder));
    }


    private Resource loadAsResource(String filename, Path targetDirectory) {
        try {
            Path file = targetDirectory.resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException("Could not read file or file does not exist: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageException("Could not read file due to malformed URL: " + filename, e);
        }
    }
}
