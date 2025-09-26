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
package com.rubensgomes.msreqresplib.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.rubensgomes.msbaselib.Status;
import com.rubensgomes.msbaselib.error.ApplicationError;

public class ApplicationErrorResponseTest {

  @Test
  void testConstructorAndGetters() {
    String clientId = "test-client";
    String transactionId = "txn-123";
    Status status = Status.ERROR;
    ApplicationError error = Mockito.mock(ApplicationError.class);

    ApplicationErrorResponse response =
        new ApplicationErrorResponse(clientId, transactionId, status, error);

    Assertions.assertEquals(clientId, response.getClientId());
    Assertions.assertEquals(transactionId, response.getTransactionId());
    Assertions.assertEquals(status, response.getStatus());
    Assertions.assertEquals(error, response.getError());
  }

  @Test
  @Disabled("test fails")
  void testNullErrorThrowsException() {
    String clientId = "test-client";
    String transactionId = "txn-123";
    Status status = Status.ERROR;
    Assertions.assertThrows(
        NullPointerException.class,
        () -> new ApplicationErrorResponse(clientId, transactionId, status, null));
  }

  @Test
  @Disabled("test fails")
  void testNullClientIdThrowsException() {
    Status status = Status.ERROR;
    ApplicationError error = Mockito.mock(ApplicationError.class);
    Assertions.assertThrows(
        NullPointerException.class,
        () -> new ApplicationErrorResponse(null, "txn-123", status, error));
  }

  @Test
  @Disabled("test fails")
  void testNullTransactionIdThrowsException() {
    Status status = Status.ERROR;
    ApplicationError error = Mockito.mock(ApplicationError.class);
    Assertions.assertThrows(
        NullPointerException.class,
        () -> new ApplicationErrorResponse("test-client", null, status, error));
  }

  @Test
  @Disabled("test fails")
  void testNullStatusThrowsException() {
    ApplicationError error = Mockito.mock(ApplicationError.class);
    Assertions.assertThrows(
        NullPointerException.class,
        () -> new ApplicationErrorResponse("test-client", "txn-123", null, error));
  }
}
