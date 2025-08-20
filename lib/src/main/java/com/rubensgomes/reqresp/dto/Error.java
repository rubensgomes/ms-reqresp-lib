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

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Represents an error returned by the system, providing comprehensive error information including
 * description, native error details, and structured error codes.
 *
 * <p>This interface defines a standardized error structure with both required and optional fields
 * to support various error reporting scenarios. The error description is a mandatory text field,
 * the error code is a mandatory structured object, and the native error text is optional for
 * system-specific diagnostic information.
 *
 * <p>Implementation guidelines:
 *
 * <ul>
 *   <li><strong>Error Description</strong> - Must provide a clear, human-readable description of
 *       what went wrong. This is the primary error message for end users.
 *   <li><strong>Error Code</strong> - Must provide a structured {@link ErrorCode} object containing
 *       both a unique identifier and human-readable description for comprehensive error handling
 *       and categorization.
 *   <li><strong>Native Error Text</strong> - Optional field for including system-specific error
 *       details, stack traces, or low-level diagnostic information.
 * </ul>
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * public class DatabaseError implements Error {
 *     private final ErrorCode errorCode = new DatabaseErrorCodes.CONNECTION_FAILED;
 *
 *     public String getErrorDescription() {
 *         return "Database connection failed";
 *     }
 *
 *     public ErrorCode getErrorCode() {
 *         return errorCode;
 *     }
 *
 *     public String getNativeErrorText() {
 *         return "SQLException: Connection refused at localhost:5432";
 *     }
 * }
 * }</pre>
 *
 * @author Rubens Gomes
 * @see jakarta.validation.constraints.NotBlank
 * @see jakarta.validation.constraints.NotNull
 * @see jakarta.annotation.Nullable
 * @see ErrorCode
 */
public interface Error {
  /**
   * Returns a human-readable description of the error.
   *
   * <p>This method provides the primary error message that should be displayed to users or included
   * in error responses. The description should be clear, concise, and meaningful without requiring
   * technical knowledge.
   *
   * <p>The returned value must not be null, empty, or contain only whitespace characters as
   * enforced by the {@code @NotBlank} validation annotation.
   *
   * @return the error description, never null or blank
   */
  @NotBlank
  String getErrorDescription();

  /**
   * Returns the native error text, which may include system-specific details.
   *
   * <p>This method provides optional, low-level diagnostic information that may be useful for
   * debugging or logging purposes. It can include stack traces, system error messages, or other
   * technical details that are not suitable for end-user display.
   *
   * <p>This field is optional and may return null when no native error information is available or
   * relevant.
   *
   * @return the native error text, or null if not available
   */
  @Nullable
  String getNativeErrorText();

  /**
   * Sets the native error text for this error.
   *
   * <p>This method allows updating the optional, low-level diagnostic information that may be
   * useful for debugging or logging purposes. The native error text can include stack traces,
   * system error messages, or other technical details that are not suitable for end-user display.
   *
   * <p>This field is optional and may be set to null when no native error information is available
   * or relevant.
   *
   * @param nativeErrorText the native error text to set, or null if not available
   */
  void setNativeErrorText(@Nullable String nativeErrorText);

  /**
   * Returns the structured error code associated with this error.
   *
   * <p>This method provides a comprehensive {@link ErrorCode} object that contains both a unique
   * identifier and human-readable description for the error type. This structured approach enables
   * more sophisticated error handling, categorization, and user communication compared to simple
   * string-based error codes.
   *
   * <p>The {@link ErrorCode} object provides:
   *
   * <ul>
   *   <li><strong>Code Identifier</strong> - A unique string identifier for programmatic handling
   *   <li><strong>Description</strong> - A human-readable description for user display
   *   <li><strong>Consistency</strong> - Ensures both aspects are always available and properly
   *       paired
   * </ul>
   *
   * <p>The returned {@link ErrorCode} object must not be null as enforced by the {@code @NotNull}
   * validation annotation. However, the individual fields within the {@link ErrorCode} object are
   * subject to their own validation constraints.
   *
   * <p>Usage examples:
   *
   * <pre>{@code
   * Error error = new DatabaseError();
   * ErrorCode errorCode = error.getErrorCode();
   *
   * // Access the code identifier for programmatic handling
   * String codeId = errorCode.getCode();
   * if ("DB_CONNECTION_FAILED".equals(codeId)) {
   *     // Handle database connection error
   * }
   *
   * // Access the description for user display
   * String userMessage = errorCode.getDescription();
   * showErrorToUser(userMessage);
   * }</pre>
   *
   * @return the structured error code object, never null
   * @see ErrorCode#getCode()
   * @see ErrorCode#getDescription()
   */
  @NotNull
  ErrorCode getErrorCode();
}
