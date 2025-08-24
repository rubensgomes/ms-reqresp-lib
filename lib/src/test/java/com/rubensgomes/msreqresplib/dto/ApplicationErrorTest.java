/*
 * Copyright 2025 Rubens Gomes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rubensgomes.msreqresplib.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.rubensgomes.msreqresplib.error.ErrorCode;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * Unit tests for the {@link ApplicationError} record.
 *
 * <p>This test class verifies the ApplicationError record implementation including validation
 * constraints, Error interface compliance, and record behavior such as immutability,
 * equals/hashCode, and toString methods.
 *
 * @author Rubens Gomes
 * @since 0.0.2
 */
@DisplayName("ApplicationError Record Tests")
class ApplicationErrorTest {

  private Validator validator;

  /** Test implementation of the ErrorCode interface for testing purposes. */
  private static class TestErrorCode implements ErrorCode {
    private final String code;
    private final String description;

    public TestErrorCode(String code, String description) {
      this.code = code;
      this.description = description;
    }

    @Override
    public String getCode() {
      return code;
    }

    @Override
    public String getDescription() {
      return description;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      TestErrorCode that = (TestErrorCode) o;
      return java.util.Objects.equals(code, that.code)
          && java.util.Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
      return java.util.Objects.hash(code, description);
    }

    @Override
    public String toString() {
      return String.format("TestErrorCode{code='%s', description='%s'}", code, description);
    }
  }

  @BeforeEach
  void setUp() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  @Test
  @DisplayName("Should create ApplicationError with valid parameters")
  void shouldCreateApplicationErrorWithValidParameters() {
    // Given
    String errorDescription = "Database connection failed";
    ErrorCode errorCode = new TestErrorCode("DB_001", "Database error");
    String nativeErrorText = "java.sql.SQLException: Connection refused";

    // When
    ApplicationError error = new ApplicationError(errorDescription, errorCode, nativeErrorText);

    // Then
    assertNotNull(error);
    assertEquals(errorDescription, error.errorDescription());
    assertEquals(errorCode, error.errorCode());
    assertEquals(nativeErrorText, error.nativeErrorText());
  }

  @Test
  @DisplayName("Should create ApplicationError with null native error text")
  void shouldCreateApplicationErrorWithNullNativeErrorText() {
    // Given
    String errorDescription = "Validation failed";
    ErrorCode errorCode = new TestErrorCode("VAL_001", "Validation error");

    // When
    ApplicationError error = new ApplicationError(errorDescription, errorCode, null);

    // Then
    assertNotNull(error);
    assertEquals(errorDescription, error.errorDescription());
    assertEquals(errorCode, error.errorCode());
    assertNull(error.nativeErrorText());
  }

  @Test
  @DisplayName("Should implement Error interface methods correctly")
  void shouldImplementErrorInterfaceMethodsCorrectly() {
    // Given
    String errorDescription = "API call failed";
    ErrorCode errorCode = new TestErrorCode("API_001", "API error");
    String nativeErrorText = "HTTP 500: Internal Server Error";
    ApplicationError error = new ApplicationError(errorDescription, errorCode, nativeErrorText);

    // When & Then
    assertEquals(errorDescription, error.errorDescription());
    assertEquals(errorCode, error.errorCode());
    assertEquals(nativeErrorText, error.nativeErrorText());
  }

  @Test
  @DisplayName("Should pass validation with valid fields")
  void shouldPassValidationWithValidFields() {
    // Given
    ApplicationError error =
        new ApplicationError(
            "Valid error description",
            new TestErrorCode("VALID_001", "Valid error"),
            "Valid native text");

    // When
    Set<ConstraintViolation<ApplicationError>> violations = validator.validate(error);

    // Then
    assertTrue(violations.isEmpty(), "Should have no validation violations");
  }

  @Test
  @DisplayName("Should fail validation when errorDescription is null")
  void shouldFailValidationWhenErrorDescriptionIsNull() {
    // Given
    ApplicationError error =
        new ApplicationError(
            null, new TestErrorCode("VALID_001", "Valid error"), "Valid native text");

    // When
    Set<ConstraintViolation<ApplicationError>> violations = validator.validate(error);

    // Then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());
    ConstraintViolation<ApplicationError> violation = violations.iterator().next();
    assertEquals("errorDescription", violation.getPropertyPath().toString());
    assertTrue(
        violation.getMessage().contains("must not be blank")
            || violation.getMessage().contains("NotBlank"));
  }

