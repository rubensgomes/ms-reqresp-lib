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
package com.rubensgomes.msreqresplib;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.rubensgomes.msreqresplib.error.ApplicationError;
import com.rubensgomes.msreqresplib.error.Error;
import com.rubensgomes.msreqresplib.error.ErrorCode;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@DisplayName("BaseResponse Tests")
class BaseResponseTest {

  private ValidatorFactory factory;
  private Validator validator;

  @BeforeEach
  void setUp() {
    factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /**
   * Concrete implementation of BaseResponse for testing purposes. This allows us to test the
   * abstract BaseResponse class functionality.
   */
  @EqualsAndHashCode(callSuper = true)
  static class TestResponse extends BaseResponse {
    private final String data;

    public TestResponse(String clientId, String transactionId, Status status) {
      super(clientId, transactionId, status);
      this.data = null;
    }

    public TestResponse(String clientId, String transactionId, Status status, String data) {
      super(clientId, transactionId, status);
      this.data = data;
    }

    public String getData() {
      return data;
    }
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
    @DisplayName("Should create response with valid parameters")
    void shouldCreateResponseWithValidParameters() {
      // Given
      String clientId = "test-service";
      String transactionId = UUID.randomUUID().toString();
      Status status = Status.SUCCESS;

      // When
      TestResponse response = new TestResponse(clientId, transactionId, status);

      // Then
      assertNotNull(response);
      assertEquals(clientId, response.getClientId());
      assertEquals(transactionId, response.getTransactionId());
      assertEquals(status, response.getStatus());
      assertNull(response.getError());
    }

    @Test
    @DisplayName("Should create response with all status types")
    void shouldCreateResponseWithAllStatusTypes() {
      // Given
      String clientId = "status-test-service";
      String transactionId = "status-test-123";

      // When/Then - Test all available status values
      for (Status status : Status.values()) {
        TestResponse response = new TestResponse(clientId, transactionId, status);
        assertEquals(status, response.getStatus());
      }
    }

    @Test
    @DisplayName("Should create response with additional data")
    void shouldCreateResponseWithAdditionalData() {
      // Given
      String clientId = "data-service";
      String transactionId = "data-test-456";
      Status status = Status.SUCCESS;
      String testData = "response-data";

      // When
      TestResponse response = new TestResponse(clientId, transactionId, status, testData);

      // Then
      assertEquals(clientId, response.getClientId());
      assertEquals(transactionId, response.getTransactionId());
      assertEquals(status, response.getStatus());
      assertEquals(testData, response.getData());
      assertNull(response.getError());
    }
  }

  @Nested
  @DisplayName("Validation Tests")
  class ValidationTests {

    @Test
    @DisplayName("Should pass validation with valid fields")
    void shouldPassValidationWithValidFields() {
      // Given
      TestResponse response = new TestResponse("valid-service", "valid-tx-123", Status.SUCCESS);

      // When
      Set<ConstraintViolation<TestResponse>> violations = validator.validate(response);

      // Then
      assertTrue(violations.isEmpty(), "Should have no validation violations");
    }

    @Test
    @DisplayName("Should fail validation when clientId is null")
    void shouldFailValidationWhenClientIdIsNull() {
      // Given
      TestResponse response = new TestResponse(null, "valid-transaction-id", Status.SUCCESS);

      // When
      Set<ConstraintViolation<TestResponse>> violations = validator.validate(response);

      // Then
      assertFalse(violations.isEmpty(), "Should have validation violations");
      assertTrue(
          violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("clientId")),
          "Should have clientId validation violation");
      assertTrue(
          violations.stream().anyMatch(v -> v.getMessage().equals("clientId is required")),
          "Should have correct error message");
    }

    @Test
    @DisplayName("Should fail validation when clientId is empty")
    void shouldFailValidationWhenClientIdIsEmpty() {
      // Given
      TestResponse response = new TestResponse("", "valid-transaction-id", Status.SUCCESS);

      // When
      Set<ConstraintViolation<TestResponse>> violations = validator.validate(response);

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
      TestResponse response = new TestResponse("   ", "valid-transaction-id", Status.SUCCESS);

      // When
      Set<ConstraintViolation<TestResponse>> violations = validator.validate(response);

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
      TestResponse response = new TestResponse("valid-client-id", null, Status.SUCCESS);

      // When
      Set<ConstraintViolation<TestResponse>> violations = validator.validate(response);

      // Then
      assertFalse(violations.isEmpty(), "Should have validation violations");
      assertTrue(
          violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("transactionId")),
          "Should have transactionId validation violation");
      assertTrue(
          violations.stream().anyMatch(v -> v.getMessage().equals("transactionId is required")),
          "Should have correct error message");
    }

