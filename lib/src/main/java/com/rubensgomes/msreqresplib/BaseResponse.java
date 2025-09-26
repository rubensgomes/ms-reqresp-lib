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
package com.rubensgomes.msreqresplib;

import com.rubensgomes.msbaselib.Status;
import com.rubensgomes.msbaselib.error.ApplicationError;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract base class for all response DTOs in the microservices architecture.
 *
 * <p>This class provides the foundational structure for all service response objects, ensuring
 * consistent correlation tracking, status reporting, and error handling across the entire
 * distributed system. It implements an immutable design pattern where core fields are final and set
 * through constructor parameters, with optional mutable fields for flexible response enrichment.
 *
 * <h2>Design Principles</h2>
 *
 * <h3>Hybrid Immutability</h3>
 *
 * <p>The class implements a hybrid approach to immutability:
 *
 * <ul>
 *   <li><strong>Core Fields (Final):</strong> clientId, transactionId, and status are immutable for
 *       consistency and thread safety
 *   <li><strong>Optional Fields (Mutable):</strong> error can be set after construction for
 *       flexible response building
 *   <li><strong>Thread Safety:</strong> Core correlation data is thread-safe by design
 *   <li><strong>Defensive Programming:</strong> Prevents accidental modification of critical fields
 * </ul>
 *
 * <h3>Status-Driven Architecture</h3>
 *
 * <p>Every response includes an explicit status to enable consistent error handling:
 *
 * <ul>
 *   <li><strong>Success Indication:</strong> Clear success/failure status for all operations
 *   <li><strong>Error Context:</strong> Optional error details for failure scenarios
 *   <li><strong>Structured Error Handling:</strong> Standardized error information format
 * </ul>
 *
 * <h3>Correlation and Traceability</h3>
 *
 * <p>Maintains end-to-end traceability with the originating request:
 *
 * <ul>
 *   <li><strong>Request Correlation:</strong> Links responses back to their originating requests
 *   <li><strong>Client Tracking:</strong> Identifies which service initiated the request
 *   <li><strong>Transaction Tracing:</strong> Enables distributed tracing across service boundaries
 *   <li><strong>Debugging Support:</strong> Built-in logging for troubleshooting
 * </ul>
 *
 * <h2>Usage Patterns</h2>
 *
 * <h3>Creating Service-Specific Response DTOs</h3>
 *
 * <pre>{@code
 * @Data
 * @EqualsAndHashCode(callSuper = true)
 * public class UserCreateResponse extends BaseResponse {
 *     private final String userId;
 *     private final String username;
 *     private final String email;
 *     private final Instant createdAt;
 *
 *     public UserCreateResponse(String clientId, String transactionId, Status status,
 *                              String userId, String username, String email, Instant createdAt) {
 *         super(clientId, transactionId, status);
 *         this.userId = userId;
 *         this.username = username;
 *         this.email = email;
 *         this.createdAt = createdAt;
 *     }
 * }
 * }</pre>
 *
 * <h3>Successful Response Creation</h3>
 *
 * <pre>{@code
 * // Create a successful response with data
 * UserCreateResponse response = new UserCreateResponse(
 *     request.getClientId(),
 *     request.getTransactionId(),
 *     Status.SUCCESS,
 *     user.getId(),
 *     user.getUsername(),
 *     user.getEmail(),
 *     user.getCreatedAt()
 * );
 *
 * // Log the response for debugging
 * response.logResponse();
 * }</pre>
 *
 * <h3>Error Response Creation</h3>
 *
 * <pre>{@code
 * // Create an error response
 * UserCreateResponse errorResponse = new UserCreateResponse(
 *     request.getClientId(),
 *     request.getTransactionId(),
 *     Status.ERROR,
 *     null, null, null, null  // No user data for error case
 * );
 *
 * // Add error details
 * Error error = new ApplicationError(
 *     "A user with this email address already exists",
 *     ApplicationErrorCode.VALIDATION_DUPLICATE_VALUE,
 *     "Email validation failed during user creation"
 * );
 * errorResponse.setError(error);
 *
 * // Log the error response
 * errorResponse.logResponse();
 * }</pre>
 *
 * <h3>Controller Integration</h3>
 *
 * <pre>{@code
 * @PostMapping("/users")
 * public ResponseEntity<UserCreateResponse> createUser(
 *         @Valid @RequestBody UserCreateRequest request) {
 *
 *     try {
 *         User user = userService.createUser(request);
 *
 *         UserCreateResponse response = new UserCreateResponse(
 *             request.getClientId(),
 *             request.getTransactionId(),
 *             Status.SUCCESS,
 *             user.getId(),
 *             user.getUsername(),
 *             user.getEmail(),
 *             user.getCreatedAt()
 *         );
 *
 *         response.logResponse();
 *         return ResponseEntity.ok(response);
 *
 *     } catch (DuplicateUserException e) {
 *         return createErrorResponse(request, e, HttpStatus.CONFLICT);
 *     } catch (ValidationException e) {
 *         return createErrorResponse(request, e, HttpStatus.BAD_REQUEST);
 *     }
 * }
 *
 * private ResponseEntity<UserCreateResponse> createErrorResponse(
 *         UserCreateRequest request, Exception e, HttpStatus status) {
 *
 *     UserCreateResponse errorResponse = new UserCreateResponse(
 *         request.getClientId(),
 *         request.getTransactionId(),
 *         Status.ERROR,
 *         null, null, null, null
 *     );
 *
 *     Error error = new ApplicationError(
 *         e.getMessage(),
 *         determineErrorCode(e),
 *         e.getCause() != null ? e.getCause().getMessage() : null
 *     );
 *
 *     errorResponse.setError(error);
 *     errorResponse.logResponse();
 *
 *     return ResponseEntity.status(status).body(errorResponse);
 * }
 * }</pre>
 *
 * <h3>Asynchronous Processing Responses</h3>
 *
 * <pre>{@code
 * // For long-running operations, return processing status
 * UserCreateResponse asyncResponse = new UserCreateResponse(
 *     request.getClientId(),
 *     request.getTransactionId(),
 *     Status.PROCESSING,
 *     taskId,  // Use task ID instead of final user ID
 *     null, null, null
 * );
 *
 * asyncResponse.logResponse();
 * return ResponseEntity.accepted().body(asyncResponse);
 * }</pre>
 *
 * <h2>Field Descriptions</h2>
 *
 * <h3>Core Immutable Fields</h3>
 *
 * <h4>Client ID</h4>
 *
 * <p>Inherited from the originating request, this field maintains client context:
 *
 * <ul>
 *   <li><strong>Service Identification:</strong> Tracks which service initiated the operation
 *   <li><strong>Response Routing:</strong> Enables proper response routing in complex architectures
 *   <li><strong>Audit Trail:</strong> Maintains accountability for operations
 *   <li><strong>Rate Limiting:</strong> Supports client-specific rate limiting and quotas
 * </ul>
 *
 * <h4>Transaction ID</h4>
 *
 * <p>Correlation identifier that links the response to its originating request:
 *
 * <ul>
 *   <li><strong>End-to-End Tracing:</strong> Enables complete request/response correlation
 *   <li><strong>Distributed Debugging:</strong> Facilitates troubleshooting across services
 *   <li><strong>Performance Monitoring:</strong> Supports request timing and performance analysis
 *   <li><strong>Log Correlation:</strong> Links all log entries for a single operation
 * </ul>
 *
 * <h4>Status</h4>
 *
 * <p>Explicit indication of the operation outcome:
 *
 * <ul>
 *   <li><strong>SUCCESS:</strong> Operation completed successfully
 *   <li><strong>ERROR:</strong> Operation failed with error details in the error field
 *   <li><strong>PROCESSING:</strong> Operation is still in progress (for async operations)
 *   <li><strong>PARTIAL_SUCCESS:</strong> Operation partially completed (for batch operations)
 * </ul>
 *
 * <h3>Optional Mutable Fields</h3>
 *
 * <h4>Error</h4>
 *
 * <p>Structured error information for failure scenarios:
 *
 * <ul>
 *   <li><strong>Error Codes:</strong> Machine-readable error identification
 *   <li><strong>Error Messages:</strong> Human-readable error descriptions
 *   <li><strong>Error Details:</strong> Additional context and debugging information
 *   <li><strong>Structured Format:</strong> Consistent error response format
 * </ul>
 *
 * <h2>Integration with Microservices Patterns</h2>
 *
 * <h3>Circuit Breaker Integration</h3>
 *
 * <pre>{@code
 * // Handle circuit breaker scenarios
 * if (circuitBreaker.isOpen()) {
 *     UserCreateResponse fallbackResponse = new UserCreateResponse(
 *         request.getClientId(),
 *         request.getTransactionId(),
 *         Status.ERROR,
 *         null, null, null, null
 *     );
 *
 *     Error error = new ApplicationError(
 *         "User service is temporarily unavailable",
 *         ApplicationErrorCode.SYSTEM_SERVICE_UNAVAILABLE,
 *         "Circuit breaker is open"
 *     );
 *
 *     fallbackResponse.setError(error);
 *     return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(fallbackResponse);
 * }
 * }</pre>
 *
 * <h3>Saga Pattern Support</h3>
 *
 * <pre>{@code
 * // Support for distributed transactions with saga pattern
 * public BaseResponse compensateUserCreation(String transactionId) {
 *     CompensationResponse response = new CompensationResponse(
 *         "saga-orchestrator",
 *         transactionId,
 *         Status.SUCCESS
 *     );
 *
 *     return response;
 * }
 * }</pre>
 *
 * <h2>Monitoring and Observability</h2>
 *
 * <h3>Metrics Integration</h3>
 *
 * <p>The response structure supports comprehensive monitoring:
 *
 * <ul>
 *   <li><strong>Status Metrics:</strong> Track success/error rates by status
 *   <li><strong>Client Metrics:</strong> Monitor performance by client
 *   <li><strong>Error Classification:</strong> Categorize errors for analysis
 *   <li><strong>Response Time Correlation:</strong> Link timing to transaction IDs
 * </ul>
 *
 * <h3>Health Check Responses</h3>
 *
 * <pre>{@code
 * @GetMapping("/health")
 * public ResponseEntity<HealthResponse> healthCheck() {
 *     HealthResponse response = new HealthResponse(
 *         "health-monitor",
 *         UUID.randomUUID().toString(),
 *         Status.SUCCESS
 *     );
 *
 *     return ResponseEntity.ok(response);
 * }
 * }</pre>
 *
 * <h2>Best Practices</h2>
 *
 * <ul>
 *   <li><strong>Always Set Status:</strong> Every response must have an explicit status
 *   <li><strong>Correlate Responses:</strong> Always use the request's clientId and transactionId
 *   <li><strong>Structure Errors:</strong> Use the Error object for consistent error reporting
 *   <li><strong>Log Responses:</strong> Call logResponse() for debugging and monitoring
 *   <li><strong>Handle Async Operations:</strong> Use appropriate status for long-running
 *       operations
 *   <li><strong>Validate Required Fields:</strong> Ensure core fields are never null
 *   <li><strong>Consistent Error Codes:</strong> Use standardized error codes for similar failures
 * </ul>
 *
 * <h2>Thread Safety Considerations</h2>
 *
 * <p>The hybrid immutability approach provides:
 *
 * <ul>
 *   <li><strong>Core Field Safety:</strong> Immutable core fields are thread-safe
 *   <li><strong>Optional Field Caution:</strong> Mutable error field requires synchronization if
 *       shared
 *   <li><strong>Builder Pattern:</strong> Consider using builders for complex response construction
 *   <li><strong>Defensive Copying:</strong> Copy responses when sharing across threads
 * </ul>
 *
 * @author Rubens Gomes
 * @since 0.0.1
 * @see com.rubensgomes.msbaselib.Status
 * @see com.rubensgomes.msbaselib.error.ApplicationError
 * @see BaseRequest
 * @see jakarta.validation.constraints.NotBlank
 * @see jakarta.validation.constraints.NotNull
 */
@Slf4j
@Data
public abstract class BaseResponse {
  /** Identifier of the client application originating the request. */
  @NotBlank(message = "clientId is required")
  private final String clientId;

  /** Correlation identifier used to trace a request across systems. */
  @NotBlank(message = "transactionId is required")
  private final String transactionId;

  /** Overall processing outcome for this response. */
  @NotNull(message = "status is required")
  private final Status status;

  /** Optional error details when the response indicates a failure status. */
  @Nullable private ApplicationError error;

  /** Logs the response details for debugging and tracing purposes. */
  public void logResponse() {
    log.debug(
        "Response - clientId: {}, transactionId: {}, status: {}, error: {}",
        clientId,
        transactionId,
        status,
        error);
  }
}
