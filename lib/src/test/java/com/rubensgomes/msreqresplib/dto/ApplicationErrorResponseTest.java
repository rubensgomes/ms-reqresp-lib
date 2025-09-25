/*
 * Copyright 2025 Rubens Gomes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
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
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.rubensgomes.msreqresplib.Status;
import com.rubensgomes.msreqresplib.error.ApplicationError;
import com.rubensgomes.msreqresplib.error.Error;
import com.rubensgomes.msreqresplib.error.ErrorCode;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@DisplayName("ApplicationErrorResponse Tests")
class ApplicationErrorResponseTest {

  private ValidatorFactory factory;
  private Validator validator;

  @BeforeEach
  void setUp() {
    factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /** Simple test implementation of ErrorCode for testing purposes. */
  static class TestErrorCode implements ErrorCode {
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
  }

  @Nested
  @DisplayName("Constructor Tests")
  class ConstructorTests {

    @Test
    @DisplayName("Should create error response with valid parameters")
    void shouldCreateErrorResponseWithValidParameters() {
      // Given
      String clientId = "test-service";
      String transactionId = UUID.randomUUID().toString();
      Status status = Status.ERROR;
      TestErrorCode errorCode = new TestErrorCode("TEST001", "Test error");
      Error error = new ApplicationError("Test error message", errorCode);

      // When
      ApplicationErrorResponse response =
          new ApplicationErrorResponse(clientId, transactionId, status, error);

      // Then
      assertNotNull(response);
      assertEquals(clientId, response.getClientId());
      assertEquals(transactionId, response.getTransactionId());
      assertEquals(status, response.getStatus());
      assertEquals(error, response.getError());
    }

    @Test
    @DisplayName("Should create error response with different status values")
    void shouldCreateErrorResponseWithDifferentStatusValues() {
      // Given
      String clientId = "status-test-service";
      String transactionId = "status-test-123";
      TestErrorCode errorCode = new TestErrorCode("TEST002", "Status test error");
      Error error = new ApplicationError("Status test message", errorCode);

      // When/Then - Test with various status values
      Status[] testStatuses = {Status.ERROR, Status.PROCESSING};
      for (Status status : testStatuses) {
        ApplicationErrorResponse response =
            new ApplicationErrorResponse(clientId, transactionId, status, error);
        assertEquals(status, response.getStatus());
        assertEquals(error, response.getError());
      }
    }

    @Test
    @DisplayName("Should create error response with complex error object")
    void shouldCreateErrorResponseWithComplexErrorObject() {
      // Given
      String clientId = "complex-service";
      String transactionId = "complex-test-456";
      Status status = Status.ERROR;
      TestErrorCode errorCode = new TestErrorCode("COMPLEX001", "Complex error code");
      ApplicationError complexError =
          new ApplicationError("Complex error with detailed information", errorCode);
      complexError.setNativeErrorText("Detailed native error information for debugging");

      // When
      ApplicationErrorResponse response =
          new ApplicationErrorResponse(clientId, transactionId, status, complexError);

      // Then
      assertEquals(clientId, response.getClientId());
      assertEquals(transactionId, response.getTransactionId());
      assertEquals(status, response.getStatus());
      assertEquals(complexError, response.getError());
    }

    @Test
    @DisplayName("Should allow creation with null parameters but fail validation")
    void shouldAllowCreationWithNullParametersButFailValidation() {
      // Given
      String clientId = "valid-client-id";
      String transactionId = "valid-transaction-id";
      Status status = Status.ERROR;
      TestErrorCode errorCode = new TestErrorCode("TEST003", "Null test");
      Error error = new ApplicationError("Test error", errorCode);

      // When - These should not throw exceptions at construction time
      assertDoesNotThrow(() -> new ApplicationErrorResponse(null, transactionId, status, error));
      assertDoesNotThrow(() -> new ApplicationErrorResponse(clientId, null, status, error));
      assertDoesNotThrow(() -> new ApplicationErrorResponse(clientId, transactionId, null, error));
      assertDoesNotThrow(() -> new ApplicationErrorResponse(clientId, transactionId, status, null));

      // Then - But validation should catch these issues
      ApplicationErrorResponse responseWithNullClient =
          new ApplicationErrorResponse(null, transactionId, status, error);
      Set<ConstraintViolation<ApplicationErrorResponse>> violations =
          validator.validate(responseWithNullClient);
      assertFalse(violations.isEmpty(), "Should have validation violations for null clientId");
    }
  }

  @Nested
  @DisplayName("Validation Tests")
  class ValidationTests {

    @Test
    @DisplayName("Should pass validation with valid fields")
    void shouldPassValidationWithValidFields() {
      // Given
      TestErrorCode errorCode = new TestErrorCode("VALID001", "Valid error code");
      Error error = new ApplicationError("Valid error message", errorCode);
      ApplicationErrorResponse response =
          new ApplicationErrorResponse("valid-service", "valid-tx-123", Status.ERROR, error);

      // When
      Set<ConstraintViolation<ApplicationErrorResponse>> violations = validator.validate(response);

      // Then
      assertTrue(violations.isEmpty(), "Should have no validation violations");
    }

    @Test
    @DisplayName("Should fail validation when clientId is null")
    void shouldFailValidationWhenClientIdIsNull() {
      // Given
      TestErrorCode errorCode = new TestErrorCode("NULL001", "Null client test");
      Error error = new ApplicationError("Test error", errorCode);
      ApplicationErrorResponse response =
          new ApplicationErrorResponse(null, "valid-transaction-id", Status.ERROR, error);

      // When
      Set<ConstraintViolation<ApplicationErrorResponse>> violations = validator.validate(response);

      // Then
      assertFalse(violations.isEmpty(), "Should have validation violations");
      assertTrue(
          violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("clientId")),
          "Should have clientId validation violation");
    }

    @Test
    @DisplayName("Should fail validation when clientId is empty")
    void shouldFailValidationWhenClientIdIsEmpty() {
      // Given
      TestErrorCode errorCode = new TestErrorCode("EMPTY001", "Empty client test");
      Error error = new ApplicationError("Test error", errorCode);
      ApplicationErrorResponse response =
          new ApplicationErrorResponse("", "valid-transaction-id", Status.ERROR, error);

      // When
      Set<ConstraintViolation<ApplicationErrorResponse>> violations = validator.validate(response);

      // Then
      assertFalse(violations.isEmpty(), "Should have validation violations");
      assertTrue(
          violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("clientId")),
          "Should have clientId validation violation");
    }

    @Test
    @DisplayName("Should fail validation when clientId is blank")
    void shouldFailValidationWhenClientIdIsBlank() {
      // Given
      TestErrorCode errorCode = new TestErrorCode("BLANK001", "Blank client test");
      Error error = new ApplicationError("Test error", errorCode);
      ApplicationErrorResponse response =
          new ApplicationErrorResponse("   ", "valid-transaction-id", Status.ERROR, error);

      // When
      Set<ConstraintViolation<ApplicationErrorResponse>> violations = validator.validate(response);

      // Then
      assertFalse(violations.isEmpty(), "Should have validation violations");
      assertTrue(
          violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("clientId")),
          "Should have clientId validation violation");
    }

    @Test
    @DisplayName("Should fail validation when transactionId is null")
    void shouldFailValidationWhenTransactionIdIsNull() {
      // Given
      TestErrorCode errorCode = new TestErrorCode("NULL002", "Null transaction test");
      Error error = new ApplicationError("Test error", errorCode);
      ApplicationErrorResponse response =
          new ApplicationErrorResponse("valid-client-id", null, Status.ERROR, error);

      // When
      Set<ConstraintViolation<ApplicationErrorResponse>> violations = validator.validate(response);

      // Then
      assertFalse(violations.isEmpty(), "Should have validation violations");
      assertTrue(
          violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("transactionId")),
          "Should have transactionId validation violation");
    }

    @Test
    @DisplayName("Should fail validation when transactionId is empty")
    void shouldFailValidationWhenTransactionIdIsEmpty() {
      // Given
      TestErrorCode errorCode = new TestErrorCode("EMPTY002", "Empty transaction test");
      Error error = new ApplicationError("Test error", errorCode);
      ApplicationErrorResponse response =
          new ApplicationErrorResponse("valid-client-id", "", Status.ERROR, error);

      // When
      Set<ConstraintViolation<ApplicationErrorResponse>> violations = validator.validate(response);

      // Then
      assertFalse(violations.isEmpty(), "Should have validation violations");
      assertTrue(
          violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("transactionId")),
          "Should have transactionId validation violation");
    }

    @Test
    @DisplayName("Should fail validation when transactionId is blank")
    void shouldFailValidationWhenTransactionIdIsBlank() {
      // Given
      TestErrorCode errorCode = new TestErrorCode("BLANK002", "Blank transaction test");
      Error error = new ApplicationError("Test error", errorCode);
      ApplicationErrorResponse response =
          new ApplicationErrorResponse("valid-client-id", "\t\n ", Status.ERROR, error);

      // When
      Set<ConstraintViolation<ApplicationErrorResponse>> violations = validator.validate(response);

      // Then
      assertFalse(violations.isEmpty(), "Should have validation violations");
      assertTrue(
          violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("transactionId")),
          "Should have transactionId validation violation");
    }

    @Test
    @DisplayName("Should fail validation when status is null")
    void shouldFailValidationWhenStatusIsNull() {
      // Given
      TestErrorCode errorCode = new TestErrorCode("NULL003", "Null status test");
      Error error = new ApplicationError("Test error", errorCode);
      ApplicationErrorResponse response =
          new ApplicationErrorResponse("valid-client-id", "valid-transaction-id", null, error);

      // When
      Set<ConstraintViolation<ApplicationErrorResponse>> violations = validator.validate(response);

      // Then
      assertFalse(violations.isEmpty(), "Should have validation violations");
      assertTrue(
          violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("status")),
          "Should have status validation violation");
    }
  }

  @Nested
  @DisplayName("Error Handling Tests")
  class ErrorHandlingTests {

    @Test
    @DisplayName("Should maintain error information after construction")
    void shouldMaintainErrorInformationAfterConstruction() {
      // Given
      String clientId = "error-maintain-service";
      String transactionId = "error-maintain-test";
      Status status = Status.ERROR;
      TestErrorCode errorCode = new TestErrorCode("MAINTAIN001", "Maintain test error");
      Error originalError = new ApplicationError("Original error message", errorCode);

      // When
      ApplicationErrorResponse response =
          new ApplicationErrorResponse(clientId, transactionId, status, originalError);

      // Then
      assertNotNull(response.getError());
      assertEquals(originalError, response.getError());
      assertEquals("Original error message", response.getError().getErrorDescription());
      assertEquals("MAINTAIN001", response.getError().getErrorCode().getCode());
    }

    @Test
    @DisplayName("Should allow error modification after construction")
    void shouldAllowErrorModificationAfterConstruction() {
      // Given
      String clientId = "error-modify-service";
      String transactionId = "error-modify-test";
      Status status = Status.ERROR;
      TestErrorCode originalErrorCode = new TestErrorCode("MODIFY001", "Original error");
      TestErrorCode newErrorCode = new TestErrorCode("MODIFY002", "Modified error");
      Error originalError = new ApplicationError("Original error message", originalErrorCode);
      Error newError = new ApplicationError("Modified error message", newErrorCode);

      ApplicationErrorResponse response =
          new ApplicationErrorResponse(clientId, transactionId, status, originalError);

      // When
      response.setError(newError);

      // Then
      assertEquals(newError, response.getError());
      assertEquals("Modified error message", response.getError().getErrorDescription());
      assertEquals("MODIFY002", response.getError().getErrorCode().getCode());
      assertNotEquals(originalError, response.getError());
    }

    @Test
    @DisplayName("Should handle different error types")
    void shouldHandleDifferentErrorTypes() {
      // Given
      String clientId = "error-types-service";
      String transactionId = "error-types-test";
      Status status = Status.ERROR;

      // Test with basic ApplicationError
      TestErrorCode basicErrorCode = new TestErrorCode("BASIC001", "Basic error");
      Error basicError = new ApplicationError("Basic error message", basicErrorCode);

      // Test with ApplicationError containing native error text
      TestErrorCode detailedErrorCode = new TestErrorCode("DETAILED001", "Detailed error");
      ApplicationError detailedError =
          new ApplicationError("Detailed error message", detailedErrorCode);
      detailedError.setNativeErrorText("Native system error details");

      // When/Then
      ApplicationErrorResponse basicResponse =
          new ApplicationErrorResponse(clientId, transactionId, status, basicError);
      assertEquals(basicError, basicResponse.getError());

      ApplicationErrorResponse detailedResponse =
          new ApplicationErrorResponse(clientId, transactionId, status, detailedError);
      assertEquals(detailedError, detailedResponse.getError());
      assertEquals("Native system error details", detailedError.getNativeErrorText());
    }
  }

  @Nested
  @DisplayName("Inheritance Tests")
  class InheritanceTests {

    @Test
    @DisplayName("Should inherit BaseResponse functionality")
    void shouldInheritBaseResponseFunctionality() {
      // Given
      String clientId = "inheritance-service";
      String transactionId = "inheritance-test";
      Status status = Status.ERROR;
      TestErrorCode errorCode = new TestErrorCode("INHERIT001", "Inheritance test");
      Error error = new ApplicationError("Inheritance test error", errorCode);

      // When
      ApplicationErrorResponse response =
          new ApplicationErrorResponse(clientId, transactionId, status, error);

      // Then - Should have all BaseResponse functionality
      assertEquals(clientId, response.getClientId());
      assertEquals(transactionId, response.getTransactionId());
      assertEquals(status, response.getStatus());
      assertDoesNotThrow(() -> response.logResponse());
      assertNotNull(response.toString());
      assertTrue(response.hashCode() != 0);
    }

    @Test
    @DisplayName("Should be instance of BaseResponse")
    void shouldBeInstanceOfBaseResponse() {
      // Given
      TestErrorCode errorCode = new TestErrorCode("INSTANCE001", "Instance test");
      Error error = new ApplicationError("Instance test error", errorCode);
      ApplicationErrorResponse response =
          new ApplicationErrorResponse("instance-service", "instance-test", Status.ERROR, error);

      // When/Then
      assertTrue(
          response instanceof com.rubensgomes.msreqresplib.BaseResponse,
          "Should be instance of BaseResponse");
    }
  }

  @Nested
  @DisplayName("Equals and HashCode Tests")
  class EqualsAndHashCodeTests {

    @Test
    @DisplayName("Should be equal when all fields are the same")
    void shouldBeEqualWhenAllFieldsAreTheSame() {
      // Given
      String clientId = "equals-service";
      String transactionId = "equals-test-123";
      Status status = Status.ERROR;
      TestErrorCode errorCode = new TestErrorCode("EQUALS001", "Equals test");
      Error error = new ApplicationError("Equals test error", errorCode);

      ApplicationErrorResponse response1 =
          new ApplicationErrorResponse(clientId, transactionId, status, error);
      ApplicationErrorResponse response2 =
          new ApplicationErrorResponse(clientId, transactionId, status, error);

      // When/Then
      assertEquals(response1, response2, "Responses with same fields should be equal");
      assertEquals(
          response1.hashCode(), response2.hashCode(), "Equal responses should have same hash code");
    }

    @Test
    @DisplayName("Should not be equal when error differs")
    void shouldNotBeEqualWhenErrorDiffers() {
      // Given
      String clientId = "error-diff-service";
      String transactionId = "error-diff-test";
      Status status = Status.ERROR;
      TestErrorCode errorCode1 = new TestErrorCode("DIFF001", "First error");
      TestErrorCode errorCode2 = new TestErrorCode("DIFF002", "Second error");
      Error error1 = new ApplicationError("First error message", errorCode1);
      Error error2 = new ApplicationError("Second error message", errorCode2);

      ApplicationErrorResponse response1 =
          new ApplicationErrorResponse(clientId, transactionId, status, error1);
      ApplicationErrorResponse response2 =
          new ApplicationErrorResponse(clientId, transactionId, status, error2);

      // When/Then
      assertNotEquals(response1, response2, "Responses with different errors should not be equal");
    }

    @Test
    @DisplayName("Should not be equal when core fields differ")
    void shouldNotBeEqualWhenCoreFieldsDiffer() {
      // Given
      TestErrorCode errorCode = new TestErrorCode("CORE001", "Core fields test");
      Error error = new ApplicationError("Core fields test error", errorCode);

      ApplicationErrorResponse response1 =
          new ApplicationErrorResponse("service-a", "tx-111", Status.ERROR, error);
      ApplicationErrorResponse response2 =
          new ApplicationErrorResponse("service-b", "tx-222", Status.ERROR, error);

      // When/Then
      assertNotEquals(
          response1, response2, "Responses with different core fields should not be equal");
    }
  }

  @Nested
  @DisplayName("ToString Tests")
  class ToStringTests {

    @Test
    @DisplayName("Should contain all fields in toString")
    void shouldContainAllFieldsInToString() {
      // Given
      String clientId = "toString-service";
      String transactionId = "toString-test-789";
      Status status = Status.ERROR;
      TestErrorCode errorCode = new TestErrorCode("TOSTRING001", "ToString test");
      Error error = new ApplicationError("ToString test error", errorCode);

      ApplicationErrorResponse response =
          new ApplicationErrorResponse(clientId, transactionId, status, error);

      // When
      String toString = response.toString();

      // Then
      assertNotNull(toString, "toString should not return null");
      assertTrue(toString.contains(clientId), "toString should contain clientId");
      assertTrue(toString.contains(transactionId), "toString should contain transactionId");
      assertTrue(toString.contains(status.toString()), "toString should contain status");
      assertTrue(toString.contains("error"), "toString should mention error field");
    }

    @Test
    @DisplayName("Should handle special characters in toString")
    void shouldHandleSpecialCharactersInToString() {
      // Given
      TestErrorCode errorCode = new TestErrorCode("SPECIAL001", "Special chars test");
      Error error = new ApplicationError("Special chars: @#$%^&*()", errorCode);
      ApplicationErrorResponse response =
          new ApplicationErrorResponse("service@special#chars", "tx$123%456", Status.ERROR, error);

      // When/Then
      assertDoesNotThrow(
          () -> response.toString(),
          "toString should handle special characters without exceptions");
    }
  }

  @Nested
  @DisplayName("Logging Tests")
  class LoggingTests {

    @Test
    @DisplayName("Should log response without exceptions")
    void shouldLogResponseWithoutExceptions() {
      // Given
      TestErrorCode errorCode = new TestErrorCode("LOG001", "Logging test");
      Error error = new ApplicationError("Logging test error", errorCode);
      ApplicationErrorResponse response =
          new ApplicationErrorResponse("log-service", "log-test-123", Status.ERROR, error);

      // When/Then - Should not throw any exceptions
      assertDoesNotThrow(() -> response.logResponse(), "logResponse should not throw exceptions");
    }

    @Test
    @DisplayName("Should handle logging with complex error details")
    void shouldHandleLoggingWithComplexErrorDetails() {
      // Given
      TestErrorCode errorCode = new TestErrorCode("COMPLEX_LOG001", "Complex logging test");
      ApplicationError complexError =
          new ApplicationError("Complex error with detailed information", errorCode);
      complexError.setNativeErrorText("Native error text with\nnewlines\tand\ttabs");

      ApplicationErrorResponse response =
          new ApplicationErrorResponse(
              "complex-log-service", "complex-log-test", Status.ERROR, complexError);

      // When/Then - Should not throw any exceptions
      assertDoesNotThrow(
          () -> response.logResponse(),
          "logResponse should handle complex error details without issues");
    }
  }

  @Nested
  @DisplayName("Thread Safety Tests")
  class ThreadSafetyTests {

    @Test
    @DisplayName("Should be thread-safe for read operations")
    void shouldBeThreadSafeForReadOperations() throws InterruptedException {
      // Given
      TestErrorCode errorCode = new TestErrorCode("THREAD001", "Thread safety test");
      Error error = new ApplicationError("Thread safety test error", errorCode);
      ApplicationErrorResponse response =
          new ApplicationErrorResponse("thread-service", "thread-test", Status.ERROR, error);

      int numberOfThreads = 10;
      int operationsPerThread = 1000;
      Thread[] threads = new Thread[numberOfThreads];

      // When
      for (int i = 0; i < numberOfThreads; i++) {
        threads[i] =
            new Thread(
                () -> {
                  for (int j = 0; j < operationsPerThread; j++) {
                    // Perform read operations concurrently
                    String clientId = response.getClientId();
                    String transactionId = response.getTransactionId();
                    Status status = response.getStatus();
                    Error responseError = response.getError();
                    String toString = response.toString();
                    int hashCode = response.hashCode();
                    response.logResponse();

                    // Verify values remain consistent
                    assertEquals("thread-service", clientId);
                    assertEquals("thread-test", transactionId);
                    assertEquals(Status.ERROR, status);
                    assertEquals(error, responseError);
                    assertNotNull(toString);
                    assertTrue(hashCode != 0);
                  }
                });
        threads[i].start();
      }

      // Wait for all threads to complete
      for (Thread thread : threads) {
        thread.join();
      }

      // Then - If we reach here without exceptions, the test passes
      assertTrue(true, "Concurrent read operations completed without issues");
    }
  }

  @Nested
  @DisplayName("Edge Cases Tests")
  class EdgeCasesTests {

    @Test
    @DisplayName("Should handle very long field values")
    void shouldHandleVeryLongFieldValues() {
      // Given
      String longClientId = "a".repeat(1000);
      String longTransactionId = "b".repeat(1000);
      Status status = Status.ERROR;
      TestErrorCode errorCode = new TestErrorCode("LONG001", "Long fields test");
      Error error = new ApplicationError("Very long error message: " + "c".repeat(500), errorCode);

      // When
      ApplicationErrorResponse response =
          new ApplicationErrorResponse(longClientId, longTransactionId, status, error);

      // Then
      assertEquals(longClientId, response.getClientId());
      assertEquals(longTransactionId, response.getTransactionId());
      assertEquals(status, response.getStatus());
      assertEquals(error, response.getError());
      assertDoesNotThrow(() -> response.logResponse());
      assertDoesNotThrow(() -> response.toString());
    }

    @Test
    @DisplayName("Should handle Unicode characters")
    void shouldHandleUnicodeCharacters() {
      // Given
      String unicodeClientId = "服务-αβγ-сервис";
      String unicodeTransactionId = "交易-δεζ-транзакция";
      Status status = Status.ERROR;
      TestErrorCode errorCode = new TestErrorCode("UNICODE001", "Unicode test ñáéíóú");
      Error error = new ApplicationError("Unicode error: 测试错误信息", errorCode);

      // When
      ApplicationErrorResponse response =
          new ApplicationErrorResponse(unicodeClientId, unicodeTransactionId, status, error);

      // Then
      assertEquals(unicodeClientId, response.getClientId());
      assertEquals(unicodeTransactionId, response.getTransactionId());
      assertEquals(status, response.getStatus());
      assertEquals(error, response.getError());
      assertDoesNotThrow(() -> response.logResponse());
      assertDoesNotThrow(() -> response.toString());
    }

    @Test
    @DisplayName("Should handle boundary field values")
    void shouldHandleBoundaryFieldValues() {
      // Given - Using single character values (boundary case for @NotBlank)
      String minClientId = "a";
      String minTransactionId = "1";
      Status status = Status.ERROR;
      TestErrorCode errorCode = new TestErrorCode("B", "Boundary test");
      Error error = new ApplicationError("E", errorCode);

      // When
      ApplicationErrorResponse response =
          new ApplicationErrorResponse(minClientId, minTransactionId, status, error);

      // Then
      Set<ConstraintViolation<ApplicationErrorResponse>> violations = validator.validate(response);
      assertTrue(violations.isEmpty(), "Single character values should be valid");
      assertEquals(minClientId, response.getClientId());
      assertEquals(minTransactionId, response.getTransactionId());
      assertEquals(status, response.getStatus());
      assertEquals(error, response.getError());
    }
  }
}
