package fa.training.lms.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@Service
public class StorageService {

    private final Path rootLocation =
            Paths.get("uploads");

    public StorageService() {
        init();
    }

    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not initialize upload folder");
        }
    }

    public String store(MultipartFile file) {

        if (file.isEmpty()) {
            throw new RuntimeException(
                    "Failed to store empty file");
        }

        try {

            String filename =
                    file.getOriginalFilename();

            Files.copy(
                    file.getInputStream(),
                    rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING
            );

            return filename;

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to store file");
        }
    }

    public Resource loadAsResource(String filename) {

        try {

            Path file =
                    rootLocation.resolve(filename);

            Resource resource =
                    new UrlResource(file.toUri());

            if (resource.exists()
                    || resource.isReadable()) {
                return resource;
            }

            throw new RuntimeException(
                    "Could not read file");

        } catch (MalformedURLException e) {
            throw new RuntimeException(
                    "Could not read file");
        }
    }
}