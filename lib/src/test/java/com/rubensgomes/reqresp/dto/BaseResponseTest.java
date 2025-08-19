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
 * Unit tests for the {@link BaseResponse} abstract class.
 *
 * <p>This test class verifies the behavior of BaseResponse by testing a concrete implementation
 * that covers all the fields, methods, and validation constraints.
 *
 * @author Rubens Gomes
 */
@DisplayName("BaseResponse Tests")
class BaseResponseTest {

  private Validator validator;
  private ValidatorFactory factory;
  private TestBaseResponseImpl response;
  private ListAppender<ILoggingEvent> logAppender;
  private Logger logger;

  /** Concrete implementation of BaseResponse for testing purposes. */
  private static class TestBaseResponseImpl extends BaseResponse {
    // No additional methods needed for testing the base functionality
  }

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
    response = new TestBaseResponseImpl();

    // Set up logging capture
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
  @DisplayName("Validation should pass when all required fields are present")
  void validation_shouldPass_whenRequiredFieldsPresent() {
    // Given
    response.setClientId("test-client");
    response.setTransactionId("test-transaction");
    response.setStatus(Status.SUCCESS);

    // When
    Set<ConstraintViolation<TestBaseResponseImpl>> violations = validator.validate(response);

    // Then
    assertTrue(violations.isEmpty(), "Should have no validation violations");
  }

  @Test
  @DisplayName("Validation should fail when clientId is null")
  void validation_shouldFail_whenClientIdIsNull() {
    // Given
    response.setClientId(null);
    response.setTransactionId("test-transaction");
    response.setStatus(Status.SUCCESS);

    // When
    Set<ConstraintViolation<TestBaseResponseImpl>> violations = validator.validate(response);

    // Then
    assertEquals(1, violations.size());
    ConstraintViolation<TestBaseResponseImpl> violation = violations.iterator().next();
    assertEquals("clientId", violation.getPropertyPath().toString());
    assertEquals("clientId is required", violation.getMessage());
  }

  @Test
  @DisplayName("Validation should fail when clientId is blank")
  void validation_shouldFail_whenClientIdIsBlank() {
    // Given
    response.setClientId("");
    response.setTransactionId("test-transaction");
    response.setStatus(Status.SUCCESS);

    // When
    Set<ConstraintViolation<TestBaseResponseImpl>> violations = validator.validate(response);

    // Then
    assertEquals(1, violations.size());
    ConstraintViolation<TestBaseResponseImpl> violation = violations.iterator().next();
    assertEquals("clientId", violation.getPropertyPath().toString());
    assertEquals("clientId is required", violation.getMessage());
  }

  @Test
  @DisplayName("Validation should fail when clientId is whitespace only")
  void validation_shouldFail_whenClientIdIsWhitespaceOnly() {
    // Given
    response.setClientId("   ");
    response.setTransactionId("test-transaction");
    response.setStatus(Status.SUCCESS);

    // When
    Set<ConstraintViolation<TestBaseResponseImpl>> violations = validator.validate(response);

    // Then
    assertEquals(1, violations.size());
    ConstraintViolation<TestBaseResponseImpl> violation = violations.iterator().next();
    assertEquals("clientId", violation.getPropertyPath().toString());
    assertEquals("clientId is required", violation.getMessage());
  }

  @Test
  @DisplayName("Validation should fail when transactionId is null")
  void validation_shouldFail_whenTransactionIdIsNull() {
    // Given
    response.setClientId("test-client");
    response.setTransactionId(null);
    response.setStatus(Status.SUCCESS);

    // When
    Set<ConstraintViolation<TestBaseResponseImpl>> violations = validator.validate(response);

    // Then
    assertEquals(1, violations.size());
    ConstraintViolation<TestBaseResponseImpl> violation = violations.iterator().next();
    assertEquals("transactionId", violation.getPropertyPath().toString());
    assertEquals("transactionId is required", violation.getMessage());
  }

