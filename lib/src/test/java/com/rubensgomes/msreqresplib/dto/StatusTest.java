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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class StatusTest {

  @Test
  void values_shouldContainExpectedConstants() {
    log.debug("Testing Status enum values");
    assertNotNull(Status.valueOf("SUCCESS"));
    assertNotNull(Status.valueOf("FAILURE"));
  }

  @Test
  void toString_shouldReturnName() {
    log.debug("Testing Status toString method");
    assertEquals("SUCCESS", Status.SUCCESS.toString());
  }
}
