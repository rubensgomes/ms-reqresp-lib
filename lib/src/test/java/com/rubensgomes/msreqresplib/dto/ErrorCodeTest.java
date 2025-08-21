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

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * Unit tests for the {@link ErrorCode} interface.
 *
 * <p>This test class verifies the contract of the ErrorCode interface by testing concrete
 * implementations that cover all the interface methods and validation constraints.
 *
 * @author Rubens Gomes
 */
@DisplayName("ErrorCode Interface Tests")
class ErrorCodeTest {

  private Validator validator;
  private ValidatorFactory factory;
  private ErrorCode errorCode;

  /** Simple test implementation of the ErrorCode interface for testing purposes. */
  private static class TestErrorCodeImpl implements ErrorCode {
    private final String code;
    private final String description;

    public TestErrorCodeImpl(String code, String description) {
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
      TestErrorCodeImpl that = (TestErrorCodeImpl) o;
      return java.util.Objects.equals(code, that.code)
          && java.util.Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
      return java.util.Objects.hash(code, description);
    }

    @Override
    public String toString() {
      return String.format("TestErrorCodeImpl{code='%s', description='%s'}", code, description);
    }
  }

  /** Enum-based test implementation of ErrorCode interface to test typical usage patterns. */
  private enum TestErrorCodeEnum implements ErrorCode {
    VALIDATION_FAILED("VALIDATION_FAILED", "Input validation failed"),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", "The requested resource could not be found"),
    UNAUTHORIZED_ACCESS("UNAUTHORIZED_ACCESS", "Access denied - insufficient permissions"),
    INTERNAL_ERROR("INTERNAL_ERROR", "An internal server error occurred");

    private final String code;
    private final String description;

    TestErrorCodeEnum(String code, String description) {
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
  }

  @BeforeEach
  void setUp() {
    factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
    errorCode = new TestErrorCodeImpl("DB_CONNECTION_FAILED", "Database connection failed");
  }

  @Test
  @DisplayName("getCode should return the error code identifier")
  void getCode_shouldReturnCodeIdentifier() {
    // When
    String result = errorCode.getCode();

    // Then
    assertEquals("DB_CONNECTION_FAILED", result);
    assertNotNull(result);
  }

  @Test
  @DisplayName("getDescription should return the error description")
  void getDescription_shouldReturnDescription() {
    // When
    String result = errorCode.getDescription();

    // Then
    assertEquals("Database connection failed", result);
    assertNotNull(result);
  }

  @Test
  @DisplayName("Validation should pass when both code and description are valid")
  void validation_shouldPass_whenBothFieldsAreValid() {
    // Given
    ErrorCode validErrorCode = new TestErrorCodeImpl("VALID_CODE", "Valid description");

    // When
    Set<ConstraintViolation<ErrorCode>> violations = validator.validate(validErrorCode);

    // Then
    assertTrue(violations.isEmpty(), "Should have no validation violations");
  }

  @Test
  @DisplayName("Validation should fail when code is null")
  void validation_shouldFail_whenCodeIsNull() {
    // Given
    ErrorCode invalidErrorCode = new TestErrorCodeImpl(null, "Valid description");

    // When
    Set<ConstraintViolation<ErrorCode>> violations = validator.validate(invalidErrorCode);

    // Then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());
    ConstraintViolation<ErrorCode> violation = violations.iterator().next();
    assertEquals("code", violation.getPropertyPath().toString());
  }

  @Test
  @DisplayName("Validation should fail when code is blank")
  void validation_shouldFail_whenCodeIsBlank() {
    // Given
    ErrorCode invalidErrorCode = new TestErrorCodeImpl("", "Valid description");

    // When
    Set<ConstraintViolation<ErrorCode>> violations = validator.validate(invalidErrorCode);

    // Then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());
    ConstraintViolation<ErrorCode> violation = violations.iterator().next();
    assertEquals("code", violation.getPropertyPath().toString());
  }

  @Test
  @DisplayName("Validation should fail when code is whitespace only")
  void validation_shouldFail_whenCodeIsWhitespaceOnly() {
    // Given
    ErrorCode invalidErrorCode = new TestErrorCodeImpl("   ", "Valid description");

    // When
    Set<ConstraintViolation<ErrorCode>> violations = validator.validate(invalidErrorCode);

    // Then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());
    ConstraintViolation<ErrorCode> violation = violations.iterator().next();
    assertEquals("code", violation.getPropertyPath().toString());
  }

