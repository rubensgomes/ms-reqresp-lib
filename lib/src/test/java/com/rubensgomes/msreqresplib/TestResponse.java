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
package com.rubensgomes.msreqresplib;

import java.util.List;
import java.util.Map;

import com.rubensgomes.msbaselib.Status;
import com.rubensgomes.msbaselib.error.ApplicationError;

public class TestResponse extends BaseResponse {

  private String name;
  private String job;
  private String id;
  private String createdAt;
  private List<String> error;
  private Map<String, String> meta;

  public TestResponse(String clientId, String transactionId, Status status) {
    super(clientId, transactionId, status);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getJob() {
    return job;
  }

  public void setJob(String job) {
    this.job = job;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public ApplicationError getError() {
    return (ApplicationError) error;
  }

  public void setError(List<String> error) {
    this.error = error;
  }

  public Map<String, String> getMeta() {
    return meta;
  }

  public void setMeta(Map<String, String> meta) {
    this.meta = meta;
  }
}
