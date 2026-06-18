package com.lms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AssessmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testListAssessments() throws Exception {
        mockMvc.perform(get("/assessments"))
                .andExpect(status().isOk())
                .andExpect(view().name("assessments"))
                .andExpect(model().attributeExists("assessments"));
    }

    @Test
    public void testUploadAssessmentSuccess() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test-assignment.pdf",
                "application/pdf",
                "Dummy content for pdf assessment".getBytes()
        );

        mockMvc.perform(multipart("/assessments/upload")
                .file(mockFile)
                .param("title", "Java Programming Assignment 1")
                .param("description", "Complete the validation task for the course entity."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/assessments"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    public void testUploadAssessmentEmptyFileFails() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "empty.pdf",
                "application/pdf",
                new byte[0]
        );

        mockMvc.perform(multipart("/assessments/upload")
                .file(mockFile)
                .param("title", "Empty Assignment")
                .param("description", "Should fail due to empty file"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/assessments"))
                .andExpect(flash().attribute("errorMessage", "Failed to upload: File is empty."));
    }
}
