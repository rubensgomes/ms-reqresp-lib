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

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

/**
 * Represents an error returned by the system, providing details such as message, native error text,
 * and error code.
 *
 * @author Rubens Gomes
 */
public interface Error {
  /**
   * Returns a human-readable error message describing the error.
   *
   * @return the error message
   */
  @NotBlank
  String getErrorMessage();

  /**
   * Returns the native error text, which may include system-specific details.
   *
   * @return the native error text
   */
  @Nullable
  String getNativeErrorText();

  /**
   * Returns the error code associated with this error.
   *
   * @return the error code
   */
  @Nullable
  String getErrorCode();
}
