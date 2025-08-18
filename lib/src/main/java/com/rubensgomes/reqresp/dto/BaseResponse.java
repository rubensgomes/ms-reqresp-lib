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
import lombok.Data;

/**
 * Base response DTO containing common fields shared by service responses.
 *
 * <p>Provides identifiers for tracing, a {@link Status} to indicate the outcome, and optional
 * diagnostic context for failures.
 *
 * <p>Validation annotations declare required fields and can be enforced during request/response
 * handling.
 *
 * @author Rubens Gomes
 */
@Data
public abstract class BaseResponse {
  /** Identifier of the client application originating the request. */
  @NotBlank(message = "clientId is required")
  private String clientId;

  /** Correlation identifier used to trace a request across systems. */
  @NotBlank(message = "transactionId is required")
  private String transactionId;

  /** Overall processing outcome for this response. */
  @NotNull(message = "status is required")
  private Status status;

  /** Optional human-readable message providing additional context. */
  private String message;

  /** Optional root cause or debugging detail when the response indicates an error. */
  private String rootCause;
}
