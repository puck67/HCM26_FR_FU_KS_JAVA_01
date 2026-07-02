package com.fpt.lms;

import com.fpt.lms.model.AssessmentMaterial;
import com.fpt.lms.repository.AssessmentMaterialRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Asm2ApplicationTests {

    @Autowired
    private AssessmentMaterialRepository repository;

    @Test
    void contextLoads() {
        assertNotNull(repository);
    }

    @Test
    void testSaveAndFind() {
        repository.deleteAll();
        AssessmentMaterial material = AssessmentMaterial.builder()
                .title("Integration Test")
                .description("Verifying database save works")
                .fileName("test.pdf")
                .build();
        AssessmentMaterial saved = repository.save(material);
        assertNotNull(saved.getId());
        
        List<AssessmentMaterial> list = repository.findAll();
        assertEquals(1, list.size());
        assertEquals("Integration Test", list.get(0).getTitle());
    }
}