    @Test
    @DisplayName("Should fail validation when transactionId is empty")
    void shouldFailValidationWhenTransactionIdIsEmpty() {
      // Given
      TestResponse response = new TestResponse("valid-client-id", "", Status.SUCCESS);

      // When
      Set<ConstraintViolation<TestResponse>> violations = validator.validate(response);

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
      TestResponse response = new TestResponse("valid-client-id", "\t\n ", Status.SUCCESS);

      // When
      Set<ConstraintViolation<TestResponse>> violations = validator.validate(response);

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
      TestResponse response = new TestResponse("valid-client-id", "valid-transaction-id", null);

      // When
      Set<ConstraintViolation<TestResponse>> violations = validator.validate(response);

      // Then
      assertFalse(violations.isEmpty(), "Should have validation violations");
      assertTrue(
          violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("status")),
          "Should have status validation violation");
      assertTrue(
          violations.stream().anyMatch(v -> v.getMessage().equals("status is required")),
          "Should have correct error message");
    }

    @Test
    @DisplayName("Should fail validation when all required fields are invalid")
    void shouldFailValidationWhenAllRequiredFieldsAreInvalid() {
      // Given
      TestResponse response = new TestResponse(null, "", null);

      // When
      Set<ConstraintViolation<TestResponse>> violations = validator.validate(response);

      // Then
      assertEquals(3, violations.size(), "Should have exactly 3 validation violations");

      boolean hasClientIdViolation =
          violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("clientId"));
      boolean hasTransactionIdViolation =
          violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("transactionId"));
      boolean hasStatusViolation =
          violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("status"));

      assertTrue(hasClientIdViolation, "Should have clientId validation violation");
      assertTrue(hasTransactionIdViolation, "Should have transactionId validation violation");
      assertTrue(hasStatusViolation, "Should have status validation violation");
    }
  }

  @Nested
  @DisplayName("Error Handling Tests")
  class ErrorHandlingTests {

    @Test
    @DisplayName("Should allow setting error after construction")
    void shouldAllowSettingErrorAfterConstruction() {
      // Given
      TestResponse response = new TestResponse("error-service", "error-test-123", Status.ERROR);
      TestErrorCode errorCode = new TestErrorCode("TEST001", "Test error code");
      Error error = new ApplicationError("Test error message", errorCode);

      // When
      response.setError(error);

      // Then
      assertNotNull(response.getError());
      assertEquals(error, response.getError());
    }

    @Test
    @DisplayName("Should allow null error")
    void shouldAllowNullError() {
      // Given
      TestResponse response =
          new TestResponse("null-error-service", "null-error-test", Status.SUCCESS);

      // When
      response.setError(null);

      // Then
      assertNull(response.getError());
    }

    @Test
    @DisplayName("Should allow changing error after setting")
    void shouldAllowChangingErrorAfterSetting() {
      // Given
      TestResponse response =
          new TestResponse("change-error-service", "change-error-test", Status.ERROR);
      TestErrorCode errorCode1 = new TestErrorCode("TEST002", "First error code");
      TestErrorCode errorCode2 = new TestErrorCode("TEST003", "Second error code");
      Error firstError = new ApplicationError("First error", errorCode1);
      Error secondError = new ApplicationError("Second error", errorCode2);

      // When
      response.setError(firstError);
      assertEquals(firstError, response.getError());

      response.setError(secondError);

      // Then
      assertEquals(secondError, response.getError());
      assertNotEquals(firstError, response.getError());
    }

    @Test
    @DisplayName("Should handle error with different status combinations")
    void shouldHandleErrorWithDifferentStatusCombinations() {
      // Test SUCCESS status with no error
      TestResponse successResponse = new TestResponse("status-test", "tx-1", Status.SUCCESS);
      assertNull(successResponse.getError());

      // Test ERROR status with error
      TestResponse errorResponse = new TestResponse("status-test", "tx-2", Status.ERROR);
      TestErrorCode errorCode = new TestErrorCode("TEST004", "Test error occurred");
      Error error = new ApplicationError("Error occurred", errorCode);
      errorResponse.setError(error);
      assertNotNull(errorResponse.getError());

      // Test other statuses
      TestResponse processingResponse = new TestResponse("status-test", "tx-3", Status.PROCESSING);
      assertNull(processingResponse.getError());
    }
  }

  @Nested
  @DisplayName("Immutability Tests")
  class ImmutabilityTests {

    @Test
    @DisplayName("Should have final core fields that cannot be modified")
    void shouldHaveFinalCoreFields() throws NoSuchFieldException {
      // Given/When
      var clientIdField = BaseResponse.class.getDeclaredField("clientId");
      var transactionIdField = BaseResponse.class.getDeclaredField("transactionId");
      var statusField = BaseResponse.class.getDeclaredField("status");

      // Then
      assertTrue(
          java.lang.reflect.Modifier.isFinal(clientIdField.getModifiers()),
          "clientId field should be final");
      assertTrue(
          java.lang.reflect.Modifier.isFinal(transactionIdField.getModifiers()),
          "transactionId field should be final");
      assertTrue(
          java.lang.reflect.Modifier.isFinal(statusField.getModifiers()),
          "status field should be final");
    }

    @Test
    @DisplayName("Should have non-final error field for mutability")
    void shouldHaveNonFinalErrorField() throws NoSuchFieldException {
      // Given/When
      var errorField = BaseResponse.class.getDeclaredField("error");

      // Then
      assertFalse(
          java.lang.reflect.Modifier.isFinal(errorField.getModifiers()),
          "error field should not be final to allow mutation");
    }

    @Test
    @DisplayName("Should maintain same core values after creation")
    void shouldMaintainSameCoreValuesAfterCreation() {
      // Given
      String originalClientId = "immutable-service";
      String originalTransactionId = "immutable-tx-123";
      Status originalStatus = Status.SUCCESS;
      TestResponse response =
          new TestResponse(originalClientId, originalTransactionId, originalStatus);

      // When (accessing values multiple times)
      String clientId1 = response.getClientId();
      String clientId2 = response.getClientId();
      String transactionId1 = response.getTransactionId();
      String transactionId2 = response.getTransactionId();
      Status status1 = response.getStatus();
      Status status2 = response.getStatus();

      // Then
      assertEquals(originalClientId, clientId1);
      assertEquals(originalClientId, clientId2);
      assertEquals(originalTransactionId, transactionId1);
      assertEquals(originalTransactionId, transactionId2);
      assertEquals(originalStatus, status1);
      assertEquals(originalStatus, status2);
      assertSame(clientId1, clientId2, "Should return same reference");
      assertSame(transactionId1, transactionId2, "Should return same reference");
      assertSame(status1, status2, "Should return same reference");
    }
  }

  @Nested
  @DisplayName("Logging Tests")
  class LoggingTests {

    @Test
    @DisplayName("Should execute logResponse without exceptions")
    void shouldExecuteLogResponseWithoutExceptions() {
      // Given
      TestResponse response = new TestResponse("log-service", "log-test-123", Status.SUCCESS);

      // When/Then - Should not throw any exceptions
      assertDoesNotThrow(
          () -> response.logResponse(), "logResponse should not throw any exceptions");
    }

    @Test
    @DisplayName("Should handle logging with error present")
    void shouldHandleLoggingWithErrorPresent() {
      // Given
      TestResponse response = new TestResponse("log-error-service", "log-error-test", Status.ERROR);
      TestErrorCode errorCode = new TestErrorCode("TEST005", "Log test error");
      Error error = new ApplicationError("Test error", errorCode);
      response.setError(error);

      // When/Then - Should not throw any exceptions
      assertDoesNotThrow(
          () -> response.logResponse(), "logResponse should handle error without issues");
    }

    @Test
    @DisplayName("Should handle logging with null error")
    void shouldHandleLoggingWithNullError() {
      // Given
      TestResponse response = new TestResponse("log-null-service", "log-null-test", Status.SUCCESS);
      response.setError(null);

      // When/Then - Should not throw any exceptions
      assertDoesNotThrow(
          () -> response.logResponse(), "logResponse should handle null error without issues");
    }

    @Test
    @DisplayName("Should handle logging with special characters")
    void shouldHandleLoggingWithSpecialCharacters() {
      // Given
      TestResponse response =
          new TestResponse("log-special@service", "log#special$test", Status.SUCCESS);

      // When/Then - Should not throw any exceptions
      assertDoesNotThrow(
          () -> response.logResponse(),
          "logResponse should handle special characters without issues");
    }
  }

  @Nested
  @DisplayName("Equals and HashCode Tests")
  class EqualsAndHashCodeTests {

    @Test
    @DisplayName("Should be equal when all core fields are the same")
    void shouldBeEqualWhenAllCoreFieldsAreTheSame() {
      // Given
      String clientId = "equals-service";
      String transactionId = "equals-test-123";
      Status status = Status.SUCCESS;
      TestResponse response1 = new TestResponse(clientId, transactionId, status);
      TestResponse response2 = new TestResponse(clientId, transactionId, status);

      // When/Then
      assertEquals(response1, response2, "Responses with same core fields should be equal");
      assertEquals(
          response1.hashCode(), response2.hashCode(), "Equal responses should have same hash code");
    }

    @Test
    @DisplayName("Should not be equal when clientId differs")
    void shouldNotBeEqualWhenClientIdDiffers() {
      // Given
      String transactionId = "diff-client-test";
      Status status = Status.SUCCESS;
      TestResponse response1 = new TestResponse("service-a", transactionId, status);
      TestResponse response2 = new TestResponse("service-b", transactionId, status);

      // When/Then
      assertNotEquals(
          response1, response2, "Responses with different clientId should not be equal");
    }

    @Test
    @DisplayName("Should not be equal when transactionId differs")
    void shouldNotBeEqualWhenTransactionIdDiffers() {
      // Given
      String clientId = "diff-tx-service";
      Status status = Status.SUCCESS;
      TestResponse response1 = new TestResponse(clientId, "tx-111", status);
      TestResponse response2 = new TestResponse(clientId, "tx-222", status);

      // When/Then
      assertNotEquals(
          response1, response2, "Responses with different transactionId should not be equal");
    }

    @Test
    @DisplayName("Should not be equal when status differs")
    void shouldNotBeEqualWhenStatusDiffers() {
      // Given
      String clientId = "diff-status-service";
      String transactionId = "diff-status-test";
      TestResponse response1 = new TestResponse(clientId, transactionId, Status.SUCCESS);
      TestResponse response2 = new TestResponse(clientId, transactionId, Status.ERROR);

      // When/Then
      assertNotEquals(response1, response2, "Responses with different status should not be equal");
    }

    @Test
    @DisplayName("Should not be equal when error field differs")
    void shouldNotBeEqualWhenErrorFieldDiffers() {
      // Given
      String clientId = "error-equals-service";
      String transactionId = "error-equals-test";
      Status status = Status.ERROR;
      TestResponse response1 = new TestResponse(clientId, transactionId, status);
      TestResponse response2 = new TestResponse(clientId, transactionId, status);

      TestErrorCode errorCode = new TestErrorCode("TEST006", "Equals test error");
      Error error = new ApplicationError("Test error", errorCode);
      response1.setError(error);
      // response2 has no error set

      // When/Then - Since @Data includes all fields in equals, different error fields mean not
      // equal
      assertNotEquals(
          response1, response2, "Responses with different error fields should not be equal");
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
      // Given
      TestResponse response = new TestResponse("null-test-service", "null-test", Status.SUCCESS);

      // When/Then
      assertNotEquals(response, null, "Response should not be equal to null");
    }

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
      // Given
      TestResponse response = new TestResponse("self-service", "self-test", Status.SUCCESS);

      // When/Then
      assertEquals(response, response, "Response should be equal to itself");
    }
  }

  @Nested
  @DisplayName("ToString Tests")
  class ToStringTests {

    @Test
    @DisplayName("Should contain core fields in toString")
    void shouldContainCoreFieldsInToString() {
      // Given
      String clientId = "toString-service";
      String transactionId = "toString-test-789";
      Status status = Status.SUCCESS;
      TestResponse response = new TestResponse(clientId, transactionId, status);

      // When
      String toString = response.toString();

      // Then
      assertNotNull(toString, "toString should not return null");
      assertTrue(toString.contains(clientId), "toString should contain clientId");
      assertTrue(toString.contains(transactionId), "toString should contain transactionId");
      assertTrue(toString.contains(status.toString()), "toString should contain status");
    }

    @Test
    @DisplayName("Should include error in toString when present")
    void shouldIncludeErrorInToStringWhenPresent() {
      // Given
      TestResponse response =
          new TestResponse("error-toString-service", "error-toString-test", Status.ERROR);
      TestErrorCode errorCode = new TestErrorCode("TEST007", "ToString test error");
      Error error = new ApplicationError("Test error", errorCode);
      response.setError(error);

      // When
      String toString = response.toString();

      // Then
      assertNotNull(toString, "toString should not return null");
      assertTrue(toString.contains("error"), "toString should mention error field");
    }

    @Test
    @DisplayName("Should handle special characters in toString")
    void shouldHandleSpecialCharactersInToString() {
      // Given
      TestResponse response =
          new TestResponse("service@special#chars", "tx$123%456", Status.SUCCESS);

      // When/Then
      assertDoesNotThrow(
          () -> response.toString(),
          "toString should handle special characters without exceptions");
    }
  }

  @Nested
  @DisplayName("Thread Safety Tests")
  class ThreadSafetyTests {

    @Test
    @DisplayName("Should be thread-safe for core field access")
    void shouldBeThreadSafeForCoreFieldAccess() throws InterruptedException {
      // Given
      TestResponse response = new TestResponse("concurrent-service", "thread-test", Status.SUCCESS);
      int numberOfThreads = 10;
      int operationsPerThread = 1000;
      Thread[] threads = new Thread[numberOfThreads];

      // When
      for (int i = 0; i < numberOfThreads; i++) {
        threads[i] =
            new Thread(
                () -> {
                  for (int j = 0; j < operationsPerThread; j++) {
                    // Perform read operations concurrently on immutable fields
                    String clientId = response.getClientId();
                    String transactionId = response.getTransactionId();
                    Status status = response.getStatus();
                    String toString = response.toString();
                    int hashCode = response.hashCode();
                    response.logResponse();

                    // Verify core values remain consistent
                    assertEquals("concurrent-service", clientId);
                    assertEquals("thread-test", transactionId);
                    assertEquals(Status.SUCCESS, status);
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
      assertTrue(true, "Concurrent access to core fields completed without issues");
    }

    @Test
    @DisplayName("Should handle concurrent error field modifications")
    void shouldHandleConcurrentErrorFieldModifications() throws InterruptedException {
      // Given
      TestResponse response =
          new TestResponse("error-concurrent-service", "error-thread-test", Status.ERROR);
      int numberOfThreads = 5;
      Thread[] threads = new Thread[numberOfThreads];

      // When - Multiple threads modify the error field
      for (int i = 0; i < numberOfThreads; i++) {
        final int threadIndex = i;
        threads[i] =
            new Thread(
                () -> {
                  TestErrorCode errorCode =
                      new TestErrorCode(
                          "TEST" + (100 + threadIndex), "Thread error " + threadIndex);
                  Error error = new ApplicationError("Error from thread " + threadIndex, errorCode);
                  response.setError(error);
                  // Verify we can read the error (might be from any thread)
                  assertNotNull(response.getError());
                });
        threads[i].start();
      }

      // Wait for all threads to complete
      for (Thread thread : threads) {
        thread.join();
      }

      // Then - Should have some error set (from one of the threads)
      assertNotNull(response.getError(), "Error should be set by one of the threads");
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
      Status status = Status.SUCCESS;

      // When
      TestResponse response = new TestResponse(longClientId, longTransactionId, status);

      // Then
      assertEquals(longClientId, response.getClientId());
      assertEquals(longTransactionId, response.getTransactionId());
      assertEquals(status, response.getStatus());
      assertDoesNotThrow(() -> response.logResponse());
      assertDoesNotThrow(() -> response.toString());
    }

    @Test
    @DisplayName("Should handle Unicode characters")
    void shouldHandleUnicodeCharacters() {
      // Given
      String unicodeClientId = "服务-αβγ-сервис";
      String unicodeTransactionId = "交易-δεζ-транзакция";
      Status status = Status.SUCCESS;

      // When
      TestResponse response = new TestResponse(unicodeClientId, unicodeTransactionId, status);

      // Then
      assertEquals(unicodeClientId, response.getClientId());
      assertEquals(unicodeTransactionId, response.getTransactionId());
      assertEquals(status, response.getStatus());
      assertDoesNotThrow(() -> response.logResponse());
      assertDoesNotThrow(() -> response.toString());
    }

    @Test
    @DisplayName("Should handle boundary field values")
    void shouldHandleBoundaryFieldValues() {
      // Given - Using single character values (boundary case for @NotBlank)
      String minClientId = "a";
      String minTransactionId = "1";
      Status status = Status.SUCCESS;

      // When
      TestResponse response = new TestResponse(minClientId, minTransactionId, status);

      // Then
      Set<ConstraintViolation<TestResponse>> violations = validator.validate(response);
      assertTrue(violations.isEmpty(), "Single character values should be valid");
      assertEquals(minClientId, response.getClientId());
      assertEquals(minTransactionId, response.getTransactionId());
      assertEquals(status, response.getStatus());
    }

    @Test
    @DisplayName("Should handle complex error objects")
    void shouldHandleComplexErrorObjects() {
      // Given
      TestResponse response =
          new TestResponse("complex-error-service", "complex-error-test", Status.ERROR);
      TestErrorCode errorCode = new TestErrorCode("COMPLEX001", "Complex error code");
      ApplicationError complexError =
          new ApplicationError(
              "Complex error with special chars: @#$%^&*(){}[]|\\:;\"'<>?,./", errorCode);
      complexError.setNativeErrorText("Detailed description with\nnewlines\tand\ttabs");

      // When
      response.setError(complexError);

      // Then
      assertEquals(complexError, response.getError());
      assertDoesNotThrow(() -> response.logResponse());
      assertDoesNotThrow(() -> response.toString());
    }
  }
}
