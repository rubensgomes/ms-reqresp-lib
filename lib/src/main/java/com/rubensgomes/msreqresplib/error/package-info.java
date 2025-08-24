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

/**
 * Error handling and error code management for microservices applications.
 *
 * <p>This package provides a comprehensive framework for standardizing error handling across
 * microservices architectures. It defines structured error codes, error data models, and
 * standardized error reporting mechanisms that ensure consistent error communication between
 * services and to end users.
 *
 * <h2>Package Overview</h2>
 *
 * <p>The error handling framework is built on two core components that work together to provide a
 * complete error management solution:
 *
 * <ul>
 *   <li><strong>Error Code Contracts:</strong> Standardized interfaces for defining error
 *       identifiers with both machine-readable codes and human-readable descriptions
 *   <li><strong>Error Data Structures:</strong> Comprehensive error information containers that
 *       support both structured error reporting and diagnostic information
 * </ul>
 *
 * <h2>Core Components</h2>
 *
 * <h3>{@link com.rubensgomes.msreqresplib.error.ErrorCode}</h3>
 *
 * <p>Defines the fundamental contract for all error codes in the system. This interface ensures
 * that every error code provides:
 *
 * <ul>
 *   <li><strong>Unique Identifier:</strong> A machine-readable code for programmatic handling
 *   <li><strong>Human Description:</strong> Clear, user-friendly error message
 *   <li><strong>Validation Constraints:</strong> Both fields are mandatory and must be non-blank
 * </ul>
 *
 * <p>Key features:
 *
 * <ul>
 *   <li>Supports internationalization through code-based message lookup
 *   <li>Enables consistent error handling across all microservices
 *   <li>Provides inline documentation through descriptive text
 *   <li>Facilitates automated error processing and routing
 * </ul>
 *
 * <h3>{@link com.rubensgomes.msreqresplib.error.Error}</h3>
 *
 * <p>Defines the structure for comprehensive error information that can be returned by system
 * operations. This interface provides both required and optional fields to support various error
 * reporting scenarios:
 *
 * <ul>
 *   <li><strong>Required Fields:</strong> Error description and structured error code
 *   <li><strong>Optional Fields:</strong> Native error text for system-specific diagnostic
 *       information
 *   <li><strong>Validation Support:</strong> Built-in constraints ensure data integrity
 * </ul>
 *
 * <p>The Error interface uses modern Java patterns with method names that match record components:
 *
 * <ul>
 *   <li>{@code errorDescription()} - Returns the human-readable error description
 *   <li>{@code errorCode()} - Returns the structured ErrorCode object
 *   <li>{@code nativeErrorText()} - Returns optional native error information
 * </ul>
 *
 * <h2>Design Principles</h2>
 *
 * <h3>Consistency and Standardization</h3>
 *
 * <p>All error handling follows consistent patterns:
 *
 * <ul>
 *   <li><strong>Uniform Structure:</strong> All errors follow the same data model
 *   <li><strong>Predictable Interface:</strong> Error interfaces use consistent method naming
 *   <li><strong>Standard Responses:</strong> Error responses have consistent format across services
 *   <li><strong>Validation Integration:</strong> All error components are validated for integrity
 * </ul>
 *
 * <h3>Modern Java Design</h3>
 *
 * <p>The framework leverages modern Java features:
 *
 * <ul>
 *   <li><strong>Record Support:</strong> Error implementations can use Java records for
 *       immutability
 *   <li><strong>Interface Design:</strong> Clean interfaces that work well with records
 *   <li><strong>Annotation-Driven Validation:</strong> Uses Jakarta validation annotations
 *   <li><strong>Type Safety:</strong> Strong typing prevents runtime errors
 * </ul>
 *
 * <h3>Extensibility and Flexibility</h3>
 *
 * <p>The framework supports growth and customization:
 *
 * <ul>
 *   <li><strong>Custom Error Codes:</strong> Services can implement their own ErrorCode types
 *   <li><strong>Custom Error Implementations:</strong> Services can implement custom error data
 *       structures while maintaining interface compatibility
 *   <li><strong>Backward Compatibility:</strong> New error implementations don't break existing
 *       error handling
 *   <li><strong>Framework Integration:</strong> Works seamlessly with Spring Boot and other
 *       frameworks
 * </ul>
 *
 * <h3>Developer Experience</h3>
 *
 * <p>The framework prioritizes ease of use:
 *
 * <ul>
 *   <li><strong>Type Safety:</strong> Strong typing prevents runtime errors
 *   <li><strong>IDE Support:</strong> Interface-based design provides autocomplete and
 *       documentation
 *   <li><strong>Clear Documentation:</strong> Comprehensive Javadoc for all components
 *   <li><strong>Validation Feedback:</strong> Immediate feedback on invalid error data
 *   <li><strong>Record Compatibility:</strong> Modern Java records work seamlessly with interfaces
 * </ul>
 *
 * <h2>Usage Examples</h2>
 *
 * <h3>Implementing Custom Error Codes</h3>
 *
 * <pre>{@code
 * // Simple error code implementation
 * public enum ApplicationErrorCodes implements ErrorCode {
 *     VALIDATION_REQUIRED_FIELD("VALGN001", "Required field is missing"),
 *     VALIDATION_INVALID_FORMAT("VALGN002", "Invalid field format"),
 *     SECURITY_AUTHENTICATION_FAILED("SECGN001", "Authentication failed"),
 *     SYSTEM_DATABASE_ERROR("SYSGN001", "Database connection failed");
 *
 *     private final String code;
 *     private final String description;
 *
 *     ApplicationErrorCodes(String code, String description) {
 *         this.code = code;
 *         this.description = description;
 *     }
 *
 *     @Override
 *     public String getCode() { return code; }
 *
 *     @Override
 *     public String getDescription() { return description; }
 * }
 * }</pre>
 *
 * <h3>Using Error Records</h3>
 *
 * <pre>{@code
 * // Modern Java record implementation of Error interface
 * public record ApplicationError(
 *     @NotBlank String errorDescription,
 *     @NotNull ErrorCode errorCode,
 *     @Nullable String nativeErrorText
 * ) implements Error {
 *     // Record automatically implements interface methods through component names
 * }
 *
 * // Usage example
 * ErrorCode code = ApplicationErrorCodes.VALIDATION_REQUIRED_FIELD;
 * ApplicationError error = new ApplicationError(
 *     "Username is required",
 *     code,
 *     "Field validation failed: username cannot be null or empty"
 * );
 *
 * // Access error information
 * String description = error.errorDescription();  // "Username is required"
 * String codeId = error.errorCode().getCode();    // "VALGN001"
 * String nativeText = error.nativeErrorText();    // "Field validation..."
 * }</pre>
 *
 * <h3>Error Handling Patterns</h3>
 *
 * <pre>{@code
 * // Handle errors by code pattern
 * public ResponseEntity<?> handleError(Error error) {
 *     String errorCode = error.errorCode().getCode();
 *
 *     if (errorCode.startsWith("VALGN")) {
 *         // Handle validation errors - return 400 Bad Request
 *         return ResponseEntity.badRequest().body(createErrorResponse(error));
 *     } else if (errorCode.startsWith("SECGN")) {
 *         // Handle security errors - return 401/403
 *         return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
 *             .body(createErrorResponse(error));
 *     } else if (errorCode.startsWith("SYSGN")) {
 *         // Handle system errors - return 500 Internal Server Error
 *         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
 *             .body(createErrorResponse(error));
 *     }
 *     // Default handling
 *     return ResponseEntity.badRequest().body(createErrorResponse(error));
 * }
 * }</pre>
 *
 * <h3>Service-Specific Error Implementations</h3>
 *
 * <pre>{@code
 * // Define service-specific error codes
 * public enum UserServiceErrorCode implements ErrorCode {
 *     USER_ALREADY_EXISTS("USRMS001", "User with this username already exists"),
 *     USER_NOT_FOUND("USRMS002", "User not found"),
 *     USER_PROFILE_INCOMPLETE("USRMS003", "User profile is incomplete"),
 *     USER_ACCOUNT_SUSPENDED("USRMS004", "User account has been suspended");
 *
 *     private final String code;
 *     private final String description;
 *
 *     UserServiceErrorCode(String code, String description) {
 *         this.code = code;
 *         this.description = description;
 *     }
 *
 *     @Override
 *     public String getCode() { return code; }
 *
 *     @Override
 *     public String getDescription() { return description; }
 * }
 *
 * // Use with error records
 * UserServiceErrorCode code = UserServiceErrorCode.USER_NOT_FOUND;
 * ApplicationError error = new ApplicationError(
 *     "The requested user could not be found",
 *     code,
 *     "Database query returned no results for userId: 12345"
 * );
 * }</pre>
 *
 * <h2>Integration Guidelines</h2>
 *
 * <p>To effectively integrate this error handling framework:
 *
 * <ol>
 *   <li><strong>Implement ErrorCode Interface:</strong> Create enums or classes that implement the
 *       ErrorCode interface for your error codes
 *   <li><strong>Use Error Records:</strong> Leverage Java records that implement the Error
 *       interface for immutable error data structures
 *   <li><strong>Follow Naming Conventions:</strong> Use consistent prefixes for error codes (e.g.,
 *       VALGN for validation, SECGN for security)
 *   <li><strong>Implement Validation:</strong> Use Jakarta validation annotations to ensure error
 *       data integrity
 *   <li><strong>Add Error Monitoring:</strong> Implement metrics collection for error tracking
 *   <li><strong>Document Error Codes:</strong> Provide clear descriptions for all error codes
 *   <li><strong>Handle by Pattern:</strong> Implement error handling logic that processes errors by
 *       code patterns or categories
 * </ol>
 *
 * <h2>Best Practices</h2>
 *
 * <ul>
 *   <li><strong>Error Code Uniqueness:</strong> Ensure all error codes are unique across the
 *       application ecosystem
 *   <li><strong>Meaningful Descriptions:</strong> Write clear, actionable error descriptions that
 *       help users understand what went wrong
 *   <li><strong>Consistent Categorization:</strong> Use consistent prefixes for error code
 *       categories
 *   <li><strong>Immutable Design:</strong> Use records or immutable classes for error data
 *   <li><strong>Validation Integration:</strong> Always validate error data using annotations
 *   <li><strong>Backward Compatibility:</strong> Avoid changing existing error codes; add new ones
 *       instead
 *   <li><strong>Logging Integration:</strong> Log error codes along with detailed diagnostic
 *       information for troubleshooting
 *   <li><strong>Client Communication:</strong> Use error descriptions for user-facing messages and
 *       error codes for programmatic handling
 * </ul>
 *
 * <h2>Framework Integration</h2>
 *
 * <p>This error handling framework integrates seamlessly with:
 *
 * <ul>
 *   <li><strong>Spring Boot:</strong> Error handling in controllers and services
 *   <li><strong>Jakarta Validation:</strong> Automatic validation of error data
 *   <li><strong>Microservices:</strong> Consistent error communication between services
 *   <li><strong>Monitoring Tools:</strong> Error code-based metrics and alerting
 *   <li><strong>API Documentation:</strong> Clear error response documentation
 * </ul>
 *
 * @author Rubens Gomes
 * @version 0.0.7
 * @since 0.0.1
 * @see com.rubensgomes.msreqresplib.error.ErrorCode
 * @see com.rubensgomes.msreqresplib.error.Error
 * @see com.rubensgomes.msreqresplib.dto.ApplicationError
 * @see jakarta.validation.constraints.NotBlank
 * @see jakarta.validation.constraints.NotNull
 * @see jakarta.annotation.Nullable
 */
package com.rubensgomes.msreqresplib.error;
