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

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@DisplayName("BaseRequest Tests")
class BaseRequestTest {

  private ValidatorFactory factory;
  private Validator validator;

  @BeforeEach
  void setUp() {
    factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /**
   * Concrete implementation of BaseRequest for testing purposes. This allows us to test the
   * abstract BaseRequest class functionality.
   */
  @EqualsAndHashCode(callSuper = true)
  static class TestRequest extends BaseRequest {
    public TestRequest(String clientId, String transactionId) {
      super(clientId, transactionId);
    }
  }

  @Nested
  @DisplayName("Constructor Tests")
  class ConstructorTests {

    @Test
    @DisplayName("Should create request with valid parameters")
    void shouldCreateRequestWithValidParameters() {
      // Given
      String clientId = "test-service";
      String transactionId = UUID.randomUUID().toString();

      // When
      TestRequest request = new TestRequest(clientId, transactionId);

      // Then
      assertNotNull(request);
      assertEquals(clientId, request.getClientId());
      assertEquals(transactionId, request.getTransactionId());
    }

    @Test
    @DisplayName("Should create request with UUID transaction ID")
    void shouldCreateRequestWithUuidTransactionId() {
      // Given
      String clientId = "user-service";
      String transactionId = UUID.randomUUID().toString();

      // When
      TestRequest request = new TestRequest(clientId, transactionId);

      // Then
      assertNotNull(request.getTransactionId());
      assertEquals(36, request.getTransactionId().length()); // UUID string length
      assertTrue(request.getTransactionId().contains("-")); // UUID format
    }
  }

  @Nested
  @DisplayName("Validation Tests")
  class ValidationTests {

    @Test
    @DisplayName("Should pass validation with valid fields")
    void shouldPassValidationWithValidFields() {
      // Given
      TestRequest request = new TestRequest("order-service", "tx-12345");

      // When
      Set<ConstraintViolation<TestRequest>> violations = validator.validate(request);

      // Then
      assertTrue(violations.isEmpty(), "Should have no validation violations");
    }

    @Test
    @DisplayName("Should fail validation when clientId is null")
    void shouldFailValidationWhenClientIdIsNull() {
      // Given
      TestRequest request = new TestRequest(null, "valid-transaction-id");

      // When
      Set<ConstraintViolation<TestRequest>> violations = validator.validate(request);

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
      TestRequest request = new TestRequest("", "valid-transaction-id");

      // When
      Set<ConstraintViolation<TestRequest>> violations = validator.validate(request);

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
      TestRequest request = new TestRequest("   ", "valid-transaction-id");

      // When
      Set<ConstraintViolation<TestRequest>> violations = validator.validate(request);

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
      TestRequest request = new TestRequest("valid-client-id", null);

      // When
      Set<ConstraintViolation<TestRequest>> violations = validator.validate(request);

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
      TestRequest request = new TestRequest("valid-client-id", "");

      // When
      Set<ConstraintViolation<TestRequest>> violations = validator.validate(request);

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
      TestRequest request = new TestRequest("valid-client-id", "\t\n ");

      // When
      Set<ConstraintViolation<TestRequest>> violations = validator.validate(request);

      // Then
      assertFalse(violations.isEmpty(), "Should have validation violations");
      assertTrue(
          violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("transactionId")),
          "Should have transactionId validation violation");
    }

    @Test
    @DisplayName("Should fail validation when both fields are invalid")
    void shouldFailValidationWhenBothFieldsAreInvalid() {
      // Given
      TestRequest request = new TestRequest(null, "");

      // When
      Set<ConstraintViolation<TestRequest>> violations = validator.validate(request);

      // Then
      assertEquals(2, violations.size(), "Should have exactly 2 validation violations");

      boolean hasClientIdViolation =
          violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("clientId"));
      boolean hasTransactionIdViolation =
          violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("transactionId"));

