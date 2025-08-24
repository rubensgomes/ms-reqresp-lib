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
 *   <li><strong>Cross-Language Support:</strong> Support for both Java and Kotlin implementations
 *       to accommodate diverse microservices architectures
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
 * <p>A Kotlin-based implementation of the {@link com.rubensgomes.msreqresplib.error.Error}
 * interface that provides a concrete data structure for representing application errors:
 *
 * <ul>
 *   <li><strong>Comprehensive Error Details:</strong> Encapsulates human-readable descriptions,
 *       standardized error codes, and optional native error text
 *   <li><strong>Cross-Language Compatibility:</strong> Kotlin implementation that integrates
 *       seamlessly with Java-based microservices
 *   <li><strong>Validation Integration:</strong> Built-in validation constraints ensure data
 *       integrity
 *   <li><strong>Flexible Error Context:</strong> Optional native error text supports detailed
 *       diagnostic information from underlying systems
 * </ul>
 *
 * <p>Design benefits:
 *
 * <ul>
 *   <li>Immutable data class design for thread safety
 *   <li>Null-safe handling of optional diagnostic information
 *   <li>Integration with standardized error code framework
 *   <li>Support for multi-layered error reporting (application + native)
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
 * <h3>Multi-Language Support</h3>
 *
 * <p>The package demonstrates support for polyglot microservices architectures:
 *
 * <ul>
 *   <li><strong>Java Integration:</strong> Full compatibility with Java-based microservices
 *   <li><strong>Kotlin Support:</strong> Native Kotlin implementations for services built in Kotlin
 *   <li><strong>Interoperability:</strong> Seamless interaction between Java and Kotlin components
 *   <li><strong>Common Interfaces:</strong> Shared contracts ensure consistent behavior across
 *       languages
 * </ul>
 *
 * <h2>Usage Examples</h2>
 *
 * <h3>Creating Comprehensive Error Responses</h3>
 *
 * <pre>{@code
 * // Java usage - creating detailed error response
 * ApplicationError error = new ApplicationError(
 *     "Database connection failed",
 *     ApplicationErrorCode.SYSTEM_DATABASE_CONNECTION,
 *     "Connection timeout after 30 seconds to database server"
 * );
 *
 * ApplicationErrorResponse response = new ApplicationErrorResponse(
 *     "order-service",
 *     "txn-12345",
 *     Status.ERROR,
 *     "Unable to process order due to database connectivity issues",
 *     error
 * );
 * }</pre>
 *
 * <h3>Kotlin Error Handling</h3>
 *
 * <pre>{@code
 * // Kotlin usage - leveraging data class features
 * val error = ApplicationError(
 *     errorDescription = "Invalid payment method",
 *     errorCode = ApplicationErrorCode.PAYMENT_METHOD_INVALID,
 *     nativeErrorText = "Card number failed Luhn algorithm validation"
 * )
 *
 * // Error can be easily copied with modifications
 * val enhancedError = error.copy(
 *     nativeErrorText = "${error.nativeErrorText} - Additional context from payment gateway"
 * )
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
 *         ApplicationError error = new ApplicationError(
 *             e.getMessage(),
 *             ApplicationErrorCode.VALIDATION_REQUIRED_FIELD,
 *             e.getFieldErrors().toString()
 *         );
 *
 *         ApplicationErrorResponse errorResponse = new ApplicationErrorResponse(
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
 *         ApplicationError error = new ApplicationError(
 *             "Database operation failed",
 *             ApplicationErrorCode.SYSTEM_DATABASE_CONNECTION,
 *             e.getNativeSqlException().getMessage()
 *         );
 *
 *         ApplicationErrorResponse errorResponse = new ApplicationErrorResponse(
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
 *   "timestamp": "2025-01-15T10:30:00Z",
 *   "message": "Unable to process order due to database connectivity issues",
 *   "error": {
 *     "errorDescription": "Database connection failed",
 *     "errorCode": {
 *       "code": "SYSGN003",
 *       "description": "Database connection failure"
 *     },
 *     "nativeErrorText": "Connection timeout after 30 seconds to database server"
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
 *   <li><strong>Maintain Validation Standards:</strong> Ensure all required fields are populated
 *       and validation constraints are met
 *   <li><strong>Support Multi-Language Teams:</strong> Use appropriate language implementations
 *       (Java vs Kotlin) based on your team's preferences and existing codebase
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
 *   <li><strong>Extend Base Classes:</strong> Build upon {@link
 *       com.rubensgomes.msreqresplib.BaseRequest} or {@link
 *       com.rubensgomes.msreqresplib.BaseResponse} when appropriate
 *   <li><strong>Implement Enhanced Validation:</strong> Add validation constraints that make sense
 *       for the specific use case
 *   <li><strong>Document Purpose:</strong> Clearly document what scenarios the DTO is designed for
 *       and how it differs from base classes
 *   <li><strong>Consider Language Choice:</strong> Choose Java or Kotlin based on the target
 *       audience and integration requirements
 *   <li><strong>Maintain Consistency:</strong> Follow the same patterns established by existing
 *       DTOs in this package
 * </ul>
 *
 * <h2>Performance Considerations</h2>
 *
 * <ul>
 *   <li><strong>Immutable Design:</strong> Both Java and Kotlin implementations use immutable
 *       patterns for thread safety and caching benefits
 *   <li><strong>Validation Caching:</strong> Validation results can be cached due to immutable
 *       nature
 *   <li><strong>Serialization Optimization:</strong> DTOs are optimized for JSON
 *       serialization/deserialization performance
 *   <li><strong>Memory Efficiency:</strong> Structured error data prevents string concatenation and
 *       improves memory usage patterns
 * </ul>
 *
 * @author Rubens Gomes
 * @version 0.0.4
 * @since 0.0.2
 * @see com.rubensgomes.msreqresplib.BaseRequest
 * @see com.rubensgomes.msreqresplib.BaseResponse
 * @see com.rubensgomes.msreqresplib.error
 * @see com.rubensgomes.msreqresplib.dto.ApplicationErrorResponse
 * @see com.rubensgomes.msreqresplib.dto.ApplicationError
 */
package com.rubensgomes.msreqresplib.dto;
