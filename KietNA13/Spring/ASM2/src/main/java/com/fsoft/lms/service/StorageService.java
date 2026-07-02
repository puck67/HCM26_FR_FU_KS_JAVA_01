package com.fsoft.lms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.UUID;

/**
 * Service quản lý lưu trữ file upload trên đĩa.
 * Dùng UUID để tránh xung đột tên file khi tải lên đồng thời.
 */
@Service
public class StorageService {

    private static final Logger log = LoggerFactory.getLogger(StorageService.class);

    private final Path rootLocation;

    public StorageService(@Value("${storage.location:./uploads}") String storageLocation) {
        this.rootLocation = Paths.get(storageLocation).toAbsolutePath().normalize();
    }

    /** Khởi tạo thư mục lưu file khi ứng dụng start */
    public void initStorage() {
        try {
            Files.createDirectories(rootLocation);
            log.info("Storage initialized at: {}", rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location: " + rootLocation, e);
        }
    }

    /**
     * Lưu file upload vào thư mục storage.
     * Sinh UUID để tránh trùng tên, kiểm tra path traversal attack.
     *
     * @return tên file đã lưu trên đĩa
     */
    public String saveUploadedFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new StorageException("Cannot store an empty file");
        }

        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "unknown");

        if (originalFilename.contains("..")) {
            throw new StorageException(
                    "Security: filename contains invalid path traversal sequence: " + originalFilename);
        }

        // Lấy extension gốc, ghép với UUID để tạo tên duy nhất
        int dotIndex = originalFilename.lastIndexOf('.');
        String extension = (dotIndex >= 0) ? originalFilename.substring(dotIndex) : "";
        String storedFilename = UUID.randomUUID() + extension;

        Path destination = rootLocation.resolve(storedFilename);

        // Kiểm tra lại destination nằm trong rootLocation (phòng thủ kép)
        if (!destination.getParent().equals(rootLocation)) {
            throw new StorageException("Resolved destination is outside storage root.");
        }

        try (java.io.InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            log.info("File saved: {} -> {}", originalFilename, storedFilename);
        } catch (IOException e) {
            throw new StorageException("Failed to store file: " + originalFilename, e);
        }

        return storedFilename;
    }

    /**
     * Tải file từ thư mục storage theo tên file đã lưu.
     *
     * @throws StorageFileNotFoundException nếu file không tồn tại hoặc không đọc được
     */
    public Resource loadFileAsResource(String filename) {
        try {
            Path filePath = rootLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            throw new StorageFileNotFoundException("File not found or not readable: " + filename);
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Malformed file path: " + filename, e);
        }
    }

    /** Trả về đường dẫn tuyệt đối đến file trong storage */
    public Path resolveFilePath(String filename) {
        return rootLocation.resolve(filename).normalize();
    }
}
