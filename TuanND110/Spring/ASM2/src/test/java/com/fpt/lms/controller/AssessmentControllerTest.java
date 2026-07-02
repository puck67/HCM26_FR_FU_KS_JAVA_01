package com.fpt.lms.controller;

import com.fpt.lms.model.AssessmentMaterial;
import com.fpt.lms.service.StorageService;
import com.fpt.lms.service.AssessmentMaterialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AssessmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StorageService storageService;

    @Mock
    private AssessmentMaterialService assessmentMaterialService;

    @InjectMocks
    private AssessmentController assessmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(assessmentController)
                .setViewResolvers(new org.springframework.web.servlet.view.InternalResourceViewResolver("/templates/", ".html"))
                .build();
    }

    @Test
    void testListAssessments() throws Exception {
        when(assessmentMaterialService.findAll()).thenReturn(java.util.Collections.emptyList());
        mockMvc.perform(get("/assessments"))
                .andExpect(status().isOk())
                .andExpect(view().name("assessments"))
                .andExpect(model().attributeExists("materials"));
    }

    @Test
    void testUploadMaterialSuccess() throws Exception {
        mockMvc.perform(post("/assessments/upload")
                        .param("title", "Test Title")
                        .param("description", "Test Description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/assessments"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(assessmentMaterialService, times(1)).save(any(AssessmentMaterial.class));
    }

    @Test
    void testUploadMaterialFailure() throws Exception {
        when(assessmentMaterialService.save(any(AssessmentMaterial.class))).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/assessments/upload")
                        .param("title", "Test Title")
                        .param("description", "Test Description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/assessments"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

    @Test
    void testDownloadMaterialSuccess() throws Exception {
        Resource resource = new ByteArrayResource("test content".getBytes());
        when(storageService.loadAsResource("test.txt")).thenReturn(resource);

        mockMvc.perform(get("/assessments/download/test.txt"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"test.txt\""))
                .andExpect(content().bytes("test content".getBytes()));
    }

    @Test
    void testDownloadMaterialNotFound() throws Exception {
        when(storageService.loadAsResource("nonexistent.txt")).thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(get("/assessments/download/nonexistent.txt"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDownloadTemplate() throws Exception {
        mockMvc.perform(get("/assessments/template"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"assessment_template.csv\""))
                .andExpect(content().contentType("text/csv; charset=utf-8"));
    }

    @Test
    void testExportAssessments() throws Exception {
        AssessmentMaterial material = AssessmentMaterial.builder()
                .title("Exported Test")
                .description("Test Description")
                .fileName("exported.pdf")
                .build();
        when(assessmentMaterialService.findAll()).thenReturn(java.util.Collections.singletonList(material));

        mockMvc.perform(get("/assessments/export"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"assessments_export.csv\""))
                .andExpect(content().contentType("text/csv; charset=utf-8"));
    }

    @Test
    void testImportAssessments() throws Exception {
        String csvContent = "Title;Description;File Name\n" +
                "Imported Test;Description of Imported;imported.pdf\n";
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.csv", "text/csv", csvContent.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/assessments/import").file(file))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/assessments"));

        verify(assessmentMaterialService, times(1)).save(any(AssessmentMaterial.class));
    }
}
