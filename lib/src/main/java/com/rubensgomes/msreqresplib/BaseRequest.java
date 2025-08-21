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
package com.rubensgomes.msreqresplib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Abstract base class for all request DTOs in the microservices architecture.
 *
 * <p>This class provides the foundational structure for all service request objects, ensuring
 * consistent correlation tracking and client identification across the entire distributed system.
 * It implements an immutable design pattern where all fields are final and set through constructor
 * parameters.
 *
 * <h2>Design Principles</h2>
 *
 * <h3>Immutability</h3>
 *
 * <p>All fields in this class are declared as {@code final}, ensuring thread safety and preventing
 * accidental modification after construction. This design promotes:
 *
 * <ul>
 *   <li><strong>Thread Safety:</strong> Immutable objects can be safely shared across threads
 *   <li><strong>Defensive Programming:</strong> Prevents unintended state changes
 *   <li><strong>Functional Programming:</strong> Supports functional programming paradigms
 *   <li><strong>Cache Friendly:</strong> Immutable objects can be safely cached
 * </ul>
 *
 * <h3>Validation</h3>
 *
 * <p>The class uses Jakarta Bean Validation annotations to ensure data integrity:
 *
 * <ul>
 *   <li><strong>{@code @NotBlank}:</strong> Ensures fields are not null, empty, or whitespace-only
 *   <li><strong>Custom Messages:</strong> Provides clear validation error messages
 *   <li><strong>Runtime Validation:</strong> Validation occurs at runtime using validators
 * </ul>
 *
 * <h3>Correlation and Traceability</h3>
 *
 * <p>The class provides essential fields for distributed system observability:
 *
 * <ul>
 *   <li><strong>Client Identification:</strong> Tracks which application originated the request
 *   <li><strong>Transaction Correlation:</strong> Enables end-to-end request tracing
 *   <li><strong>Debugging Support:</strong> Built-in logging for troubleshooting
 * </ul>
 *
 * <h2>Usage Patterns</h2>
 *
 * <h3>Creating Service-Specific Request DTOs</h3>
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
 *     @Size(min = 8, message = "Password must be at least 8 characters")
 *     private final String password;
 *
 *     public UserCreateRequest(String clientId, String transactionId,
 *                             String username, String email, String password) {
 *         super(clientId, transactionId);
 *         this.username = username;
 *         this.email = email;
 *         this.password = password;
 *     }
 * }
 * }</pre>
 *
 * <h3>Request Creation with Correlation IDs</h3>
 *
 * <pre>{@code
 * // Generate correlation ID for request tracking
 * String correlationId = UUID.randomUUID().toString();
 * String clientId = "user-management-service";
 *
 * UserCreateRequest request = new UserCreateRequest(
 *     clientId,
 *     correlationId,
 *     "john.doe",
 *     "john.doe@example.com",
 *     "securePassword123"
 * );
 *
 * // Log the request for debugging
 * request.logRequest();
 * }</pre>
 *
 * <h3>Validation Usage</h3>
 *
 * <pre>{@code
 * // Validate the request
 * ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
 * Validator validator = factory.getValidator();
 * Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);
 *
 * if (!violations.isEmpty()) {
 *     violations.forEach(violation ->
 *         log.error("Validation error: {} = {}",
 *                   violation.getPropertyPath(), violation.getMessage()));
 * }
 * }</pre>
 *
 * <h3>Controller Integration</h3>
 *
 * <pre>{@code
 * @PostMapping("/users")
 * public ResponseEntity<UserCreateResponse> createUser(
 *         @Valid @RequestBody UserCreateRequest request) {
 *
 *     // Log incoming request for tracing
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
 *             user.getUsername(),
 *             user.getEmail()
 *         );
 *
 *         return ResponseEntity.ok(response);
 *     } catch (ValidationException e) {
 *         return handleValidationError(request, e);
 *     }
 * }
 * }</pre>
 *
 * <h2>Field Descriptions</h2>
 *
 * <h3>Client ID</h3>
 *
 * <p>The {@code clientId} field identifies the originating application or service. This is crucial
 * for:
 *
 * <ul>
 *   <li><strong>Service Identification:</strong> Knowing which service made the request
 *   <li><strong>Authorization:</strong> Implementing service-level access controls
 *   <li><strong>Rate Limiting:</strong> Applying different limits per client
 *   <li><strong>Monitoring:</strong> Tracking usage patterns by client
 * </ul>
 *
 * <h3>Transaction ID</h3>
 *
 * <p>The {@code transactionId} field provides end-to-end correlation across services. Best
 * practices include:
 *
 * <ul>
 *   <li><strong>UUID Generation:</strong> Use UUID.randomUUID() for uniqueness
 *   <li><strong>Propagation:</strong> Pass the same ID through all related service calls
 *   <li><strong>Logging:</strong> Include in all log statements for correlation
 *   <li><strong>Headers:</strong> Include in HTTP headers for distributed tracing
 * </ul>
 *
 * <h2>Integration with Microservices Patterns</h2>
 *
 * <h3>Distributed Tracing</h3>
 *
 * <p>The transaction ID supports distributed tracing implementations:
 *
 * <ul>
 *   <li><strong>Zipkin/Jaeger:</strong> Can be used as trace IDs
 *   <li><strong>OpenTelemetry:</strong> Supports span correlation
 *   <li><strong>Custom Logging:</strong> Enables manual log correlation
 * </ul>
 *
 * <h3>API Gateway Integration</h3>
 *
 * <p>Works seamlessly with API gateways:
 *
 * <ul>
 *   <li><strong>Request Enrichment:</strong> Gateway can inject client and transaction IDs
 *   <li><strong>Rate Limiting:</strong> Gateway can use client ID for rate limiting
 *   <li><strong>Authentication:</strong> Client ID can be derived from API keys
 * </ul>
 *
 * <h2>Thread Safety and Performance</h2>
 *
 * <p>This class is designed for high-performance, multi-threaded environments:
 *
 * <ul>
 *   <li><strong>Immutable:</strong> Thread-safe by design
 *   <li><strong>No Synchronization:</strong> No need for locks or synchronized blocks
 *   <li><strong>Memory Efficient:</strong> Objects can be shared safely
 *   <li><strong>GC Friendly:</strong> Immutable objects reduce GC pressure
 * </ul>
 *
 * <h2>Best Practices</h2>
 *
 * <ul>
 *   <li><strong>Always Validate:</strong> Use {@code @Valid} annotation in controllers
 *   <li><strong>Log Requests:</strong> Call {@code logRequest()} for debugging
 *   <li><strong>Consistent IDs:</strong> Use UUIDs for transaction IDs
 *   <li><strong>Meaningful Client IDs:</strong> Use descriptive client identifiers
 *   <li><strong>Propagate Context:</strong> Pass transaction IDs through all service calls
 *   <li><strong>Handle Validation:</strong> Implement proper validation error handling
 * </ul>
 *
 * @author Rubens Gomes
 * @since 0.0.1
 * @see jakarta.validation.constraints.NotBlank
 * @see lombok.Data
 * @see BaseResponse
 */
@Data
public abstract class BaseRequest {
  /** Identifier of the client application originating the request. */
  @NotBlank(message = "clientId is required")
  private final String clientId;

  /** Correlation identifier used to trace a request across systems. */
  @NotBlank(message = "transactionId is required")
  private final String transactionId;

  /** Logs the request details for debugging and tracing purposes. */
  public void logRequest() {
    log.debug("Request - clientId: {}, transactionId: {}", clientId, transactionId);
  }

  private static final Logger log = LoggerFactory.getLogger(BaseRequest.class);
}
