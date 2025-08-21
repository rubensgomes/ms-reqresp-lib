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

/**
 * Microservices Request-Response Library for Enterprise Applications.
 *
 * <p>This library provides a comprehensive framework for standardizing request and response
 * handling across microservices architectures. It offers consistent data transfer objects, error
 * handling mechanisms, and status management to ensure uniform communication patterns throughout
 * distributed systems.
 *
 * <h2>Library Overview</h2>
 *
 * <p>The ms-reqresp-lib is designed to solve common challenges in microservices communication:
 *
 * <ul>
 *   <li><strong>Standardized Request/Response Patterns:</strong> Consistent DTOs for all service
 *       communications
 *   <li><strong>Unified Error Handling:</strong> Hierarchical error codes and standardized error
 *       responses
 *   <li><strong>Status Management:</strong> Common status enumerations for operation results
 *   <li><strong>Type Safety:</strong> Strong typing for all communication contracts
 *   <li><strong>Documentation:</strong> Comprehensive JavaDoc for all public APIs
 * </ul>
 *
 * <h2>Core Packages</h2>
 *
 * <h3>{@link com.rubensgomes.msreqresplib.dto}</h3>
 *
 * <p>Data Transfer Objects for request and response handling:
 *
 * <ul>
 *   <li>{@link com.rubensgomes.msreqresplib.dto.BaseRequest} - Foundation for all request DTOs
 *   <li>{@link com.rubensgomes.msreqresplib.dto.BaseResponse} - Foundation for all response DTOs
 *   <li>{@link com.rubensgomes.msreqresplib.dto.ApplicationErrorResponse} - Standardized error
 *       responses
 * </ul>
 *
 * <h3>{@link com.rubensgomes.msreqresplib.error}</h3>
 *
 * <p>Comprehensive error handling framework:
 *
 * <ul>
 *   <li>{@link com.rubensgomes.msreqresplib.error.ErrorCode} - Interface for error code contracts
 *   <li>{@link com.rubensgomes.msreqresplib.error.ApplicationErrorCode} - Standard error codes
 *   <li>{@link com.rubensgomes.msreqresplib.error.Error} - Error data structures
 * </ul>
 *
 * <h2>Key Features</h2>
 *
 * <h3>Hierarchical Error Codes</h3>
 *
 * <p>The library implements a sophisticated error code system with:
 *
 * <ul>
 *   <li><strong>Generic Codes (XXXGN###):</strong> Apply across all microservices
 *   <li><strong>Service-Specific Codes (XXXMS###):</strong> Unique to individual services
 *   <li><strong>Categorized Errors:</strong> Business, Payment, Resource, Security, System,
 *       Validation
 *   <li><strong>Machine and Human Readable:</strong> Both programmatic handling and user display
 * </ul>
 *
 * <h3>Request/Response Standardization</h3>
 *
 * <p>All communication follows consistent patterns:
 *
 * <ul>
 *   <li><strong>Base Classes:</strong> Common structure for all requests and responses
 *   <li><strong>Validation Support:</strong> Built-in validation annotations
 *   <li><strong>Metadata Support:</strong> Correlation IDs, timestamps, and other metadata
 *   <li><strong>Extensibility:</strong> Easy to extend for specific service needs
 * </ul>
 *
 * <h2>Usage Examples</h2>
 *
 * <h3>Basic Request/Response</h3>
 *
 * <pre>{@code
 * // Creating a request
 * MyServiceRequest request = MyServiceRequest.builder()
 *     .correlationId(UUID.randomUUID().toString())
 *     .requestData(data)
 *     .build();
 *
 * // Creating a successful response
 * MyServiceResponse response = MyServiceResponse.builder()
 *     .correlationId(request.getCorrelationId())
 *     .status(Status.SUCCESS)
 *     .responseData(result)
 *     .build();
 * }</pre>
 *
 * <h3>Error Response Handling</h3>
 *
 * <pre>{@code
 * // Creating an error response
 * ApplicationErrorResponse errorResponse = ApplicationErrorResponse.builder()
 *     .correlationId(request.getCorrelationId())
 *     .status(Status.ERROR)
 *     .errorCode(ApplicationErrorCode.VALIDATION_REQUIRED_FIELD.getCode())
 *     .errorMessage(ApplicationErrorCode.VALIDATION_REQUIRED_FIELD.getDescription())
 *     .timestamp(Instant.now())
 *     .build();
 * }</pre>
 *
 * <h3>Error Code Usage</h3>
 *
 * <pre>{@code
 * // Using error codes programmatically
 * ApplicationErrorCode errorCode = ApplicationErrorCode.RESOURCE_NOT_FOUND;
 *
 * // Category-based handling
 * if (errorCode.getCode().startsWith("RESGN")) {
 *     handleResourceError(errorCode);
 * }
 * }</pre>
 *
 * <h2>Integration Guidelines</h2>
 *
 * <p>To integrate this library into your microservices:
 *
 * <ol>
 *   <li><strong>Extend Base Classes:</strong> Create service-specific request/response DTOs by
 *       extending {@link com.rubensgomes.msreqresplib.dto.BaseRequest} and {@link
 *       com.rubensgomes.msreqresplib.dto.BaseResponse}
 *   <li><strong>Use Standard Error Codes:</strong> Leverage existing error codes or add
 *       service-specific ones following the naming conventions
 *   <li><strong>Implement Consistent Patterns:</strong> Follow the established patterns for
 *       request/response handling across all endpoints
 *   <li><strong>Document Extensions:</strong> Document any service-specific extensions or
 *       customizations
 * </ol>
 *
 * <h2>Version Information</h2>
 *
 * <p>This library follows semantic versioning. Current major features:
 *
 * <ul>
 *   <li><strong>0.0.1:</strong> Initial release with basic DTOs and error handling
 *   <li><strong>0.0.2:</strong> Enhanced error codes with hierarchical naming and improved
 *       documentation
 * </ul>
 *
 * @author Rubens Gomes
 * @version 0.0.2
 * @since 0.0.1
 * @see com.rubensgomes.msreqresplib.dto
 * @see com.rubensgomes.msreqresplib.error
 */
package com.rubensgomes.msreqresplib;
