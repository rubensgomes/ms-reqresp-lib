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
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Base request DTO containing common identifiers shared by service requests.
 *
 * <p>Provides fields used for correlation and traceability across microservices.
 *
 * @author Rubens Gomes
 */
@Data
@Slf4j
public abstract class BaseRequest {
  /** Identifier of the client application originating the request. */
  @NotBlank(message = "clientId is required")
  private String clientId;

  /** Correlation identifier used to trace a request across systems. */
  @NotBlank(message = "transactionId is required")
  private String transactionId;

  /** Logs the request details for debugging and tracing purposes. */
  public void logRequest() {
    log.debug("Request - clientId: {}, transactionId: {}", clientId, transactionId);
  }
}
