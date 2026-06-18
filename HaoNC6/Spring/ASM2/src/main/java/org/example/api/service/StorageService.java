package org.example.api.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for handling file storage operations.
 * Task 2: File Service for Handling Storage Logic
 */
public interface StorageService {

    /**
     * Initializes the upload directory on startup.
     */
    void init();

    /**
     * Stores the given multipart file to the upload directory.
     *
     * @param file the multipart file to store
     * @return the stored filename (may be prefixed with a timestamp to avoid collisions)
     */
    String store(MultipartFile file);

    /**
     * Loads a file as a Spring Resource by its filename.
     *
     * @param filename the name of the file on disk
     * @return a Resource representing the file
     */
    Resource loadAsResource(String filename);
}