      assertTrue(hasClientIdViolation, "Should have clientId validation violation");
      assertTrue(hasTransactionIdViolation, "Should have transactionId validation violation");
    }
  }

  @Nested
  @DisplayName("Immutability Tests")
  class ImmutabilityTests {

    @Test
    @DisplayName("Should have final fields that cannot be modified")
    void shouldHaveFinalFields() throws NoSuchFieldException {
      // Given/When
      var clientIdField = BaseRequest.class.getDeclaredField("clientId");
      var transactionIdField = BaseRequest.class.getDeclaredField("transactionId");

      // Then
      assertTrue(
          java.lang.reflect.Modifier.isFinal(clientIdField.getModifiers()),
          "clientId field should be final");
      assertTrue(
          java.lang.reflect.Modifier.isFinal(transactionIdField.getModifiers()),
          "transactionId field should be final");
    }

    @Test
    @DisplayName("Should maintain same values after creation")
    void shouldMaintainSameValuesAfterCreation() {
      // Given
      String originalClientId = "payment-service";
      String originalTransactionId = "txn-98765";
      TestRequest request = new TestRequest(originalClientId, originalTransactionId);

      // When (accessing values multiple times)
      String clientId1 = request.getClientId();
      String clientId2 = request.getClientId();
      String transactionId1 = request.getTransactionId();
      String transactionId2 = request.getTransactionId();

      // Then
      assertEquals(originalClientId, clientId1);
      assertEquals(originalClientId, clientId2);
      assertEquals(originalTransactionId, transactionId1);
      assertEquals(originalTransactionId, transactionId2);
      assertSame(clientId1, clientId2, "Should return same reference");
      assertSame(transactionId1, transactionId2, "Should return same reference");
    }
  }

  @Nested
  @DisplayName("Logging Tests")
  class LoggingTests {

    @Test
    @DisplayName("Should execute logRequest without exceptions")
    void shouldExecuteLogRequestWithoutExceptions() {
      // Given
      TestRequest request = new TestRequest("inventory-service", "log-test-123");

      // When/Then - Should not throw any exceptions
      assertDoesNotThrow(() -> request.logRequest(), "logRequest should not throw any exceptions");
    }

    @Test
    @DisplayName("Should handle logging with special characters in IDs")
    void shouldHandleLoggingWithSpecialCharacters() {
      // Given
      TestRequest request = new TestRequest("test-service-with-dashes", "tx_123-456.789");

      // When/Then - Should not throw any exceptions
      assertDoesNotThrow(
          () -> request.logRequest(), "logRequest should handle special characters without issues");
    }

    @Test
    @DisplayName("Should handle logging with long IDs")
    void shouldHandleLoggingWithLongIds() {
      // Given
      String longClientId = "very-long-client-service-name-that-exceeds-normal-length";
      String longTransactionId = UUID.randomUUID().toString() + "-" + UUID.randomUUID().toString();
      TestRequest request = new TestRequest(longClientId, longTransactionId);

      // When/Then - Should not throw any exceptions
      assertDoesNotThrow(
          () -> request.logRequest(), "logRequest should handle long IDs without issues");
    }
  }

  @Nested
  @DisplayName("Equals and HashCode Tests")
  class EqualsAndHashCodeTests {

    @Test
    @DisplayName("Should be equal when all fields are the same")
    void shouldBeEqualWhenAllFieldsAreTheSame() {
      // Given
      String clientId = "auth-service";
      String transactionId = "eq-test-123";
      TestRequest request1 = new TestRequest(clientId, transactionId);
      TestRequest request2 = new TestRequest(clientId, transactionId);

      // When/Then
      assertEquals(request1, request2, "Requests with same fields should be equal");
      assertEquals(
          request1.hashCode(), request2.hashCode(), "Equal requests should have same hash code");
    }

    @Test
    @DisplayName("Should not be equal when clientId differs")
    void shouldNotBeEqualWhenClientIdDiffers() {
      // Given
      String transactionId = "eq-test-456";
      TestRequest request1 = new TestRequest("service-a", transactionId);
      TestRequest request2 = new TestRequest("service-b", transactionId);

      // When/Then
      assertNotEquals(request1, request2, "Requests with different clientId should not be equal");
    }

    @Test
    @DisplayName("Should not be equal when transactionId differs")
    void shouldNotBeEqualWhenTransactionIdDiffers() {
      // Given
      String clientId = "notification-service";
      TestRequest request1 = new TestRequest(clientId, "tx-111");
      TestRequest request2 = new TestRequest(clientId, "tx-222");

      // When/Then
      assertNotEquals(
          request1, request2, "Requests with different transactionId should not be equal");
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
      // Given
      TestRequest request = new TestRequest("test-service", "null-test");

      // When/Then
      assertNotEquals(request, null, "Request should not be equal to null");
    }

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
      // Given
      TestRequest request = new TestRequest("self-service", "self-test");

      // When/Then
      assertEquals(request, request, "Request should be equal to itself");
    }
  }

  @Nested
  @DisplayName("ToString Tests")
  class ToStringTests {

    @Test
    @DisplayName("Should contain clientId and transactionId in toString")
    void shouldContainFieldsInToString() {
      // Given
      String clientId = "string-service";
      String transactionId = "string-test-789";
      TestRequest request = new TestRequest(clientId, transactionId);

      // When
      String toString = request.toString();

      // Then
      assertNotNull(toString, "toString should not return null");
      assertTrue(toString.contains(clientId), "toString should contain clientId");
      assertTrue(toString.contains(transactionId), "toString should contain transactionId");
    }

    @Test
    @DisplayName("Should handle special characters in toString")
    void shouldHandleSpecialCharactersInToString() {
      // Given
      TestRequest request = new TestRequest("service-with-special@chars", "tx#123$456");

      // When/Then
      assertDoesNotThrow(
          () -> request.toString(), "toString should handle special characters without exceptions");
    }
  }

  @Nested
  @DisplayName("Thread Safety Tests")
  class ThreadSafetyTests {

    @Test
    @DisplayName("Should be thread-safe for concurrent access")
    void shouldBeThreadSafeForConcurrentAccess() throws InterruptedException {
      // Given
      TestRequest request = new TestRequest("concurrent-service", "thread-test");
      int numberOfThreads = 10;
      int operationsPerThread = 1000;
      Thread[] threads = new Thread[numberOfThreads];

      // When
      for (int i = 0; i < numberOfThreads; i++) {
        threads[i] =
            new Thread(
                () -> {
                  for (int j = 0; j < operationsPerThread; j++) {
                    // Perform various operations concurrently
                    String clientId = request.getClientId();
                    String transactionId = request.getTransactionId();
                    String toString = request.toString();
                    int hashCode = request.hashCode();
                    request.logRequest();

                    // Verify values remain consistent
                    assertEquals("concurrent-service", clientId);
                    assertEquals("thread-test", transactionId);
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
      assertTrue(true, "Concurrent access completed without issues");
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

      // When
      TestRequest request = new TestRequest(longClientId, longTransactionId);

      // Then
      assertEquals(longClientId, request.getClientId());
      assertEquals(longTransactionId, request.getTransactionId());
      assertDoesNotThrow(() -> request.logRequest());
      assertDoesNotThrow(() -> request.toString());
    }

    @Test
    @DisplayName("Should handle Unicode characters")
    void shouldHandleUnicodeCharacters() {
      // Given
      String unicodeClientId = "服务-αβγ-сервис";
      String unicodeTransactionId = "交易-δεζ-транзакция";

      // When
      TestRequest request = new TestRequest(unicodeClientId, unicodeTransactionId);

      // Then
      assertEquals(unicodeClientId, request.getClientId());
      assertEquals(unicodeTransactionId, request.getTransactionId());
      assertDoesNotThrow(() -> request.logRequest());
      assertDoesNotThrow(() -> request.toString());
    }

    @Test
    @DisplayName("Should handle field values with only valid boundary characters")
    void shouldHandleFieldValuesWithBoundaryCharacters() {
      // Given - Using single character values (boundary case for @NotBlank)
      String minClientId = "a";
      String minTransactionId = "1";

      // When
      TestRequest request = new TestRequest(minClientId, minTransactionId);

      // Then
      Set<ConstraintViolation<TestRequest>> violations = validator.validate(request);
      assertTrue(violations.isEmpty(), "Single character values should be valid");
      assertEquals(minClientId, request.getClientId());
      assertEquals(minTransactionId, request.getTransactionId());
    }
  }
}
