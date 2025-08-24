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
package com.rubensgomes.msreqresplib.dto

import com.rubensgomes.msreqresplib.error.Error
import com.rubensgomes.msreqresplib.error.ErrorCode
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

/**
 * Data transfer object representing an application error.
 *
 * This class implements the [Error] interface and provides a concrete representation of errors that
 * occur within the application. It encapsulates error details including a human-readable
 * description, standardized error code, and optional native error text from underlying systems.
 *
 * @property errorDescription Human-readable description of the error
 * @property errorCode Standardized error code categorizing the type of error
 * @property nativeErrorText Optional native error message from underlying systems or APIs
 * @constructor Creates an ApplicationError with the specified error details
 * @author Rubens Gomes
 * @since 0.0.2
 */
class ApplicationError(
    val errorDescription: String,
    val errorCode: ErrorCode,
    val nativeErrorText: String?
) : Error {

  /**
   * Returns the human-readable error description.
   *
   * @return the error description, never null or blank
   */
  override fun getErrorDescription(): @NotBlank String? {
    return errorDescription
  }

  /**
   * Returns the native error text from underlying systems.
   *
   * @return the native error text, or null if not available
   */
  override fun getNativeErrorText(): String? {
    return nativeErrorText
  }

  /**
   * Returns the standardized error code.
   *
   * @return the error code categorizing this error, never null
   */
  override fun getErrorCode(): @NotNull ErrorCode? {
    return errorCode
  }
}