  @Test
  @DisplayName("Validation should fail when transactionId is blank")
  void validation_shouldFail_whenTransactionIdIsBlank() {
    // Given
    response.setClientId("test-client");
    response.setTransactionId("");
    response.setStatus(Status.SUCCESS);

    // When
    Set<ConstraintViolation<TestBaseResponseImpl>> violations = validator.validate(response);

    // Then
    assertEquals(1, violations.size());
    ConstraintViolation<TestBaseResponseImpl> violation = violations.iterator().next();
    assertEquals("transactionId", violation.getPropertyPath().toString());
    assertEquals("transactionId is required", violation.getMessage());
  }

  @Test
  @DisplayName("Validation should fail when status is null")
  void validation_shouldFail_whenStatusIsNull() {
    // Given
    response.setClientId("test-client");
    response.setTransactionId("test-transaction");
    response.setStatus(null);

    // When
    Set<ConstraintViolation<TestBaseResponseImpl>> violations = validator.validate(response);

    // Then
    assertEquals(1, violations.size());
    ConstraintViolation<TestBaseResponseImpl> violation = violations.iterator().next();
    assertEquals("status", violation.getPropertyPath().toString());
    assertEquals("status is required", violation.getMessage());
  }

  @Test
  @DisplayName("Validation should fail when multiple required fields are missing")
  void validation_shouldFail_whenMultipleRequiredFieldsMissing() {
    // Given - response with all null required fields

    // When
    Set<ConstraintViolation<TestBaseResponseImpl>> violations = validator.validate(response);

    // Then
    assertEquals(
        3, violations.size(), "Should have violations for clientId, transactionId, and status");
  }

  @Test
  @DisplayName("Optional fields should be allowed to be null")
  void validation_shouldPass_whenOptionalFieldsAreNull() {
    // Given
    response.setClientId("test-client");
    response.setTransactionId("test-transaction");
    response.setStatus(Status.SUCCESS);
    response.setMessage(null);
    response.setError(null);

    // When
    Set<ConstraintViolation<TestBaseResponseImpl>> violations = validator.validate(response);

    // Then
    assertTrue(violations.isEmpty(), "Optional fields should not cause validation failures");
  }

  @Test
  @DisplayName("Should allow setting and getting all fields")
  void shouldAllowSettingAndGettingAllFields() {
    // Given
    String clientId = "test-client-123";
    String transactionId = "txn-456";
    Status status = Status.FAILURE;
    String message = "Test message";
    ErrorCode errorCode = new TestErrorCodeImpl("TEST_ERROR", "Test error occurred");
    Error error = new TestErrorImpl("Test error description", "Native error text", errorCode);

    // When
    response.setClientId(clientId);
    response.setTransactionId(transactionId);
    response.setStatus(status);
    response.setMessage(message);
    response.setError(error);

    // Then
    assertEquals(clientId, response.getClientId());
    assertEquals(transactionId, response.getTransactionId());
    assertEquals(status, response.getStatus());
    assertEquals(message, response.getMessage());
    assertEquals(error, response.getError());
  }

  @Test
  @DisplayName("logResponse should log all fields at DEBUG level")
  void logResponse_shouldLogAllFieldsAtDebugLevel() {
    // Given
    response.setClientId("test-client");
    response.setTransactionId("test-transaction");
    response.setStatus(Status.SUCCESS);
    response.setMessage("Success message");
    ErrorCode errorCode = new TestErrorCodeImpl("SUCCESS_CODE", "Operation successful");
    Error error = new TestErrorImpl("Success details", "No native error", errorCode);
    response.setError(error);

    // When
    response.logResponse();

    // Then
    assertEquals(1, logAppender.list.size());
    ILoggingEvent logEvent = logAppender.list.get(0);
    assertEquals(Level.DEBUG, logEvent.getLevel());

    String logMessage = logEvent.getFormattedMessage();
    assertTrue(logMessage.contains("test-client"));
    assertTrue(logMessage.contains("test-transaction"));
    assertTrue(logMessage.contains("SUCCESS"));
    assertTrue(logMessage.contains("Success message"));
    assertTrue(logMessage.contains("TestErrorImpl"));
  }

