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
 *   <li><strong>Unified Error Handling:</strong> Comprehensive error framework with structured
 *       error codes and standardized error responses
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
 *   <li>{@link com.rubensgomes.msreqresplib.Status} - Standardized status enumeration for request
 *       lifecycle management and operation outcomes
 * </ul>
 *
 * <h3>Supporting Packages</h3>
 *
 * <h4>{@link com.rubensgomes.msreqresplib.dto}</h4>
 *
 * <p>Specialized Data Transfer Objects for specific scenarios:
 *
 * <ul>
 *   <li>{@link com.rubensgomes.msreqresplib.dto.ApplicationErrorResponse} - Specialized error
 *       response DTO with guaranteed error information and enhanced validation
 * </ul>
 *
 * <h4>{@link com.rubensgomes.msreqresplib.error}</h4>
 *
 * <p>Comprehensive error handling framework:
 *
 * <ul>
 *   <li>{@link com.rubensgomes.msreqresplib.error.ErrorCode} - Interface for standardized error
 *       code contracts and implementations
 *   <li>{@link com.rubensgomes.msreqresplib.error.Error} - Core interface for error data structures
 *       and comprehensive error reporting
 *   <li>{@link com.rubensgomes.msreqresplib.error.ApplicationError} - Concrete implementation of
 *       the Error interface with Lombok integration and hybrid immutability
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
 *       without synchronization concerns
 *   <li><strong>Defensive Programming:</strong> Prevents unintended state changes after
 *       construction, reducing bugs
 *   <li><strong>Memory Efficiency:</strong> Objects can be safely cached and reused across multiple
 *       operations
 *   <li><strong>Functional Programming:</strong> Supports functional programming paradigms and
 *       method chaining
 *   <li><strong>GC Friendly:</strong> Reduced garbage collection pressure through object reuse
 * </ul>
 *
 * <h3>Hybrid Immutability for Error Handling</h3>
 *
 * <p>Advanced immutability pattern that balances consistency with flexibility:
 *
 * <ul>
 *   <li><strong>Core Field Immutability:</strong> Critical fields like error descriptions and error
 *       codes are immutable once set
 *   <li><strong>Diagnostic Mutability:</strong> Optional diagnostic information can be updated as
 *       additional context becomes available
 *   <li><strong>Thread-Safe Reads:</strong> Read operations on immutable fields are completely
 *       thread-safe
 *   <li><strong>Controlled Updates:</strong> Mutable fields are clearly documented and isolated for
 *       safe concurrent access patterns
 * </ul>
 *
 * <h3>Distributed Tracing and Correlation</h3>
 *
 * <p>Built-in support for microservices observability:
 *
 * <ul>
 *   <li><strong>Client Identification:</strong> Track which service originated each request for
 *       debugging and monitoring
 *   <li><strong>Transaction Correlation:</strong> End-to-end request tracing with unique
 *       transaction IDs that propagate across service boundaries
 *   <li><strong>Observability Integration:</strong> Seamless integration with distributed tracing
 *       systems like Zipkin, Jaeger, and OpenTelemetry
 *   <li><strong>Request Context:</strong> Automatic context propagation for enhanced debugging and
 *       monitoring capabilities
 * </ul>
 *
 * <h3>Validation and Type Safety</h3>
 *
 * <p>Comprehensive validation framework integration:
 *
 * <ul>
 *   <li><strong>Jakarta Bean Validation:</strong> Runtime validation using standard annotations
 *       like {@code @NotBlank}, {@code @NotNull}, {@code @Valid}
 *   <li><strong>Custom Validation Messages:</strong> Clear, actionable validation error messages
 *   <li><strong>Null Safety:</strong> Designed to minimize null pointer exceptions through proper
 *       validation and nullable annotations
 *   <li><strong>Type Safety:</strong> Strong typing prevents runtime type errors and improves IDE
 *       support with comprehensive auto-completion
 * </ul>
 *
 * <h2>Usage Examples</h2>
 *
 * <h3>Creating a Custom Request DTO</h3>
 *
 * <pre>{@code
 * @Data
 * @EqualsAndHashCode(callSuper = true)
 * public class UserRegistrationRequest extends BaseRequest {
 *     @NotBlank(message = "Username is required")
 *     private final String username;
 *
 *     @Email(message = "Valid email address is required")
 *     @NotBlank(message = "Email is required")
 *     private final String email;
 *
 *     public UserRegistrationRequest(String clientId, String transactionId,
 *                                   String username, String email) {
 *         super(clientId, transactionId);
 *         this.username = username;
 *         this.email = email;
 *     }
 * }
 * }</pre>
 *
 * <h3>Creating a Custom Response DTO</h3>
 *
 * <pre>{@code
 * @Data
 * @EqualsAndHashCode(callSuper = true)
 * public class UserRegistrationResponse extends BaseResponse {
 *     private final String userId;
 *     private final String username;
 *     private final Instant createdAt;
 *
 *     public UserRegistrationResponse(String clientId, String transactionId,
 *                                    Status status, String userId, String username,
 *                                    Instant createdAt) {
 *         super(clientId, transactionId, status);
 *         this.userId = userId;
 *         this.username = username;
 *         this.createdAt = createdAt;
 *     }
 * }
 * }</pre>
 *
 * <h3>Error Handling with ApplicationError</h3>
 *
 * <pre>{@code
 * // Define custom error codes
 * public enum UserServiceErrorCodes implements ErrorCode {
 *     DUPLICATE_USERNAME("USR001", "Username already exists"),
 *     INVALID_EMAIL("USR002", "Invalid email format"),
 *     REGISTRATION_FAILED("USR003", "User registration failed");
 *
 *     private final String code;
 *     private final String description;
 *
 *     UserServiceErrorCodes(String code, String description) {
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
 * // Create error responses
 * try {
 *     // Service operation
 *     User newUser = userService.createUser(request);
 *     return new UserRegistrationResponse(
 *         request.getClientId(),
 *         request.getTransactionId(),
 *         Status.SUCCESS,
 *         newUser.getId(),
 *         newUser.getUsername(),
 *         newUser.getCreatedAt()
 *     );
 * } catch (DuplicateUsernameException e) {
 *     // Create detailed error information
 *     ApplicationError error = new ApplicationError(
 *         "Username is already taken",
 *         UserServiceErrorCodes.DUPLICATE_USERNAME
 *     );
 *     error.setNativeErrorText("Database constraint violation: unique_username");
 *
 *     // Return guaranteed error response
 *     return new ApplicationErrorResponse(
 *         request.getClientId(),
 *         request.getTransactionId(),
 *         Status.ERROR,
 *         error
 *     );
 * }
 * }</pre>
 *
 * <h3>Response Logging and Monitoring</h3>
 *
 * <pre>{@code
 * @Service
 * public class UserService {
 *
 *     public BaseResponse processUserRequest(UserRegistrationRequest request) {
 *         BaseResponse response;
 *         try {
 *             // Process the request
 *             response = createUser(request);
 *         } catch (Exception e) {
 *             response = createErrorResponse(request, e);
 *         }
 *
 *         // Log response for monitoring and debugging
 *         response.logResponse();
 *         return response;
 *     }
 * }
 * }</pre>
 *
 * <h2>Best Practices</h2>
 *
 * <h3>Request/Response Design</h3>
 *
 * <ul>
 *   <li><strong>Always extend base classes:</strong> Use {@code BaseRequest} and {@code
 *       BaseResponse} for all DTOs to ensure consistency and correlation tracking
 *   <li><strong>Immutable fields:</strong> Declare all fields as {@code final} and initialize them
 *       in constructors for thread safety
 *   <li><strong>Validation annotations:</strong> Use Jakarta Bean Validation annotations for data
 *       integrity and clear error messages
 *   <li><strong>Meaningful names:</strong> Use descriptive class and field names that clearly
 *       indicate their purpose and constraints
 *   <li><strong>Lombok integration:</strong> Use {@code @Data} and
 *       {@code @EqualsAndHashCode(callSuper = true)} for cleaner code and proper inheritance
 *       behavior
 * </ul>
 *
 * <h3>Error Handling</h3>
 *
 * <ul>
 *   <li><strong>Use ApplicationErrorResponse:</strong> Always use {@code ApplicationErrorResponse}
 *       for error scenarios to guarantee error information presence
 *   <li><strong>Implement ErrorCode interface:</strong> Create service-specific error codes that
 *       implement {@code ErrorCode} for consistent error classification
 *   <li><strong>Provide comprehensive context:</strong> Include meaningful error messages for users
 *       and diagnostic information for developers
 *   <li><strong>Maintain correlation:</strong> Always preserve transaction IDs in error responses
 *       for end-to-end tracing
 *   <li><strong>Structured error information:</strong> Use {@code ApplicationError} for rich error
 *       context with optional native error text
 * </ul>
 *
 * <h3>Status Management</h3>
 *
 * <ul>
 *   <li><strong>Appropriate status values:</strong> Use {@code Status} enum values that accurately
 *       reflect the operation outcome (SUCCESS, ERROR, PROCESSING)
 *   <li><strong>Lifecycle tracking:</strong> Update status as operations progress through different
 *       phases for long-running processes
 *   <li><strong>Async operations:</strong> Use {@code Status.PROCESSING} for long-running
 *       operations with separate completion notifications
 *   <li><strong>Consistent mapping:</strong> Map status values to appropriate HTTP status codes in
 *       REST APIs
 * </ul>
 *
 * <h3>Logging and Monitoring</h3>
 *
 * <ul>
 *   <li><strong>Response logging:</strong> Use the built-in {@code logResponse()} method for
 *       consistent logging across all services
 *   <li><strong>Transaction correlation:</strong> Ensure transaction IDs are logged for request
 *       correlation and debugging
 *   <li><strong>Error tracking:</strong> Log error responses with sufficient detail for monitoring
 *       and alerting systems
 *   <li><strong>Performance monitoring:</strong> Track response times and error rates by
 *       transaction ID and client ID
 * </ul>
 *
 * <h2>Dependencies</h2>
 *
 * <p>This library requires the following dependencies:
 *
 * <ul>
 *   <li><strong>Jakarta Bean Validation API:</strong> For runtime validation support
 *       ({@code @NotBlank}, {@code @NotNull}, {@code @Valid})
 *   <li><strong>SLF4J:</strong> For logging abstraction and built-in response logging
 *   <li><strong>Lombok:</strong> For code generation and boilerplate reduction (compile-time only)
 *   <li><strong>Jakarta Annotations API:</strong> For {@code @Nullable} and other standard
 *       annotations
 * </ul>
 *
 * <h2>Version Information</h2>
 *
 * <p>Current version: 0.0.9-SNAPSHOT
 *
 * <p>This library follows semantic versioning principles. For version history and migration guides,
 * please refer to the project's CHANGELOG.md file.
 *
 * <h2>Thread Safety</h2>
 *
 * <p>All classes in this library are designed with thread safety in mind:
 *
 * <ul>
 *   <li><strong>Immutable Core Fields:</strong> Base request/response fields are immutable and
 *       thread-safe for concurrent read access
 *   <li><strong>Hybrid Immutability:</strong> Error classes use a hybrid approach where core error
 *       information is immutable while diagnostic information can be safely updated
 *   <li><strong>No Shared State:</strong> Each request/response instance maintains its own state
 *       without shared mutable references
 *   <li><strong>Safe Construction:</strong> Objects are safely constructed before being shared
 *       across threads
 * </ul>
 *
 * <h2>Performance Considerations</h2>
 *
 * <p>The design patterns used throughout this library provide several performance benefits:
 *
 * <ul>
 *   <li><strong>Object Caching:</strong> Immutable objects can be safely cached and reused across
 *       multiple operations
 *   <li><strong>Memory Efficiency:</strong> Reduced object churn and garbage collection pressure
 *       through immutable design
 *   <li><strong>CPU Cache Friendly:</strong> Immutable objects are more likely to remain in CPU
 *       cache due to their stable memory footprint
 *   <li><strong>No Synchronization Overhead:</strong> Thread-safe operations without locks or
 *       synchronization mechanisms
 *   <li><strong>Lombok Optimization:</strong> Generated code is optimized for performance and
 *       memory usage
 * </ul>
 *
 * <h2>Testing Support</h2>
 *
 * <p>The library design facilitates comprehensive testing:
 *
 * <ul>
 *   <li><strong>Unit Testing:</strong> Immutable objects are easy to test with predictable behavior
 *       and clear contracts
 *   <li><strong>Integration Testing:</strong> Standardized request/response patterns simplify
 *       service integration testing
 *   <li><strong>Error Testing:</strong> Comprehensive error handling framework supports thorough
 *       error scenario testing
 *   <li><strong>Validation Testing:</strong> Jakarta Bean Validation integration enables automated
 *       validation testing
 * </ul>
 *
 * @author Rubens Gomes
 * @version 0.0.9-SNAPSHOT
 * @since 0.0.1
 * @see com.rubensgomes.msreqresplib.BaseRequest
 * @see com.rubensgomes.msreqresplib.BaseResponse
 * @see com.rubensgomes.msreqresplib.Status
 * @see com.rubensgomes.msreqresplib.dto
 * @see com.rubensgomes.msreqresplib.error
 */
package com.rubensgomes.msreqresplib;
