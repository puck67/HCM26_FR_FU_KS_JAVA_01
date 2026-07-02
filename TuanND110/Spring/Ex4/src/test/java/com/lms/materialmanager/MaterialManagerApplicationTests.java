package com.lms.materialmanager;

import com.lms.materialmanager.entity.Material;
import com.lms.materialmanager.entity.Subject;
import com.lms.materialmanager.repository.MaterialRepository;
import com.lms.materialmanager.repository.SubjectRepository;
import com.lms.materialmanager.service.StorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class MaterialManagerApplicationTests {

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private MaterialRepository materialRepository;

	@Autowired
	private StorageService storageService;

	@Value("${app.upload.dir:uploads/materials}")
	private String uploadDir;

	@Test
	void contextLoads() {
	}

	@Test
	void testSubjectAndMaterialRelations() {
		// Create a test subject
		Subject subject = new Subject();
		subject.setSubjectCode("TEST01");
		subject.setSubjectName("Test Subject");
		subject.setDuration(10);
		Subject savedSubject = subjectRepository.save(subject);

		Assertions.assertNotNull(savedSubject.getSubjectId());

		// Create a test material associated with the subject
		Material material = new Material();
		material.setFileName("test_doc.pdf");
		material.setStoredFileName("abcd1234-test_doc.pdf");
		material.setFileSize(1024L);
		material.setFileType("application/pdf");
		material.setDescription("Test Document Description");
		material.setCategory("Lecture");
		material.setUploadDate(LocalDateTime.now());
		material.setSubject(savedSubject);
		savedSubject.getMaterials().add(material);

		Material savedMaterial = materialRepository.save(material);
		Assertions.assertNotNull(savedMaterial.getMaterialId());

		// Fetch and verify relation
		List<Material> materials = materialRepository.findBySubjectSubjectId(savedSubject.getSubjectId());
		Assertions.assertEquals(1, materials.size());
		Assertions.assertEquals("test_doc.pdf", materials.get(0).getFileName());
		Assertions.assertEquals("Lecture", materials.get(0).getCategory());

		// Clean up
		subjectRepository.delete(savedSubject);
	}

	@Test
	void testFileValidationBlockedFormats() {
		// Mock a blocked EXE file upload
		MockMultipartFile exeFile = new MockMultipartFile(
				"files",
				"malicious.exe",
				"application/octet-stream",
				"dummy content".getBytes()
		);

		// Verify that trying to store this file throws IllegalArgumentException with "Unsupported file format."
		IllegalArgumentException exception = Assertions.assertThrows(
				IllegalArgumentException.class,
				() -> storageService.storeFile(exeFile)
		);
		Assertions.assertEquals("Unsupported file format.", exception.getMessage());
		
		// Mock a blocked SH file upload
		MockMultipartFile shFile = new MockMultipartFile(
				"files",
				"script.sh",
				"text/x-shellscript",
				"echo Hello".getBytes()
		);
		
		exception = Assertions.assertThrows(
				IllegalArgumentException.class,
				() -> storageService.storeFile(shFile)
		);
		Assertions.assertEquals("Unsupported file format.", exception.getMessage());
	}

	@Test
	void testFileValidationAllowedFormats() throws IOException {
		// Mock an allowed PDF file upload
		MockMultipartFile pdfFile = new MockMultipartFile(
				"files",
				"lecture_notes.pdf",
				"application/pdf",
				"PDF content".getBytes()
		);

		// Storing should succeed and return a unique stored name
		String storedName = storageService.storeFile(pdfFile);
		Assertions.assertNotNull(storedName);
		Assertions.assertTrue(storedName.endsWith("-lecture_notes.pdf"));
		Assertions.assertEquals(8, storedName.indexOf("-")); // First 8 chars are UUID segment

		// Verify physical file was copied
		Path physicalFile = Paths.get(uploadDir).resolve(storedName);
		Assertions.assertTrue(Files.exists(physicalFile));

		// Clean up physical file
		storageService.deleteFile(storedName);
		Assertions.assertFalse(Files.exists(physicalFile));
	}

	@Test
	void testFileSizeLimitValidation() {
		// Mock a file larger than 10MB
		byte[] largeBytes = new byte[11 * 1024 * 1024]; // 11MB
		MockMultipartFile largeFile = new MockMultipartFile(
				"files",
				"large.pdf",
				"application/pdf",
				largeBytes
		);

		IllegalArgumentException exception = Assertions.assertThrows(
				IllegalArgumentException.class,
				() -> storageService.storeFile(largeFile)
		);
		Assertions.assertEquals("Maximum file size is 10MB.", exception.getMessage());
	}

	@Test
	void testSearchQueryMatching() {
		Subject subject = new Subject();
		subject.setSubjectCode("SRCH01");
		subject.setSubjectName("Search Testing Class");
		subject.setDuration(12);
		Subject savedSubject = subjectRepository.save(subject);

		Material mat1 = new Material();
		mat1.setFileName("Java_Quickstart.pdf");
		mat1.setStoredFileName("uuid1111-Java_Quickstart.pdf");
		mat1.setFileSize(500L);
		mat1.setFileType("application/pdf");
		mat1.setDescription("Learn programming fast");
		mat1.setCategory("Reference");
		mat1.setSubject(savedSubject);
		savedSubject.getMaterials().add(mat1);
		materialRepository.save(mat1);

		Material mat2 = new Material();
		mat2.setFileName("Database_SQL_Lab.zip");
		mat2.setStoredFileName("uuid2222-Database_SQL_Lab.zip");
		mat2.setFileSize(25000L);
		mat2.setFileType("application/zip");
		mat2.setDescription("SQL homework template files");
		mat2.setCategory("Assignment");
		mat2.setSubject(savedSubject);
		savedSubject.getMaterials().add(mat2);
		materialRepository.save(mat2);

		// Search by File Name (case insensitive)
		List<Material> nameResults = materialRepository.searchMaterials("quickstart");
		Assertions.assertEquals(1, nameResults.size());
		Assertions.assertEquals("Java_Quickstart.pdf", nameResults.get(0).getFileName());

		// Search by Description
		List<Material> descResults = materialRepository.searchMaterials("homework");
		Assertions.assertEquals(1, descResults.size());
		Assertions.assertEquals("Database_SQL_Lab.zip", descResults.get(0).getFileName());

		// Search by Subject Name
		List<Material> subjResults = materialRepository.searchMaterials("testing");
		Assertions.assertEquals(2, subjResults.size());

		// Clean up
		subjectRepository.delete(savedSubject);
	}
}