  @Test
  @DisplayName("Should fail validation when errorDescription is empty")
  void shouldFailValidationWhenErrorDescriptionIsEmpty() {
    // Given
    ApplicationError error =
        new ApplicationError(
            "", new TestErrorCode("VALID_001", "Valid error"), "Valid native text");

    // When
    Set<ConstraintViolation<ApplicationError>> violations = validator.validate(error);

    // Then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());
    ConstraintViolation<ApplicationError> violation = violations.iterator().next();
    assertEquals("errorDescription", violation.getPropertyPath().toString());
  }

  @Test
  @DisplayName("Should fail validation when errorDescription is whitespace only")
  void shouldFailValidationWhenErrorDescriptionIsWhitespaceOnly() {
    // Given
    ApplicationError error =
        new ApplicationError(
            "   ", new TestErrorCode("VALID_001", "Valid error"), "Valid native text");

    // When
    Set<ConstraintViolation<ApplicationError>> violations = validator.validate(error);

    // Then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());
    ConstraintViolation<ApplicationError> violation = violations.iterator().next();
    assertEquals("errorDescription", violation.getPropertyPath().toString());
  }

  @Test
  @DisplayName("Should fail validation when errorCode is null")
  void shouldFailValidationWhenErrorCodeIsNull() {
    // Given
    ApplicationError error =
        new ApplicationError("Valid error description", null, "Valid native text");

    // When
    Set<ConstraintViolation<ApplicationError>> violations = validator.validate(error);

    // Then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());
    ConstraintViolation<ApplicationError> violation = violations.iterator().next();
    assertEquals("errorCode", violation.getPropertyPath().toString());
    assertTrue(
        violation.getMessage().contains("must not be null")
            || violation.getMessage().contains("NotNull"));
  }

  @Test
  @DisplayName("Should pass validation when nativeErrorText is null")
  void shouldPassValidationWhenNativeErrorTextIsNull() {
    // Given
    ApplicationError error =
        new ApplicationError(
            "Valid error description", new TestErrorCode("VALID_001", "Valid error"), null);

    // When
    Set<ConstraintViolation<ApplicationError>> violations = validator.validate(error);

    // Then
    assertTrue(
        violations.isEmpty(), "Should have no validation violations when nativeErrorText is null");
  }

  @Test
  @DisplayName("Should be equal when all fields are equal")
  void shouldBeEqualWhenAllFieldsAreEqual() {
    // Given
    ErrorCode errorCode1 = new TestErrorCode("TEST_001", "Test error");
    ErrorCode errorCode2 = new TestErrorCode("TEST_001", "Test error");

    ApplicationError error1 = new ApplicationError("Test error", errorCode1, "Test native");
    ApplicationError error2 = new ApplicationError("Test error", errorCode2, "Test native");

    // When & Then
    assertEquals(error1, error2);
    assertEquals(error1.hashCode(), error2.hashCode());
  }

  @Test
  @DisplayName("Should not be equal when errorDescription differs")
  void shouldNotBeEqualWhenErrorDescriptionDiffers() {
    // Given
    ErrorCode errorCode = new TestErrorCode("TEST_001", "Test error");
    ApplicationError error1 = new ApplicationError("Error 1", errorCode, "Test native");
    ApplicationError error2 = new ApplicationError("Error 2", errorCode, "Test native");

    // When & Then
    assertNotEquals(error1, error2);
    assertNotEquals(error1.hashCode(), error2.hashCode());
  }

  @Test
  @DisplayName("Should not be equal when errorCode differs")
  void shouldNotBeEqualWhenErrorCodeDiffers() {
    // Given
    ErrorCode errorCode1 = new TestErrorCode("TEST_001", "Test error");
    ErrorCode errorCode2 = new TestErrorCode("TEST_002", "Different error");
    ApplicationError error1 = new ApplicationError("Test error", errorCode1, "Test native");
    ApplicationError error2 = new ApplicationError("Test error", errorCode2, "Test native");

    // When & Then
    assertNotEquals(error1, error2);
    assertNotEquals(error1.hashCode(), error2.hashCode());
  }

