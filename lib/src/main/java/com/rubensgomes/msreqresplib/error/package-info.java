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
 * <p>The error handling framework is built on three core components that work together to provide a
 * complete error management solution:
 *
 * <ul>
 *   <li><strong>Error Code Contracts:</strong> Standardized interfaces for defining error
 *       identifiers with both machine-readable codes and human-readable descriptions
 *   <li><strong>Hierarchical Error Taxonomy:</strong> Organized error categorization system with
 *       consistent naming conventions for easy classification and handling
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
 * <h3>{@link com.rubensgomes.msreqresplib.error.ApplicationErrorCode}</h3>
 *
 * <p>A comprehensive enumeration of standardized error codes organized in a hierarchical taxonomy.
 * This enum implements the {@link ErrorCode} interface and provides a complete set of predefined
 * error codes for common microservices scenarios.
 *
 * <p>Error Code Categories:
 *
 * <ul>
 *   <li><strong>BUSGN### - Business Logic Errors:</strong> Domain-specific constraint violations,
 *       invalid operation states, and business rule failures
 *   <li><strong>PAYGN### - Payment Processing Errors:</strong> Financial transaction failures,
 *       payment gateway issues, and billing-related problems
 *   <li><strong>RESGN### - Resource Management Errors:</strong> Resource access denials,
 *       availability issues, quota violations, and capacity limitations
 *   <li><strong>SECGN### - Security Errors:</strong> Authentication failures, authorization
 *       violations, token issues, and session management problems
 *   <li><strong>SYSGN### - System Errors:</strong> Infrastructure failures, database connectivity
 *       issues, external service unavailability, and internal server errors
 *   <li><strong>VALGN### - Validation Errors:</strong> Input validation failures, format
 *       violations, data integrity issues, and constraint violations
 *   <li><strong>XXXMS### - Service-Specific Errors:</strong> Microservice-specific error codes
 *       (e.g., USRMS for user management service)
 * </ul>
 *
 * <p>Benefits of the hierarchical naming convention:
 *
 * <ul>
 *   <li><strong>Easy Categorization:</strong> Errors can be grouped and handled by category prefix
 *   <li><strong>Scalable Design:</strong> New error codes can be added without naming conflicts
 *   <li><strong>Monitoring Integration:</strong> Error metrics can be aggregated by category
 *   <li><strong>Documentation Organization:</strong> Error handling guides can be structured by
 *       type
 *   <li><strong>Automated Processing:</strong> Error routing and handling can be category-based
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
 * <h2>Design Principles</h2>
 *
 * <h3>Consistency and Standardization</h3>
 *
 * <p>All error handling follows consistent patterns:
 *
 * <ul>
 *   <li><strong>Uniform Structure:</strong> All errors follow the same data model
 *   <li><strong>Predictable Naming:</strong> Error codes use consistent hierarchical naming
 *   <li><strong>Standard Responses:</strong> Error responses have consistent format across services
 *   <li><strong>Validation Integration:</strong> All error components are validated for integrity
 * </ul>
 *
 * <h3>Extensibility and Flexibility</h3>
 *
 * <p>The framework supports growth and customization:
 *
 * <ul>
 *   <li><strong>Service-Specific Codes:</strong> Services can define their own error codes using
 *       the XXXMS### pattern
 *   <li><strong>Category Extension:</strong> New error categories can be added as needed
 *   <li><strong>Custom Implementations:</strong> Services can implement custom error data
 *       structures while maintaining interface compatibility
 *   <li><strong>Backward Compatibility:</strong> New error codes don't break existing error
 *       handling
 * </ul>
 *
 * <h3>Developer Experience</h3>
 *
 * <p>The framework prioritizes ease of use:
 *
 * <ul>
 *   <li><strong>Type Safety:</strong> Strong typing prevents runtime errors
 *   <li><strong>IDE Support:</strong> Enum-based error codes provide autocomplete and documentation
 *   <li><strong>Clear Documentation:</strong> Every error code includes descriptive text
 *   <li><strong>Validation Feedback:</strong> Immediate feedback on invalid error data
 * </ul>
 *
 * <h2>Usage Examples</h2>
 *
 * <h3>Using Predefined Error Codes</h3>
 *
 * <pre>{@code
 * // Using a validation error code
 * ErrorCode validationError = ApplicationErrorCode.VALIDATION_REQUIRED_FIELD;
 * String code = validationError.getCode();         // "VALGN001"
 * String description = validationError.getDescription(); // "Required field is missing"
 *
 * // Using in error responses
 * ApplicationErrorResponse response = new ApplicationErrorResponse(
 *     clientId,
 *     transactionId,
 *     Status.ERROR,
 *     validationError.getCode(),
 *     validationError.getDescription(),
 *     "The 'username' field is required",
 *     Instant.now()
 * );
 * }</pre>
 *
 * <h3>Error Categorization and Handling</h3>
 *
 * <pre>{@code
 * // Handle errors by category
 * public ResponseEntity<?> handleError(String errorCode) {
 *     if (errorCode.startsWith("VALGN")) {
 *         // Handle validation errors - return 400 Bad Request
 *         return ResponseEntity.badRequest().body(createErrorResponse(errorCode));
 *     } else if (errorCode.startsWith("SECGN")) {
 *         // Handle security errors - return 401/403
 *         return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
 *             .body(createErrorResponse(errorCode));
 *     } else if (errorCode.startsWith("SYSGN")) {
 *         // Handle system errors - return 500 Internal Server Error
 *         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
 *             .body(createErrorResponse(errorCode));
 *     }
 *     // Default handling
 *     return ResponseEntity.badRequest().body(createErrorResponse(errorCode));
 * }
 * }</pre>
 *
 * <h3>Custom Service-Specific Error Codes</h3>
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
 * }</pre>
 *
 * <h3>Error Monitoring and Metrics</h3>
 *
 * <pre>{@code
 * // Count errors by category for monitoring
 * public void recordErrorMetric(String errorCode) {
 *     String category = errorCode.substring(0, 5); // Extract category prefix
 *
 *     switch (category) {
 *         case "VALGN":
 *             validationErrorCounter.increment();
 *             break;
 *         case "SECGN":
 *             securityErrorCounter.increment();
 *             break;
 *         case "SYSGN":
 *             systemErrorCounter.increment();
 *             break;
 *         // Add other categories as needed
 *     }
 *
 *     // Also record specific error code
 *     errorCodeCounter.increment(Tags.of("error_code", errorCode));
 * }
 * }</pre>
 *
 * <h2>Integration Guidelines</h2>
 *
 * <p>To effectively integrate this error handling framework:
 *
 * <ol>
 *   <li><strong>Use Standard Error Codes:</strong> Leverage predefined {@link ApplicationErrorCode}
 *       values whenever possible before creating custom ones
 *   <li><strong>Follow Naming Conventions:</strong> Use the hierarchical naming pattern for any
 *       service-specific error codes (XXXMS###)
 *   <li><strong>Implement Consistent Responses:</strong> Use standardized error response DTOs that
 *       include error codes and descriptions
 *   <li><strong>Add Error Monitoring:</strong> Implement metrics collection for error categories
 *       and specific error codes
 *   <li><strong>Document Service Errors:</strong> Document any service-specific error codes and
 *       their meanings
 *   <li><strong>Handle by Category:</strong> Implement error handling logic that can process errors
 *       by category prefix
 *   <li><strong>Validate Error Data:</strong> Ensure all error information passes validation
 *       constraints
 * </ol>
 *
 * <h2>Best Practices</h2>
 *
 * <ul>
 *   <li><strong>Error Code Uniqueness:</strong> Ensure all error codes are unique across the
 *       application ecosystem
 *   <li><strong>Meaningful Descriptions:</strong> Write clear, actionable error descriptions that
 *       help users understand what went wrong
 *   <li><strong>Consistent Categorization:</strong> Place new error codes in appropriate categories
 *       based on their nature
 *   <li><strong>Backward Compatibility:</strong> Avoid changing existing error codes; add new ones
 *       instead
 *   <li><strong>Logging Integration:</strong> Log error codes along with detailed diagnostic
 *       information for troubleshooting
 *   <li><strong>Client Communication:</strong> Use error descriptions for user-facing messages and
 *       error codes for programmatic handling
 * </ul>
 *
 * @author Rubens Gomes
 * @version 0.0.4
 * @since 0.0.1
 * @see com.rubensgomes.msreqresplib.error.ErrorCode
 * @see com.rubensgomes.msreqresplib.error.ApplicationErrorCode
 * @see com.rubensgomes.msreqresplib.error.Error
 * @see com.rubensgomes.msreqresplib.dto.ApplicationErrorResponse
 */
package com.rubensgomes.msreqresplib.error;
