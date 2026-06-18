package fa.training.TrainingMaterialManagementSystem.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class StorageUtil {
    private final Path root = Paths.get("uploads/materials");

    public StorageUtil() throws IOException {
        Files.createDirectories(root);
    }

    public String save(MultipartFile file)
            throws IOException {

        String generatedName = UUID.randomUUID()
                + "-"
                + file.getOriginalFilename();

        Files.copy(
                file.getInputStream(),
                root.resolve(generatedName),
                StandardCopyOption.REPLACE_EXISTING);

        return generatedName;
    }

    public Resource load(String filename)
            throws MalformedURLException {

        Path file = root.resolve(filename);

        return new UrlResource(file.toUri());
    }

    public void delete(String filename)
            throws IOException {

        Files.deleteIfExists(
                root.resolve(filename));
    }
}