  @Test
  @DisplayName("Validation should fail when description is null")
  void validation_shouldFail_whenDescriptionIsNull() {
    // Given
    ErrorCode invalidErrorCode = new TestErrorCodeImpl("VALID_CODE", null);

    // When
    Set<ConstraintViolation<ErrorCode>> violations = validator.validate(invalidErrorCode);

    // Then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());
    ConstraintViolation<ErrorCode> violation = violations.iterator().next();
    assertEquals("description", violation.getPropertyPath().toString());
  }

  @Test
  @DisplayName("Validation should fail when description is blank")
  void validation_shouldFail_whenDescriptionIsBlank() {
    // Given
    ErrorCode invalidErrorCode = new TestErrorCodeImpl("VALID_CODE", "");

    // When
    Set<ConstraintViolation<ErrorCode>> violations = validator.validate(invalidErrorCode);

    // Then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());
    ConstraintViolation<ErrorCode> violation = violations.iterator().next();
    assertEquals("description", violation.getPropertyPath().toString());
  }

  @Test
  @DisplayName("Validation should fail when both code and description are invalid")
  void validation_shouldFail_whenBothFieldsAreInvalid() {
    // Given
    ErrorCode invalidErrorCode = new TestErrorCodeImpl(null, null);

    // When
    Set<ConstraintViolation<ErrorCode>> violations = validator.validate(invalidErrorCode);

    // Then
    assertEquals(2, violations.size(), "Should have violations for both code and description");
  }

  @Test
  @DisplayName("Should handle different code formats correctly")
  void shouldHandleDifferentCodeFormats() {
    // Test various recommended code formats

    // Hierarchical format
    ErrorCode hierarchical =
        new TestErrorCodeImpl("DB_CONNECTION_FAILED", "Database connection failed");
    assertEquals("DB_CONNECTION_FAILED", hierarchical.getCode());

    // Numeric format
    ErrorCode numeric = new TestErrorCodeImpl("ERR_001", "Generic error");
    assertEquals("ERR_001", numeric.getCode());

    // HTTP-style format
    ErrorCode httpStyle = new TestErrorCodeImpl("404_NOT_FOUND", "Resource not found");
    assertEquals("404_NOT_FOUND", httpStyle.getCode());

    // Validation should pass for all formats
    assertTrue(validator.validate(hierarchical).isEmpty());
    assertTrue(validator.validate(numeric).isEmpty());
    assertTrue(validator.validate(httpStyle).isEmpty());
  }

  @Test
  @DisplayName("Should handle long strings in both fields")
  void shouldHandleLongStrings() {
    // Given
    String longCode = "A".repeat(1000);
    String longDescription = "B".repeat(2000);
    ErrorCode longErrorCode = new TestErrorCodeImpl(longCode, longDescription);

    // When & Then
    assertEquals(longCode, longErrorCode.getCode());
    assertEquals(longDescription, longErrorCode.getDescription());
    assertEquals(1000, longErrorCode.getCode().length());
    assertEquals(2000, longErrorCode.getDescription().length());

    // Validation should still pass
    Set<ConstraintViolation<ErrorCode>> violations = validator.validate(longErrorCode);
    assertTrue(violations.isEmpty());
  }

  @Test
  @DisplayName("Should handle special characters and Unicode")
  void shouldHandleSpecialCharactersAndUnicode() {
    // Given
    String specialCode = "ERROR_WITH_SPECIAL_CHARS_!@#$%";
    String unicodeDescription = "Error with unicode: ñáéíóú 测试 🚀 ❌";
    ErrorCode specialErrorCode = new TestErrorCodeImpl(specialCode, unicodeDescription);

    // When & Then
    assertEquals(specialCode, specialErrorCode.getCode());
    assertEquals(unicodeDescription, specialErrorCode.getDescription());

    // Validation should pass
    Set<ConstraintViolation<ErrorCode>> violations = validator.validate(specialErrorCode);
    assertTrue(violations.isEmpty());
  }

  @Test
  @DisplayName("Enum implementation should work correctly")
  void enumImplementation_shouldWorkCorrectly() {
    // Test each enum value
    ErrorCode validationFailed = TestErrorCodeEnum.VALIDATION_FAILED;
    assertEquals("VALIDATION_FAILED", validationFailed.getCode());
    assertEquals("Input validation failed", validationFailed.getDescription());

    ErrorCode notFound = TestErrorCodeEnum.RESOURCE_NOT_FOUND;
    assertEquals("RESOURCE_NOT_FOUND", notFound.getCode());
    assertEquals("The requested resource could not be found", notFound.getDescription());

    ErrorCode unauthorized = TestErrorCodeEnum.UNAUTHORIZED_ACCESS;
    assertEquals("UNAUTHORIZED_ACCESS", unauthorized.getCode());
    assertEquals("Access denied - insufficient permissions", unauthorized.getDescription());

    // Validation should pass for all enum values
    assertTrue(validator.validate(validationFailed).isEmpty());
    assertTrue(validator.validate(notFound).isEmpty());
    assertTrue(validator.validate(unauthorized).isEmpty());
  }

