package fa.training.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    @Test
    @DisplayName("Should return true for valid IDs")
    void testIsValidId_ValidId_ReturnsTrue() {
        assertTrue(ValidationUtil.isValidId("ST-12345678", "ST"));
        assertTrue(ValidationUtil.isValidId("AP-ABCD1234", "AP"));
    }

    @Test
    @DisplayName("Should return false for invalid IDs")
    void testIsValidId_InvalidId_ReturnsFalse() {
        assertFalse(ValidationUtil.isValidId("ST-123", "ST")); // Too short
        assertFalse(ValidationUtil.isValidId("ST-123456789", "ST")); // Too long
        assertFalse(ValidationUtil.isValidId("AP-abcd1234", "AP")); // Lowercase is invalid
        assertFalse(ValidationUtil.isValidId(null, "ST")); // Null value
        assertFalse(ValidationUtil.isValidId("", "ST")); // Empty string
    }

    @ParameterizedTest
    @ValueSource(strings = { "abc@gmail.com", "test.user@domain.co", "hello_world123@yahoo.com.vn" })
    @DisplayName("Should return true for valid email formats")
    void testIsValidEmail_ValidEmail_ReturnsTrue(String email) {
        assertTrue(ValidationUtil.isValidEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = { "plainaddress", "@missingusername.com", "username@.com", "user@domain..com" })
    @DisplayName("Should return false for invalid email formats")
    void testIsValidEmail_InvalidEmail_ReturnsFalse(String email) {
        assertFalse(ValidationUtil.isValidEmail(email));
    }

    @Test
    @DisplayName("Should return false when email is null or empty")
    void testIsValidEmail_NullOrEmpty_ReturnsFalse() {
        assertFalse(ValidationUtil.isValidEmail(null));
        assertFalse(ValidationUtil.isValidEmail(""));
    }

    @ParameterizedTest
    @ValueSource(strings = { "0987654321", "0123456789", "0356789123" })
    @DisplayName("Should return true for valid 10-digit Vietnamese phone numbers")
    void testIsValidPhone_ValidPhone_ReturnsTrue(String phone) {
        assertTrue(ValidationUtil.isValidPhone(phone));
    }

    @ParameterizedTest
    @ValueSource(strings = { "1234567890", "012345678", "01234567890", "abc1234567" })
    @DisplayName("Should return false for invalid phone numbers")
    void testIsValidPhone_InvalidPhone_ReturnsFalse(String phone) {
        assertFalse(ValidationUtil.isValidPhone(phone));
    }
}
