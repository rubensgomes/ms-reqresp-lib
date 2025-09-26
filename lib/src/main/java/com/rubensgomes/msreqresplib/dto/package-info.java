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
 * Specialized Data Transfer Objects for microservices communication.
 *
 * <p>This package provides purpose-built DTOs that extend and complement the base request/response
 * framework with specialized functionality for specific communication scenarios. These DTOs are
 * designed to handle complex data structures that require more than the basic request/response
 * patterns provided by the core library.
 *
 * <h2>Package Overview</h2>
 *
 * <p>The DTO package focuses on specialized communication scenarios that require enhanced data
 * structures beyond the standard {@link com.rubensgomes.msreqresplib.BaseRequest} and {@link
 * com.rubensgomes.msreqresplib.BaseResponse} classes. Currently, this package specializes in
 * comprehensive error reporting and communication through enhanced response structures.
 *
 * <p>Key design principles for DTOs in this package:
 *
 * <ul>
 *   <li><strong>Specialized Purpose:</strong> Each DTO is designed for specific communication
 *       scenarios with enhanced functionality beyond base classes
 *   <li><strong>Enhanced Validation:</strong> Stricter validation constraints than base classes
 *       where appropriate to ensure data integrity
 *   <li><strong>Comprehensive Information:</strong> Rich data structures that provide complete
 *       context for their specific use cases
 *   <li><strong>Framework Integration:</strong> Seamless integration with validation frameworks,
 *       serialization libraries, and monitoring systems
 *   <li><strong>Type Safety:</strong> Strong typing and validation prevent runtime errors and
 *       provide excellent IDE support
 * </ul>
 *
 * <h2>Core Components</h2>
 *
 * <h3>{@link com.rubensgomes.msbaselib.error.ApplicationError}</h3>
 *
 * <p>A specialized response DTO designed specifically for error communication scenarios. This class
 * extends {@link com.rubensgomes.msreqresplib.BaseResponse} with enhanced requirements for error
 * reporting:
 *
 * <ul>
 *   <li><strong>Mandatory Error Information:</strong> Unlike the base response where error details
 *       are optional, this class requires comprehensive error information at construction time
 *   <li><strong>Enhanced Validation:</strong> Stricter validation constraints ensure that error
 *       responses always contain sufficient information for client-side handling
 *   <li><strong>Structured Error Data:</strong> Integration with the {@link
 *       com.rubensgomes.msbaselib.error} package for standardized error reporting
 *   <li><strong>Client Debugging Support:</strong> Rich error context to facilitate client-side
 *       debugging and error resolution
 *   <li><strong>Guaranteed Error Presence:</strong> Constructor enforces that error information is
 *       always provided, eliminating null error scenarios
 * </ul>
 *
 * <p>Key features:
 *
 * <ul>
 *   <li>Enforces presence of both error message and error details at construction time
 *   <li>Maintains correlation tracking from base response functionality
 *   <li>Provides standardized error communication across all microservices
 *   <li>Supports both programmatic error handling and user-facing error messages
 *   <li>Full compatibility with existing BaseResponse infrastructure
 * </ul>
 *
 * <h2>Design Patterns and Principles</h2>
 *
 * <h3>Enhanced Validation Strategy</h3>
 *
 * <p>DTOs in this package implement stricter validation than their base counterparts:
 *
 * <ul>
 *   <li><strong>Required Error Context:</strong> Error-specific DTOs mandate presence of error
 *       information where base classes make it optional
 *   <li><strong>Constructor Validation:</strong> Critical parameters are validated at construction
 *       time through Jakarta Bean Validation annotations
 *   <li><strong>Semantic Validation:</strong> Validation rules that ensure data makes sense in
 *       context (e.g., error responses must have error details)
 *   <li><strong>Framework Integration:</strong> Leverages Jakarta Bean Validation for consistent
 *       validation behavior across the application
 * </ul>
 *
 * <h3>Error Information Structure</h3>
 *
 * <p>The package works in conjunction with the {@link com.rubensgomes.msbaselib.error} package to
 * provide comprehensive error reporting:
 *
 * <ul>
 *   <li><strong>Error Interface Implementation:</strong> Uses {@link
 *       com.rubensgomes.msbaselib.error.ApplicationError} for structured error data
 *   <li><strong>ApplicationError Integration:</strong> Leverages {@link
 *       com.rubensgomes.msbaselib.error.ApplicationError} for comprehensive error details
 *   <li><strong>ErrorCode Support:</strong> Supports standardized error codes through {@link
 *       com.rubensgomes.msbaselib.error.ApplicationErrorCode} implementations
 *   <li><strong>Diagnostic Information:</strong> Includes support for native error text and
 *       diagnostic information for debugging purposes
 * </ul>
 *
 * <h3>Specialization Over Generalization</h3>
 *
 * <p>Rather than creating overly flexible generic structures, this package provides specialized
 * DTOs for specific scenarios:
 *
 * <ul>
 *   <li><strong>Purpose-Built Classes:</strong> Each DTO is optimized for its specific use case
 *       with appropriate validation and field requirements
 *   <li><strong>Clear Contracts:</strong> Validation and field requirements are explicit and
 *       enforced at compile time and runtime
 *   <li><strong>Type Safety:</strong> Strong typing prevents misuse and provides comprehensive IDE
 *       support with auto-completion
 *   <li><strong>Self-Documenting:</strong> Class names and structure clearly indicate intended use
 *       and constraints
 * </ul>
 *
 * <h2>Usage Examples</h2>
 *
 * <h3>Creating Comprehensive Error Responses</h3>
 *
 * <pre>{@code
 * // Create error code implementation
 * public class DatabaseErrorCode implements ErrorCode {
 *     private final String code;
 *     private final String description;
 *
 *     public DatabaseErrorCode(String code, String description) {
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
 * // Create error using ApplicationError from the error package
 * ErrorCode errorCode = new DatabaseErrorCode("DB001", "Database connection failed");
 * ApplicationError error = new ApplicationError(
 *     "Unable to connect to database",
 *     errorCode
 * );
 *
 * // Add diagnostic information if needed
 * error.setNativeErrorText("Connection timeout after 30 seconds to database server");
 *
 * // Create guaranteed error response
 * ApplicationErrorResponse response = new ApplicationErrorResponse(
 *     "order-service",
 *     "txn-12345",
 *     Status.ERROR,
 *     error
 * );
 *
 * // Access error information
 * String description = response.getError().getErrorDescription();  // "Unable to connect to database"
 * String code = response.getError().getErrorCode().getCode();      // "DB001"
 * String nativeText = response.getError().getNativeErrorText();    // "Connection timeout..."
 * }</pre>
 *
 * <h3>Validation Error Handling</h3>
 *
 * <pre>{@code
 * // Create validation error
 * ErrorCode validationCode = new ValidationErrorCode("VAL001", "Required field is missing");
 * ApplicationError error = new ApplicationError(
 *     "Username is required",
 *     validationCode
 * );
 * error.setNativeErrorText("Field validation failed: username cannot be null or empty");
 *
 * // Create error response with guaranteed error information
 * ApplicationErrorResponse response = new ApplicationErrorResponse(
 *     "user-service",
 *     "req-67890",
 *     Status.ERROR,
 *     error
 * );
 *
 * // Response automatically includes all error details
 * Error responseError = response.getError();
 * String clientMessage = responseError.getErrorDescription();  // For user display
 * String errorCode = responseError.getErrorCode().getCode();   // For programmatic handling
 * String diagnostic = responseError.getNativeErrorText();     // For debugging
 * }</pre>
 *
 * <h3>Controller Integration with Enhanced Error Handling</h3>
 *
 * <pre>{@code
 * @RestController
 * @RequestMapping("/api/orders")
 * public class OrderController {
 *
 *     @PostMapping
 *     public ResponseEntity<?> createOrder(@Valid @RequestBody OrderCreateRequest request) {
 *         try {
 *             // Process order logic
 *             OrderCreateResponse successResponse = orderService.createOrder(request);
 *             return ResponseEntity.ok(successResponse);
 *
 *         } catch (ValidationException e) {
 *             // Create detailed validation error
 *             ErrorCode validationCode = ApplicationErrorCodes.VALIDATION_FAILED;
 *             ApplicationError error = new ApplicationError(
 *                 "Order validation failed",
 *                 validationCode
 *             );
 *             error.setNativeErrorText(e.getMessage());
 *
 *             ApplicationErrorResponse errorResponse = new ApplicationErrorResponse(
 *                 request.getClientId(),
 *                 request.getTransactionId(),
 *                 Status.ERROR,
 *                 error
 *             );
 *
 *             return ResponseEntity.badRequest().body(errorResponse);
 *
 *         } catch (DatabaseException e) {
 *             // Create system error with diagnostic information
 *             ErrorCode dbCode = ApplicationErrorCodes.DATABASE_ERROR;
 *             ApplicationError error = new ApplicationError(
 *                 "Order processing temporarily unavailable",
 *                 dbCode
 *             );
 *
 *             // Add diagnostic information if available
 *             if (e.getDiagnosticInfo() != null) {
 *                 error.setNativeErrorText(e.getDiagnosticInfo());
 *             }
 *
 *             ApplicationErrorResponse errorResponse = new ApplicationErrorResponse(
 *                 request.getClientId(),
 *                 request.getTransactionId(),
 *                 Status.ERROR,
 *                 error
 *             );
 *
 *             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
 *         }
 *     }
 * }
 * }</pre>
 *
 * <h2>Integration Guidelines</h2>
 *
 * <h3>Error Response Strategy</h3>
 *
 * <p>When implementing error handling in microservices, follow these guidelines:
 *
 * <ul>
 *   <li><strong>Consistent Error Structure:</strong> Always use ApplicationErrorResponse for error
 *       scenarios to ensure consistent client experience across all services
 *   <li><strong>Appropriate Error Details:</strong> Include sufficient detail for client-side error
 *       handling without exposing sensitive system information
 *   <li><strong>Transaction Correlation:</strong> Preserve transaction IDs across service
 *       boundaries for end-to-end tracing and debugging
 *   <li><strong>Status Code Mapping:</strong> Map error types to appropriate HTTP status codes for
 *       REST APIs (400 for validation, 500 for system errors, etc.)
 *   <li><strong>Error Code Consistency:</strong> Use standardized error codes across services for
 *       programmatic error handling by clients
 * </ul>
 *
 * <h3>Validation Best Practices</h3>
 *
 * <p>Leverage the enhanced validation capabilities effectively:
 *
 * <ul>
 *   <li><strong>Early Validation:</strong> Validate inputs at service boundaries using Jakarta
 *       validation annotations on request DTOs
 *   <li><strong>Meaningful Error Messages:</strong> Provide clear, actionable error descriptions
 *       for client developers and end users
 *   <li><strong>Structured Error Codes:</strong> Use consistent error code schemes across services
 *       for programmatic handling and monitoring
 *   <li><strong>Diagnostic Information:</strong> Include native error text for debugging while
 *       keeping user-facing messages clean and actionable
 *   <li><strong>Constructor Validation:</strong> Rely on constructor validation to ensure error
 *       responses are always properly formed
 * </ul>
 *
 * <h3>Framework Integration</h3>
 *
 * <p>These DTOs integrate seamlessly with common frameworks and libraries:
 *
 * <ul>
 *   <li><strong>Spring Boot:</strong> Works with Spring's validation, serialization, and error
 *       handling mechanisms out of the box
 *   <li><strong>Jackson:</strong> Fully compatible with JSON serialization for REST APIs with
 *       proper field naming and structure
 *   <li><strong>Jakarta Validation:</strong> Integrates with validation frameworks for automatic
 *       constraint checking and error reporting
 *   <li><strong>Monitoring Systems:</strong> Structured error information facilitates integration
 *       with monitoring and alerting systems
 *   <li><strong>Testing Frameworks:</strong> Clear structure and validation make unit testing and
 *       integration testing straightforward
 * </ul>
 *
 * <h2>Thread Safety Considerations</h2>
 *
 * <p>DTOs in this package follow consistent thread safety patterns:
 *
 * <ul>
 *   <li><strong>Immutable Core Fields:</strong> Critical fields are final and set during
 *       construction
 *   <li><strong>Safe Read Operations:</strong> Read operations on immutable fields are thread-safe
 *   <li><strong>Controlled Mutability:</strong> Where mutability is allowed (e.g., native error
 *       text), it's clearly documented and isolated
 *   <li><strong>Defensive Design:</strong> DTOs are designed to be safely shared across threads for
 *       read operations
 * </ul>
 *
 * @author Rubens Gomes
 * @see com.rubensgomes.msreqresplib.BaseRequest
 * @see com.rubensgomes.msreqresplib.BaseResponse
 * @see com.rubensgomes.msbaselib.Status
 * @see com.rubensgomes.msbaselib.error.ApplicationError
 * @see com.rubensgomes.msbaselib.error.ApplicationErrorCode
 * @see jakarta.validation.constraints
 * @since 0.0.2
 */
package com.rubensgomes.msreqresplib.dto;
