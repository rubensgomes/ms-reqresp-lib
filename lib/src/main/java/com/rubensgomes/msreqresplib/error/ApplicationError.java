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
package com.rubensgomes.msreqresplib.error;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Data transfer object representing an application error with comprehensive error information.
 *
 * <p>This class implements the {@link Error} interface and provides a concrete, immutable
 * representation of errors that occur within the application. It encapsulates error details
 * including a human-readable description, standardized error code, and optional native error text
 * from underlying systems or APIs.
 *
 * <p>The class follows a hybrid immutability pattern where core error information (errorDescription
 * and errorCode) is immutable once set during construction, while the native error text can be
 * updated after creation. This design supports scenarios where additional diagnostic information
 * becomes available during error processing or propagation.
 *
 * <p><strong>Thread Safety:</strong> This class is thread-safe for read operations on immutable
 * fields, but not thread-safe for concurrent modifications of the native error text. External
 * synchronization is required if multiple threads need to update the native error text.
 *
 * <p><strong>Validation:</strong> The class uses Jakarta Bean Validation annotations to enforce
 * data integrity constraints:
 *
 * <ul>
 *   <li>Error description must be non-null, non-empty, and contain non-whitespace characters
 *   <li>Error code must be non-null
 *   <li>Native error text is optional and may be null
 * </ul>
 *
 * <p><strong>Serialization:</strong> This class is suitable for JSON serialization and can be used
 * as a response body in REST APIs or message payloads in messaging systems.
 *
 * <p><strong>Lombok Features:</strong> This class uses Lombok's {@code @Data} annotation which
 * automatically generates:
 *
 * <ul>
 *   <li>Getter methods for all fields
 *   <li>Setter methods for non-final fields
 *   <li>equals() and hashCode() methods
 *   <li>toString() method
 *   <li>Constructor for final fields
 * </ul>
 *
 * <p>Example usage scenarios:
 *
 * <pre>{@code
 * // Basic error creation
 * ErrorCode validationCode = ApplicationErrorCodes.VALIDATION_REQUIRED_FIELD;
 * ApplicationError error = new ApplicationError(
 *     "Username is required",
 *     validationCode
 * );
 *
 * // Error with native system details
 * ErrorCode dbCode = ApplicationErrorCodes.DATABASE_CONNECTION_FAILED;
 * ApplicationError dbError = new ApplicationError(
 *     "Database connection failed",
 *     dbCode
 * );
 * dbError.setNativeErrorText("SQLException: Connection refused at localhost:5432");
 *
 * // Access error details using Lombok-generated getters
 * String description = error.getErrorDescription();  // "Username is required"
 * String codeId = error.getErrorCode().getCode();    // "VALGN001"
 * String nativeText = error.getNativeErrorText();    // May be null
 * }</pre>
 *
 * @author Rubens Gomes
 * @since 0.0.2
 * @see Error
 * @see ErrorCode
 * @see jakarta.validation.constraints.NotBlank
 * @see jakarta.validation.constraints.NotNull
 * @see jakarta.annotation.Nullable
 * @see lombok.Data
 */
@Data
public class ApplicationError implements Error {

  /**
   * Human-readable description of the error.
   *
   * <p>This field contains the primary error message that should be displayed to users or included
   * in error responses. The description is immutable once set during construction and must not be
   * null, empty, or contain only whitespace characters.
   */
  @NotBlank private final String errorDescription;

  /**
   * Standardized error code categorizing the type of error.
   *
   * <p>This field contains a structured {@link ErrorCode} object that provides both a unique
   * identifier and human-readable description for comprehensive error handling. The error code is
   * immutable once set during construction and must not be null.
   */
  @NotNull private final ErrorCode errorCode;

  /**
   * Optional native error message from underlying systems or APIs.
   *
   * <p>This field contains system-specific diagnostic information that may be useful for debugging
   * or logging purposes. Unlike the core error fields, this can be updated after construction using
   * the Lombok-generated setter method. May be null when no native error information is available.
   */
  @Nullable private String nativeErrorText;
}
