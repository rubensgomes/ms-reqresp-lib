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
 * Data Transfer Objects (DTOs) for standardized request and response handling in microservices.
 *
 * <p>This package provides a comprehensive set of base classes and specialized DTOs that enable
 * consistent communication patterns across microservices architectures. The DTOs are designed to
 * promote type safety, validation, and standardization while maintaining flexibility for
 * service-specific requirements.
 *
 * <h2>Core Components</h2>
 *
 * <h3>Base Classes</h3>
 *
 * <p>Foundation classes that provide common structure and functionality:
 *
 * <ul>
 *   <li>{@link com.rubensgomes.msreqresplib.BaseRequest} - Base class for all request DTOs with
 *       common metadata fields like correlation IDs and timestamps
 *   <li>{@link com.rubensgomes.msreqresplib.BaseResponse} - Base class for all response DTOs with
 *       status information and correlation tracking
 * </ul>
 *
 * <h3>Specialized Response Types</h3>
 *
 * <p>Purpose-built response DTOs for specific scenarios:
 *
 * <ul>
 *   <li>{@link com.rubensgomes.msreqresplib.dto.ApplicationErrorResponse} - Standardized error
 *       response with detailed error information, codes, and debugging context
 * </ul>
 *
 * <h2>Design Principles</h2>
 *
 * <h3>Consistency</h3>
 *
 * <p>All DTOs follow consistent naming conventions, field structures, and validation patterns:
 *
 * <ul>
 *   <li><strong>Common Fields:</strong> Every request includes correlation ID and timestamp
 *   <li><strong>Status Tracking:</strong> All responses include operation status information
 *   <li><strong>Error Handling:</strong> Standardized error response format across all services
 *   <li><strong>Validation:</strong> Built-in validation annotations for data integrity
 * </ul>
 *
 * <h3>Extensibility</h3>
 *
 * <p>DTOs are designed to be easily extended for service-specific needs:
 *
 * <ul>
 *   <li><strong>Inheritance:</strong> Service-specific DTOs extend base classes
 *   <li><strong>Composition:</strong> Complex data structures can be composed from simpler DTOs
 *   <li><strong>Flexibility:</strong> Generic type parameters allow for type-safe customization
 * </ul>
 *
 * <h3>Type Safety</h3>
 *
 * <p>Strong typing ensures compile-time safety and clear contracts:
 *
 * <ul>
 *   <li><strong>Generic Types:</strong> Parameterized types for flexible, type-safe DTOs
 *   <li><strong>Enum Usage:</strong> Status and error codes use enums for type safety
 *   <li><strong>Validation Annotations:</strong> Jakarta validation for runtime safety
 * </ul>
 *
 * <h2>Usage Patterns</h2>
 *
 * <h3>Creating Service-Specific Request DTOs</h3>
 *
 * <pre>{@code
 * @Data
 * @EqualsAndHashCode(callSuper = true)
 * @SuperBuilder
 * @NoArgsConstructor
 * @AllArgsConstructor
 * public class UserCreateRequest extends BaseRequest {
 *     @NotBlank(message = "Username is required")
 *     private String username;
 *
 *     @Email(message = "Valid email address is required")
 *     private String email;
 *
 *     @Size(min = 8, message = "Password must be at least 8 characters")
 *     private String password;
 * }
 * }</pre>
 *
 * <h3>Creating Service-Specific Response DTOs</h3>
 *
 * <pre>{@code
 * @Data
 * @EqualsAndHashCode(callSuper = true)
 * @SuperBuilder
 * @NoArgsConstructor
 * @AllArgsConstructor
 * public class UserCreateResponse extends BaseResponse {
 *     private String userId;
 *     private String username;
 *     private String email;
 *     private Instant createdAt;
 * }
 * }</pre>
 *
 * <h3>Handling Successful Operations</h3>
 *
 * <pre>{@code
 * // In your service controller
 * @PostMapping("/users")
 * public ResponseEntity<UserCreateResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
 *     try {
 *         User user = userService.createUser(request);
 *
 *         UserCreateResponse response = UserCreateResponse.builder()
 *             .correlationId(request.getCorrelationId())
 *             .status(Status.SUCCESS)
 *             .timestamp(Instant.now())
 *             .userId(user.getId())
 *             .username(user.getUsername())
 *             .email(user.getEmail())
 *             .createdAt(user.getCreatedAt())
 *             .build();
 *
 *         return ResponseEntity.ok(response);
 *     } catch (ValidationException e) {
 *         return handleValidationError(request, e);
 *     }
 * }
 * }</pre>
 *
 * <h3>Handling Error Scenarios</h3>
 *
 * <pre>{@code
 * // Error handling method
 * private ResponseEntity<ApplicationErrorResponse> handleValidationError(
 *         BaseRequest request, ValidationException e) {
 *
 *     ApplicationErrorResponse errorResponse = ApplicationErrorResponse.builder()
 *         .correlationId(request.getCorrelationId())
 *         .status(Status.ERROR)
 *         .errorCode(ApplicationErrorCode.VALIDATION_REQUIRED_FIELD.getCode())
 *         .errorMessage(ApplicationErrorCode.VALIDATION_REQUIRED_FIELD.getDescription())
 *         .errorDetails(e.getMessage())
 *         .timestamp(Instant.now())
 *         .build();
 *
 *     return ResponseEntity.badRequest().body(errorResponse);
 * }
 * }</pre>
 *
 * <h3>Request Correlation and Tracing</h3>
 *
 * <pre>{@code
 * // Generate correlation ID for request tracking
 * UserCreateRequest request = UserCreateRequest.builder()
 *     .correlationId(UUID.randomUUID().toString())
 *     .timestamp(Instant.now())
 *     .username("john.doe")
 *     .email("john.doe@example.com")
 *     .password("securePassword123")
 *     .build();
 *
 * // The correlation ID flows through the entire request lifecycle
 * logger.info("Processing user creation request: {}", request.getCorrelationId());
 * }</pre>
 *
 * <h2>Integration Guidelines</h2>
 *
 * <h3>Service Implementation</h3>
 *
 * <p>When implementing microservices using these DTOs:
 *
 * <ol>
 *   <li><strong>Extend Base Classes:</strong> Always extend {@link
 *       com.rubensgomes.msreqresplib.BaseRequest} and {@link
 *       com.rubensgomes.msreqresplib.BaseResponse} for service-specific DTOs
 *   <li><strong>Use Validation:</strong> Apply Jakarta validation annotations for input validation
 *   <li><strong>Maintain Correlation:</strong> Ensure correlation IDs flow through the entire
 *       request lifecycle
 *   <li><strong>Handle Errors Consistently:</strong> Use {@link
 *       com.rubensgomes.msreqresplib.dto.ApplicationErrorResponse} for all error scenarios
 *   <li><strong>Follow Naming Conventions:</strong> Use clear, descriptive names for
 *       service-specific DTOs
 * </ol>
 *
 * <h3>Client Implementation</h3>
 *
 * <p>When consuming services that use these DTOs:
 *
 * <ol>
 *   <li><strong>Generate Correlation IDs:</strong> Always provide unique correlation IDs for
 *       tracking
 *   <li><strong>Handle All Response Types:</strong> Be prepared to handle both success and error
 *       responses
 *   <li><strong>Validate Inputs:</strong> Perform client-side validation before sending requests
 *   <li><strong>Log Correlations:</strong> Use correlation IDs for distributed tracing and
 *       debugging
 * </ol>
 *
 * <h2>Best Practices</h2>
 *
 * <ul>
 *   <li><strong>Immutability:</strong> Use builders and final fields where possible for thread
 *       safety
 *   <li><strong>Validation:</strong> Apply appropriate validation annotations to ensure data
 *       integrity
 *   <li><strong>Documentation:</strong> Document all fields with clear JavaDoc comments
 *   <li><strong>Versioning:</strong> Consider backward compatibility when modifying existing DTOs
 *   <li><strong>Testing:</strong> Create comprehensive unit tests for all custom DTOs
 *   <li><strong>Serialization:</strong> Ensure DTOs are properly serializable for JSON/XML
 *       processing
 * </ul>
 *
 * <h2>Integration with Error Framework</h2>
 *
 * <p>This package integrates seamlessly with the error handling framework provided in {@link
 * com.rubensgomes.msreqresplib.error}, enabling consistent error responses across all microservices
 * using standardized error codes and descriptions.
 *
 * @author Rubens Gomes
 * @since 0.0.1
 * @see com.rubensgomes.msreqresplib.BaseRequest
 * @see com.rubensgomes.msreqresplib.BaseResponse
 * @see com.rubensgomes.msreqresplib.dto.ApplicationErrorResponse
 * @see com.rubensgomes.msreqresplib.error
 * @see com.rubensgomes.msreqresplib.Status
 */
package com.rubensgomes.msreqresplib.dto;
