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
 * Microservices Request-Response Library for Enterprise Applications.
 *
 * <p>This library provides a comprehensive framework for standardizing request and response
 * handling across microservices architectures. It offers consistent data transfer objects, error
 * handling mechanisms, and status management to ensure uniform communication patterns throughout
 * distributed systems.
 *
 * <h2>Library Overview</h2>
 *
 * <p>The ms-reqresp-lib is designed to solve common challenges in microservices communication:
 *
 * <ul>
 *   <li><strong>Standardized Request/Response Patterns:</strong> Consistent DTOs for all service
 *       communications with immutable design principles
 *   <li><strong>Unified Error Handling:</strong> Hierarchical error codes and standardized error
 *       responses
 *   <li><strong>Distributed Tracing Support:</strong> Built-in correlation ID and client
 *       identification for end-to-end observability
 *   <li><strong>Thread-Safe Design:</strong> Immutable objects that can be safely shared across
 *       concurrent operations
 *   <li><strong>Validation Framework:</strong> Jakarta Bean Validation integration for runtime data
 *       integrity
 *   <li><strong>Type Safety:</strong> Strong typing for all communication contracts
 *   <li><strong>Documentation:</strong> Comprehensive JavaDoc for all public APIs
 * </ul>
 *
 * <h2>Core Components</h2>
 *
 * <h3>Base Request/Response Classes</h3>
 *
 * <p>Foundation classes that provide common structure and functionality:
 *
 * <ul>
 *   <li>{@link com.rubensgomes.msreqresplib.BaseRequest} - Immutable foundation for all request
 *       DTOs with client identification and correlation tracking
 *   <li>{@link com.rubensgomes.msreqresplib.BaseResponse} - Foundation for all response DTOs with
 *       status information and correlation tracking
 * </ul>
 *
 * <h3>Supporting Packages</h3>
 *
 * <h4>{@link com.rubensgomes.msreqresplib.dto}</h4>
 *
 * <p>Specialized Data Transfer Objects for specific scenarios:
 *
 * <ul>
 *   <li>{@link com.rubensgomes.msreqresplib.dto.ApplicationErrorResponse} - Standardized error
 *       responses with detailed error information
 * </ul>
 *
 * <h4>{@link com.rubensgomes.msreqresplib.error}</h4>
 *
 * <p>Comprehensive error handling framework:
 *
 * <ul>
 *   <li>{@link com.rubensgomes.msreqresplib.error.ErrorCode} - Interface for error code contracts
 *   <li>{@link com.rubensgomes.msreqresplib.error.ApplicationErrorCode} - Standard error codes with
 *       hierarchical naming (XXXGN### for generic, XXXMS### for service-specific)
 *   <li>{@link com.rubensgomes.msreqresplib.error.Error} - Error data structures
 * </ul>
 *
 * <h2>Key Features</h2>
 *
 * <h3>Immutable Design Pattern</h3>
 *
 * <p>All base classes implement immutability for enhanced reliability:
 *
 * <ul>
 *   <li><strong>Thread Safety:</strong> Immutable objects can be safely shared across threads
 *   <li><strong>Defensive Programming:</strong> Prevents unintended state changes after
 *       construction
 *   <li><strong>Memory Efficiency:</strong> Objects can be safely cached and reused
 *   <li><strong>Functional Programming:</strong> Supports functional programming paradigms
 *   <li><strong>GC Friendly:</strong> Reduced garbage collection pressure
 * </ul>
 *
 * <h3>Distributed Tracing and Correlation</h3>
 *
 * <p>Built-in support for microservices observability:
 *
 * <ul>
 *   <li><strong>Client Identification:</strong> Track which service originated each request
 *   <li><strong>Transaction Correlation:</strong> End-to-end request tracing with unique
 *       transaction IDs
 *   <li><strong>Logging Integration:</strong> Built-in logging methods for debugging and monitoring
 *   <li><strong>OpenTelemetry Compatible:</strong> Transaction IDs can be used with distributed
 *       tracing systems
 *   <li><strong>API Gateway Integration:</strong> Seamless integration with gateway request
 *       enrichment
 * </ul>
 *
 * <h3>Hierarchical Error Codes</h3>
 *
 * <p>The library implements a sophisticated error code system with:
 *
 * <ul>
 *   <li><strong>Generic Codes (XXXGN###):</strong> Apply across all microservices
 *   <li><strong>Service-Specific Codes (XXXMS###):</strong> Unique to individual services
 *   <li><strong>Categorized Errors:</strong> Business, Payment, Resource, Security, System,
 *       Validation
 *   <li><strong>Machine and Human Readable:</strong> Both programmatic handling and user display
 * </ul>
 *
 * <h3>Validation Framework Integration</h3>
 *
 * <p>Comprehensive validation support:
 *
 * <ul>
 *   <li><strong>Jakarta Bean Validation:</strong> Standard annotations like {@code @NotBlank},
 *       {@code @Valid}
 *   <li><strong>Custom Messages:</strong> Clear, user-friendly validation error messages
 *   <li><strong>Runtime Validation:</strong> Automatic validation in Spring Boot controllers
 *   <li><strong>Consistent Error Responses:</strong> Standardized validation error handling
 * </ul>
 *
 * <h2>Usage Examples</h2>
 *
 * <h3>Creating Immutable Request DTOs</h3>
 *
 * <pre>{@code
 * @Data
 * @EqualsAndHashCode(callSuper = true)
 * public class UserCreateRequest extends BaseRequest {
 *     @NotBlank(message = "Username is required")
 *     private final String username;
 *
 *     @Email(message = "Valid email address is required")
 *     private final String email;
 *
 *     public UserCreateRequest(String clientId, String transactionId,
 *                             String username, String email) {
 *         super(clientId, transactionId);
 *         this.username = username;
 *         this.email = email;
 *     }
 * }
 * }</pre>
 *
 * <h3>Request Creation with Correlation</h3>
 *
 * <pre>{@code
 * // Generate correlation ID for end-to-end tracing
 * String transactionId = UUID.randomUUID().toString();
 * String clientId = "user-management-service";
 *
 * UserCreateRequest request = new UserCreateRequest(
 *     clientId,
 *     transactionId,
 *     "john.doe",
 *     "john.doe@example.com"
 * );
 *
 * // Log the request for debugging and tracing
 * request.logRequest();
 * }</pre>
 *
 * <h3>Controller Integration with Validation</h3>
 *
 * <pre>{@code
 * @PostMapping("/users")
 * public ResponseEntity<UserCreateResponse> createUser(
 *         @Valid @RequestBody UserCreateRequest request) {
 *
 *     // Log incoming request for distributed tracing
 *     request.logRequest();
 *
 *     try {
 *         User user = userService.createUser(request);
 *
 *         UserCreateResponse response = new UserCreateResponse(
 *             request.getClientId(),
 *             request.getTransactionId(),
 *             Status.SUCCESS,
 *             user.getId(),
 *             user.getUsername()
 *         );
 *
 *         return ResponseEntity.ok(response);
 *     } catch (ValidationException e) {
 *         return handleValidationError(request, e);
 *     }
 * }
 * }</pre>
 *
 * <h3>Error Response Handling</h3>
 *
 * <pre>{@code
 * // Creating a standardized error response
 * ApplicationErrorResponse errorResponse = new ApplicationErrorResponse(
 *     request.getClientId(),
 *     request.getTransactionId(),
 *     Status.ERROR,
 *     ApplicationErrorCode.VALIDATION_REQUIRED_FIELD.getCode(),
 *     ApplicationErrorCode.VALIDATION_REQUIRED_FIELD.getDescription(),
 *     "Username field cannot be empty",
 *     Instant.now()
 * );
 * }</pre>
 *
 * <h3>Distributed Tracing Integration</h3>
 *
 * <pre>{@code
 * // Pass transaction ID through service calls
 * public UserProfile getUserProfile(String userId, String transactionId) {
 *     // Use the same transaction ID for downstream calls
 *     ProfileRequest profileRequest = new ProfileRequest(
 *         "profile-service",
 *         transactionId,  // Same transaction ID for correlation
 *         userId
 *     );
 *
 *     return profileService.getProfile(profileRequest);
 * }
 * }</pre>
 *
 * <h2>Integration Guidelines</h2>
 *
 * <p>To integrate this library into your microservices:
 *
 * <ol>
 *   <li><strong>Extend Base Classes:</strong> Create service-specific request/response DTOs by
 *       extending {@link com.rubensgomes.msreqresplib.BaseRequest} and {@link
 *       com.rubensgomes.msreqresplib.BaseResponse}
 *   <li><strong>Implement Immutable Design:</strong> Use final fields and constructor-based
 *       initialization for all custom DTOs
 *   <li><strong>Enable Validation:</strong> Use {@code @Valid} annotations in controllers and apply
 *       appropriate validation annotations to fields
 *   <li><strong>Implement Correlation:</strong> Generate unique transaction IDs and propagate them
 *       through all service calls
 *   <li><strong>Use Standard Error Codes:</strong> Leverage existing error codes or add
 *       service-specific ones following the XXXGN### and XXXMS### naming conventions
 *   <li><strong>Add Logging:</strong> Call {@code logRequest()} methods for debugging and
 *       monitoring
 *   <li><strong>Document Extensions:</strong> Document any service-specific extensions or
 *       customizations
 * </ol>
 *
 * <h2>Performance and Scalability</h2>
 *
 * <p>The library is designed for high-performance, scalable microservices:
 *
 * <ul>
 *   <li><strong>Zero-Copy Operations:</strong> Immutable objects can be shared safely
 *   <li><strong>No Synchronization Overhead:</strong> Thread-safe by design
 *   <li><strong>Memory Efficient:</strong> Objects can be cached and reused
 *   <li><strong>Fast Serialization:</strong> Optimized for JSON serialization/deserialization
 *   <li><strong>Minimal Dependencies:</strong> Lightweight with minimal external dependencies
 * </ul>
 *
 * <h2>Version Information</h2>
 *
 * <p>This library follows semantic versioning. Current major features:
 *
 * <ul>
 *   <li><strong>0.0.1:</strong> Initial release with basic DTOs and error handling
 *   <li><strong>0.0.2:</strong> Enhanced error codes with hierarchical naming and improved
 *       documentation
 *   <li><strong>0.0.3:</strong> Immutable design pattern, enhanced correlation tracking, and
 *       comprehensive validation framework integration
 * </ul>
 *
 * @author Rubens Gomes
 * @version 0.0.3
 * @since 0.0.1
 * @see com.rubensgomes.msreqresplib.BaseRequest
 * @see com.rubensgomes.msreqresplib.BaseResponse
 * @see com.rubensgomes.msreqresplib.dto
 * @see com.rubensgomes.msreqresplib.error
 */
package com.rubensgomes.msreqresplib;
