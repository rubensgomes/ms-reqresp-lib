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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the lifecycle status or outcome of processing a request/response in microservices
 * architectures.
 *
 * <p>This enum provides a standardized way to communicate the current state or final outcome of
 * operations across distributed services. It supports both synchronous and asynchronous processing
 * patterns, enabling consistent status reporting throughout the request lifecycle.
 *
 * <p>The status values are designed to cover the complete spectrum of operation states, from
 * initial queuing through final completion or failure. This comprehensive coverage enables detailed
 * tracking of request processing through complex microservices workflows.
 *
 * <h2>Status Categories</h2>
 *
 * <p>The status values can be grouped into several logical categories:
 *
 * <ul>
 *   <li><strong>Success States:</strong> {@link #SUCCESS}, {@link #COMPLETED} - Operation finished
 *       successfully
 *   <li><strong>Failure States:</strong> {@link #FAILURE}, {@link #ERROR}, {@link #TIMEOUT}, {@link
 *       #ABORTED} - Operation failed for various reasons
 *   <li><strong>Progress States:</strong> {@link #PENDING}, {@link #PROCESSING} - Operation is in
 *       progress
 *   <li><strong>Termination States:</strong> {@link #CANCELLED} - Operation was explicitly stopped
 * </ul>
 *
 * <h2>Usage Patterns</h2>
 *
 * <h3>Synchronous Operations</h3>
 *
 * <p>For immediate request/response patterns, typically only final states are used:
 *
 * <ul>
 *   <li>{@link #SUCCESS} - Operation completed successfully
 *   <li>{@link #ERROR} - Operation failed with an error
 *   <li>{@link #FAILURE} - Operation failed due to business logic constraints
 * </ul>
 *
 * <h3>Asynchronous Operations</h3>
 *
 * <p>For long-running or queued operations, the full lifecycle is represented:
 *
 * <ol>
 *   <li>{@link #PENDING} - Request accepted and queued
 *   <li>{@link #PROCESSING} - Work has begun
 *   <li>Final state: {@link #SUCCESS}, {@link #COMPLETED}, {@link #ERROR}, {@link #FAILURE}, {@link
 *       #TIMEOUT}, {@link #ABORTED}, or {@link #CANCELLED}
 * </ol>
 *
 * <h2>HTTP Status Code Mapping</h2>
 *
 * <p>When used in REST APIs, these statuses typically map to HTTP status codes as follows:
 *
 * <ul>
 *   <li><strong>{@link #SUCCESS}, {@link #COMPLETED}:</strong> 200 OK, 201 Created, 202 Accepted
 *   <li><strong>{@link #PENDING}:</strong> 202 Accepted
 *   <li><strong>{@link #PROCESSING}:</strong> 102 Processing (WebDAV) or 202 Accepted
 *   <li><strong>{@link #ERROR}:</strong> 500 Internal Server Error
 *   <li><strong>{@link #FAILURE}:</strong> 400 Bad Request, 422 Unprocessable Entity
 *   <li><strong>{@link #TIMEOUT}:</strong> 408 Request Timeout, 504 Gateway Timeout
 *   <li><strong>{@link #CANCELLED}:</strong> 499 Client Closed Request
 *   <li><strong>{@link #ABORTED}:</strong> 500 Internal Server Error
 * </ul>
 *
 * <h2>Monitoring and Observability</h2>
 *
 * <p>Status values are designed to support comprehensive monitoring:
 *
 * <ul>
 *   <li><strong>Success Rate Tracking:</strong> Monitor ratio of {@link #SUCCESS}/{@link
 *       #COMPLETED} vs failure states
 *   <li><strong>Performance Monitoring:</strong> Track time spent in {@link #PROCESSING} state
 *   <li><strong>Queue Health:</strong> Monitor {@link #PENDING} backlog and processing times
 *   <li><strong>Error Analysis:</strong> Categorize failures by specific error types
 * </ul>
 *
 * <h2>Thread Safety</h2>
 *
 * <p>As an enum, this class is inherently thread-safe and immutable. Status instances can be safely
 * shared across threads and used in concurrent processing scenarios without synchronization.
 *
 * <h2>Usage Examples</h2>
 *
 * <pre>{@code
 * // Synchronous operation
 * public BaseResponse processPayment(PaymentRequest request) {
 *     try {
 *         PaymentResult result = paymentService.process(request);
 *         return new PaymentResponse(
 *             request.getClientId(),
 *             request.getTransactionId(),
 *             Status.SUCCESS,
 *             result
 *         );
 *     } catch (PaymentException e) {
 *         return new PaymentResponse(
 *             request.getClientId(),
 *             request.getTransactionId(),
 *             Status.FAILURE,
 *             null,
 *             e.getMessage()
 *         );
 *     }
 * }
 *
 * // Asynchronous operation
 * public BaseResponse submitLongRunningTask(TaskRequest request) {
 *     String taskId = taskService.submit(request);
 *
 *     return new TaskResponse(
 *         request.getClientId(),
 *         request.getTransactionId(),
 *         Status.PENDING,
 *         taskId
 *     );
 * }
 *
 * // Status polling endpoint
 * public BaseResponse getTaskStatus(String taskId) {
 *     TaskStatus taskStatus = taskService.getStatus(taskId);
 *
 *     Status status = switch (taskStatus) {
 *         case QUEUED -> Status.PENDING;
 *         case RUNNING -> Status.PROCESSING;
 *         case FINISHED -> Status.COMPLETED;
 *         case FAILED -> Status.ERROR;
 *         case CANCELLED -> Status.CANCELLED;
 *         case TIMED_OUT -> Status.TIMEOUT;
 *     };
 *
 *     return new TaskStatusResponse(
 *         "polling-client",
 *         UUID.randomUUID().toString(),
 *         status,
 *         taskStatus.getProgress()
 *     );
 * }
 * }</pre>
 *
 * @author Rubens Gomes
 * @version 0.0.4
 * @since 0.0.1
 * @see com.rubensgomes.msreqresplib.BaseResponse
 * @see com.rubensgomes.msreqresplib.dto.ApplicationErrorResponse
 */
public enum Status {
  /**
   * Operation completed successfully without any errors.
   *
   * <p>This status indicates that the requested operation was executed successfully and produced
   * the expected result. All business logic validation passed, and any side effects (such as
   * database updates or external API calls) completed successfully.
   *
   * <p>Use this status for:
   *
   * <ul>
   *   <li>Data retrieval operations that found and returned the requested data
   *   <li>Create operations that successfully created new resources
   *   <li>Update operations that successfully modified existing resources
   *   <li>Delete operations that successfully removed resources
   *   <li>Business operations that completed all required steps
   * </ul>
   *
   * <p>Typically maps to HTTP 200 OK, 201 Created, or 204 No Content.
   */
  SUCCESS,

  /**
   * Operation failed and could not be completed due to business logic constraints or validation
   * errors.
   *
   * <p>This status indicates that while the system is functioning correctly, the specific operation
   * could not be completed due to business rules, data validation failures, or constraint
   * violations. This is typically a client-side error where the request was malformed or violated
   * business logic.
   *
   * <p>Use this status for:
   *
   * <ul>
   *   <li>Validation errors (missing required fields, invalid formats)
   *   <li>Business rule violations (insufficient funds, duplicate records)
   *   <li>Authorization failures (user not permitted to perform action)
   *   <li>Resource conflicts (trying to create duplicate unique resources)
   *   <li>Precondition failures (resource in wrong state for operation)
   * </ul>
   *
   * <p>Typically maps to HTTP 400 Bad Request, 409 Conflict, or 422 Unprocessable Entity.
   */
  FAILURE,

  /**
   * Operation is queued but has not started processing yet.
   *
   * <p>This status indicates that the request has been accepted and placed in a processing queue,
   * but actual work has not yet begun. This is common in asynchronous processing patterns where
   * operations may need to wait for available resources or processing capacity.
   *
   * <p>Use this status for:
   *
   * <ul>
   *   <li>Batch processing jobs waiting in queue
   *   <li>Long-running operations that haven't started yet
   *   <li>Resource-intensive tasks waiting for available compute capacity
   *   <li>Scheduled operations waiting for their execution time
   *   <li>Tasks waiting for dependencies to complete
   * </ul>
   *
   * <p>Typically maps to HTTP 202 Accepted with a location header for status polling.
   */
  PENDING,

  /**
   * Operation is currently executing and making progress.
   *
   * <p>This status indicates that the operation has begun and is actively being processed. Work is
   * in progress, and the operation is expected to eventually reach a final state (success or
   * failure). This status is particularly useful for long-running operations where clients need to
   * track progress.
   *
   * <p>Use this status for:
   *
   * <ul>
   *   <li>File uploads or downloads in progress
   *   <li>Data processing or transformation tasks
   *   <li>External API integrations that take time
   *   <li>Batch operations processing multiple items
   *   <li>Workflow steps that are currently executing
   * </ul>
   *
   * <p>Typically maps to HTTP 102 Processing (WebDAV extension) or continued 202 Accepted responses
   * during polling.
   */
  PROCESSING,

  /**
   * Operation finished successfully and reached its final state.
   *
   * <p>This status is similar to {@link #SUCCESS} but specifically indicates that a multi-step or
   * long-running process has reached its completion. While {@link #SUCCESS} might be used for
   * immediate operations, {@link #COMPLETED} emphasizes that a process with multiple phases has
   * finished entirely.
   *
   * <p>Use this status for:
   *
   * <ul>
   *   <li>Multi-step workflows that have finished all phases
   *   <li>Long-running batch processes that have completed
   *   <li>Asynchronous operations that have reached their final state
   *   <li>Scheduled tasks that have finished execution
   *   <li>Complex business processes that involve multiple services
   * </ul>
   *
   * <p>Typically maps to HTTP 200 OK with the final result or 201 Created for completion of
   * resource creation processes.
   */
  COMPLETED,

  /**
   * Operation was explicitly cancelled by a client or system administrator.
   *
   * <p>This status indicates that an operation was intentionally stopped before completion, either
   * by client request or administrative action. Unlike failure states, cancellation is not due to
   * an error but rather an explicit decision to stop processing.
   *
   * <p>Use this status for:
   *
   * <ul>
   *   <li>Long-running operations cancelled by user request
   *   <li>Batch jobs stopped by administrative action
   *   <li>Scheduled tasks cancelled before execution
   *   <li>Operations cancelled due to changing business requirements
   *   <li>Tasks cancelled as part of system shutdown or maintenance
   * </ul>
   *
   * <p>Typically maps to HTTP 499 Client Closed Request or a custom 200 OK response indicating
   * successful cancellation.
   */
  CANCELLED,

  /**
   * A non-timeout error occurred during processing due to system-level issues.
   *
   * <p>This status indicates that the operation failed due to technical problems rather than
   * business logic issues. These are typically server-side errors that are not the client's fault
   * and may be retryable depending on the specific error condition.
   *
   * <p>Use this status for:
   *
   * <ul>
   *   <li>Database connectivity issues
   *   <li>External service integration failures
   *   <li>System resource exhaustion (memory, disk space)
   *   <li>Configuration errors or missing dependencies
   *   <li>Unexpected exceptions during processing
   *   <li>Network failures to dependent services
   * </ul>
   *
   * <p>Typically maps to HTTP 500 Internal Server Error, 502 Bad Gateway, or 503 Service
   * Unavailable.
   */
  ERROR,

  /**
   * Operation did not finish within the allotted time limit.
   *
   * <p>This status indicates that the operation was cancelled because it exceeded its maximum
   * allowed execution time. This can happen due to performance issues, resource contention, or
   * operations that are more complex than anticipated.
   *
   * <p>Use this status for:
   *
   * <ul>
   *   <li>Long-running operations that exceed configured time limits
   *   <li>External API calls that don't respond within timeout period
   *   <li>Database queries that take too long to execute
   *   <li>Batch processes that don't complete within allowed window
   *   <li>User-facing operations that exceed reasonable response times
   * </ul>
   *
   * <p>Typically maps to HTTP 408 Request Timeout, 504 Gateway Timeout, or 503 Service Unavailable.
   */
  TIMEOUT,

  /**
   * Operation was aborted prematurely due to a fatal condition or critical error.
   *
   * <p>This status indicates that the operation was forcibly stopped due to a serious problem that
   * made continuation impossible or dangerous. Unlike cancellation (which is intentional) or
   * regular errors (which might be recoverable), abortion indicates a critical failure that
   * requires immediate attention.
   *
   * <p>Use this status for:
   *
   * <ul>
   *   <li>Critical system errors that make continuation unsafe
   *   <li>Data corruption detected during processing
   *   <li>Security violations that require immediate termination
   *   <li>Resource exhaustion that threatens system stability
   *   <li>Fatal configuration errors discovered during execution
   *   <li>Circuit breaker activation due to cascade failures
   * </ul>
   *
   * <p>Typically maps to HTTP 500 Internal Server Error with additional alerting and logging.
   */
  ABORTED;

  private static final Logger log = LoggerFactory.getLogger(Status.class);

  /**
   * Logs the current status for debugging and monitoring purposes.
   *
   * <p>This method provides a convenient way to log status changes during request processing. The
   * log output includes the status name and can be useful for debugging workflows and monitoring
   * status transitions in asynchronous operations.
   *
   * <p>The logging is performed at DEBUG level to avoid cluttering production logs while still
   * providing valuable debugging information during development and troubleshooting.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Status status = Status.PROCESSING;
   * status.logStatus(); // Logs: "Status: PROCESSING"
   * }</pre>
   */
  public void logStatus() {
    log.debug("Status: {}", this.name());
  }

  /**
   * Determines if this status represents a final state (operation has completed or failed).
   *
   * <p>This method is useful for workflow management and polling scenarios where you need to
   * determine if an operation has reached its conclusion and no further status changes are
   * expected.
   *
   * <p>Final states include all success states, failure states, and termination states: {@link
   * #SUCCESS}, {@link #COMPLETED}, {@link #FAILURE}, {@link #ERROR}, {@link #TIMEOUT}, {@link
   * #CANCELLED}, and {@link #ABORTED}.
   *
   * <p>Non-final states are: {@link #PENDING} and {@link #PROCESSING}.
   *
   * @return {@code true} if this status represents a final state, {@code false} if the operation is
   *     still in progress
   */
  public boolean isFinal() {
    return this != PENDING && this != PROCESSING;
  }

  /**
   * Determines if this status represents a successful outcome.
   *
   * <p>This method provides a convenient way to check if an operation completed successfully,
   * regardless of whether it used {@link #SUCCESS} or {@link #COMPLETED} status.
   *
   * @return {@code true} if this status represents a successful outcome ({@link #SUCCESS} or {@link
   *     #COMPLETED}), {@code false} otherwise
   */
  public boolean isSuccess() {
    return this == SUCCESS || this == COMPLETED;
  }

  /**
   * Determines if this status represents a failure outcome.
   *
   * <p>This method provides a convenient way to check if an operation failed, covering all possible
   * failure scenarios including business logic failures, system errors, timeouts, cancellations,
   * and aborted operations.
   *
   * @return {@code true} if this status represents any type of failure ({@link #FAILURE}, {@link
   *     #ERROR}, {@link #TIMEOUT}, {@link #CANCELLED}, or {@link #ABORTED}), {@code false}
   *     otherwise
   */
  public boolean isFailure() {
    return this == FAILURE
        || this == ERROR
        || this == TIMEOUT
        || this == CANCELLED
        || this == ABORTED;
  }

  /**
   * Determines if this status represents an operation that is currently in progress.
   *
   * <p>This method is useful for determining if an operation is still active and may transition to
   * a different status in the future. In-progress statuses require continued monitoring until they
   * reach a final state.
   *
   * @return {@code true} if this status represents an in-progress operation ({@link #PENDING} or
   *     {@link #PROCESSING}), {@code false} otherwise
   */
  public boolean isInProgress() {
    return this == PENDING || this == PROCESSING;
  }
}
