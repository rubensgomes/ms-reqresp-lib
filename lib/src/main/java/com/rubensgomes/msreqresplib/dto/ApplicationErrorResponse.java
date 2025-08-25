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
package com.rubensgomes.msreqresplib.dto;

import com.rubensgomes.msreqresplib.BaseResponse;
import com.rubensgomes.msreqresplib.Status;
import com.rubensgomes.msreqresplib.error.Error;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Specialized error response for microservices communication with guaranteed error information.
 *
 * <p>This class extends {@link BaseResponse} and provides a specialized response type specifically
 * designed for error scenarios in microservices architectures. Unlike the base response where error
 * information is optional, this class enforces that comprehensive error details are always present,
 * ensuring consistent error reporting across service boundaries.
 *
 * <p>The class is particularly useful in scenarios where:
 *
 * <ul>
 *   <li>Guaranteed error information is required for client error handling
 *   <li>Consistent error response structure is needed across multiple services
 *   <li>Detailed diagnostic information must be preserved during error propagation
 *   <li>API contracts require standardized error response formats
 * </ul>
 *
 * <p><strong>Design Principles:</strong>
 *
 * <ul>
 *   <li><strong>Error-First Design</strong> - Error information is mandatory, not optional
 *   <li><strong>Validation Enforcement</strong> - All constructor parameters are validated
 *   <li><strong>Inheritance Benefits</strong> - Leverages BaseResponse for common functionality
 *   <li><strong>Type Safety</strong> - Compile-time guarantee of error presence
 * </ul>
 *
 * <p><strong>Thread Safety:</strong> This class inherits thread safety characteristics from {@link
 * BaseResponse}. Read operations are thread-safe, but modifications to the error object should be
 * handled with appropriate synchronization.
 *
 * <p><strong>Serialization:</strong> Fully compatible with JSON serialization frameworks like
 * Jackson, making it suitable for REST API responses and inter-service communication.
 *
 * <p><strong>Usage Patterns:</strong>
 *
 * <pre>{@code
 * // Basic error response creation
 * ErrorCode validationCode = ApplicationErrorCodes.VALIDATION_FAILED;
 * Error validationError = new ApplicationError(
 *     "Input validation failed",
 *     validationCode,
 *     "Field 'username' is required but was null"
 * );
 *
 * ApplicationErrorResponse response = new ApplicationErrorResponse(
 *     "mobile-app-v1",
 *     "req-789012",
 *     Status.ERROR,
 *     validationError
 * );
 *
 * // Database connection error with diagnostic information
 * ErrorCode dbCode = ApplicationErrorCodes.DATABASE_CONNECTION_FAILED;
 * Error dbError = new ApplicationError(
 *     "Database operation failed",
 *     dbCode,
 *     "Connection pool exhausted: 0/10 connections available"
 * );
 *
 * ApplicationErrorResponse dbResponse = new ApplicationErrorResponse(
 *     "order-service",
 *     "txn-456789",
 *     Status.ERROR,
 *     dbError
 * );
 *
 * // Service-to-service error propagation
 * ApplicationErrorResponse downstreamResponse = new ApplicationErrorResponse(
 *     request.getClientId(),
 *     request.getTransactionId(),
 *     Status.ERROR,
 *     downstreamError
 * );
 * }</pre>
 *
 * <p><strong>API Integration:</strong>
 *
 * <pre>{@code
 * // REST Controller usage
 * @RestController
 * public class UserController {
 *
 *     @PostMapping("/users")
 *     public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
 *         try {
 *             // Process user creation
 *             return ResponseEntity.ok(successResponse);
 *         } catch (ValidationException e) {
 *             ApplicationErrorResponse errorResponse = new ApplicationErrorResponse(
 *                 request.getClientId(),
 *                 request.getTransactionId(),
 *                 Status.ERROR,
 *                 e.getError()
 *             );
 *             return ResponseEntity.badRequest().body(errorResponse);
 *         }
 *     }
 * }
 * }</pre>
 *
 * @author Rubens Gomes
 * @since 0.0.2
 * @see BaseResponse
 * @see Error
 * @see Status
 * @see com.rubensgomes.msreqresplib.error.ErrorCode
 * @see jakarta.validation.constraints.NotBlank
 * @see jakarta.validation.constraints.NotNull
 */
