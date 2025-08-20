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
package com.rubensgomes.reqresp.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * Unit tests for the {@link ApplicationErrorResponse} class.
 *
 * <p>This test class verifies the behavior of ApplicationErrorResponse, focusing on its validation
 * constraints, constructor behavior, and error-specific functionality.
 *
 * @author Rubens Gomes
 */
@DisplayName("ApplicationErrorResponse Tests")
class ApplicationErrorResponseTest {

  private Validator validator;
  private ValidatorFactory factory;
  private ListAppender<ILoggingEvent> logAppender;
  private Logger logger;

  /** Test implementation of the ErrorCode interface for testing error scenarios. */
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

  /** Test implementation of the Error interface for testing error scenarios. */
  private static class TestErrorImpl implements Error {
    private final String errorDescription;
    private String nativeErrorText;
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
    public void setNativeErrorText(String nativeErrorText) {
      this.nativeErrorText = nativeErrorText;
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

  @BeforeEach
  void setUp() {
    factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();

    // Set up logging capture - capture logs from BaseResponse since that's where logResponse() is
    // implemented
    logger = (Logger) LoggerFactory.getLogger(BaseResponse.class);
    logAppender = new ListAppender<>();
    logAppender.start();
    logger.addAppender(logAppender);
    logger.setLevel(Level.DEBUG);
  }

  @AfterEach
  void tearDown() {
    factory.close();
    logger.detachAppender(logAppender);
  }

  @Test
  @DisplayName("Constructor should create valid ApplicationErrorResponse with all required fields")
  void constructor_shouldCreateValidResponse_whenAllRequiredFieldsProvided() {
    // Given
    String clientId = "test-client";
    String transactionId = "test-transaction";
    Status status = Status.FAILURE;
    String message = "Database connection failed";
    ErrorCode errorCode = new TestErrorCodeImpl("DB_001", "Database error");
    Error error = new TestErrorImpl("Connection timeout error", "Connection timeout", errorCode);

    // When
    ApplicationErrorResponse response =
        new ApplicationErrorResponse(clientId, transactionId, status, message, error);

    // Then
    assertEquals(clientId, response.getClientId());
    assertEquals(transactionId, response.getTransactionId());
    assertEquals(status, response.getStatus());
    assertEquals(message, response.getMessage());
    assertEquals(error, response.getError());
  }

  @Test
  @DisplayName("Constructor should pass validation when all fields are valid")
  void constructor_shouldPassValidation_whenAllFieldsValid() {
    // Given
    ErrorCode errorCode = new TestErrorCodeImpl("ERR001", "Test error");
    Error error = new TestErrorImpl("Generic error", "Error details", errorCode);
    ApplicationErrorResponse response =
        new ApplicationErrorResponse(
            "client-123", "txn-456", Status.FAILURE, "Error occurred", error);

    // When
    Set<ConstraintViolation<ApplicationErrorResponse>> violations = validator.validate(response);

    // Then
    assertTrue(violations.isEmpty(), "Should have no validation violations");
  }

  @Test
  @DisplayName("Constructor should accept null parameters but validation will catch them later")
  void constructor_allowsNullParameters_butValidationCatchesLater() {
    // The constructor doesn't enforce validation automatically at runtime
    // This is expected behavior - validation happens when explicitly called

    ErrorCode validErrorCode = new TestErrorCodeImpl("VALID_CODE", "Valid error");
    Error validError = new TestErrorImpl("Valid error", "Valid native", validErrorCode);

    // Given & When - Constructor should not throw exceptions
    ApplicationErrorResponse response1 =
        new ApplicationErrorResponse(null, "txn-456", Status.FAILURE, "Error occurred", validError);

    ApplicationErrorResponse response2 =
        new ApplicationErrorResponse(
            "client-123", null, Status.FAILURE, "Error occurred", validError);

    ApplicationErrorResponse response3 =
        new ApplicationErrorResponse("client-123", "txn-456", null, "Error occurred", validError);

    ApplicationErrorResponse response4 =
        new ApplicationErrorResponse("client-123", "txn-456", Status.FAILURE, null, validError);

    ApplicationErrorResponse response5 =
        new ApplicationErrorResponse(
            "client-123", "txn-456", Status.FAILURE, "Error occurred", null);

    // Then - Constructor succeeds but validation will fail
    assertNotNull(response1);
    assertNotNull(response2);
    assertNotNull(response3);
    assertNotNull(response4);
    assertNotNull(response5);

    // When validated, they should fail for required fields only
    // Note: message and error are @Nullable in BaseResponse, so nulls are allowed
    Set<ConstraintViolation<ApplicationErrorResponse>> violations1 = validator.validate(response1);
    Set<ConstraintViolation<ApplicationErrorResponse>> violations2 = validator.validate(response2);
    Set<ConstraintViolation<ApplicationErrorResponse>> violations3 = validator.validate(response3);
    Set<ConstraintViolation<ApplicationErrorResponse>> violations4 = validator.validate(response4);
    Set<ConstraintViolation<ApplicationErrorResponse>> violations5 = validator.validate(response5);

    // Verify each response has validation violations for required fields only
    assertFalse(
        violations1.isEmpty(), "Response with null clientId should have validation violations");
    assertFalse(
        violations2.isEmpty(),
        "Response with null transactionId should have validation violations");
    assertFalse(
        violations3.isEmpty(), "Response with null status should have validation violations");
    // message and error are @Nullable in BaseResponse, so no violations expected
    assertTrue(
        violations4.isEmpty(),
        "Response with null message should NOT have validation violations (field is @Nullable)");
    assertTrue(
        violations5.isEmpty(),
        "Response with null error should NOT have validation violations (field is @Nullable)");

    // Verify specific violation counts and field names for required fields
    assertEquals(1, violations1.size(), "Should have exactly 1 violation for null clientId");
    assertEquals(1, violations2.size(), "Should have exactly 1 violation for null transactionId");
    assertEquals(1, violations3.size(), "Should have exactly 1 violation for null status");

    // Verify the violation property paths
    assertEquals("clientId", violations1.iterator().next().getPropertyPath().toString());
    assertEquals("transactionId", violations2.iterator().next().getPropertyPath().toString());
    assertEquals("status", violations3.iterator().next().getPropertyPath().toString());
  }

  @Test
  @DisplayName("Constructor should accept blank strings but validation will catch them")
  void constructor_acceptsBlankStrings_butValidationCatchesThem() {
    // Given
    ErrorCode validErrorCode = new TestErrorCodeImpl("VALID_CODE", "Valid error");
    Error validError = new TestErrorImpl("Valid error", "Valid native", validErrorCode);

    // When - Constructor accepts blank strings
    ApplicationErrorResponse response1 =
        new ApplicationErrorResponse("", "txn-456", Status.FAILURE, "Error occurred", validError);

    ApplicationErrorResponse response2 =
        new ApplicationErrorResponse(
            "client-123", "", Status.FAILURE, "Error occurred", validError);

    ApplicationErrorResponse response3 =
        new ApplicationErrorResponse("client-123", "txn-456", Status.FAILURE, "", validError);

    // Then - Constructor succeeds but validation fails for required fields
    assertNotNull(response1);
    assertNotNull(response2);
    assertNotNull(response3);

    // Validation should fail for @NotBlank fields (clientId, transactionId)
    // but message field is @Nullable so blank is allowed
    assertFalse(validator.validate(response1).isEmpty(), "Blank clientId should fail validation");
    assertFalse(
        validator.validate(response2).isEmpty(), "Blank transactionId should fail validation");
    assertTrue(
        validator.validate(response3).isEmpty(),
        "Blank message should NOT fail validation (field is @Nullable)");
  }

  @Test
  @DisplayName("Should support Status.SUCCESS for non-error scenarios")
  void constructor_shouldSupport_statusSuccess() {
    // Given
    ErrorCode warningCode = new TestErrorCodeImpl("WARN001", "Warning message");
    Error warning = new TestErrorImpl("Warning occurred", "Minor issue", warningCode);

    // When
    ApplicationErrorResponse response =
        new ApplicationErrorResponse(
            "client-123", "txn-456", Status.SUCCESS, "Operation completed with warnings", warning);

    // Then
    assertEquals(Status.SUCCESS, response.getStatus());
    assertEquals("Operation completed with warnings", response.getMessage());
    assertNotNull(response.getError());
  }

  @Test
  @DisplayName("Should inherit logResponse functionality from BaseResponse")
  void logResponse_shouldLogAllFields() {
    // Given
    ErrorCode errorCode = new TestErrorCodeImpl("DB_002", "Database operation error");
    Error error =
        new TestErrorImpl("Database error occurred", "Database connection lost", errorCode);
    ApplicationErrorResponse response =
        new ApplicationErrorResponse(
            "test-client", "test-txn", Status.FAILURE, "Database operation failed", error);

    // When
    response.logResponse();

    // Then
    assertEquals(1, logAppender.list.size());
    ILoggingEvent logEvent = logAppender.list.get(0);
    assertEquals(Level.DEBUG, logEvent.getLevel());

    String logMessage = logEvent.getFormattedMessage();
    assertTrue(logMessage.contains("test-client"));
    assertTrue(logMessage.contains("test-txn"));
    assertTrue(logMessage.contains("FAILURE"));
    assertTrue(logMessage.contains("Database operation failed"));
    assertTrue(logMessage.contains("TestErrorImpl"));
  }

  @Test
  @DisplayName("Should handle long strings in all fields")
  void constructor_shouldHandleLongStrings() {
    // Given
    String longClientId = "a".repeat(1000);
    String longTransactionId = "b".repeat(1000);
    String longMessage = "c".repeat(2000);
    ErrorCode errorCode = new TestErrorCodeImpl("LONG001", "Long error");
    Error error = new TestErrorImpl("Long error description", "Error with long text", errorCode);

    // When
    ApplicationErrorResponse response =
        new ApplicationErrorResponse(
            longClientId, longTransactionId, Status.FAILURE, longMessage, error);

    // Then
    assertEquals(longClientId, response.getClientId());
    assertEquals(longTransactionId, response.getTransactionId());
    assertEquals(longMessage, response.getMessage());
    assertEquals(error, response.getError());

    // Validation should still pass
    Set<ConstraintViolation<ApplicationErrorResponse>> violations = validator.validate(response);
    assertTrue(violations.isEmpty());
  }

  @Test
  @DisplayName("Should handle special characters and Unicode")
  void constructor_shouldHandleSpecialCharacters() {
    // Given
    String specialClientId = "client-ñáéíóú-!@#$%";
    String specialTransactionId = "txn-🚀-测试";
    String specialMessage = "Error with émojis: ❌ Failed operation";
    ErrorCode errorCode = new TestErrorCodeImpl("SPEC001", "Special character error");
    Error error = new TestErrorImpl("Special error", "Special chars: ñáéíóú", errorCode);

    // When
    ApplicationErrorResponse response =
        new ApplicationErrorResponse(
            specialClientId, specialTransactionId, Status.FAILURE, specialMessage, error);

    // Then
    assertEquals(specialClientId, response.getClientId());
    assertEquals(specialTransactionId, response.getTransactionId());
    assertEquals(specialMessage, response.getMessage());
    assertEquals(error, response.getError());

    // Validation should pass
    Set<ConstraintViolation<ApplicationErrorResponse>> violations = validator.validate(response);
    assertTrue(violations.isEmpty());
  }

  @Test
  @DisplayName("Should support typical error response scenarios")
  void constructor_shouldSupportTypicalErrorScenarios() {
    // Test various common error scenarios

    // Database error
    ErrorCode dbErrorCode = new TestErrorCodeImpl("DB_001", "Database connection error");
    Error dbError =
        new TestErrorImpl("Database connection error", "Database connection failed", dbErrorCode);
    ApplicationErrorResponse dbResponse =
        new ApplicationErrorResponse(
            "web-app", "req-001", Status.FAILURE, "Unable to process user request", dbError);
    assertEquals(Status.FAILURE, dbResponse.getStatus());

    // Validation error
    ErrorCode validationErrorCode = new TestErrorCodeImpl("VAL_001", "Validation error");
    Error validationError =
        new TestErrorImpl("Validation error", "Invalid input data", validationErrorCode);
    ApplicationErrorResponse validationResponse =
        new ApplicationErrorResponse(
            "mobile-app", "req-002", Status.FAILURE, "Request validation failed", validationError);
    assertEquals(Status.FAILURE, validationResponse.getStatus());

    // Service unavailable
    ErrorCode serviceErrorCode = new TestErrorCodeImpl("SVC_001", "Service unavailable error");
    Error serviceError =
        new TestErrorImpl(
            "Service unavailable error", "External service unavailable", serviceErrorCode);
    ApplicationErrorResponse serviceResponse =
        new ApplicationErrorResponse(
            "api-client",
            "req-003",
            Status.FAILURE,
            "External dependency not available",
            serviceError);
    assertEquals(Status.FAILURE, serviceResponse.getStatus());
  }

  @Test
  @DisplayName("toString should include all field values when using Lombok @Data")
  void toString_shouldIncludeAllFieldValues() {
    // Given
    ErrorCode errorCode = new TestErrorCodeImpl("TEST001", "Test error");
    Error error = new TestErrorImpl("Test error description", "Test error native", errorCode);
    ApplicationErrorResponse response =
        new ApplicationErrorResponse(
            "test-client", "test-txn", Status.FAILURE, "Test message", error);

    // When
    String result = response.toString();

    // Then
    assertNotNull(result);
    assertTrue(result.contains("test-client"));
    assertTrue(result.contains("test-txn"));
    assertTrue(result.contains("FAILURE"));
    assertTrue(result.contains("Test message"));
  }

  @Test
  @DisplayName("equals and hashCode should work correctly when using Lombok @Data")
  void equalsAndHashCode_shouldWorkCorrectly() {
    // Given
    ErrorCode errorCode1 = new TestErrorCodeImpl("ERR001", "Error 1");
    ErrorCode errorCode2 = new TestErrorCodeImpl("ERR001", "Error 1");
    Error error1 = new TestErrorImpl("Error 1 description", "Error 1 native", errorCode1);
    Error error2 = new TestErrorImpl("Error 1 description", "Error 1 native", errorCode2);

    ApplicationErrorResponse response1 =
        new ApplicationErrorResponse("client1", "txn1", Status.FAILURE, "message1", error1);
    ApplicationErrorResponse response2 =
        new ApplicationErrorResponse("client1", "txn1", Status.FAILURE, "message1", error2);
    ApplicationErrorResponse response3 =
        new ApplicationErrorResponse("client2", "txn1", Status.FAILURE, "message1", error1);

    // When & Then
    assertEquals(response1, response2);
    assertEquals(response1.hashCode(), response2.hashCode());
    assertNotEquals(response1, response3);
    assertNotEquals(response1.hashCode(), response3.hashCode());
  }

  @Test
  @DisplayName("Should enforce error-specific requirements vs BaseResponse")
  void shouldEnforceErrorSpecificRequirements() {
    // ApplicationErrorResponse requires both message and error to be non-null
    // while BaseResponse allows them to be optional

    ErrorCode errorCode =
        new TestErrorCodeImpl("REQUIRED_ERROR", "Required error for ApplicationErrorResponse");
    Error error = new TestErrorImpl("Required error", "Required native error", errorCode);

    // When creating ApplicationErrorResponse, both message and error are required
    ApplicationErrorResponse response =
        new ApplicationErrorResponse("client", "txn", Status.FAILURE, "Required message", error);

    // Then both fields should be populated
    assertNotNull(response.getMessage());
    assertNotNull(response.getError());
    assertEquals("Required message", response.getMessage());
    assertEquals(error, response.getError());

    // Should pass validation since all required fields are provided
    assertTrue(validator.validate(response).isEmpty());
  }

  @Test
  @DisplayName("Should work with complex Error objects correctly")
  void shouldWorkWithComplexErrorObjects() {
    // Given complex error with structured ErrorCode
    ErrorCode complexErrorCode =
        new TestErrorCodeImpl(
            "COMPLEX_DB_CONNECTION_TIMEOUT", "Database connection timeout with retry exhausted");
    Error complexError =
        new TestErrorImpl(
            "Database connection failed after multiple retry attempts",
            "java.sql.SQLTimeoutException: Connection attempt timed out after 30000ms, retries exhausted (attempted 3 times)",
            complexErrorCode);

    // When
    ApplicationErrorResponse response =
        new ApplicationErrorResponse(
            "enterprise-app",
            "txn-db-operation-12345",
            Status.FAILURE,
            "Unable to complete database operation due to connection issues",
            complexError);

    // Then
    assertEquals("enterprise-app", response.getClientId());
    assertEquals("txn-db-operation-12345", response.getTransactionId());
    assertEquals(Status.FAILURE, response.getStatus());
    assertEquals(
        "Unable to complete database operation due to connection issues", response.getMessage());
    assertNotNull(response.getError());
    assertEquals(
        "Database connection failed after multiple retry attempts",
        response.getError().getErrorDescription());
    assertEquals("COMPLEX_DB_CONNECTION_TIMEOUT", response.getError().getErrorCode().getCode());
    assertEquals(
        "Database connection timeout with retry exhausted",
        response.getError().getErrorCode().getDescription());

    // Should pass validation
    assertTrue(validator.validate(response).isEmpty());
  }

  @Test
  @DisplayName("Should demonstrate constructor validation annotation behavior")
  void shouldDemonstrateConstructorValidationBehavior() {
    // The @NotBlank and @NotNull annotations on constructor parameters are for documentation
    // and potential framework validation, but don't automatically enforce validation at runtime

    ErrorCode errorCode = new TestErrorCodeImpl("DEMO_ERROR", "Demo error");
    Error error = new TestErrorImpl("Demo error", "Demo native", errorCode);

    // Constructor accepts invalid values without throwing exceptions
    ApplicationErrorResponse invalidResponse = new ApplicationErrorResponse("", "", null, "", null);

    // But Bean Validation will catch these when explicitly validated
    // Note: Only required fields (clientId, transactionId, status) will have violations
    // message and error are @Nullable in BaseResponse
    Set<ConstraintViolation<ApplicationErrorResponse>> violations =
        validator.validate(invalidResponse);
    assertFalse(violations.isEmpty());
    assertEquals(3, violations.size()); // Only 3 required fields should have violations

    // Valid response passes validation
    ApplicationErrorResponse validResponse =
        new ApplicationErrorResponse(
            "valid-client", "valid-txn", Status.FAILURE, "Valid message", error);
    assertTrue(validator.validate(validResponse).isEmpty());
  }
}
