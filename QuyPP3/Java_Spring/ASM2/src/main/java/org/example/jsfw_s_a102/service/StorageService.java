package org.example.jsfw_s_a102.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService {

    private final Path uploadDir = Paths.get("uploads");

    public StorageService() {
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize upload directory", e);
        }
    }

    public void store(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        Path destination = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
    }

    public Resource loadAsResource(String filename) throws MalformedURLException {
        Path file = uploadDir.resolve(filename);
        Resource resource = new UrlResource(file.toUri());
        if (resource.exists() && resource.isReadable()) {
            return resource;
        }
        throw new RuntimeException("Could not read file: " + filename);
    }
}