  @Test
  @DisplayName("Should not be equal when nativeErrorText differs")
  void shouldNotBeEqualWhenNativeErrorTextDiffers() {
    // Given
    ErrorCode errorCode = new TestErrorCode("TEST_001", "Test error");
    ApplicationError error1 = new ApplicationError("Test error", errorCode, "Native 1");
    ApplicationError error2 = new ApplicationError("Test error", errorCode, "Native 2");

    // When & Then
    assertNotEquals(error1, error2);
    assertNotEquals(error1.hashCode(), error2.hashCode());
  }

  @Test
  @DisplayName("Should handle null nativeErrorText in equality")
  void shouldHandleNullNativeErrorTextInEquality() {
    // Given
    ErrorCode errorCode = new TestErrorCode("TEST_001", "Test error");
    ApplicationError error1 = new ApplicationError("Test error", errorCode, null);
    ApplicationError error2 = new ApplicationError("Test error", errorCode, null);
    ApplicationError error3 = new ApplicationError("Test error", errorCode, "Some text");

    // When & Then
    assertEquals(error1, error2);
    assertEquals(error1.hashCode(), error2.hashCode());
    assertNotEquals(error1, error3);
    assertNotEquals(error1.hashCode(), error3.hashCode());
  }

  @Test
  @DisplayName("Should include all fields in toString")
  void shouldIncludeAllFieldsInToString() {
    // Given
    ErrorCode errorCode = new TestErrorCode("TEST_001", "Test error");
    ApplicationError error = new ApplicationError("Test error", errorCode, "Test native");

    // When
    String result = error.toString();

    // Then
    assertNotNull(result);
    assertTrue(result.contains("Test error"));
    assertTrue(result.contains("TEST_001"));
    assertTrue(result.contains("Test native"));
  }

  @Test
  @DisplayName("Should handle null nativeErrorText in toString")
  void shouldHandleNullNativeErrorTextInToString() {
    // Given
    ErrorCode errorCode = new TestErrorCode("TEST_001", "Test error");
    ApplicationError error = new ApplicationError("Test error", errorCode, null);

    // When
    String result = error.toString();

    // Then
    assertNotNull(result);
    assertTrue(result.contains("Test error"));
    assertTrue(result.contains("TEST_001"));
    assertTrue(result.contains("null") || result.contains("nativeErrorText=null"));
  }

  @Test
  @DisplayName("Should be immutable")
  void shouldBeImmutable() {
    // Given
    ErrorCode errorCode = new TestErrorCode("TEST_001", "Test error");
    ApplicationError error = new ApplicationError("Test error", errorCode, "Test native");

    // When & Then - Record fields are final by design
    assertEquals("Test error", error.errorDescription());
    assertEquals(errorCode, error.errorCode());
    assertEquals("Test native", error.nativeErrorText());

    // Create new instance to verify immutability
    ApplicationError error2 =
        new ApplicationError("Different error", errorCode, "Different native");
    assertNotEquals(error, error2);
  }

  @Test
  @DisplayName("Should handle long strings in all fields")
  void shouldHandleLongStringsInAllFields() {
    // Given
    String longDescription = "A".repeat(2000);
    String longNativeText = "B".repeat(3000);
    ErrorCode longCodeDescription = new TestErrorCode("LONG_001", "C".repeat(1000));
    ApplicationError error =
        new ApplicationError(longDescription, longCodeDescription, longNativeText);

    // When & Then
    assertEquals(longDescription, error.errorDescription());
    assertEquals(longCodeDescription, error.errorCode());
    assertEquals(longNativeText, error.nativeErrorText());
    assertEquals(2000, error.errorDescription().length());
    assertEquals(3000, error.nativeErrorText().length());

    // Validation should still pass
    Set<ConstraintViolation<ApplicationError>> violations = validator.validate(error);
    assertTrue(violations.isEmpty());
  }

