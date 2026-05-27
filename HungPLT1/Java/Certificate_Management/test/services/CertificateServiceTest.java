package services;

import entities.Certificate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Validator;

import static org.junit.jupiter.api.Assertions.*;

public class CertificateServiceTest {

    private final CertificateService service = new CertificateService();
    private final String TEST_ID = "C_TEST";
    private final String TEST_USER_ID = "U001";

    @BeforeEach
    public void setUp() {
        service.deleteCertificate(TEST_ID);
    }

    @AfterEach
    public void tearDown() {
        service.deleteCertificate(TEST_ID);
    }

    @Test
    public void testInsertRecord() {
        Certificate cert = new Certificate(
                TEST_ID,
                "Test Certificate",
                "999999",
                "15/05/2026",
                "15/05/2028",
                3.5,
                TEST_USER_ID
        );
        boolean result = service.addCertificate(cert);
        assertTrue(result);
        
        Certificate found = service.findById(TEST_ID);
        assertNotNull(found);
        assertEquals("Test Certificate", found.getCertificateName());
    }

    @Test
    public void testFindById() {
        Certificate cert = service.findById("C001");
        assertNotNull(cert);
        assertEquals("C001", cert.getId());
        assertEquals("U001", cert.getUserId());
    }

    @Test
    public void testUpdateRecord() {
        Certificate cert = new Certificate(
                TEST_ID,
                "Test Certificate",
                "999999",
                "15/05/2026",
                "15/05/2028",
                3.5,
                TEST_USER_ID
        );
        service.addCertificate(cert);

        cert.setCertificateName("Updated Name");
        cert.setScore(4.0);
        boolean result = service.updateCertificate(cert);
        assertTrue(result);

        Certificate updated = service.findById(TEST_ID);
        assertNotNull(updated);
        assertEquals("Updated Name", updated.getCertificateName());
        assertEquals(4.0, updated.getScore(), 0.001);
    }

    @Test
    public void testDeleteRecord() {
        Certificate cert = new Certificate(
                TEST_ID,
                "Test Certificate",
                "999999",
                "15/05/2026",
                "15/05/2028",
                3.5,
                TEST_USER_ID
        );
        service.addCertificate(cert);

        boolean result = service.deleteCertificate(TEST_ID);
        assertTrue(result);

        Certificate found = service.findById(TEST_ID);
        assertNull(found);
    }

    @Test
    public void testValidation() {
        assertTrue(Validator.isValidEmail("test@email.com"));
        assertFalse(Validator.isValidEmail("invalid-email"));
        assertFalse(Validator.isValidEmail(""));
        assertFalse(Validator.isValidEmail(null));

        assertTrue(Validator.isValidPhone("0912345678"));
        assertFalse(Validator.isValidPhone("09123"));
        assertFalse(Validator.isValidPhone("abc1234567"));

        assertTrue(Validator.isValidScore(4.0));
        assertTrue(Validator.isValidScore(0.0));
        assertTrue(Validator.isValidScore(2.5));
        assertFalse(Validator.isValidScore(-0.5));
        assertFalse(Validator.isValidScore(4.1));

        assertTrue(Validator.isValidId("C001"));
        assertFalse(Validator.isValidId(""));
        assertFalse(Validator.isValidId("C12345678901"));
    }
}
