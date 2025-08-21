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

/**
 * Represents the lifecycle or outcome of processing a request/response.
 *
 * <p>This enum is intended for use in DTOs to communicate processing state in a concise and
 * consistent way across services.
 *
 * @author Rubens Gomes
 */
public enum Status {
  /** Operation completed successfully. */
  SUCCESS,
  /** Operation failed and could not be completed. */
  FAILURE,
  /** Operation is queued but has not started yet. */
  PENDING,
  /** Operation is currently executing. */
  PROCESSING,
  /** Operation finished (may be synonymous with SUCCESS for some flows). */
  COMPLETED,
  /** Operation was explicitly cancelled by a client or system. */
  CANCELLED,
  /** A non-timeout error occurred during processing. */
  ERROR,
  /** Operation did not finish within the allotted time. */
  TIMEOUT,
  /** Operation was aborted prematurely due to a fatal condition. */
  ABORTED;

  private static final Logger log = LoggerFactory.getLogger(Status.class);

  /** Logs the status for debugging purposes. */
  public void logStatus() {
    log.debug("Status: {}", this.name());
  }
}
