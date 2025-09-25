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
 * @since 0.0.1
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
   * displayed in error messages or user interfaces without requiring technical knowledge.
   *
   * <p>The description serves as the primary communication mechanism between the system and users
   * when errors occur. Unlike the error code which is designed for programmatic handling, the
   * description is intended for human consumption and should provide meaningful context about what
   * went wrong and potentially how to resolve the issue.
   *
   * <p>Description guidelines and best practices:
   *
   * <ul>
   *   <li><strong>Clarity:</strong> Use clear, concise language appropriate for end users
   *   <li><strong>Actionability:</strong> Provide actionable information when possible (e.g.,
   *       "Please check your input" rather than just "Invalid input")
   *   <li><strong>Consistency:</strong> Maintain consistent tone, style, and terminology across all
   *       error descriptions
   *   <li><strong>Avoidance of Technical Jargon:</strong> Avoid implementation details, stack
   *       traces, or technical terminology that users wouldn't understand
   *   <li><strong>Appropriate Length:</strong> Keep descriptions concise but informative -
   *       typically one to two sentences
   *   <li><strong>Internationalization Ready:</strong> Write descriptions that can be easily
   *       translated to other languages
   *   <li><strong>Contextual Relevance:</strong> Ensure the description accurately reflects the
   *       specific error condition
   * </ul>
   *
   * <p>The description is commonly used in various scenarios:
   *
   * <ul>
   *   <li><strong>User Interface Messages:</strong> Displayed directly to users in web
   *       applications, mobile apps, or desktop software
   *   <li><strong>API Error Responses:</strong> Included in REST API error responses to help client
   *       developers understand issues
   *   <li><strong>Log Messages:</strong> Combined with error codes in log entries for human
   *       readability during troubleshooting
   *   <li><strong>Error Documentation:</strong> Used in API documentation and error catalogs
   *   <li><strong>Support and Debugging:</strong> Helps support teams quickly understand the nature
   *       of reported issues
   *   <li><strong>Monitoring Dashboards:</strong> Displayed in monitoring tools for quick error
   *       identification
   * </ul>
   *
   * <p>Example descriptions demonstrating good practices:
   *
   * <pre>{@code
   * // Good: Clear and actionable
   * "Required field is missing. Please provide a value."
   *
   * // Good: Specific but user-friendly
   * "The provided email address format is invalid."
   *
   * // Good: Helpful guidance
   * "Insufficient account balance. Please add funds and try again."
   *
   * // Poor: Too technical
   * "NullPointerException in validation layer"
   *
   * // Poor: Not actionable
   * "Error occurred"
   *
   * // Poor: Too verbose
   * "The system has encountered an unexpected condition that prevents..."
   * }</pre>
   *
   * <p>Integration with internationalization:
   *
   * <pre>{@code
   * // Use error codes as message keys for i18n
   * String errorCode = ApplicationErrorCode.VALIDATION_REQUIRED_FIELD.getCode();
   * String localizedMessage = messageSource.getMessage(
   *     errorCode,
   *     null,
   *     ApplicationErrorCode.VALIDATION_REQUIRED_FIELD.getDescription(), // fallback
   *     locale
   * );
   * }</pre>
   *
   * <p>The returned value must not be null, empty, or contain only whitespace characters as
   * enforced by the {@code @NotBlank} validation annotation. This ensures that every error code
   * provides meaningful human-readable information.
   *
   * @return the human-readable error description, never null or blank
   * @see #getCode() for the machine-readable error identifier
   * @see com.rubensgomes.msreqresplib.error.ApplicationErrorCode for examples of well-formed
   *     descriptions
   */
  @NotBlank
  String getDescription();
}