  @Test
  @DisplayName("Multiple ErrorCode instances should be independent")
  void multipleInstances_shouldBeIndependent() {
    // Given
    ErrorCode errorCode1 = new TestErrorCodeImpl("CODE_1", "Description 1");
    ErrorCode errorCode2 = new TestErrorCodeImpl("CODE_2", "Description 2");

    // When & Then
    assertEquals("CODE_1", errorCode1.getCode());
    assertEquals("CODE_2", errorCode2.getCode());
    assertEquals("Description 1", errorCode1.getDescription());
    assertEquals("Description 2", errorCode2.getDescription());

    // Verify they are truly independent
    assertNotEquals(errorCode1.getCode(), errorCode2.getCode());
    assertNotEquals(errorCode1.getDescription(), errorCode2.getDescription());
  }

  @Test
  @DisplayName("equals and hashCode should work correctly for test implementation")
  void equalsAndHashCode_shouldWorkCorrectly() {
    // Given
    ErrorCode errorCode1 = new TestErrorCodeImpl("SAME_CODE", "Same description");
    ErrorCode errorCode2 = new TestErrorCodeImpl("SAME_CODE", "Same description");
    ErrorCode errorCode3 = new TestErrorCodeImpl("DIFFERENT_CODE", "Same description");

    // When & Then
    assertEquals(errorCode1, errorCode2);
    assertEquals(errorCode1.hashCode(), errorCode2.hashCode());
    assertNotEquals(errorCode1, errorCode3);
    assertNotEquals(errorCode1.hashCode(), errorCode3.hashCode());
  }

  @Test
  @DisplayName("toString should include both code and description")
  void toString_shouldIncludeBothFields() {
    // Given
    ErrorCode errorCode = new TestErrorCodeImpl("TEST_CODE", "Test description");

    // When
    String result = errorCode.toString();

    // Then
    assertNotNull(result);
    assertTrue(result.contains("TEST_CODE"));
    assertTrue(result.contains("Test description"));
  }

  @Test
  @DisplayName("Should support typical error handling use cases")
  void shouldSupportTypicalErrorHandlingUseCases() {
    // Test programmatic handling scenario
    ErrorCode errorCode = TestErrorCodeEnum.VALIDATION_FAILED;

    // Code-based switch handling
    String handling =
        switch (errorCode.getCode()) {
          case "VALIDATION_FAILED" -> "Handle validation error";
          case "RESOURCE_NOT_FOUND" -> "Handle not found error";
          case "UNAUTHORIZED_ACCESS" -> "Handle authorization error";
          default -> "Handle unknown error";
        };

    assertEquals("Handle validation error", handling);

    // User message display
    String userMessage = errorCode.getDescription();
    assertEquals("Input validation failed", userMessage);
    assertFalse(userMessage.isBlank());
  }

  @Test
  @DisplayName("Should work with different ErrorCode implementations")
  void shouldWorkWithDifferentImplementations() {
    // Test that interface works with both class and enum implementations
    ErrorCode classImpl = new TestErrorCodeImpl("CLASS_IMPL", "Class implementation");
    ErrorCode enumImpl = TestErrorCodeEnum.INTERNAL_ERROR;

    // Both should work identically through the interface
    assertNotNull(classImpl.getCode());
    assertNotNull(classImpl.getDescription());
    assertNotNull(enumImpl.getCode());
    assertNotNull(enumImpl.getDescription());

    // Both should pass validation
    assertTrue(validator.validate(classImpl).isEmpty());
    assertTrue(validator.validate(enumImpl).isEmpty());
  }

  @Test
  @DisplayName("Should handle edge cases gracefully")
  void shouldHandleEdgeCases() {
    // Test minimum valid values (single character)
    ErrorCode minimal = new TestErrorCodeImpl("A", "B");
    assertEquals("A", minimal.getCode());
    assertEquals("B", minimal.getDescription());
    assertTrue(validator.validate(minimal).isEmpty());

    // Test codes with numbers and underscores
    ErrorCode mixed = new TestErrorCodeImpl("ERROR_123_MIXED", "Error 123 mixed format");
    assertEquals("ERROR_123_MIXED", mixed.getCode());
    assertTrue(validator.validate(mixed).isEmpty());
  }
}