  @Test
  @DisplayName("logResponse should handle null optional fields gracefully")
  void logResponse_shouldHandleNullOptionalFieldsGracefully() {
    // Given
    response.setClientId("test-client");
    response.setTransactionId("test-transaction");
    response.setStatus(Status.SUCCESS);
    response.setMessage(null);
    response.setError(null);

    // When
    response.logResponse();

    // Then
    assertEquals(1, logAppender.list.size());
    ILoggingEvent logEvent = logAppender.list.get(0);
    assertEquals(Level.DEBUG, logEvent.getLevel());

    String logMessage = logEvent.getFormattedMessage();
    assertTrue(logMessage.contains("test-client"));
    assertTrue(logMessage.contains("test-transaction"));
    assertTrue(logMessage.contains("SUCCESS"));
    assertTrue(logMessage.contains("null")); // Should contain null for message and error
  }

  @Test
  @DisplayName("Should support different Status values")
  void shouldSupportDifferentStatusValues() {
    // Given & When & Then
    response.setStatus(Status.SUCCESS);
    assertEquals(Status.SUCCESS, response.getStatus());

    response.setStatus(Status.FAILURE);
    assertEquals(Status.FAILURE, response.getStatus());
  }

  @Test
  @DisplayName("Should handle long strings in fields")
  void shouldHandleLongStringsInFields() {
    // Given
    String longClientId = "a".repeat(1000);
    String longTransactionId = "b".repeat(1000);
    String longMessage = "c".repeat(2000);

    // When
    response.setClientId(longClientId);
    response.setTransactionId(longTransactionId);
    response.setStatus(Status.SUCCESS);
    response.setMessage(longMessage);

    // Then
    assertEquals(longClientId, response.getClientId());
    assertEquals(longTransactionId, response.getTransactionId());
    assertEquals(longMessage, response.getMessage());

    // Validation should still pass
    Set<ConstraintViolation<TestBaseResponseImpl>> violations = validator.validate(response);
    assertTrue(violations.isEmpty());
  }

  @Test
  @DisplayName("Should handle special characters in fields")
  void shouldHandleSpecialCharactersInFields() {
    // Given
    String specialClientId = "client-!@#$%^&*()_+{}[]";
    String specialTransactionId = "txn-ñáéíóú";
    String specialMessage = "Message with émojis: 🚀 ✅ ❌";

    // When
    response.setClientId(specialClientId);
    response.setTransactionId(specialTransactionId);
    response.setStatus(Status.SUCCESS);
    response.setMessage(specialMessage);

    // Then
    assertEquals(specialClientId, response.getClientId());
    assertEquals(specialTransactionId, response.getTransactionId());
    assertEquals(specialMessage, response.getMessage());
  }

  @Test
  @DisplayName("toString should include all field values when using Lombok @Data")
  void toString_shouldIncludeAllFieldValues() {
    // Given
    response.setClientId("test-client");
    response.setTransactionId("test-transaction");
    response.setStatus(Status.SUCCESS);
    response.setMessage("test message");
    ErrorCode errorCode = new TestErrorCodeImpl("TEST_CODE", "Test error");
    Error error = new TestErrorImpl("error description", "native error", errorCode);
    response.setError(error);

    // When
    String result = response.toString();

    // Then
    assertNotNull(result);
    assertTrue(result.contains("test-client"));
    assertTrue(result.contains("test-transaction"));
    assertTrue(result.contains("SUCCESS"));
    assertTrue(result.contains("test message"));
  }

