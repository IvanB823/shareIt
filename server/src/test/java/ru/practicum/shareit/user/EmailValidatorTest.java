package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.validator.EmailValidator;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {

    @Test
    void isValidEmail_WithValidEmail_ShouldReturnTrue() {
        assertTrue(EmailValidator.isValidEmail("test@example.com"));
        assertTrue(EmailValidator.isValidEmail("user.name@domain.org"));
        assertTrue(EmailValidator.isValidEmail("email123@test-domain.net"));
        assertTrue(EmailValidator.isValidEmail("a@b.co"));
        assertTrue(EmailValidator.isValidEmail("user+tag@example.com"));
        assertTrue(EmailValidator.isValidEmail("user_name@sub.domain.com"));
        assertTrue(EmailValidator.isValidEmail("123@456.ru"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "plainaddress",
            "@missingdomain.com",
            "missing@.com",
            "missing@domain",
            "spaces @domain.com",
            "user@",
            "@domain.com",
            "user@domain.",
            "user@@domain.com",
            "user@domain@com",
            "user name@domain.com",
            "user@domain com",
            "user@domain.c",
            ""
    })
    void isValidEmail_WithInvalidEmail_ShouldReturnFalse(String email) {
        assertFalse(EmailValidator.isValidEmail(email));
    }

    @Test
    void isValidEmail_WithNullEmail_ShouldReturnFalse() {
        assertFalse(EmailValidator.isValidEmail(null));
    }

    @Test
    void isValidEmail_WithEmptyString_ShouldReturnFalse() {
        assertFalse(EmailValidator.isValidEmail(""));
    }

    @Test
    void isValidEmail_WithWhitespaceOnlyString_ShouldReturnFalse() {
        assertFalse(EmailValidator.isValidEmail("   "));
        assertFalse(EmailValidator.isValidEmail("\t"));
        assertFalse(EmailValidator.isValidEmail("\n"));
    }

    @Test
    void isValidEmail_WithEmailContainingWhitespace_ShouldTrimAndValidate() {
        assertTrue(EmailValidator.isValidEmail("  test@example.com  "));
        assertTrue(EmailValidator.isValidEmail("\tuser@domain.org\t"));
        assertTrue(EmailValidator.isValidEmail("\nvalid@email.net\n"));
    }

    @Test
    void isValidEmail_WithSpecialCharacters_ShouldHandleCorrectly() {
        assertTrue(EmailValidator.isValidEmail("test.email@example.com"));
        assertTrue(EmailValidator.isValidEmail("test_email@example.com"));
        assertTrue(EmailValidator.isValidEmail("test+tag@example.com"));
        assertTrue(EmailValidator.isValidEmail("test-email@example.com"));
        assertTrue(EmailValidator.isValidEmail("123@example.com"));

        assertFalse(EmailValidator.isValidEmail("test#email@example.com"));
        assertFalse(EmailValidator.isValidEmail("test$email@example.com"));
        assertFalse(EmailValidator.isValidEmail("test&email@example.com"));
        assertFalse(EmailValidator.isValidEmail("test*email@example.com"));
    }

    @Test
    void isValidEmail_WithInternationalDomains_ShouldWork() {
        assertTrue(EmailValidator.isValidEmail("user@example.ru"));
        assertTrue(EmailValidator.isValidEmail("test@domain.org"));
        assertTrue(EmailValidator.isValidEmail("email@site.info"));
        assertTrue(EmailValidator.isValidEmail("user@test.museum"));
    }

    @Test
    void isValidEmail_WithSubdomains_ShouldWork() {
        assertTrue(EmailValidator.isValidEmail("user@mail.example.com"));
        assertTrue(EmailValidator.isValidEmail("test@sub.domain.org"));
        assertTrue(EmailValidator.isValidEmail("email@very.long.subdomain.example.net"));
    }

    // Тесты для метода validateEmail()

    @Test
    void validateEmail_WithValidEmail_ShouldNotThrowException() {
        assertDoesNotThrow(() -> EmailValidator.validateEmail("test@example.com"));
        assertDoesNotThrow(() -> EmailValidator.validateEmail("user.name@domain.org"));
        assertDoesNotThrow(() -> EmailValidator.validateEmail("valid123@test.net"));
    }

    @Test
    void validateEmail_WithNullEmail_ShouldThrowIllegalArgumentException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> EmailValidator.validateEmail(null)
        );
        assertEquals("Email не должен быть пустым или null", exception.getMessage());
    }

    @Test
    void validateEmail_WithEmptyEmail_ShouldThrowIllegalArgumentException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> EmailValidator.validateEmail("")
        );
        assertEquals("Email не должен быть пустым или null", exception.getMessage());
    }

    @Test
    void validateEmail_WithWhitespaceOnlyEmail_ShouldThrowValidationException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> EmailValidator.validateEmail("   ")
        );
        assertEquals("Email не должен быть пустым или null", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "plainaddress",
            "@missingdomain.com",
            "missing@.com",
            "missing@domain",
            "user@",
            "@domain.com",
            "user@domain.",
            "user@@domain.com",
            "user name@domain.com"
    })
    void validateEmail_WithInvalidEmail_ShouldThrowValidationException(String email) {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> EmailValidator.validateEmail(email)
        );
        assertEquals("Email имеет неверный формат", exception.getMessage());
    }

    @Test
    void validateEmail_WithValidEmailContainingWhitespace_ShouldNotThrowException() {
        assertDoesNotThrow(() -> EmailValidator.validateEmail("  test@example.com  "));
        assertDoesNotThrow(() -> EmailValidator.validateEmail("\tuser@domain.org\t"));
    }

    @Test
    void validateEmail_ShouldTrimEmailBeforeValidation() {
        // Эти email'ы валидны после обрезки пробелов
        assertDoesNotThrow(() -> EmailValidator.validateEmail("  valid@email.com  "));
        assertDoesNotThrow(() -> EmailValidator.validateEmail("\ttest@domain.org\n"));

        // Этот email невалиден даже после обрезки пробелов
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> EmailValidator.validateEmail("  invalid@  ")
        );
        assertEquals("Email имеет неверный формат", exception.getMessage());
    }

    // Тесты граничных случаев

    @Test
    void emailValidation_WithMinimumValidEmail_ShouldWork() {
        String minEmail = "a@b.co"; // Минимально возможный валидный email
        assertTrue(EmailValidator.isValidEmail(minEmail));
        assertDoesNotThrow(() -> EmailValidator.validateEmail(minEmail));
    }

    @Test
    void emailValidation_WithVeryLongValidEmail_ShouldWork() {
        String longEmail = "very.long.email.address.with.many.dots@very.long.domain.name.example.com";
        assertTrue(EmailValidator.isValidEmail(longEmail));
        assertDoesNotThrow(() -> EmailValidator.validateEmail(longEmail));
    }

    @Test
    void emailValidation_WithNumericEmail_ShouldWork() {
        String numericEmail = "123456@789.ru";
        assertTrue(EmailValidator.isValidEmail(numericEmail));
        assertDoesNotThrow(() -> EmailValidator.validateEmail(numericEmail));
    }

    @Test
    void emailValidation_ConsistencyBetweenMethods() {
        String[] testEmails = {
                "valid@email.com",
                "invalid@",
                null,
                "",
                "  ",
                "test@example.org",
                "bad@email@domain.com"
        };

        for (String email : testEmails) {
            if (EmailValidator.isValidEmail(email)) {
                // Если isValidEmail возвращает true, validateEmail не должен бросать исключение
                assertDoesNotThrow(() -> EmailValidator.validateEmail(email),
                        "Inconsistency for email: " + email);
            } else {
                // Если isValidEmail возвращает false, validateEmail должен бросить исключение
                assertThrows(ValidationException.class,
                        () -> EmailValidator.validateEmail(email),
                        "Inconsistency for email: " + email);
            }
        }
    }
}