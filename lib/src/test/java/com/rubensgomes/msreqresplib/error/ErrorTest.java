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
package com.rubensgomes.msreqresplib.error;

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
 * Unit tests for the {@link com.rubensgomes.msreqresplib.error.Error} interface.
 *
 * <p>This test class verifies the contract of the Error interface by testing concrete
 * implementations that cover all the interface methods and validation constraints.
 *
 * @author Rubens Gomes
 * @since 0.0.1
 */
@DisplayName("Error Interface Tests")
class ErrorTest {

  private Validator validator;
  private com.rubensgomes.msreqresplib.error.Error error;

  /** Test implementation of the ErrorCode interface for testing purposes. */
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

  /** Test implementation of the Error interface for testing purposes. */
  private static class TestErrorImpl implements com.rubensgomes.msreqresplib.error.Error {
    private final String errorDescription;
    private final String nativeErrorText;
    private final ErrorCode errorCode;

    public TestErrorImpl(String errorDescription, String nativeErrorText, ErrorCode errorCode) {
      this.errorDescription = errorDescription;
      this.nativeErrorText = nativeErrorText;
      this.errorCode = errorCode;
    }

    @Override
    public String getErrorDescription() {
      return errorDescription;
    }

    @Override
    public String getNativeErrorText() {
      return nativeErrorText;
    }

    @Override
    public ErrorCode getErrorCode() {
      return errorCode;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      TestErrorImpl that = (TestErrorImpl) o;
      return java.util.Objects.equals(errorDescription, that.errorDescription)
          && java.util.Objects.equals(nativeErrorText, that.nativeErrorText)
          && java.util.Objects.equals(errorCode, that.errorCode);
    }

    @Override
    public int hashCode() {
      return java.util.Objects.hash(errorDescription, nativeErrorText, errorCode);
    }

    @Override
    public String toString() {
      return String.format(
          "TestErrorImpl{errorDescription='%s', nativeErrorText='%s', errorCode=%s}",
          errorDescription, nativeErrorText, errorCode);
    }
  }

  /** Mutable test implementation for scenarios that require modification. */
  private static class MutableTestErrorImpl implements com.rubensgomes.msreqresplib.error.Error {
    private final String errorDescription;
    private String nativeErrorText;
    private final ErrorCode errorCode;

    public MutableTestErrorImpl(
        String errorDescription, String nativeErrorText, ErrorCode errorCode) {
      this.errorDescription = errorDescription;
      this.nativeErrorText = nativeErrorText;
      this.errorCode = errorCode;
    }

    @Override
    public String getErrorDescription() {
      return errorDescription;
    }

    @Override
    public String getNativeErrorText() {
      return nativeErrorText;
    }

    @Override
    public ErrorCode getErrorCode() {
      return errorCode;
    }

    // Additional method for testing - not part of the Error interface
    public void setNativeErrorText(String nativeErrorText) {
      this.nativeErrorText = nativeErrorText;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      MutableTestErrorImpl that = (MutableTestErrorImpl) o;
      return java.util.Objects.equals(errorDescription, that.errorDescription)
          && java.util.Objects.equals(nativeErrorText, that.nativeErrorText)
          && java.util.Objects.equals(errorCode, that.errorCode);
    }

    @Override
    public int hashCode() {
      return java.util.Objects.hash(errorDescription, nativeErrorText, errorCode);
    }

    @Override
    public String toString() {
      return String.format(
          "MutableTestErrorImpl{errorDescription='%s', nativeErrorText='%s', errorCode=%s}",
          errorDescription, nativeErrorText, errorCode);
    }
  }

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();

