package com.fpt.lms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class StorageServiceTest {

    private StorageService storageService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        storageService = new StorageService(tempDir.toString());
        storageService.init();
    }

    @Test
    void testStoreAndLoadFile() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-file.txt",
                "text/plain",
                "storage service content".getBytes()
        );

        String storedName = storageService.store(file);
        assertEquals("test-file.txt", storedName);

        Path physicalFile = tempDir.resolve(storedName);
        assertTrue(Files.exists(physicalFile));

        Resource resource = storageService.loadAsResource(storedName);
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
        
        byte[] content = Files.readAllBytes(physicalFile);
        assertEquals("storage service content", new String(content));
    }

    @Test
    void testLoadNonExistentFileThrowsException() {
        assertThrows(RuntimeException.class, () -> storageService.loadAsResource("does-not-exist.txt"));
    }
}
