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

import com.rubensgomes.msreqresplib.error.Error;
import com.rubensgomes.msreqresplib.error.ErrorCode;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data transfer object representing an application error.
 *
 * <p>This record implements the {@link Error} interface and provides a concrete representation of
 * errors that occur within the application. It encapsulates error details including a
 * human-readable description, standardized error code, and optional native error text from
 * underlying systems.
 *
 * <p>As a record, this class is immutable by design and automatically provides implementations for
 * {@code equals()}, {@code hashCode()}, {@code toString()}, and accessor methods for all
 * components. The record also implements the {@link Error} interface methods by delegating to the
 * appropriate record components.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * ErrorCode code = ApplicationErrorCodes.VALIDATION_REQUIRED_FIELD;
 * ApplicationError error = new ApplicationError(
 *     "Username is required",
 *     code,
 *     "Field validation failed: username cannot be null or empty"
 * );
 *
 * // Access error details
 * String description = error.errorDescription();
 * ErrorCode errorCode = error.errorCode();
 * String nativeText = error.nativeErrorText();
 * }</pre>
 *
 * @param errorDescription Human-readable description of the error. Must not be null, empty, or
 *     contain only whitespace characters.
 * @param errorCode Standardized error code categorizing the type of error. Must not be null.
 * @param nativeErrorText Optional native error message from underlying systems or APIs. May be null
 *     when no native error information is available.
 * @author Rubens Gomes
 * @since 0.0.2
 * @see Error
 * @see ErrorCode
 * @see jakarta.validation.constraints.NotBlank
 * @see jakarta.validation.constraints.NotNull
 * @see jakarta.annotation.Nullable
 */
public record ApplicationError(
    @NotBlank String errorDescription,
    @NotNull ErrorCode errorCode,
    @Nullable String nativeErrorText)
    implements Error {}
