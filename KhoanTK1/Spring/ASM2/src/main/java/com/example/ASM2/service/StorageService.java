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

    private final Path uploadDir = Paths.get("./uploads");

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location directory", e);
        }
    }

    public String store(MultipartFile fileData) {
        return store(fileData, uploadDir);
    }

    public String store(MultipartFile fileData, String folder) {
        Path targetDir = uploadDir.resolve(folder);
        try {
            Files.createDirectories(targetDir);
        } catch (IOException e) {
            throw new StorageException("Could not create subfolder directory: " + folder, e);
        }
        return store(fileData, targetDir);
    }

    private String store(MultipartFile fileData, Path targetDirectory) {
        if (fileData.isEmpty()) {
            throw new StorageException("Failed to store empty file.");
        }

        String fName = StringUtils.cleanPath(fileData.getOriginalFilename());
        if (fName.contains("..")) {
            throw new StorageException("Cannot store file with relative path outside current directory: " + fName);
        }

        try {
            String prefix = fName;
            String ext = "";
            int dotIndex = fName.lastIndexOf('.');
            if (dotIndex > 0) {
                prefix = fName.substring(0, dotIndex);
                ext = fName.substring(dotIndex);
            }

            Path destFile = targetDirectory.resolve(fName);
            for (int num = 1; Files.exists(destFile); num++) {
                fName = prefix + "_" + num + ext;
                destFile = targetDirectory.resolve(fName);
            }

            Files.copy(fileData.getInputStream(), destFile, StandardCopyOption.REPLACE_EXISTING);
            return fName;
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + fName, e);
        }
    }

    public Path load(String fName) {
        return uploadDir.resolve(fName);
    }

    public Path load(String fName, String folder) {
        return uploadDir.resolve(folder).resolve(fName);
    }

    public Resource loadAsResource(String fName) {
        return loadAsResource(fName, uploadDir);
    }

    public Resource loadAsResource(String fName, String folder) {
        return loadAsResource(fName, uploadDir.resolve(folder));
    }

    private Resource loadAsResource(String fName, Path targetDirectory) {
        try {
            Path file = targetDirectory.resolve(fName).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists() && !resource.isReadable()) {
                throw new StorageException("Could not read file or file does not exist: " + fName);
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new StorageException("Could not read file due to malformed URL: " + fName, e);
        }
    }
}
