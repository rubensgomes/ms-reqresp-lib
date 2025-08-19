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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Error} interface.
 *
 * <p>This test class verifies the contract of the Error interface by testing a concrete
 * implementation that covers all the interface methods.
 *
 * @author Rubens Gomes
 */
@DisplayName("Error Interface Tests")
class ErrorTest {

  private Error error;

  /** Test implementation of the Error interface for testing purposes. */
  private static class TestErrorImpl implements Error {
    private final String errorMessage;
    private final String nativeErrorText;
    private final String errorCode;

    public TestErrorImpl(String errorMessage, String nativeErrorText, String errorCode) {
      this.errorMessage = errorMessage;
      this.nativeErrorText = nativeErrorText;
      this.errorCode = errorCode;
    }

    @Override
    public String getErrorMessage() {
      return errorMessage;
    }

    @Override
    public String getNativeErrorText() {
      return nativeErrorText;
    }

    @Override
    public String getErrorCode() {
      return errorCode;
    }
  }

  @BeforeEach
  void setUp() {
    error =
        new TestErrorImpl(
            "Database connection failed", "java.sql.SQLException: Connection refused", "DB_001");
  }

  @Test
  @DisplayName("getErrorMessage should return the error message")
  void testGetErrorMessage() {
    // When
    String result = error.getErrorMessage();

    // Then
    assertEquals("Database connection failed", result);
    assertNotNull(result);
  }

  @Test
  @DisplayName("getNativeErrorText should return the native error text")
  void testGetNativeErrorText() {
    // When
    String result = error.getNativeErrorText();

    // Then
    assertEquals("java.sql.SQLException: Connection refused", result);
    assertNotNull(result);
  }

  @Test
  @DisplayName("getErrorCode should return the error code")
  void testGetErrorCode() {
    // When
    String result = error.getErrorCode();

    // Then
    assertEquals("DB_001", result);
    assertNotNull(result);
  }

  @Test
  @DisplayName("Error implementation should handle null values gracefully")
  void testNullValues() {
    // Given
    Error nullError = new TestErrorImpl(null, null, null);

    // When & Then
    assertNull(nullError.getErrorMessage());
    assertNull(nullError.getNativeErrorText());
    assertNull(nullError.getErrorCode());
  }

  @Test
  @DisplayName("Error implementation should handle empty strings")
  void testEmptyStrings() {
    // Given
    Error emptyError = new TestErrorImpl("", "", "");

    // When & Then
    assertEquals("", emptyError.getErrorMessage());
    assertEquals("", emptyError.getNativeErrorText());
    assertEquals("", emptyError.getErrorCode());
  }

  @Test
  @DisplayName("Error implementation should handle whitespace-only strings")
  void testWhitespaceStrings() {
    // Given
    Error whitespaceError = new TestErrorImpl("   ", "\t", "\n");

    // When & Then
    assertEquals("   ", whitespaceError.getErrorMessage());
    assertEquals("\t", whitespaceError.getNativeErrorText());
    assertEquals("\n", whitespaceError.getErrorCode());
  }

  @Test
  @DisplayName("Error implementation should handle long strings")
  void testLongStrings() {
    // Given
    String longMessage = "A".repeat(1000);
    String longNativeText = "B".repeat(2000);
    String longCode = "C".repeat(100);
    Error longError = new TestErrorImpl(longMessage, longNativeText, longCode);

    // When & Then
    assertEquals(longMessage, longError.getErrorMessage());
    assertEquals(longNativeText, longError.getNativeErrorText());
    assertEquals(longCode, longError.getErrorCode());
    assertEquals(1000, longError.getErrorMessage().length());
    assertEquals(2000, longError.getNativeErrorText().length());
    assertEquals(100, longError.getErrorCode().length());
  }

  @Test
  @DisplayName("Error implementation should handle special characters")
  void testSpecialCharacters() {
    // Given
    String specialMessage = "Error with special chars: !@#$%^&*()";
    String specialNativeText = "Native text with unicode: ñáéíóú";
    String specialCode = "ERR-001_SPECIAL";
    Error specialError = new TestErrorImpl(specialMessage, specialNativeText, specialCode);

    // When & Then
    assertEquals(specialMessage, specialError.getErrorMessage());
    assertEquals(specialNativeText, specialError.getNativeErrorText());
    assertEquals(specialCode, specialError.getErrorCode());
  }

  @Test
  @DisplayName("Multiple Error instances should be independent")
  void testMultipleInstances() {
    // Given
    Error error1 = new TestErrorImpl("Error 1", "Native 1", "CODE1");
    Error error2 = new TestErrorImpl("Error 2", "Native 2", "CODE2");

    // When & Then
    assertEquals("Error 1", error1.getErrorMessage());
    assertEquals("Error 2", error2.getErrorMessage());
    assertEquals("Native 1", error1.getNativeErrorText());
    assertEquals("Native 2", error2.getNativeErrorText());
    assertEquals("CODE1", error1.getErrorCode());
    assertEquals("CODE2", error2.getErrorCode());

    // Verify they are truly independent
    assertNotEquals(error1.getErrorMessage(), error2.getErrorMessage());
    assertNotEquals(error1.getNativeErrorText(), error2.getNativeErrorText());
    assertNotEquals(error1.getErrorCode(), error2.getErrorCode());
  }
}
