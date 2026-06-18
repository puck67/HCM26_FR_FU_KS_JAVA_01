package com.assessment.service.impl;

import com.assessment.exception.StorageException;
import com.assessment.service.StorageService;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class StorageServiceImpl implements StorageService {

    private final Path rootLocation = Paths.get("./uploads");
    private final List<String> allowedExtensions = Arrays.asList("pdf", "docx", "pptx", "zip", "txt", "xlsx", "png", "jpg");
    private final List<String> blockedExtensions = Arrays.asList("exe", "bat", "cmd", "sh");

    @Override
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + originalFilename);
            }
            if (file.getSize() > 10 * 1024 * 1024) {
                throw new StorageException("Maximum file size is 10MB.");
            }

            // Extract extension
            String fileExtension = "";
            int i = originalFilename.lastIndexOf('.');
            if (i > 0) {
                fileExtension = originalFilename.substring(i + 1).toLowerCase();
            }

            // Validation checks
            if (blockedExtensions.contains(fileExtension)) {
                throw new StorageException("Unsupported file format (Blocked extension: ." + fileExtension + ")");
            }
            if (!allowedExtensions.contains(fileExtension)) {
                throw new StorageException("Unsupported file format. Only PDF, DOCX, PPTX, XLSX, ZIP, TXT, PNG, JPG are allowed.");
            }

            // Generate unique filename
            String storedFileName = UUID.randomUUID().toString() + "-" + originalFilename;

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(storedFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
            return storedFileName;
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + originalFilename, e);
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException("Requested file does not exist.");
            }
        } catch (MalformedURLException e) {
            throw new StorageException("Requested file does not exist.", e);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new StorageException("Could not delete physical file: " + filename, e);
        }
    }
}
