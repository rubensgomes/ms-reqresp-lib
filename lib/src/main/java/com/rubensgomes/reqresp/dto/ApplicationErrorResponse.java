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
package com.rubensgomes.reqresp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Application-specific error response extending the base response functionality.
 *
 * <p>This response class is specifically designed for error scenarios where the application needs
 * to communicate detailed error information back to the client. It extends {@link BaseResponse} and
 * enforces that both a descriptive message and error details are provided when constructing error
 * responses.
 *
 * <p>Unlike the base response where message and error are optional, this class requires both fields
 * to be present, ensuring that error responses always contain sufficient information for
 * client-side error handling and debugging.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * Error error = new DatabaseError("Connection timeout", "SQLException: timeout", "DB_001");
 * ApplicationErrorResponse response = new ApplicationErrorResponse(
 *     "client-app",
 *     "txn-12345",
 *     Status.FAILURE,
 *     "Database operation failed",
 *     error
 * );
 * }</pre>
 *
 * @author Rubens Gomes
 * @see BaseResponse
 * @see Error
 * @see Status
 */
public class ApplicationErrorResponse extends BaseResponse {

  /**
   * Constructs an ApplicationErrorResponse with all required fields for error reporting.
   *
   * <p>This constructor enforces validation constraints to ensure that error responses always
   * contain complete information necessary for proper error handling. All parameters except for
   * optional fields in the parent class are required and validated.
   *
   * <p>The validation constraints ensure:
   *
   * <ul>
   *   <li>clientId and transactionId are not null, empty, or whitespace-only
   *   <li>status and error objects are not null
   *   <li>message is not null, empty, or whitespace-only
   * </ul>
   *
   * @param clientId the identifier of the client application originating the request. Must not be
   *     null, empty, or contain only whitespace.
   * @param transactionId the correlation identifier used to trace a request across systems. Must
   *     not be null, empty, or contain only whitespace.
   * @param status the overall processing outcome for this response. Must not be null. Typically
   *     {@link Status#FAILURE} for error responses.
   * @param message a human-readable message describing the error condition. Must not be null,
   *     empty, or contain only whitespace.
   * @param error detailed error information including error codes, native error text, and
   *     additional diagnostic information. Must not be null.
   * @throws jakarta.validation.ConstraintViolationException if any validation constraints are
   *     violated
   * @see BaseResponse#setClientId(String)
   * @see BaseResponse#setTransactionId(String)
   * @see BaseResponse#setStatus(Status)
   * @see BaseResponse#setMessage(String)
   * @see BaseResponse#setError(Error)
   */
  public ApplicationErrorResponse(
      @NotBlank String clientId,
      @NotBlank String transactionId,
      @NotNull Status status,
      @NotBlank String message,
      @NotNull Error error) {
    this.setClientId(clientId);
    this.setTransactionId(transactionId);
    this.setStatus(status);
    this.setMessage(message);
    this.setError(error);
  }
}