    ErrorCode testErrorCode = new TestErrorCodeImpl("DB_001", "Database connection failed");
    error =
        new TestErrorImpl(
            "Database connection failed",
            "java.sql.SQLException: Connection refused",
            testErrorCode);
  }

  @Test
  @DisplayName("getErrorDescription should return the error description")
  void getErrorDescription_shouldReturnDescription() {
    // When
    String result = error.getErrorDescription();

    // Then
    assertEquals("Database connection failed", result);
    assertNotNull(result);
  }

  @Test
  @DisplayName("getNativeErrorText should return the native error text")
  void getNativeErrorText_shouldReturnNativeErrorText() {
    // When
    String result = error.getNativeErrorText();

    // Then
    assertEquals("java.sql.SQLException: Connection refused", result);
    assertNotNull(result);
  }

  @Test
  @DisplayName("getErrorCode should return the ErrorCode object")
  void getErrorCode_shouldReturnErrorCode() {
    // When
    ErrorCode result = error.getErrorCode();

    // Then
    assertNotNull(result);
    assertEquals("DB_001", result.getCode());
    assertEquals("Database connection failed", result.getDescription());
  }

  @Test
  @DisplayName("Validation should pass when all required fields are valid")
  void validation_shouldPass_whenAllRequiredFieldsValid() {
    // Given
    ErrorCode validCode = new TestErrorCodeImpl("VALID_CODE", "Valid description");
    com.rubensgomes.msreqresplib.error.Error validError =
        new TestErrorImpl("Valid error description", "Valid native text", validCode);

    // When
    Set<ConstraintViolation<com.rubensgomes.msreqresplib.error.Error>> violations =
        validator.validate(validError);

    // Then
    assertTrue(violations.isEmpty(), "Should have no validation violations");
  }

  @Test
  @DisplayName("Validation should fail when error description is null")
  void validation_shouldFail_whenErrorDescriptionIsNull() {
    // Given
    ErrorCode validCode = new TestErrorCodeImpl("VALID_CODE", "Valid description");
    com.rubensgomes.msreqresplib.error.Error invalidError =
        new TestErrorImpl(null, "Valid native text", validCode);

    // When
    Set<ConstraintViolation<com.rubensgomes.msreqresplib.error.Error>> violations =
        validator.validate(invalidError);

    // Then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());
    ConstraintViolation<com.rubensgomes.msreqresplib.error.Error> violation =
        violations.iterator().next();
    assertEquals("errorDescription", violation.getPropertyPath().toString());
  }

  @Test
  @DisplayName("Validation should fail when error description is blank")
  void validation_shouldFail_whenErrorDescriptionIsBlank() {
    // Given
    ErrorCode validCode = new TestErrorCodeImpl("VALID_CODE", "Valid description");
    com.rubensgomes.msreqresplib.error.Error invalidError =
        new TestErrorImpl("", "Valid native text", validCode);

    // When
    Set<ConstraintViolation<com.rubensgomes.msreqresplib.error.Error>> violations =
        validator.validate(invalidError);

    // Then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());
    ConstraintViolation<com.rubensgomes.msreqresplib.error.Error> violation =
        violations.iterator().next();
    assertEquals("errorDescription", violation.getPropertyPath().toString());
  }

  @Test
  @DisplayName("Validation should fail when ErrorCode is null")
  void validation_shouldFail_whenErrorCodeIsNull() {
    // Given
    com.rubensgomes.msreqresplib.error.Error invalidError =
        new TestErrorImpl("Valid description", "Valid native text", null);

    // When
    Set<ConstraintViolation<com.rubensgomes.msreqresplib.error.Error>> violations =
        validator.validate(invalidError);

    // Then
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());
    ConstraintViolation<com.rubensgomes.msreqresplib.error.Error> violation =
        violations.iterator().next();
    assertEquals("errorCode", violation.getPropertyPath().toString());
  }

  @Test
  @DisplayName("Native error text can be null (optional field)")
  void nativeErrorText_canBeNull() {
    // Given
    ErrorCode validCode = new TestErrorCodeImpl("VALID_CODE", "Valid description");
    com.rubensgomes.msreqresplib.error.Error errorWithNullNative =
        new TestErrorImpl("Valid description", null, validCode);

    // When
    Set<ConstraintViolation<com.rubensgomes.msreqresplib.error.Error>> violations =
        validator.validate(errorWithNullNative);
    String nativeText = errorWithNullNative.getNativeErrorText();

    // Then
    assertTrue(violations.isEmpty(), "Should have no validation violations");
    assertNull(nativeText);
  }

  @Test
  @DisplayName("Should handle different ErrorCode implementations correctly")
  void shouldHandleDifferentErrorCodeImplementations() {
    // Given
    ErrorCode hierarchicalCode =
        new TestErrorCodeImpl("DB_CONNECTION_FAILED", "Database connection failed");
    ErrorCode numericCode = new TestErrorCodeImpl("ERR_001", "Generic error occurred");
    ErrorCode httpStyleCode = new TestErrorCodeImpl("404_NOT_FOUND", "Resource not found");

    com.rubensgomes.msreqresplib.error.Error error1 =
        new TestErrorImpl("Database error", "SQL exception", hierarchicalCode);
    com.rubensgomes.msreqresplib.error.Error error2 =
        new TestErrorImpl("Generic error", "Unknown exception", numericCode);
    com.rubensgomes.msreqresplib.error.Error error3 =
        new TestErrorImpl("Resource error", "HTTP exception", httpStyleCode);

    // When & Then
    assertEquals("DB_CONNECTION_FAILED", error1.getErrorCode().getCode());
    assertEquals("ERR_001", error2.getErrorCode().getCode());
    assertEquals("404_NOT_FOUND", error3.getErrorCode().getCode());

    // All should pass validation
    assertTrue(validator.validate(error1).isEmpty());
    assertTrue(validator.validate(error2).isEmpty());
    assertTrue(validator.validate(error3).isEmpty());
  }

  @Test
  @DisplayName("Should handle long strings in all fields")
  void shouldHandleLongStrings() {
    // Given
    String longDescription = "A".repeat(2000);
    String longNativeText = "B".repeat(3000);
    ErrorCode longCode = new TestErrorCodeImpl("LONG_CODE", "C".repeat(1000));
    com.rubensgomes.msreqresplib.error.Error longError =
        new TestErrorImpl(longDescription, longNativeText, longCode);

    // When & Then
    assertEquals(longDescription, longError.getErrorDescription());
    assertEquals(longNativeText, longError.getNativeErrorText());
    assertEquals(longCode, longError.getErrorCode());
    assertEquals(2000, longError.getErrorDescription().length());
    // Safe null check before calling length()
    assertNotNull(longError.getNativeErrorText());
    assertEquals(3000, longError.getNativeErrorText().length());

    // Validation should still pass
    Set<ConstraintViolation<com.rubensgomes.msreqresplib.error.Error>> violations =
        validator.validate(longError);
    assertTrue(violations.isEmpty());
  }

  @Test
  @DisplayName("Should handle special characters and Unicode")
  void shouldHandleSpecialCharactersAndUnicode() {
    // Given
    String specialDescription = "Error with emojis: 🚀 ❌ and unicode: 测试";
    String specialNativeText = "Native text with symbols: !@#$%^&*() 测试";
    ErrorCode specialCode = new TestErrorCodeImpl("SPECIAL_001", "Special characters error");
    com.rubensgomes.msreqresplib.error.Error specialError =
        new TestErrorImpl(specialDescription, specialNativeText, specialCode);

    // When & Then
    assertEquals(specialDescription, specialError.getErrorDescription());
    assertEquals(specialNativeText, specialError.getNativeErrorText());
    assertEquals(specialCode, specialError.getErrorCode());

    // Validation should pass
    Set<ConstraintViolation<com.rubensgomes.msreqresplib.error.Error>> violations =
        validator.validate(specialError);
    assertTrue(violations.isEmpty());
  }

  @Test
  @DisplayName("Multiple Error instances should be independent")
  void multipleInstances_shouldBeIndependent() {
    // Given
    ErrorCode code1 = new TestErrorCodeImpl("CODE_1", "Description 1");
    ErrorCode code2 = new TestErrorCodeImpl("CODE_2", "Description 2");

    com.rubensgomes.msreqresplib.error.Error error1 =
        new TestErrorImpl("Error 1", "Native 1", code1);
    com.rubensgomes.msreqresplib.error.Error error2 =
        new TestErrorImpl("Error 2", "Native 2", code2);

    // When & Then
    assertEquals("Error 1", error1.getErrorDescription());
    assertEquals("Error 2", error2.getErrorDescription());
    assertEquals("Native 1", error1.getNativeErrorText());
    assertEquals("Native 2", error2.getNativeErrorText());
    assertEquals("CODE_1", error1.getErrorCode().getCode());
    assertEquals("CODE_2", error2.getErrorCode().getCode());

    // Verify they are truly independent
    assertNotEquals(error1.getErrorDescription(), error2.getErrorDescription());
    assertNotEquals(error1.getNativeErrorText(), error2.getNativeErrorText());
    assertNotEquals(error1.getErrorCode().getCode(), error2.getErrorCode().getCode());
  }

  @Test
  @DisplayName("Should support programmatic error handling use cases")
  void shouldSupportProgrammaticErrorHandling() {
    // Given
    ErrorCode code = new TestErrorCodeImpl("VALIDATION_FAILED", "Input validation failed");
    com.rubensgomes.msreqresplib.error.Error error =
        new TestErrorImpl("Validation error occurred", "Field 'email' is required", code);

    // When - Programmatic handling by code
    ErrorCode errorCode = error.getErrorCode();
    String codeId = errorCode.getCode();
    String handling =
        switch (codeId) {
          case "VALIDATION_FAILED" -> "Handle validation error";
          case "DB_CONNECTION_FAILED" -> "Handle database error";
          default -> "Handle unknown error";
        };

    // Then
    assertEquals("Handle validation error", handling);
    assertEquals("Input validation failed", errorCode.getDescription());
    assertEquals("Validation error occurred", error.getErrorDescription());
  }

  @Test
  @DisplayName("Should support user display scenarios")
  void shouldSupportUserDisplayScenarios() {
    // Given
    ErrorCode code = new TestErrorCodeImpl("USER_AUTH_FAILED", "Authentication failed");
    com.rubensgomes.msreqresplib.error.Error error =
        new TestErrorImpl("User authentication failed", "Invalid credentials", code);

    // When - User display scenarios
    String primaryMessage = error.getErrorDescription();
    String fallbackMessage = error.getErrorCode().getDescription();

    // Then
    assertEquals("User authentication failed", primaryMessage);
    assertEquals("Authentication failed", fallbackMessage);
    assertFalse(primaryMessage.isBlank());
    assertFalse(fallbackMessage.isBlank());
  }

  @Test
  @DisplayName("equals and hashCode should work correctly for test implementation")
  void equalsAndHashCode_shouldWorkCorrectly() {
    // Given
    ErrorCode code1 = new TestErrorCodeImpl("SAME_CODE", "Same description");
    ErrorCode code2 = new TestErrorCodeImpl("SAME_CODE", "Same description");
    ErrorCode code3 = new TestErrorCodeImpl("DIFFERENT_CODE", "Different description");

    com.rubensgomes.msreqresplib.error.Error error1 =
        new TestErrorImpl("Same error", "Same native", code1);
    com.rubensgomes.msreqresplib.error.Error error2 =
        new TestErrorImpl("Same error", "Same native", code2);
    com.rubensgomes.msreqresplib.error.Error error3 =
        new TestErrorImpl("Different error", "Different native", code3);

    // When & Then
    assertEquals(error1, error2);
    assertEquals(error1.hashCode(), error2.hashCode());
    assertNotEquals(error1, error3);
    assertNotEquals(error1.hashCode(), error3.hashCode());
  }

  @Test
  @DisplayName("toString should include all field information")
  void toString_shouldIncludeAllFields() {
    // Given
    ErrorCode code = new TestErrorCodeImpl("TEST_CODE", "Test description");
    com.rubensgomes.msreqresplib.error.Error error =
        new TestErrorImpl("Test error", "Test native", code);

    // When
    String result = error.toString();

    // Then
    assertNotNull(result);
    assertTrue(result.contains("Test error"));
    assertTrue(result.contains("Test native"));
    assertTrue(result.contains("TEST_CODE"));
    assertTrue(result.contains("Test description"));
  }

  @Test
  @DisplayName("Should validate complex error scenarios")
  void shouldValidateComplexErrorScenarios() {
    // Test real-world error scenarios

    // Database error scenario
    ErrorCode dbCode =
        new TestErrorCodeImpl("DB_CONNECTION_TIMEOUT", "Database connection timed out");
    com.rubensgomes.msreqresplib.error.Error dbError =
        new TestErrorImpl(
            "Unable to connect to database",
            "java.sql.SQLTimeoutException: Connection timed out after 30000ms",
            dbCode);

    // API error scenario
    ErrorCode apiCode = new TestErrorCodeImpl("API_RATE_LIMIT_EXCEEDED", "API rate limit exceeded");
    com.rubensgomes.msreqresplib.error.Error apiError =
        new TestErrorImpl(
            "Too many requests",
            "HTTP 429: Rate limit exceeded - try again in 60 seconds",
            apiCode);

    // Validation error scenario
    ErrorCode validationCode =
        new TestErrorCodeImpl("VALIDATION_REQUIRED_FIELD", "Required field missing");
    com.rubensgomes.msreqresplib.error.Error validationError =
        new TestErrorImpl(
            "Required field is missing",
            "Field 'username' cannot be null or empty",
            validationCode);

    // All should be valid and work correctly
    assertTrue(validator.validate(dbError).isEmpty());
    assertTrue(validator.validate(apiError).isEmpty());
    assertTrue(validator.validate(validationError).isEmpty());

    assertEquals("DB_CONNECTION_TIMEOUT", dbError.getErrorCode().getCode());
    assertEquals("API_RATE_LIMIT_EXCEEDED", apiError.getErrorCode().getCode());
    assertEquals("VALIDATION_REQUIRED_FIELD", validationError.getErrorCode().getCode());
  }

  @Test
  @DisplayName("Mutable implementation should allow updates to native error text")
  void mutableImplementation_shouldAllowUpdates() {
    // Given
    ErrorCode code = new TestErrorCodeImpl("TEST_CODE", "Test description");
    MutableTestErrorImpl mutableError =
        new MutableTestErrorImpl("Test error", "Initial native text", code);

    // When
    mutableError.setNativeErrorText("Updated native text");

    // Then
    assertEquals("Updated native text", mutableError.getNativeErrorText());
  }

  @Test
  @DisplayName("Mutable implementation should accept null values")
  void mutableImplementation_shouldAcceptNull() {
    // Given
    ErrorCode code = new TestErrorCodeImpl("TEST_CODE", "Test description");
    MutableTestErrorImpl mutableError =
        new MutableTestErrorImpl("Test error", "Initial native text", code);

    // When
    mutableError.setNativeErrorText(null);

    // Then
    assertNull(mutableError.getNativeErrorText());

    // Validation should still pass with null native text
    Set<ConstraintViolation<Error>> violations = validator.validate(mutableError);
    assertTrue(violations.isEmpty());
  }

  @Test
  @DisplayName("Mutable implementation should handle empty strings")
  void mutableImplementation_shouldHandleEmptyStrings() {
    // Given
    ErrorCode code = new TestErrorCodeImpl("TEST_CODE", "Test description");
    MutableTestErrorImpl mutableError =
        new MutableTestErrorImpl("Test error", "Initial native text", code);

    // When
    mutableError.setNativeErrorText("");

    // Then
    assertEquals("", mutableError.getNativeErrorText());
    String nativeText = mutableError.getNativeErrorText();
    assertNotNull(nativeText);
    assertTrue(nativeText.isEmpty());

    // Validation should still pass with empty native text
    Set<ConstraintViolation<Error>> violations = validator.validate(mutableError);
    assertTrue(violations.isEmpty());
  }

  @Test
  @DisplayName("Mutable implementation should handle long strings")
  void mutableImplementation_shouldHandleLongStrings() {
    // Given
    ErrorCode code = new TestErrorCodeImpl("TEST_CODE", "Test description");
    MutableTestErrorImpl mutableError =
        new MutableTestErrorImpl("Test error", "Initial native text", code);
    String longNativeText = "Very long native error text: " + "X".repeat(5000);

    // When
    mutableError.setNativeErrorText(longNativeText);

    // Then
    assertEquals(longNativeText, mutableError.getNativeErrorText());
    String nativeText = mutableError.getNativeErrorText();
    assertNotNull(nativeText);
    assertEquals(5029, nativeText.length()); // 29 + 5000

    // Validation should still pass
    Set<ConstraintViolation<Error>> violations = validator.validate(mutableError);
    assertTrue(violations.isEmpty());
  }

  @Test
  @DisplayName("Mutable implementation should handle special characters and Unicode")
  void mutableImplementation_shouldHandleSpecialCharacters() {
    // Given
    ErrorCode code = new TestErrorCodeImpl("TEST_CODE", "Test description");
    MutableTestErrorImpl mutableError =
        new MutableTestErrorImpl("Test error", "Initial native text", code);
    String specialText = "Error with emojis: 🚀 ❌ and unicode: 测试 and symbols: !@#$%^&*() 测试";

    // When
    mutableError.setNativeErrorText(specialText);

    // Then
    assertEquals(specialText, mutableError.getNativeErrorText());

    // Validation should still pass
    Set<ConstraintViolation<Error>> violations = validator.validate(mutableError);
    assertTrue(violations.isEmpty());
  }

  @Test
  @DisplayName("Mutable implementation should allow multiple updates")
  void mutableImplementation_shouldAllowMultipleUpdates() {
    // Given
    ErrorCode code = new TestErrorCodeImpl("TEST_CODE", "Test description");
    MutableTestErrorImpl mutableError =
        new MutableTestErrorImpl("Test error", "Initial native text", code);

    // When & Then - Multiple updates
    mutableError.setNativeErrorText("First update");
    assertEquals("First update", mutableError.getNativeErrorText());

    mutableError.setNativeErrorText("Second update");
    assertEquals("Second update", mutableError.getNativeErrorText());

    mutableError.setNativeErrorText(null);
    assertNull(mutableError.getNativeErrorText());

    mutableError.setNativeErrorText("Final update");
    assertEquals("Final update", mutableError.getNativeErrorText());
  }

  @Test
  @DisplayName("Mutable implementation should work in real-world error handling scenarios")
  void mutableImplementation_shouldWorkInRealWorldScenarios() {
    // Given - Initial error without native text
    ErrorCode code = new TestErrorCodeImpl("API_CALL_FAILED", "API call failed");
    MutableTestErrorImpl mutableError =
        new MutableTestErrorImpl("Failed to call external API", null, code);

    // When - Adding diagnostic information during error handling
    mutableError.setNativeErrorText(
        "HTTP 500: Internal Server Error - Response body: {\"error\": \"Database unavailable\"}");

    // Then
    assertEquals("Failed to call external API", mutableError.getErrorDescription());
    assertEquals(
        "HTTP 500: Internal Server Error - Response body: {\"error\": \"Database unavailable\"}",
        mutableError.getNativeErrorText());
    assertEquals("API_CALL_FAILED", mutableError.getErrorCode().getCode());

    // Should pass validation
    Set<ConstraintViolation<Error>> violations = validator.validate(mutableError);
    assertTrue(violations.isEmpty());

    // When - Updating with more specific diagnostic info
    mutableError.setNativeErrorText(
        "HTTP 500: java.net.ConnectException: Connection refused at api.example.com:443");

    // Then
    assertEquals(
        "HTTP 500: java.net.ConnectException: Connection refused at api.example.com:443",
        mutableError.getNativeErrorText());
  }

  @Test
  @DisplayName("Mutable implementation should not affect other error properties")
  void mutableImplementation_shouldNotAffectOtherProperties() {
    // Given
    ErrorCode originalCode = new TestErrorCodeImpl("ORIGINAL_CODE", "Original description");
    MutableTestErrorImpl mutableError =
        new MutableTestErrorImpl(
            "Original error description", "Original native text", originalCode);

    // When
    mutableError.setNativeErrorText("New native text");

    // Then - Only native text should change
    assertEquals("Original error description", mutableError.getErrorDescription());
    assertEquals("New native text", mutableError.getNativeErrorText());
    assertEquals(originalCode, mutableError.getErrorCode());
    assertEquals("ORIGINAL_CODE", mutableError.getErrorCode().getCode());
    assertEquals("Original description", mutableError.getErrorCode().getDescription());
  }

  @Test
  @DisplayName("Should handle null native error text safely")
  void shouldHandleNullNativeErrorTextSafely() {
    // Given
    ErrorCode code = new TestErrorCodeImpl("TEST_CODE", "Test description");
    com.rubensgomes.msreqresplib.error.Error errorWithNullNative =
        new TestErrorImpl("Test error", null, code);

    // When & Then - Safe handling of null native text
    String nativeText = errorWithNullNative.getNativeErrorText();
    assertNull(nativeText);

    // Should not call methods on null values
    if (nativeText != null) {
      nativeText.length(); // This would only be called if not null
      nativeText.isEmpty(); // This would only be called if not null
    }

    // Validation should pass
    Set<ConstraintViolation<Error>> violations = validator.validate(errorWithNullNative);
    assertTrue(violations.isEmpty());
  }
}
