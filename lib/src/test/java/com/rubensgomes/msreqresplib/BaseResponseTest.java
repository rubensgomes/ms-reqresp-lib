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
package com.rubensgomes.msreqresplib;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import com.rubensgomes.msreqresplib.error.Error;
import com.rubensgomes.msreqresplib.error.ErrorCode;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.EqualsAndHashCode;

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
  private ListAppender<ILoggingEvent> logAppender;
  private Logger logger;

  /** Concrete implementation of BaseResponse for testing purposes. */
  @EqualsAndHashCode(callSuper = true)
  static class TestBaseResponseImpl extends BaseResponse {

    public TestBaseResponseImpl(String clientId, String transactionId, Status status) {
      super(clientId, transactionId, status);
    }
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
    private final String errorCode;
    private final String errorDescription;
    private String nativeErrorText;

    public TestErrorImpl(String errorCode, String errorDescription, String nativeErrorText) {
      this.errorCode = errorCode;
      this.errorDescription = errorDescription;
      this.nativeErrorText = nativeErrorText;
    }

    @Override
    public String getErrorDescription() {
      return errorDescription;
    }

    @Override
    public String getNativeErrorText() {
      return nativeErrorText;
    }

    // Additional method for testing - not part of the Error interface
    public void setNativeErrorText(String nativeErrorText) {
      this.nativeErrorText = nativeErrorText;
    }

    @Override
    public ErrorCode getErrorCode() {
      return new TestErrorCodeImpl(errorCode, errorDescription);
    }

    @Override
    public String toString() {
      return String.format(
          "TestErrorImpl{errorCode='%s', errorDescription='%s', nativeErrorText='%s'}",
          errorCode, errorDescription, nativeErrorText);
    }
  }

  @BeforeEach
  void setUp() {
    factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();

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

  @Nested
  @DisplayName("Constructor Tests")
  class ConstructorTests {

    @Test
    @DisplayName("Should create instance with valid parameters")
    void constructor_shouldCreateInstance_withValidParameters() {
      String clientId = "test-client-123";
      String transactionId = "test-transaction-456";
      Status status = Status.SUCCESS;

      TestBaseResponseImpl response = new TestBaseResponseImpl(clientId, transactionId, status);

      assertNotNull(response, "Response should be created successfully");
      assertEquals(clientId, response.getClientId(), "ClientId should be set correctly");
      assertEquals(
          transactionId, response.getTransactionId(), "TransactionId should be set correctly");
      assertEquals(status, response.getStatus(), "Status should be set correctly");
    }

    @Test
    @DisplayName("Should accept null values in constructor")
    void constructor_shouldAcceptNullValues() {
      assertDoesNotThrow(
          () -> {
            TestBaseResponseImpl response = new TestBaseResponseImpl(null, null, null);
            assertNotNull(response, "Response should be created even with null values");
            assertNull(response.getClientId(), "ClientId should be null");
            assertNull(response.getTransactionId(), "TransactionId should be null");
            assertNull(response.getStatus(), "Status should be null");
          },
          "Constructor should accept null values without throwing exceptions");
    }
  }

  @Nested
  @DisplayName("Validation Tests")
  class ValidationTests {

    @Test
    @DisplayName("Should pass validation when all required fields are present")
    void validation_shouldPass_whenRequiredFieldsPresent() {
      TestBaseResponseImpl response =
          new TestBaseResponseImpl("test-client", "test-transaction", Status.SUCCESS);

      Set<ConstraintViolation<TestBaseResponseImpl>> violations = validator.validate(response);

      assertTrue(violations.isEmpty(), "Should have no validation violations");
    }

    @Test
    @DisplayName("Should fail validation when clientId is null")
    void validation_shouldFail_whenClientIdIsNull() {
      TestBaseResponseImpl response =
          new TestBaseResponseImpl(null, "test-transaction", Status.SUCCESS);

      Set<ConstraintViolation<TestBaseResponseImpl>> violations = validator.validate(response);

      assertEquals(1, violations.size());
      ConstraintViolation<TestBaseResponseImpl> violation = violations.iterator().next();
      assertEquals("clientId", violation.getPropertyPath().toString());
      assertEquals("clientId is required", violation.getMessage());
    }

    @Test
    @DisplayName("Should fail validation when clientId is blank")
    void validation_shouldFail_whenClientIdIsBlank() {
      TestBaseResponseImpl response =
          new TestBaseResponseImpl("", "test-transaction", Status.SUCCESS);

      Set<ConstraintViolation<TestBaseResponseImpl>> violations = validator.validate(response);

      assertEquals(1, violations.size());
      ConstraintViolation<TestBaseResponseImpl> violation = violations.iterator().next();
      assertEquals("clientId", violation.getPropertyPath().toString());
      assertEquals("clientId is required", violation.getMessage());
    }

    @Test
    @DisplayName("Should fail validation when clientId is whitespace only")
    void validation_shouldFail_whenClientIdIsWhitespaceOnly() {
      TestBaseResponseImpl response =
          new TestBaseResponseImpl("   ", "test-transaction", Status.SUCCESS);

      Set<ConstraintViolation<TestBaseResponseImpl>> violations = validator.validate(response);

      assertEquals(1, violations.size());
      ConstraintViolation<TestBaseResponseImpl> violation = violations.iterator().next();
      assertEquals("clientId", violation.getPropertyPath().toString());
      assertEquals("clientId is required", violation.getMessage());
    }

    @Test
    @DisplayName("Should fail validation when transactionId is null")
    void validation_shouldFail_whenTransactionIdIsNull() {
      TestBaseResponseImpl response = new TestBaseResponseImpl("test-client", null, Status.SUCCESS);

      Set<ConstraintViolation<TestBaseResponseImpl>> violations = validator.validate(response);

      assertEquals(1, violations.size());
      ConstraintViolation<TestBaseResponseImpl> violation = violations.iterator().next();
      assertEquals("transactionId", violation.getPropertyPath().toString());
      assertEquals("transactionId is required", violation.getMessage());
    }

    @Test
    @DisplayName("Should fail validation when transactionId is blank")
    void validation_shouldFail_whenTransactionIdIsBlank() {
      TestBaseResponseImpl response = new TestBaseResponseImpl("test-client", "", Status.SUCCESS);

      Set<ConstraintViolation<TestBaseResponseImpl>> violations = validator.validate(response);

      assertEquals(1, violations.size());
      ConstraintViolation<TestBaseResponseImpl> violation = violations.iterator().next();
      assertEquals("transactionId", violation.getPropertyPath().toString());
      assertEquals("transactionId is required", violation.getMessage());
    }

    @Test
    @DisplayName("Should fail validation when status is null")
    void validation_shouldFail_whenStatusIsNull() {
      TestBaseResponseImpl response =
          new TestBaseResponseImpl("test-client", "test-transaction", null);

      Set<ConstraintViolation<TestBaseResponseImpl>> violations = validator.validate(response);

      assertEquals(1, violations.size());
      ConstraintViolation<TestBaseResponseImpl> violation = violations.iterator().next();
      assertEquals("status", violation.getPropertyPath().toString());
      assertEquals("status is required", violation.getMessage());
    }

    @Test
    @DisplayName("Should fail validation when multiple required fields are missing")
    void validation_shouldFail_whenMultipleRequiredFieldsMissing() {
      TestBaseResponseImpl response = new TestBaseResponseImpl(null, null, null);

      Set<ConstraintViolation<TestBaseResponseImpl>> violations = validator.validate(response);

      assertEquals(
          3, violations.size(), "Should have violations for clientId, transactionId, and status");
    }

    @Test
    @DisplayName("Optional fields should be allowed to be null")
    void validation_shouldPass_whenOptionalFieldsAreNull() {
      TestBaseResponseImpl response =
          new TestBaseResponseImpl("test-client", "test-transaction", Status.SUCCESS);
      response.setMessage(null);
      response.setError(null);

      Set<ConstraintViolation<TestBaseResponseImpl>> violations = validator.validate(response);

      assertTrue(violations.isEmpty(), "Optional fields should not cause validation failures");
    }
  }

  @Nested
  @DisplayName("Field Access Tests")
  class FieldAccessTests {

    @Test
    @DisplayName("Should allow setting and getting optional fields")
    void shouldAllowSettingAndGettingOptionalFields() {
      TestBaseResponseImpl response =
          new TestBaseResponseImpl("test-client-123", "txn-456", Status.ERROR);

      String message = "Test message";
      Error error = new TestErrorImpl("TEST_ERROR", "Test error occurred", "Native error text");

      response.setMessage(message);
      response.setError(error);

      assertEquals("test-client-123", response.getClientId());
      assertEquals("txn-456", response.getTransactionId());
      assertEquals(Status.ERROR, response.getStatus());
      assertEquals(message, response.getMessage());
      assertEquals(error, response.getError());
    }

    @Test
    @DisplayName("Should handle null values for optional fields")
    void shouldHandleNullValuesForOptionalFields() {
      TestBaseResponseImpl response =
          new TestBaseResponseImpl("test-client", "test-transaction", Status.SUCCESS);

      response.setMessage(null);
      response.setError(null);

      assertNull(response.getMessage(), "Message should be null");
      assertNull(response.getError(), "Error should be null");
    }
  }

  @Nested
  @DisplayName("Logging Tests")
  class LoggingTests {

    @Test
    @DisplayName("logResponse should log all fields at DEBUG level")
    void logResponse_shouldLogAllFieldsAtDebugLevel() {
      TestBaseResponseImpl response =
          new TestBaseResponseImpl("test-client", "test-transaction", Status.SUCCESS);
      response.setMessage("Success message");
      Error error = new TestErrorImpl("SUCCESS_CODE", "Operation successful", "No native error");
      response.setError(error);

      response.logResponse();

      assertEquals(1, logAppender.list.size());
      ILoggingEvent logEvent = logAppender.list.get(0);
      assertEquals(Level.DEBUG, logEvent.getLevel());

      String logMessage = logEvent.getFormattedMessage();
      assertTrue(logMessage.contains("test-client"));
      assertTrue(logMessage.contains("test-transaction"));
      assertTrue(logMessage.contains("SUCCESS"));
      assertTrue(logMessage.contains("Success message"));
    }

    @Test
    @DisplayName("logResponse should handle null optional fields gracefully")
    void logResponse_shouldHandleNullOptionalFieldsGracefully() {
      TestBaseResponseImpl response =
          new TestBaseResponseImpl("test-client", "test-transaction", Status.SUCCESS);
      response.setMessage(null);
      response.setError(null);

      response.logResponse();

      assertEquals(1, logAppender.list.size());
      ILoggingEvent logEvent = logAppender.list.get(0);
      assertEquals(Level.DEBUG, logEvent.getLevel());

      String logMessage = logEvent.getFormattedMessage();
      assertTrue(logMessage.contains("test-client"));
      assertTrue(logMessage.contains("test-transaction"));
      assertTrue(logMessage.contains("SUCCESS"));
      assertTrue(logMessage.contains("null")); // Should contain null for message and error
    }
  }

  @Nested
  @DisplayName("Status Handling Tests")
  class StatusHandlingTests {

    @Test
    @DisplayName("Should support different Status values")
    void shouldSupportDifferentStatusValues() {
      TestBaseResponseImpl successResponse =
          new TestBaseResponseImpl("client", "txn", Status.SUCCESS);
      assertEquals(Status.SUCCESS, successResponse.getStatus());

      TestBaseResponseImpl errorResponse = new TestBaseResponseImpl("client", "txn", Status.ERROR);
      assertEquals(Status.ERROR, errorResponse.getStatus());
    }
  }

  @Nested
  @DisplayName("Equality and HashCode Tests")
  class EqualityTests {

    @Test
    @DisplayName("Should be equal when all fields match")
    void shouldBeEqual_whenAllFieldsMatch() {
      TestBaseResponseImpl response1 =
          new TestBaseResponseImpl("client-123", "transaction-456", Status.SUCCESS);
      response1.setMessage("test message");

      TestBaseResponseImpl response2 =
          new TestBaseResponseImpl("client-123", "transaction-456", Status.SUCCESS);
      response2.setMessage("test message");

      assertEquals(response1, response2, "Responses should be equal when all fields match");
      assertEquals(
          response1.hashCode(),
          response2.hashCode(),
          "Hash codes should be equal for equal objects");
    }

    @Test
    @DisplayName("Should not be equal when core fields differ")
    void shouldNotBeEqual_whenCoreFieldsDiffer() {
      TestBaseResponseImpl response1 =
          new TestBaseResponseImpl("client-123", "transaction-456", Status.SUCCESS);
      TestBaseResponseImpl response2 =
          new TestBaseResponseImpl("client-789", "transaction-456", Status.SUCCESS);

      assertNotEquals(response1, response2, "Responses should not be equal when clientId differs");
    }

    @Test
    @DisplayName("Should handle null comparisons correctly")
    void shouldHandleNullComparisons() {
      TestBaseResponseImpl response =
          new TestBaseResponseImpl("client-123", "transaction-456", Status.SUCCESS);

      assertNotEquals(null, response, "Response should not be equal to null");
      assertNotEquals("string", response, "Response should not be equal to different type");
    }
  }

  @Nested
  @DisplayName("ToString Tests")
  class ToStringTests {

    @Test
    @DisplayName("Should include field values in toString output")
    void toString_shouldIncludeFieldValues() {
      TestBaseResponseImpl response =
          new TestBaseResponseImpl("test-client", "test-transaction", Status.SUCCESS);
      response.setMessage("test message");

      String toStringOutput = response.toString();

      assertNotNull(toStringOutput, "toString should not return null");
      assertTrue(toStringOutput.contains("test-client"), "toString should include clientId value");
      assertTrue(
          toStringOutput.contains("test-transaction"),
          "toString should include transactionId value");
      assertTrue(toStringOutput.contains("SUCCESS"), "toString should include status value");
    }

    @Test
    @DisplayName("Should handle null values in toString")
    void toString_shouldHandleNullValues() {
      TestBaseResponseImpl response = new TestBaseResponseImpl(null, null, null);

      assertDoesNotThrow(
          () -> {
            String toStringOutput = response.toString();
            assertNotNull(toStringOutput, "toString should not return null even with null fields");
          },
          "toString should handle null values without throwing exceptions");
    }
  }
}