  @Test
  @DisplayName("Should handle special characters and Unicode")
  void shouldHandleSpecialCharactersAndUnicode() {
    // Given
    String specialDescription = "Error with emojis: 🚀 ❌ and unicode: 测试";
    String specialNativeText = "Native text with symbols: !@#$%^&*() 测试";
    ErrorCode specialCode = new TestErrorCode("SPECIAL_001", "Special characters error");
    ApplicationError error =
        new ApplicationError(specialDescription, specialCode, specialNativeText);

    // When & Then
    assertEquals(specialDescription, error.errorDescription());
    assertEquals(specialCode, error.errorCode());
    assertEquals(specialNativeText, error.nativeErrorText());

    // Validation should pass
    Set<ConstraintViolation<ApplicationError>> violations = validator.validate(error);
    assertTrue(violations.isEmpty());
  }

  @Test
  @DisplayName("Should work with different ErrorCode implementations")
  void shouldWorkWithDifferentErrorCodeImplementations() {
    // Given
    ErrorCode hierarchicalCode =
        new TestErrorCode("DB_CONNECTION_FAILED", "Database connection failed");
    ErrorCode numericCode = new TestErrorCode("ERR_001", "Generic error occurred");
    ErrorCode httpStyleCode = new TestErrorCode("404_NOT_FOUND", "Resource not found");

    ApplicationError error1 =
        new ApplicationError("Database error", hierarchicalCode, "SQL exception");
    ApplicationError error2 =
        new ApplicationError("Generic error", numericCode, "Unknown exception");
    ApplicationError error3 =
        new ApplicationError("Resource error", httpStyleCode, "HTTP exception");

    // When & Then
    assertEquals("DB_CONNECTION_FAILED", error1.errorCode().getCode());
    assertEquals("ERR_001", error2.errorCode().getCode());
    assertEquals("404_NOT_FOUND", error3.errorCode().getCode());

    // All should pass validation
    assertTrue(validator.validate(error1).isEmpty());
    assertTrue(validator.validate(error2).isEmpty());
    assertTrue(validator.validate(error3).isEmpty());
  }

  @Test
  @DisplayName("Should support real-world error scenarios")
  void shouldSupportRealWorldErrorScenarios() {
    // Database error scenario
    ApplicationError dbError =
        new ApplicationError(
            "Unable to connect to database",
            new TestErrorCode("DB_CONNECTION_TIMEOUT", "Database connection timed out"),
            "java.sql.SQLTimeoutException: Connection timed out after 30000ms");

    // API error scenario
    ApplicationError apiError =
        new ApplicationError(
            "Too many requests",
            new TestErrorCode("API_RATE_LIMIT_EXCEEDED", "API rate limit exceeded"),
            "HTTP 429: Rate limit exceeded - try again in 60 seconds");

    // Validation error scenario
    ApplicationError validationError =
        new ApplicationError(
            "Required field is missing",
            new TestErrorCode("VALIDATION_REQUIRED_FIELD", "Required field missing"),
            "Field 'username' cannot be null or empty");

    // All should be valid and work correctly
    assertTrue(validator.validate(dbError).isEmpty());
    assertTrue(validator.validate(apiError).isEmpty());
    assertTrue(validator.validate(validationError).isEmpty());

    assertEquals("DB_CONNECTION_TIMEOUT", dbError.errorCode().getCode());
    assertEquals("API_RATE_LIMIT_EXCEEDED", apiError.errorCode().getCode());
    assertEquals("VALIDATION_REQUIRED_FIELD", validationError.errorCode().getCode());
  }

  @Test
  @DisplayName("Should support programmatic error handling")
  void shouldSupportProgrammaticErrorHandling() {
    // Given
    ApplicationError error =
        new ApplicationError(
            "Validation error occurred",
            new TestErrorCode("VALIDATION_FAILED", "Input validation failed"),
            "Field 'email' is required");

    // When - Programmatic handling by code
    String codeId = error.errorCode().getCode();
    String handling =
        switch (codeId) {
          case "VALIDATION_FAILED" -> "Handle validation error";
          case "DB_CONNECTION_FAILED" -> "Handle database error";
          default -> "Handle unknown error";
        };

    // Then
    assertEquals("Handle validation error", handling);
    assertEquals("Input validation failed", error.errorCode().getDescription());
    assertEquals("Validation error occurred", error.errorDescription());
  }
}
