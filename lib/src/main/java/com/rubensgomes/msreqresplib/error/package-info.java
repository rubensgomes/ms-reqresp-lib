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
 * Error handling and error code management for microservices applications.
 *
 * <p>This package provides a comprehensive error handling framework designed for enterprise
 * microservices architectures. It includes standardized error codes, error interfaces, and error
 * data structures that facilitate consistent error handling across distributed systems.
 *
 * <h2>Key Components</h2>
 *
 * <h3>Error Codes</h3>
 *
 * <p>The package defines a hierarchical error code system with the following characteristics:
 *
 * <ul>
 *   <li><strong>Standardized Categories:</strong> Business, Payment, Resource, Security, System,
 *       and Validation errors
 *   <li><strong>Generic vs. Specific:</strong> Generic error codes (XXXGN###) that apply across all
 *       services, and service-specific codes (XXXMS###) for individual microservices
 *   <li><strong>Machine-Readable:</strong> Unique identifiers for programmatic error handling
 *   <li><strong>Human-Readable:</strong> Clear descriptions suitable for end-user display
 * </ul>
 *
 * <h3>Error Interfaces</h3>
 *
 * <p>The {@link com.rubensgomes.msreqresplib.error.ErrorCode} interface provides a contract for all
 * error codes, ensuring consistency in error code implementation across the application.
 *
 * <h3>Error Data Structures</h3>
 *
 * <p>The {@link com.rubensgomes.msreqresplib.error.Error} class provides a standard structure for
 * representing errors in API responses and internal error handling.
 *
 * <h2>Usage Patterns</h2>
 *
 * <h3>Basic Error Code Usage</h3>
 *
 * <pre>{@code
 * ApplicationErrorCode errorCode = ApplicationErrorCode.VALIDATION_REQUIRED_FIELD;
 * String code = errorCode.getCode();        // Returns "VALGN001"
 * String message = errorCode.getDescription(); // Returns user-friendly message
 * }</pre>
 *
 * <h3>Category-Based Error Handling</h3>
 *
 * <pre>{@code
 * String code = errorCode.getCode();
 * if (code.startsWith("VALGN")) {
 *     // Handle all validation errors
 *     handleValidationError(errorCode);
 * } else if (code.startsWith("SECGN")) {
 *     // Handle all security errors
 *     handleSecurityError(errorCode);
 * }
 * }</pre>
 *
 * <h3>Generic vs. Service-Specific Handling</h3>
 *
 * <pre>{@code
 * String code = errorCode.getCode();
 * if (code.contains("GN")) {
 *     // Handle generic errors that apply across all services
 *     handleGenericError(errorCode);
 * } else if (code.contains("MS")) {
 *     // Handle microservice-specific errors
 *     handleServiceSpecificError(errorCode);
 * }
 * }</pre>
 *
 * <h2>Extension Guidelines</h2>
 *
 * <p>When extending this error handling framework:
 *
 * <ul>
 *   <li><strong>Follow Naming Conventions:</strong> Use XXXGN### for generic errors and XXXMS###
 *       for service-specific errors
 *   <li><strong>Maintain Categories:</strong> Use established categories (BUS, PAY, RES, SEC, SYS,
 *       VAL) or define new ones consistently
 *   <li><strong>Provide Clear Descriptions:</strong> Ensure error descriptions are user-friendly
 *       and actionable
 *   <li><strong>Document New Patterns:</strong> Update package documentation when introducing new
 *       error handling patterns
 * </ul>
 *
 * <h2>Integration with Response Framework</h2>
 *
 * <p>This package integrates seamlessly with the DTO framework provided in {@link
 * com.rubensgomes.msreqresplib.dto}, allowing for consistent error responses across all
 * microservices in the architecture.
 *
 * @author Rubens Gomes
 * @since 0.0.2
 * @see com.rubensgomes.msreqresplib.error.ErrorCode
 * @see com.rubensgomes.msreqresplib.error.ApplicationErrorCode
 * @see com.rubensgomes.msreqresplib.error.Error
 * @see com.rubensgomes.msreqresplib.dto
 */
package com.rubensgomes.msreqresplib.error;
