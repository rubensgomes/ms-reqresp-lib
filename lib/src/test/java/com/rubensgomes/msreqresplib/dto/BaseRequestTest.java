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

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class BaseRequestTest {

  private static Validator validator;

  @BeforeAll
  static void setupValidator() {
    log.debug("Setting up validation factory");
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Getter
  @Setter
  static class TestRequest extends BaseRequest {}

  @Test
  void validation_shouldFail_whenRequiredFieldsMissing() {
    log.debug("Testing validation failure with missing required fields");
    TestRequest req = new TestRequest();
    Set<ConstraintViolation<TestRequest>> violations = validator.validate(req);
    assertFalse(violations.isEmpty());
    log.debug("Validation failed with {} violations", violations.size());
  }

  @Test
  void validation_shouldPass_whenRequiredFieldsPresent() {
    log.debug("Testing validation success with all required fields");
    TestRequest req = new TestRequest();
    req.setClientId("c1");
    req.setTransactionId("t1");
    Set<ConstraintViolation<TestRequest>> violations = validator.validate(req);
    assertTrue(violations.isEmpty());
    log.debug("Validation passed successfully");
  }
}