  @Test
  @DisplayName("equals and hashCode should work correctly when using Lombok @Data")
  void equalsAndHashCode_shouldWorkCorrectly() {
    // Given
    TestBaseResponseImpl response1 = new TestBaseResponseImpl();
    response1.setClientId("client1");
    response1.setTransactionId("txn1");
    response1.setStatus(Status.SUCCESS);

    TestBaseResponseImpl response2 = new TestBaseResponseImpl();
    response2.setClientId("client1");
    response2.setTransactionId("txn1");
    response2.setStatus(Status.SUCCESS);

    TestBaseResponseImpl response3 = new TestBaseResponseImpl();
    response3.setClientId("client2");
    response3.setTransactionId("txn1");
    response3.setStatus(Status.SUCCESS);

    // When & Then
    assertEquals(response1, response2);
    assertEquals(response1.hashCode(), response2.hashCode());
    assertNotEquals(response1, response3);
    assertNotEquals(response1.hashCode(), response3.hashCode());
  }

  @Test
  @DisplayName("Should handle complex Error objects correctly")
  void shouldHandleComplexErrorObjectsCorrectly() {
    // Given
    ErrorCode dbErrorCode =
        new TestErrorCodeImpl("DB_CONNECTION_FAILED", "Database connection failed");
    Error dbError =
        new TestErrorImpl(
            "Unable to connect to database",
            "java.sql.SQLException: Connection refused",
            dbErrorCode);

    response.setClientId("db-client");
    response.setTransactionId("db-txn-001");
    response.setStatus(Status.FAILURE);
    response.setMessage("Database operation failed");
    response.setError(dbError);

    // When & Then
    assertEquals("db-client", response.getClientId());
    assertEquals("db-txn-001", response.getTransactionId());
    assertEquals(Status.FAILURE, response.getStatus());
    assertEquals("Database operation failed", response.getMessage());
    assertNotNull(response.getError());
    assertEquals("Unable to connect to database", response.getError().getErrorDescription());
    assertEquals("DB_CONNECTION_FAILED", response.getError().getErrorCode().getCode());

    // Validation should pass
    Set<ConstraintViolation<TestBaseResponseImpl>> violations = validator.validate(response);
    assertTrue(violations.isEmpty());
  }

  @Test
  @DisplayName("Should support typical response scenarios")
  void shouldSupportTypicalResponseScenarios() {
    // Test successful response scenario
    response.setClientId("web-app");
    response.setTransactionId("req-001");
    response.setStatus(Status.SUCCESS);
    response.setMessage("Operation completed successfully");
    response.setError(null);

    assertTrue(validator.validate(response).isEmpty());
    assertEquals(Status.SUCCESS, response.getStatus());
    assertNull(response.getError());

    // Test failure response scenario
    ErrorCode errorCode = new TestErrorCodeImpl("VALIDATION_FAILED", "Input validation failed");
    Error validationError =
        new TestErrorImpl("Required field missing", "Field 'email' cannot be null", errorCode);

    response.setStatus(Status.FAILURE);
    response.setMessage("Request validation failed");
    response.setError(validationError);

    assertTrue(validator.validate(response).isEmpty());
    assertEquals(Status.FAILURE, response.getStatus());
    assertNotNull(response.getError());
    assertEquals("VALIDATION_FAILED", response.getError().getErrorCode().getCode());
  }

  @Test
  @DisplayName("Should work correctly when subclassed")
  void shouldWorkCorrectlyWhenSubclassed() {
    // This test verifies that the abstract BaseResponse works correctly
    // when extended by concrete implementations

    TestBaseResponseImpl concreteResponse = new TestBaseResponseImpl();

    // Should be able to set all fields
    concreteResponse.setClientId("subclass-client");
    concreteResponse.setTransactionId("subclass-txn");
    concreteResponse.setStatus(Status.SUCCESS);

    // Should inherit all behavior
    assertEquals("subclass-client", concreteResponse.getClientId());
    assertEquals("subclass-txn", concreteResponse.getTransactionId());
    assertEquals(Status.SUCCESS, concreteResponse.getStatus());

    // Should inherit validation behavior
    assertTrue(validator.validate(concreteResponse).isEmpty());

    // Should inherit logging behavior
    concreteResponse.logResponse();
    assertFalse(logAppender.list.isEmpty());
  }
}
