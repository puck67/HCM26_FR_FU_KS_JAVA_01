package com.lms.assessment.service;

import com.lms.assessment.exception.StorageException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for file storage operations (Dependency Inversion Principle).
 * Controllers depend on this abstraction, not on the concrete implementation.
 */
public interface StorageService {

    /**
     * Initialises the upload directory. Called on application startup via @PostConstruct.
     */
    void init();

    /**
     * Persists the given multipart file to the configured upload directory.
     *
     * @param file the uploaded file
     * @return the sanitised filename under which the file was saved
     * @throws StorageException if the file is empty, the filename is invalid, or an I/O error occurs
     */
    String store(MultipartFile file) throws StorageException;

    /**
     * Loads the file with the given filename as a Spring {@link Resource}.
     *
     * @param filename the file to load
     * @return the file wrapped as a Resource
     * @throws StorageException if the file does not exist or is not readable
     */
    Resource loadAsResource(String filename) throws StorageException;
}
