package fa.training.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Validator utility class.
 * No database connection required.
 */
class ValidatorTest {

    @Nested
    @DisplayName("ID Validation")
    class IdValidation {

        @Test
        @DisplayName("Valid alphanumeric ID should pass")
        void testValidId() {
            assertTrue(Validator.isValidId("MOV001"));
            assertTrue(Validator.isValidId("A1B2C3"));
            assertTrue(Validator.isValidId("abc"));
        }

        @Test
        @DisplayName("Null, blank, or too-long ID should fail")
        void testInvalidId() {
            assertFalse(Validator.isValidId(null));
            assertFalse(Validator.isValidId(""));
            assertFalse(Validator.isValidId("   "));
            assertFalse(Validator.isValidId("ABCDEFGHIJK"));
        }

        @Test
        @DisplayName("ID with special characters should fail")
        void testIdWithSpecialChars() {
            assertFalse(Validator.isValidId("MOV-001"));
            assertFalse(Validator.isValidId("MOV 001"));
            assertFalse(Validator.isValidId("MOV@01"));
        }
    }

    @Nested
    @DisplayName("Rating Validation")
    class RatingValidation {

        @Test
        @DisplayName("Rating within 0.0–10.0 should pass")
        void testValidRating() {
            assertTrue(Validator.isValidRating(0.0));
            assertTrue(Validator.isValidRating(5.5));
            assertTrue(Validator.isValidRating(10.0));
        }

        @Test
        @DisplayName("Rating outside 0.0–10.0 should fail")
        void testInvalidRating() {
            assertFalse(Validator.isValidRating(-0.1));
            assertFalse(Validator.isValidRating(10.1));
            assertFalse(Validator.isValidRating(-5.0));
            assertFalse(Validator.isValidRating(100.0));
        }
    }

    @Nested
    @DisplayName("Release Year Validation")
    class ReleaseYearValidation {

        @Test
        @DisplayName("Valid release years should pass")
        void testValidReleaseYear() {
            assertTrue(Validator.isValidReleaseYear(1888));
            assertTrue(Validator.isValidReleaseYear(2000));
            assertTrue(Validator.isValidReleaseYear(2024));
        }

        @Test
        @DisplayName("Year before 1888 or too far in future should fail")
        void testInvalidReleaseYear() {
            assertFalse(Validator.isValidReleaseYear(1887));
            assertFalse(Validator.isValidReleaseYear(0));
            assertFalse(Validator.isValidReleaseYear(-100));
            assertFalse(Validator.isValidReleaseYear(2050));
        }
    }

    @Nested
    @DisplayName("Title / Name Validation")
    class TextValidation {

        @Test
        @DisplayName("Non-blank title should pass")
        void testValidTitle() {
            assertTrue(Validator.isValidTitle("Inception"));
            assertTrue(Validator.isValidTitle("The Dark Knight"));
        }

        @Test
        @DisplayName("Null or blank title should fail")
        void testInvalidTitle() {
            assertFalse(Validator.isValidTitle(null));
            assertFalse(Validator.isValidTitle(""));
            assertFalse(Validator.isValidTitle("   "));
        }

        @Test
        @DisplayName("Non-blank name should pass")
        void testValidName() {
            assertTrue(Validator.isValidName("Christopher Nolan"));
        }

        @Test
        @DisplayName("Null or blank name should fail")
        void testInvalidName() {
            assertFalse(Validator.isValidName(null));
            assertFalse(Validator.isValidName(""));
        }
    }

    @Nested
    @DisplayName("Numeric String Validation")
    class NumericValidation {

        @ParameterizedTest
        @ValueSource(strings = {"1", "42", "999"})
        @DisplayName("Valid positive integer strings should pass")
        void testPositiveInteger(String value) {
            assertTrue(Validator.isPositiveInteger(value));
        }

        @ParameterizedTest
        @ValueSource(strings = {"0", "-1", "abc", "", "3.14"})
        @DisplayName("Non-positive or non-numeric strings should fail")
        void testInvalidPositiveInteger(String value) {
            assertFalse(Validator.isPositiveInteger(value));
        }

        @Test
        @DisplayName("Valid double strings should pass")
        void testValidDouble() {
            assertTrue(Validator.isValidDouble("3.14"));
            assertTrue(Validator.isValidDouble("0"));
            assertTrue(Validator.isValidDouble("-1.5"));
        }

        @Test
        @DisplayName("Non-numeric strings should fail as double")
        void testInvalidDouble() {
            assertFalse(Validator.isValidDouble("abc"));
            assertFalse(Validator.isValidDouble(null));
            assertFalse(Validator.isValidDouble(""));
        }
    }

    @Nested
    @DisplayName("Email Validation")
    class EmailValidation {

        @ParameterizedTest
        @ValueSource(strings = {"user@example.com", "test.name@domain.org", "a+b@sub.domain.co"})
        @DisplayName("Valid email formats should pass")
        void testValidEmail(String email) {
            assertTrue(Validator.isValidEmail(email));
        }

        @ParameterizedTest
        @ValueSource(strings = {"plaintext", "@missing.user", "user@", "user@.com", "user@domain"})
        @DisplayName("Invalid email formats should fail")
        void testInvalidEmail(String email) {
            assertFalse(Validator.isValidEmail(email));
        }

        @Test
        @DisplayName("Null email should fail")
        void testNullEmail() {
            assertFalse(Validator.isValidEmail(null));
        }
    }

    @Nested
    @DisplayName("Phone Validation")
    class PhoneValidation {

        @ParameterizedTest
        @ValueSource(strings = {"0901234567", "1234567", "123456789012345"})
        @DisplayName("Digits-only phone numbers should pass")
        void testValidPhone(String phone) {
            assertTrue(Validator.isValidPhone(phone));
        }

        @ParameterizedTest
        @ValueSource(strings = {"090-123-4567", "phone123", "+84901234567", "12345", ""})
        @DisplayName("Non-digit or too-short phone numbers should fail")
        void testInvalidPhone(String phone) {
            assertFalse(Validator.isValidPhone(phone));
        }

        @Test
        @DisplayName("Null phone should fail")
        void testNullPhone() {
            assertFalse(Validator.isValidPhone(null));
        }
    }
}
