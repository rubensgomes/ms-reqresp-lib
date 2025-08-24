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
 * comprehensive error reporting and communication.
 *
 * <p>Key design principles for DTOs in this package:
 *
 * <ul>
 *   <li><strong>Specialized Purpose:</strong> Each DTO is designed for specific communication
 *       scenarios with enhanced functionality
 *   <li><strong>Enhanced Validation:</strong> Stricter validation constraints than base classes
 *       where appropriate
 *   <li><strong>Comprehensive Information:</strong> Rich data structures that provide complete
 *       context for their specific use cases
 *   <li><strong>Modern Java Support:</strong> Leverages modern Java features like records for
 *       immutable, efficient data structures
 *   <li><strong>Framework Integration:</strong> Seamless integration with validation frameworks,
 *       serialization libraries, and monitoring systems
 * </ul>
 *
 * <h2>Core Components</h2>
 *
 * <h3>{@link com.rubensgomes.msreqresplib.dto.ApplicationErrorResponse}</h3>
 *
 * <p>A specialized response DTO designed specifically for error communication scenarios. This class
 * extends {@link com.rubensgomes.msreqresplib.BaseResponse} with enhanced requirements for error
 * reporting:
 *
 * <ul>
 *   <li><strong>Mandatory Error Information:</strong> Unlike the base response where error details
 *       are optional, this class requires both descriptive messages and structured error details
 *   <li><strong>Enhanced Validation:</strong> Stricter validation constraints ensure that error
 *       responses always contain sufficient information for client-side handling
 *   <li><strong>Structured Error Data:</strong> Integration with the {@link
 *       com.rubensgomes.msreqresplib.error} package for standardized error reporting
 *   <li><strong>Client Debugging Support:</strong> Rich error context to facilitate client-side
 *       debugging and error resolution
 * </ul>
 *
 * <p>Key features:
 *
 * <ul>
 *   <li>Enforces presence of both error message and error details
 *   <li>Maintains correlation tracking from base response functionality
 *   <li>Provides standardized error communication across all microservices
 *   <li>Supports both programmatic error handling and user-facing error messages
 * </ul>
 *
 * <h3>{@link com.rubensgomes.msreqresplib.dto.ApplicationError}</h3>
 *
 * <p>A modern Java record implementation of the {@link com.rubensgomes.msreqresplib.error.Error}
 * interface that provides a concrete data structure for representing application errors:
 *
 * <ul>
 *   <li><strong>Comprehensive Error Details:</strong> Encapsulates human-readable descriptions,
 *       standardized error codes, and optional native error text
 *   <li><strong>Record-Based Design:</strong> Leverages Java records for immutability, automatic
 *       equals/hashCode/toString, and clean syntax
 *   <li><strong>Validation Integration:</strong> Built-in validation constraints ensure data
 *       integrity with Jakarta validation annotations
 *   <li><strong>Flexible Error Context:</strong> Optional native error text supports detailed
 *       diagnostic information from underlying systems
 * </ul>
 *
 * <p>Design benefits:
 *
 * <ul>
 *   <li>Immutable record design for thread safety and performance
 *   <li>Automatic interface implementation through record component names
 *   <li>Integration with standardized error code framework
 *   <li>Support for multi-layered error reporting (application + native)
 *   <li>Compact syntax with comprehensive functionality
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
 *   <li><strong>Semantic Validation:</strong> Validation rules that ensure data makes sense in
 *       context (e.g., error responses must have error details)
 *   <li><strong>Cross-Field Validation:</strong> Validation that considers relationships between
 *       multiple fields
 *   <li><strong>Framework Integration:</strong> Leverages Jakarta Bean Validation for consistent
 *       validation behavior
 * </ul>
 *
 * <h3>Modern Java Design</h3>
 *
 * <p>The package leverages modern Java language features:
 *
 * <ul>
 *   <li><strong>Record-Based DTOs:</strong> Uses Java records for concise, immutable data
 *       structures
 *   <li><strong>Interface Integration:</strong> Records automatically implement interface methods
 *       through component name matching
 *   <li><strong>Annotation-Driven Validation:</strong> Uses Jakarta validation annotations on
 *       record components
 *   <li><strong>Type Safety:</strong> Strong typing and immutability prevent runtime errors
 * </ul>
 *
 * <h3>Specialization Over Generalization</h3>
 *
 * <p>Rather than creating overly flexible generic structures, this package provides specialized
 * DTOs for specific scenarios:
 *
 * <ul>
 *   <li><strong>Purpose-Built Classes:</strong> Each DTO is optimized for its specific use case
 *   <li><strong>Clear Contracts:</strong> Validation and field requirements are explicit and
 *       enforced
 *   <li><strong>Type Safety:</strong> Strong typing prevents misuse and provides IDE support
 *   <li><strong>Self-Documenting:</strong> Class names and structure clearly indicate intended use
 * </ul>
 *
 * <h2>Usage Examples</h2>
 *
 * <h3>Creating Application Errors with Records</h3>
 *
 * <pre>{@code
 * // Modern Java record usage - creating detailed error
 * public record CustomErrorCode(String code, String description) implements ErrorCode {
 *     @Override
 *     public String getCode() { return code; }
 *
 *     @Override
 *     public String getDescription() { return description; }
 * }
 *
 * // Create error with record syntax
 * var errorCode = new CustomErrorCode("SYSGN001", "Database connection failed");
 * var error = new ApplicationError(
 *     "Unable to connect to database",
 *     errorCode,
 *     "Connection timeout after 30 seconds to database server"
 * );
 *
 * // Access error information using record methods
 * String description = error.errorDescription();  // "Unable to connect to database"
 * String code = error.errorCode().getCode();      // "SYSGN001"
 * String nativeText = error.nativeErrorText();    // "Connection timeout..."
 * }</pre>
 *
 * <h3>Creating Comprehensive Error Responses</h3>
 *
 * <pre>{@code
 * // Create error response using ApplicationError record
 * var errorCode = new CustomErrorCode("VALGN001", "Required field is missing");
 * var error = new ApplicationError(
 *     "Username is required",
 *     errorCode,
 *     "Field validation failed: username cannot be null or empty"
 * );
 *
 * var response = new ApplicationErrorResponse(
 *     "order-service",
 *     "txn-12345",
 *     Status.ERROR,
 *     "Unable to process order due to validation errors",
 *     error
 * );
 * }</pre>
 *
 * <h3>Controller Integration with Enhanced Error Handling</h3>
 *
 * <pre>{@code
 * @PostMapping("/orders")
 * public ResponseEntity<?> createOrder(@Valid @RequestBody OrderCreateRequest request) {
 *     try {
 *         // Process order logic
 *         return ResponseEntity.ok(processOrder(request));
 *
 *     } catch (ValidationException e) {
 *         var errorCode = new CustomErrorCode("VALGN001", "Validation failed");
 *         var error = new ApplicationError(
 *             e.getMessage(),
 *             errorCode,
 *             e.getFieldErrors().toString()
 *         );
 *
 *         var errorResponse = new ApplicationErrorResponse(
 *             request.getClientId(),
 *             request.getTransactionId(),
 *             Status.ERROR,
 *             "Order validation failed",
 *             error
 *         );
 *
 *         return ResponseEntity.badRequest().body(errorResponse);
 *
 *     } catch (DatabaseException e) {
 *         var errorCode = new CustomErrorCode("SYSGN001", "Database operation failed");
 *         var error = new ApplicationError(
 *             "Database operation failed",
 *             errorCode,
 *             e.getCause().getMessage()
 *         );
 *
 *         var errorResponse = new ApplicationErrorResponse(
 *             request.getClientId(),
 *             request.getTransactionId(),
 *             Status.ERROR,
 *             "Unable to save order",
 *             error
 *         );
 *
 *         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
 *             .body(errorResponse);
 *     }
 * }
 * }</pre>
 *
 * <h3>Error Response Serialization</h3>
 *
 * <pre>{@code
 * // JSON serialization example
 * {
 *   "clientId": "order-service",
 *   "transactionId": "txn-12345",
 *   "status": "ERROR",
 *   "timestamp": "2025-08-24T10:30:00Z",
 *   "message": "Unable to process order due to validation errors",
 *   "error": {
 *     "errorDescription": "Username is required",
 *     "errorCode": {
 *       "code": "VALGN001",
 *       "description": "Required field is missing"
 *     },
 *     "nativeErrorText": "Field validation failed: username cannot be null or empty"
 *   }
 * }
 * }</pre>
 *
 * <h2>Integration Guidelines</h2>
 *
 * <p>To effectively integrate these specialized DTOs into your microservices:
 *
 * <ol>
 *   <li><strong>Use Appropriate Specializations:</strong> Choose {@link
 *       com.rubensgomes.msreqresplib.dto.ApplicationErrorResponse} for error scenarios where
 *       comprehensive error information is required
 *   <li><strong>Implement Comprehensive Error Handling:</strong> Leverage {@link
 *       com.rubensgomes.msreqresplib.dto.ApplicationError} to provide both application-level and
 *       native system error details
 *   <li><strong>Leverage Record Benefits:</strong> Take advantage of record immutability, automatic
 *       methods, and concise syntax
 *   <li><strong>Maintain Validation Standards:</strong> Ensure all required fields are populated
 *       and validation constraints are met
 *   <li><strong>Consistent Error Communication:</strong> Standardize error response formats across
 *       all microservices using these DTOs
 *   <li><strong>Monitor Error Patterns:</strong> Use the structured error data for monitoring and
 *       alerting on error patterns and trends
 * </ol>
 *
 * <h2>Extension Guidelines</h2>
 *
 * <p>When adding new specialized DTOs to this package:
 *
 * <ul>
 *   <li><strong>Follow Naming Conventions:</strong> Use descriptive names that clearly indicate the
 *       DTO's purpose and specialization
 *   <li><strong>Consider Record Design:</strong> Use Java records for immutable DTOs when
 *       appropriate
 *   <li><strong>Extend Base Classes:</strong> Build upon {@link
 *       com.rubensgomes.msreqresplib.BaseRequest} or {@link
 *       com.rubensgomes.msreqresplib.BaseResponse} when appropriate
 *   <li><strong>Implement Enhanced Validation:</strong> Add validation constraints that make sense
 *       for the specific use case
 *   <li><strong>Document Purpose:</strong> Clearly document what scenarios the DTO is designed for
 *       and how it differs from base classes
 *   <li><strong>Maintain Consistency:</strong> Follow the same patterns established by existing
 *       DTOs in this package
 * </ul>
 *
 * <h2>Performance Considerations</h2>
 *
 * <ul>
 *   <li><strong>Record Efficiency:</strong> Java records provide optimal memory layout and
 *       performance for immutable data
 *   <li><strong>Validation Caching:</strong> Validation results can be cached due to immutable
 *       nature
 *   <li><strong>Serialization Optimization:</strong> DTOs are optimized for JSON
 *       serialization/deserialization performance
 *   <li><strong>Memory Efficiency:</strong> Structured error data prevents string concatenation and
 *       improves memory usage patterns
 *   <li><strong>Thread Safety:</strong> Immutable design eliminates synchronization overhead
 * </ul>
 *
 * @author Rubens Gomes
 * @version 0.0.7
 * @since 0.0.2
 * @see com.rubensgomes.msreqresplib.BaseRequest
 * @see com.rubensgomes.msreqresplib.BaseResponse
 * @see com.rubensgomes.msreqresplib.error
 * @see com.rubensgomes.msreqresplib.dto.ApplicationErrorResponse
 * @see com.rubensgomes.msreqresplib.dto.ApplicationError
 * @see jakarta.validation.constraints.NotBlank
 * @see jakarta.validation.constraints.NotNull
 * @see jakarta.annotation.Nullable
 */
package com.rubensgomes.msreqresplib.dto;
