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

import jakarta.validation.constraints.NotBlank;

/**
 * Represents a standardized error code with both a unique identifier and human-readable
 * description.
 *
 * <p>This interface defines a contract for error codes that provides both machine-readable
 * identifiers and user-friendly descriptions. Both fields are mandatory and must contain
 * meaningful, non-blank values to ensure consistent error handling and reporting across the
 * application.
 *
 * <p>Error codes serve multiple purposes:
 *
 * <ul>
 *   <li><strong>Programmatic Handling</strong> - The code provides a unique identifier that can be
 *       used in switch statements, error mapping, and automated processing
 *   <li><strong>User Communication</strong> - The description provides clear, human-readable
 *       explanations that can be displayed to end users
 *   <li><strong>Internationalization</strong> - Codes can be used as keys for localized error
 *       messages while descriptions provide default fallback text
 *   <li><strong>Documentation</strong> - Descriptions serve as inline documentation for developers
 *       working with error handling code
 * </ul>
 *
 * <p>Implementation guidelines:
 *
 * <ul>
 *   <li>Codes should be unique within the application and follow consistent naming conventions
 *   <li>Descriptions should be clear, concise, and appropriate for end-user display
 *   <li>Both fields must be non-null, non-empty, and contain meaningful content
 *   <li>Consider using hierarchical naming for codes (e.g., "VALIDATION_REQUIRED_FIELD")
 * </ul>
 *
 * <p>Example implementation:
 *
 * <pre>{@code
 * public enum CommonErrorCodes implements ErrorCode {
 *     INVALID_INPUT("INVALID_INPUT", "The provided input is not valid"),
 *     RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", "The requested resource could not be found"),
 *     UNAUTHORIZED_ACCESS("UNAUTHORIZED_ACCESS", "Access denied - insufficient permissions");
 *
 *     private final String code;
 *     private final String description;
 *
 *     CommonErrorCodes(String code, String description) {
 *         this.code = code;
 *         this.description = description;
 *     }
 *
 *     public String getCode() { return code; }
 *     public String getDescription() { return description; }
 * }
 * }</pre>
 *
 * <p>Usage example:
 *
 * <pre>{@code
 * ErrorCode errorCode = CommonErrorCodes.INVALID_INPUT;
 *
 * // For programmatic handling
 * if ("INVALID_INPUT".equals(errorCode.getCode())) {
 *     // Handle input validation error
 * }
 *
 * // For user display
 * String userMessage = errorCode.getDescription();
 * }</pre>
 *
 * @author Rubens Gomes
 * @see jakarta.validation.constraints.NotBlank
 * @see Error
 */
public interface ErrorCode {
  /**
   * Returns the unique identifier for this error code.
   *
   * <p>The code serves as a machine-readable identifier that can be used for programmatic error
   * handling, logging, and categorization. It should be unique within the application's error code
   * namespace and follow consistent naming conventions.
   *
   * <p>Recommended code formats:
   *
   * <ul>
   *   <li>Uppercase with underscores: {@code VALIDATION_FAILED}, {@code DB_CONNECTION_ERROR}
   *   <li>Hierarchical naming: {@code AUTH_USER_NOT_FOUND}, {@code PAYMENT_CARD_EXPIRED}
   *   <li>Numeric codes: {@code ERR_001}, {@code WARN_042}
   *   <li>HTTP-style: {@code 404_NOT_FOUND}, {@code 500_INTERNAL_ERROR}
   * </ul>
   *
   * <p>The returned value must not be null, empty, or contain only whitespace characters as
   * enforced by the {@code @NotBlank} validation annotation.
   *
   * @return the error code identifier, never null or blank
   */
  @NotBlank
  String getCode();

  /**
   * Returns a human-readable description of this error code.
   *
   * <p>The description provides a clear, user-friendly explanation of what this error code
   * represents. It should be written in plain language that can be understood by end users and
   * displayed in error messages or user interfaces.
   *
   * <p>Description guidelines:
   *
   * <ul>
   *   <li>Use clear, concise language appropriate for end users
   *   <li>Avoid technical jargon or implementation details
   *   <li>Provide actionable information when possible
   *   <li>Keep descriptions consistent in tone and style
   *   <li>Consider internationalization requirements
   * </ul>
   *
   * <p>The returned value must not be null, empty, or contain only whitespace characters as
   * enforced by the {@code @NotBlank} validation annotation.
   *
   * @return the error description, never null or blank
   */
  @NotBlank
  String getDescription();
}
