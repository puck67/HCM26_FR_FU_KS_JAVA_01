package fa.training.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @Test
    void testValidId() {
        assertTrue(Validator.isValidId("V-001"));
        assertTrue(Validator.isValidId("V1"));
        assertTrue(Validator.isValidId("abcde12345")); // exactly 10 chars
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "V 001", "V0000000001", "V_001", "V@1"})
    void testInvalidId(String id) {
        assertFalse(Validator.isValidId(id));
    }

    @Test
    void testValidPrice() {
        assertTrue(Validator.isValidPrice(1.0));
        assertTrue(Validator.isValidPrice(1234.56));
    }

    @Test
    void testInvalidPrice() {
        assertFalse(Validator.isValidPrice(0.0));
        assertFalse(Validator.isValidPrice(-50.0));
    }

    @ParameterizedTest
    @ValueSource(strings = {"test@example.com", "user.name+tag+3@example.co.uk", "a@b.co"})
    void testValidEmail(String email) {
        assertTrue(Validator.isValidEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"plainaddress", "#@%^%#$@#$@#.com", "@example.com", "Joe Smith <email@example.com>", "email.example.com"})
    void testInvalidEmail(String email) {
        assertFalse(Validator.isValidEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0987654321", "01234567890"})
    void testValidPhone(String phone) {
        assertTrue(Validator.isValidPhone(phone));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456", "098765432100", "098-765-4321", "abcde12345"})
    void testInvalidPhone(String phone) {
        assertFalse(Validator.isValidPhone(phone));
    }

    @Test
    void testValidName() {
        assertTrue(Validator.isValidName("Toyota"));
        assertTrue(Validator.isValidName("Sedan"));
        assertFalse(Validator.isValidName(null));
        assertFalse(Validator.isValidName(""));
        assertFalse(Validator.isValidName("   "));
        assertFalse(Validator.isValidName("a".repeat(101)));
    }

    @Test
    void testValidCountry() {
        assertTrue(Validator.isValidCountry("Japan"));
        assertTrue(Validator.isValidCountry("USA"));
        assertFalse(Validator.isValidCountry(null));
        assertFalse(Validator.isValidCountry(""));
        assertFalse(Validator.isValidCountry("   "));
        assertFalse(Validator.isValidCountry("a".repeat(51)));
    }

    @Test
    void testValidDescription() {
        assertTrue(Validator.isValidDescription("Comfortable passenger car"));
        assertTrue(Validator.isValidDescription(null));
        assertTrue(Validator.isValidDescription(""));
        assertFalse(Validator.isValidDescription("a".repeat(256)));
    }
}
