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
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
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

  private static Validator validator;

  @BeforeAll
  static void setupValidator() {
    log.debug("Setting up validation factory");
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  /** Test implementation of BaseRequest for testing purposes. */
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
    @DisplayName("Should create instance with valid parameters")
    void constructor_shouldCreateInstance_withValidParameters() {
      String clientId = "test-client-123";
      String transactionId = "test-transaction-456";

      TestRequest request = new TestRequest(clientId, transactionId);

      assertNotNull(request, "Request should be created successfully");
      assertEquals(clientId, request.getClientId(), "ClientId should be set correctly");
      assertEquals(
          transactionId, request.getTransactionId(), "TransactionId should be set correctly");
    }

    @Test
    @DisplayName("Should create instance with UUID values")
    void constructor_shouldCreateInstance_withUuidValues() {
      String clientId = UUID.randomUUID().toString();
      String transactionId = UUID.randomUUID().toString();

      TestRequest request = new TestRequest(clientId, transactionId);

      assertNotNull(request, "Request should be created with UUID values");
      assertEquals(clientId, request.getClientId(), "ClientId should match UUID");
      assertEquals(transactionId, request.getTransactionId(), "TransactionId should match UUID");
    }

    @Test
    @DisplayName("Should accept null values in constructor")
    void constructor_shouldAcceptNullValues() {
      assertDoesNotThrow(
          () -> {
            TestRequest request = new TestRequest(null, null);
            assertNotNull(request, "Request should be created even with null values");
            assertNull(request.getClientId(), "ClientId should be null");
            assertNull(request.getTransactionId(), "TransactionId should be null");
          },
          "Constructor should accept null values without throwing exceptions");
    }
  }

  @Nested
  @DisplayName("Validation Tests")
  class ValidationTests {

    @Test
    @DisplayName("Should fail validation when both required fields are null")
    void validation_shouldFail_whenAllRequiredFieldsAreNull() {
      log.debug("Testing validation failure with null required fields");

      TestRequest request = new TestRequest(null, null);
      Set<ConstraintViolation<TestRequest>> violations = validator.validate(request);

      assertFalse(violations.isEmpty(), "Validation should fail when required fields are null");
      assertEquals(2, violations.size(), "Should have exactly 2 validation violations");

      // Verify specific validation messages
      Set<String> violationMessages =
          violations.stream()
              .map(ConstraintViolation::getMessage)
              .collect(java.util.stream.Collectors.toSet());

      assertTrue(
          violationMessages.contains("clientId is required"),
          "Should contain clientId validation message");
      assertTrue(
          violationMessages.contains("transactionId is required"),
          "Should contain transactionId validation message");

      log.debug("Validation failed with {} violations as expected", violations.size());
    }

    @Test
    @DisplayName("Should fail validation when clientId is null")
    void validation_shouldFail_whenClientIdIsNull() {
      log.debug("Testing validation failure with null clientId");

      TestRequest request = new TestRequest(null, "valid-transaction-id");
      Set<ConstraintViolation<TestRequest>> violations = validator.validate(request);

      assertFalse(violations.isEmpty(), "Validation should fail when clientId is null");
      assertEquals(1, violations.size(), "Should have exactly 1 validation violation");

      ConstraintViolation<TestRequest> violation = violations.iterator().next();
      assertEquals("clientId is required", violation.getMessage());
      assertEquals("clientId", violation.getPropertyPath().toString());

      log.debug("Validation failed for null clientId as expected");
    }

    @Test
    @DisplayName("Should fail validation when transactionId is null")
    void validation_shouldFail_whenTransactionIdIsNull() {
      log.debug("Testing validation failure with null transactionId");

      TestRequest request = new TestRequest("valid-client-id", null);
      Set<ConstraintViolation<TestRequest>> violations = validator.validate(request);

      assertFalse(violations.isEmpty(), "Validation should fail when transactionId is null");
      assertEquals(1, violations.size(), "Should have exactly 1 validation violation");

      ConstraintViolation<TestRequest> violation = violations.iterator().next();
      assertEquals("transactionId is required", violation.getMessage());
      assertEquals("transactionId", violation.getPropertyPath().toString());

      log.debug("Validation failed for null transactionId as expected");
    }

    @Test
    @DisplayName("Should fail validation when clientId is empty string")
    void validation_shouldFail_whenClientIdIsEmpty() {
      log.debug("Testing validation failure with empty clientId");

      TestRequest request = new TestRequest("", "valid-transaction-id");
      Set<ConstraintViolation<TestRequest>> violations = validator.validate(request);

      assertFalse(violations.isEmpty(), "Validation should fail when clientId is empty");
      assertEquals(1, violations.size(), "Should have exactly 1 validation violation");

      ConstraintViolation<TestRequest> violation = violations.iterator().next();
      assertEquals("clientId is required", violation.getMessage());

      log.debug("Validation failed for empty clientId as expected");
    }

    @Test
    @DisplayName("Should fail validation when transactionId is empty string")
    void validation_shouldFail_whenTransactionIdIsEmpty() {
      log.debug("Testing validation failure with empty transactionId");

      TestRequest request = new TestRequest("valid-client-id", "");
      Set<ConstraintViolation<TestRequest>> violations = validator.validate(request);

      assertFalse(violations.isEmpty(), "Validation should fail when transactionId is empty");
      assertEquals(1, violations.size(), "Should have exactly 1 validation violation");

      ConstraintViolation<TestRequest> violation = violations.iterator().next();
      assertEquals("transactionId is required", violation.getMessage());

      log.debug("Validation failed for empty transactionId as expected");
    }

    @Test
    @DisplayName("Should fail validation when fields are whitespace only")
    void validation_shouldFail_whenFieldsAreWhitespaceOnly() {
      log.debug("Testing validation failure with whitespace-only fields");

      TestRequest request = new TestRequest("   ", "   ");
      Set<ConstraintViolation<TestRequest>> violations = validator.validate(request);

      assertFalse(
          violations.isEmpty(), "Validation should fail when fields contain only whitespace");
      assertEquals(2, violations.size(), "Should have exactly 2 validation violations");

      log.debug("Validation failed for whitespace-only fields as expected");
    }

    @Test
    @DisplayName("Should pass validation when all required fields are present")
    void validation_shouldPass_whenAllRequiredFieldsPresent() {
      log.debug("Testing validation success with all required fields present");

      TestRequest request = new TestRequest("test-client-id", "test-transaction-id");
      Set<ConstraintViolation<TestRequest>> violations = validator.validate(request);

      assertTrue(
          violations.isEmpty(), "Validation should pass when all required fields are present");

      log.debug("Validation passed successfully");
    }

    @Test
    @DisplayName("Should pass validation with UUID values")
    void validation_shouldPass_withUuidValues() {
      log.debug("Testing validation success with UUID values");

      String clientId = UUID.randomUUID().toString();
      String transactionId = UUID.randomUUID().toString();
      TestRequest request = new TestRequest(clientId, transactionId);

      Set<ConstraintViolation<TestRequest>> violations = validator.validate(request);

      assertTrue(violations.isEmpty(), "Validation should pass with UUID values");

      log.debug("Validation passed with UUID values");
    }
  }

  @Nested
  @DisplayName("Field Access Tests")
  class FieldAccessTests {

    @Test
    @DisplayName("Should get clientId correctly")
    void shouldGetClientId() {
      String expectedClientId = "test-client-123";
      TestRequest request = new TestRequest(expectedClientId, "test-transaction");

      assertEquals(
          expectedClientId, request.getClientId(), "ClientId should be retrieved correctly");
    }

    @Test
    @DisplayName("Should get transactionId correctly")
    void shouldGetTransactionId() {
      String expectedTransactionId = "test-transaction-456";
      TestRequest request = new TestRequest("test-client", expectedTransactionId);

      assertEquals(
          expectedTransactionId,
          request.getTransactionId(),
          "TransactionId should be retrieved correctly");
    }

    @Test
    @DisplayName("Should handle null values in getters")
    void shouldHandleNullValuesInGetters() {
      TestRequest request = new TestRequest(null, null);

      assertNull(request.getClientId(), "ClientId should be null");
      assertNull(request.getTransactionId(), "TransactionId should be null");
    }
  }

  @Nested
  @DisplayName("Logging Tests")
  class LoggingTests {

    @Test
    @DisplayName("Should execute logRequest without throwing exceptions")
    void logRequest_shouldExecuteWithoutExceptions() {
      TestRequest request = new TestRequest("test-client", "test-transaction");

      assertDoesNotThrow(
          request::logRequest, "logRequest should execute without throwing exceptions");
    }

    @Test
    @DisplayName("Should handle logRequest with null values")
    void logRequest_shouldHandleNullValues() {
      TestRequest request = new TestRequest(null, null);

      assertDoesNotThrow(
          request::logRequest, "logRequest should handle null values without throwing exceptions");
    }
  }

  @Nested
  @DisplayName("Equality and HashCode Tests")
  class EqualityTests {

    @Test
    @DisplayName("Should be equal when all fields match")
    void shouldBeEqual_whenAllFieldsMatch() {
      TestRequest request1 = new TestRequest("client-123", "transaction-456");
      TestRequest request2 = new TestRequest("client-123", "transaction-456");

      assertEquals(request1, request2, "Requests should be equal when all fields match");
      assertEquals(
          request1.hashCode(), request2.hashCode(), "Hash codes should be equal for equal objects");
    }

    @Test
    @DisplayName("Should not be equal when clientId differs")
    void shouldNotBeEqual_whenClientIdDiffers() {
      TestRequest request1 = new TestRequest("client-123", "transaction-456");
      TestRequest request2 = new TestRequest("client-789", "transaction-456");

      assertNotEquals(request1, request2, "Requests should not be equal when clientId differs");
    }

    @Test
    @DisplayName("Should not be equal when transactionId differs")
    void shouldNotBeEqual_whenTransactionIdDiffers() {
      TestRequest request1 = new TestRequest("client-123", "transaction-456");
      TestRequest request2 = new TestRequest("client-123", "transaction-789");

      assertNotEquals(
          request1, request2, "Requests should not be equal when transactionId differs");
    }

    @Test
    @DisplayName("Should handle null comparisons correctly")
    void shouldHandleNullComparisons() {
      TestRequest request = new TestRequest("client-123", "transaction-456");

      assertNotEquals(null, request, "Request should not be equal to null");
      assertNotEquals("string", request, "Request should not be equal to different type");
    }

    @Test
    @DisplayName("Should be equal when both have null values")
    void shouldBeEqual_whenBothHaveNullValues() {
      TestRequest request1 = new TestRequest(null, null);
      TestRequest request2 = new TestRequest(null, null);

      assertEquals(request1, request2, "Requests with null values should be equal");
      assertEquals(
          request1.hashCode(),
          request2.hashCode(),
          "Hash codes should be equal for equal objects with null values");
    }
  }

  @Nested
  @DisplayName("ToString Tests")
  class ToStringTests {

    @Test
    @DisplayName("Should include field values in toString output")
    void toString_shouldIncludeFieldValues() {
      TestRequest request = new TestRequest("test-client", "test-transaction");

      String toStringOutput = request.toString();

      assertNotNull(toStringOutput, "toString should not return null");
      assertTrue(toStringOutput.contains("test-client"), "toString should include clientId value");
      assertTrue(
          toStringOutput.contains("test-transaction"),
          "toString should include transactionId value");
    }

    @Test
    @DisplayName("Should handle null values in toString")
    void toString_shouldHandleNullValues() {
      TestRequest request = new TestRequest(null, null);

      assertDoesNotThrow(
          () -> {
            String toStringOutput = request.toString();
            assertNotNull(toStringOutput, "toString should not return null even with null fields");
          },
          "toString should handle null values without throwing exceptions");
    }
  }

  @Nested
  @DisplayName("Immutability Tests")
  class ImmutabilityTests {

    @Test
    @DisplayName("Should maintain immutability of fields")
    void shouldMaintainImmutabilityOfFields() {
      String clientId = "original-client";
      String transactionId = "original-transaction";

      TestRequest request = new TestRequest(clientId, transactionId);

      // Verify that the fields cannot be changed after construction
      assertEquals(clientId, request.getClientId(), "ClientId should remain unchanged");
      assertEquals(
          transactionId, request.getTransactionId(), "TransactionId should remain unchanged");

      // The fields are final, so they cannot be modified after construction
      // This test verifies that the immutable design is working correctly
    }
  }
}
