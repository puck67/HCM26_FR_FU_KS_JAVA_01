package com.example.Ex4.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadPath;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) throws Exception {
        this.uploadPath = Paths.get(uploadDir);
        Files.createDirectories(uploadPath);
    }

    public String save(MultipartFile file) throws Exception {
        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        Files.copy(
                file.getInputStream(),
                uploadPath.resolve(filename),
                StandardCopyOption.REPLACE_EXISTING
        );
        return filename;
    }

    public Path load(String filename) {
        return uploadPath.resolve(filename);
    }

    public void delete(String filename) throws Exception {
        Files.deleteIfExists(uploadPath.resolve(filename));
    }
}