public class ApplicationErrorResponse extends BaseResponse {

  /**
   * Constructs an ApplicationErrorResponse with comprehensive error information and validation.
   *
   * <p>This constructor creates an error response that guarantees the presence of detailed error
   * information, making it suitable for scenarios where comprehensive error reporting is required.
   * The constructor enforces strict validation to ensure data integrity and consistency across
   * microservices communication.
   *
   * <p><strong>Validation Rules:</strong>
   *
   * <ul>
   *   <li>All string parameters must be non-null, non-empty, and contain non-whitespace characters
   *   <li>All object parameters must be non-null
   *   <li>Error object must contain valid error information as per its own constraints
   * </ul>
   *
   * <p><strong>Parameter Guidelines:</strong>
   *
   * <ul>
   *   <li><strong>clientId</strong> - Should uniquely identify the originating client or service
   *   <li><strong>transactionId</strong> - Should be a unique correlation ID for request tracing
   *   <li><strong>status</strong> - Typically {@link Status#ERROR} but can be other error-related
   *       statuses
   *   <li><strong>error</strong> - Must contain comprehensive error details for proper client
   *       handling
   * </ul>
   *
   * <p><strong>Error Handling Best Practices:</strong>
   *
   * <ul>
   *   <li>Include enough detail in the error for client-side error handling
   *   <li>Preserve original transaction IDs for request correlation
   *   <li>Use appropriate status codes that match the error type
   *   <li>Include native error text for debugging when appropriate
   * </ul>
   *
   * <p>Example with comprehensive error details:
   *
   * <pre>{@code
   * // Create detailed error information
   * ErrorCode authCode = ApplicationErrorCodes.AUTHENTICATION_FAILED;
   * Error authError = new ApplicationError(
   *     "Authentication failed for user",
   *     authCode,
   *     "JWT token expired at 2025-08-24T10:30:00Z"
   * );
   *
   * // Create error response with proper correlation
   * ApplicationErrorResponse response = new ApplicationErrorResponse(
   *     "web-portal-v2",           // Client identification
   *     "auth-req-987654",         // Transaction correlation
   *     Status.ERROR,              // Processing status
   *     authError                  // Detailed error information
   * );
   * }</pre>
   *
   * @param clientId the identifier of the client application, service, or system that originated
   *     the request. This is used for tracking and routing purposes. Must not be null, empty, or
   *     contain only whitespace characters.
   * @param transactionId the unique correlation identifier used to trace this request across
   *     multiple systems and service boundaries. Essential for distributed tracing and debugging.
   *     Must not be null, empty, or contain only whitespace characters.
   * @param status the overall processing outcome for this response. While typically {@link
   *     Status#ERROR} for error responses, other error-related statuses may be appropriate
   *     depending on the specific error scenario. Must not be null.
   * @param error comprehensive error information including structured error codes, human-readable
   *     descriptions, and optional native error text for diagnostic purposes. This parameter
   *     ensures that error responses always contain actionable information for client error
   *     handling. Must not be null.
   * @throws NullPointerException if any parameter is null
   * @throws jakarta.validation.ConstraintViolationException if string parameters are empty or
   *     contain only whitespace characters
   * @see BaseResponse
   * @see Error#getErrorDescription()
   * @see Error#getErrorCode()
   * @see Error#getNativeErrorText()
   */
  public ApplicationErrorResponse(
      @NotBlank String clientId,
      @NotBlank String transactionId,
      @NotNull Status status,
      @NotNull Error error) {
    super(clientId, transactionId, status);
    this.setError(error);
  }
}
