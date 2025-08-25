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
 * <p>This package provides a comprehensive framework for standardizing error handling across
 * microservices architectures. It defines structured error codes, error data models, and
 * standardized error reporting mechanisms that ensure consistent error communication between
 * services and to end users.
 *
 * <h2>Package Overview</h2>
 *
 * <p>The error handling framework is built on two core components that work together to provide a
 * complete error management solution:
 *
 * <ul>
 *   <li><strong>Error Code Contracts:</strong> Standardized interfaces for defining error
 *       identifiers with both machine-readable codes and human-readable descriptions
 *   <li><strong>Error Data Structures:</strong> Comprehensive error information containers that
 *       support both structured error reporting and diagnostic information
 * </ul>
 *
 * <h2>Core Components</h2>
 *
 * <h3>{@link com.rubensgomes.msreqresplib.error.ErrorCode}</h3>
 *
 * <p>Defines the fundamental contract for all error codes in the system. This interface ensures
 * that every error code provides:
 *
 * <ul>
 *   <li><strong>Unique Identifier:</strong> A machine-readable code for programmatic handling
 *   <li><strong>Human Description:</strong> Clear, user-friendly error message
 *   <li><strong>Validation Constraints:</strong> Both fields are mandatory and must be non-blank
 * </ul>
 *
 * <p>Key features:
 *
 * <ul>
 *   <li>Supports internationalization through code-based message lookup
 *   <li>Enables consistent error handling across all microservices
 *   <li>Provides inline documentation through descriptive text
 *   <li>Facilitates automated error processing and routing
 * </ul>
 *
 * <h3>{@link com.rubensgomes.msreqresplib.error.Error}</h3>
 *
 * <p>Defines the structure for comprehensive error information that can be returned by system
 * operations. This interface provides both required and optional fields to support various error
 * reporting scenarios:
 *
 * <ul>
 *   <li><strong>Required Fields:</strong> Error description and structured error code
 *   <li><strong>Optional Fields:</strong> Native error text for system-specific diagnostic
 *       information
 *   <li><strong>Validation Support:</strong> Built-in constraints ensure data integrity
 * </ul>
 *
 * <p>The Error interface uses standard Java getter method patterns for accessing error information:
 *
 * <ul>
 *   <li>{@code getErrorDescription()} - Returns the human-readable error description
 *   <li>{@code getErrorCode()} - Returns the structured ErrorCode object
 *   <li>{@code getNativeErrorText()} - Returns optional native error information
 *   <li>{@code setNativeErrorText(String)} - Updates the optional native error text
 * </ul>
 *
 * <h2>Design Principles</h2>
 *
 * <h3>Consistency and Standardization</h3>
 *
 * <p>All error handling follows consistent patterns:
 *
 * <ul>
 *   <li><strong>Uniform Structure:</strong> All errors follow the same data model
 *   <li><strong>Predictable Interface:</strong> Error interfaces use consistent method naming
 *   <li><strong>Standard Responses:</strong> Error responses have consistent format across services
 *   <li><strong>Validation Integration:</strong> All error components are validated for integrity
 * </ul>
 *
 * <h3>Modern Java and Framework Integration</h3>
 *
 * <p>The framework leverages modern Java features and popular frameworks:
 *
 * <ul>
 *   <li><strong>Interface-Based Design:</strong> Clean interfaces that support multiple
 *       implementation strategies including traditional classes, Lombok-enhanced classes, and Java
 *       records
 *   <li><strong>Lombok Support:</strong> Error implementations can use Lombok annotations for
 *       boilerplate code reduction while maintaining full interface compatibility
 *   <li><strong>Annotation-Driven Validation:</strong> Uses Jakarta Bean Validation annotations for
 *       comprehensive data integrity checks
 *   <li><strong>Type Safety:</strong> Strong typing prevents runtime errors and provides excellent
 *       IDE support
 *   <li><strong>Framework Integration:</strong> Seamless integration with Spring Boot, Jackson, and
 *       other modern Java frameworks
 * </ul>
 *
 * <h3>Extensibility and Flexibility</h3>
 *
 * <p>The framework supports growth and customization:
 *
 * <ul>
 *   <li><strong>Custom Error Codes:</strong> Services can implement their own ErrorCode types using
 *       enums or classes
 *   <li><strong>Custom Error Implementations:</strong> Services can implement custom error data
 *       structures while maintaining interface compatibility
 *   <li><strong>Backward Compatibility:</strong> New error implementations don't break existing
 *       error handling
 *   <li><strong>Framework Integration:</strong> Works seamlessly with Spring Boot, Jakarta EE, and
 *       other modern frameworks
 * </ul>
 *
 * <h3>Developer Experience</h3>
 *
 * <p>The framework prioritizes ease of use:
 *
 * <ul>
 *   <li><strong>Type Safety:</strong> Strong typing prevents runtime errors
 *   <li><strong>IDE Support:</strong> Interface-based design provides autocomplete and
 *       comprehensive documentation
 *   <li><strong>Clear Documentation:</strong> Extensive Javadoc for all components with examples
 *   <li><strong>Validation Feedback:</strong> Immediate feedback on invalid error data through
 *       Jakarta validation
 *   <li><strong>Record Compatibility:</strong> Modern Java records work seamlessly with interfaces
 *       through component name matching
 * </ul>
 *
 * <h2>Usage Examples</h2>
 *
 * <h3>Implementing Custom Error Codes</h3>
 *
 * <pre>{@code
 * // Simple error code implementation using enum
 * public enum ApplicationErrorCodes implements ErrorCode {
 *     VALIDATION_REQUIRED_FIELD("VALGN001", "Required field is missing"),
 *     VALIDATION_INVALID_FORMAT("VALGN002", "Invalid field format"),
 *     SECURITY_AUTHENTICATION_FAILED("SECGN001", "Authentication failed"),
 *     SYSTEM_DATABASE_ERROR("SYSGN001", "Database connection failed");
 *
 *     private final String code;
 *     private final String description;
 *
 *     ApplicationErrorCodes(String code, String description) {
 *         this.code = code;
 *         this.description = description;
 *     }
 *
 *     @Override
 *     public String getCode() {
 *         return code;
 *     }
 *
 *     @Override
 *     public String getDescription() {
 *         return description;
 *     }
 * }
 *
 * // Alternative class-based implementation
 * public class CustomErrorCode implements ErrorCode {
 *     private final String code;
 *     private final String description;
 *
 *     public CustomErrorCode(String code, String description) {
 *         this.code = code;
 *         this.description = description;
 *     }
 *
 *     @Override
 *     public String getCode() { return code; }
 *
 *     @Override
 *     public String getDescription() { return description; }
 * }
 * }</pre>
 *
 * <h3>Creating Error Objects with Different Approaches</h3>
 *
 * <pre>{@code
 * // Using ApplicationError (Lombok-enhanced class)
 * ErrorCode errorCode = ApplicationErrorCodes.VALIDATION_REQUIRED_FIELD;
 * ApplicationError lomokError = new ApplicationError(
 *     "Username is required for account creation",
 *     errorCode,
 *     "Validation failed on field: username"
 * );
 *
 * // Access error information using generated getters
 * String description = lomokError.getErrorDescription();
 * String code = lomokError.getErrorCode().getCode();
 * String nativeText = lomokError.getNativeErrorText();
 *
 * // Update native error text using generated setter
 * lomokError.setNativeErrorText("Field validation failed: username cannot be null or empty");
 *
 * // Using Java record implementation (if preferred)
 * public record RecordError(
 *     @NotBlank String errorDescription,
 *     @NotNull ErrorCode errorCode,
 *     @Nullable String nativeErrorText
 * ) implements Error {
 *
 *     @Override
 *     public String getErrorDescription() { return errorDescription; }
 *
 *     @Override
 *     public ErrorCode getErrorCode() { return errorCode; }
 *
 *     @Override
 *     public String getNativeErrorText() { return nativeErrorText; }
 *
 *     @Override
 *     public void setNativeErrorText(String nativeErrorText) {
 *         // Records are immutable, so this would need special handling
 *         throw new UnsupportedOperationException("Records are immutable");
 *     }
 * }
 * }</pre>
 *
 * <h3>Service Integration and Error Handling</h3>
 *
 * <pre>{@code
 * @Service
 * public class UserService {
 *
 *     public User createUser(CreateUserRequest request) throws ServiceException {
 *         try {
 *             // Validate input
 *             if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
 *                 ErrorCode validationCode = ApplicationErrorCodes.VALIDATION_REQUIRED_FIELD;
 *                 ApplicationError error = new ApplicationError(
 *                     "Username is required",
 *                     validationCode,
 *                     "Field validation failed: username cannot be null or empty"
 *                 );
 *                 throw new ServiceException(error);
 *             }
 *
 *             // Process user creation
 *             return userRepository.save(new User(request.getUsername()));
 *
 *         } catch (DatabaseException e) {
 *             ErrorCode dbCode = ApplicationErrorCodes.SYSTEM_DATABASE_ERROR;
 *             ApplicationError error = new ApplicationError(
 *                 "User creation failed due to system error",
 *                 dbCode,
 *                 e.getMessage()
 *             );
 *             throw new ServiceException(error);
 *         }
 *     }
 * }
 *
 * // Custom exception that carries Error information
 * public class ServiceException extends Exception {
 *     private final Error error;
 *
 *     public ServiceException(Error error) {
 *         super(error.getErrorDescription());
 *         this.error = error;
 *     }
 *
 *     public Error getError() { return error; }
 * }
 * }</pre>
 *
 * <h3>REST Controller Error Handling</h3>
 *
 * <pre>{@code
 * @RestController
 * @RequestMapping("/api/users")
 * public class UserController {
 *
 *     @Autowired
 *     private UserService userService;
 *
 *     @PostMapping
 *     public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest request) {
 *         try {
 *             User user = userService.createUser(request);
 *             BaseResponse successResponse = new BaseResponse(
 *                 request.getClientId(),
 *                 request.getTransactionId(),
 *                 Status.SUCCESS
 *             );
 *             return ResponseEntity.ok(successResponse);
 *
 *         } catch (ServiceException e) {
 *             ApplicationErrorResponse errorResponse = new ApplicationErrorResponse(
 *                 request.getClientId(),
 *                 request.getTransactionId(),
 *                 Status.ERROR,
 *                 e.getError()
 *             );
 *
 *             // Determine appropriate HTTP status based on error type
 *             String errorCode = e.getError().getErrorCode().getCode();
 *             if (errorCode.startsWith("VALGN")) {
 *                 return ResponseEntity.badRequest().body(errorResponse);
 *             } else if (errorCode.startsWith("SECGN")) {
 *                 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
 *             } else {
 *                 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
 *             }
 *         }
 *     }
 * }
 * }</pre>
 *
 * <h2>Advanced Usage Patterns</h2>
 *
 * <h3>Error Code Categories and Conventions</h3>
 *
 * <p>Establish consistent error code naming conventions across your microservices:
 *
 * <ul>
 *   <li><strong>Validation Errors:</strong> VALGN### (e.g., VALGN001, VALGN002)
 *   <li><strong>Security Errors:</strong> SECGN### (e.g., SECGN001, SECGN002)
 *   <li><strong>System Errors:</strong> SYSGN### (e.g., SYSGN001, SYSGN002)
 *   <li><strong>Business Logic Errors:</strong> BIZGN### (e.g., BIZGN001, BIZGN002)
 *   <li><strong>Integration Errors:</strong> INTGN### (e.g., INTGN001, INTGN002)
 * </ul>
 *
 * <h3>Internationalization Support</h3>
 *
 * <pre>{@code
 * // Error code with internationalization support
 * public enum InternationalErrorCode implements ErrorCode {
 *     VALIDATION_REQUIRED_FIELD("VALGN001");
 *
 *     private final String code;
 *     private final MessageSource messageSource;
 *
 *     InternationalErrorCode(String code) {
 *         this.code = code;
 *         this.messageSource = ApplicationContextUtils.getBean(MessageSource.class);
 *     }
 *
 *     @Override
 *     public String getCode() { return code; }
 *
 *     @Override
 *     public String getDescription() {
 *         return messageSource.getMessage(code, null, Locale.getDefault());
 *     }
 *
 *     public String getDescription(Locale locale) {
 *         return messageSource.getMessage(code, null, locale);
 *     }
 * }
 * }</pre>
 *
 * <h3>Error Transformation and Enrichment</h3>
 *
 * <pre>{@code
 * // Service for transforming and enriching errors across service boundaries
 * @Component
 * public class ErrorTransformationService {
 *
 *     public ApplicationError enrichError(Error originalError, String additionalContext) {
 *         ApplicationError enrichedError = new ApplicationError(
 *             originalError.getErrorDescription(),
 *             originalError.getErrorCode(),
 *             originalError.getNativeErrorText()
 *         );
 *
 *         // Add additional context to native error text
 *         String enrichedNativeText = String.format("%s | Additional context: %s",
 *             originalError.getNativeErrorText() != null ? originalError.getNativeErrorText() : "",
 *             additionalContext);
 *         enrichedError.setNativeErrorText(enrichedNativeText);
 *
 *         return enrichedError;
 *     }
 *
 *     public ApplicationError mapExternalError(ExternalServiceError externalError) {
 *         ErrorCode mappedCode = mapExternalErrorCode(externalError.getCode());
 *         return new ApplicationError(
 *             "External service error: " + externalError.getMessage(),
 *             mappedCode,
 *             "External service response: " + externalError.getDetails()
 *         );
 *     }
 * }
 * }</pre>
 *
 * <h2>Integration Guidelines</h2>
 *
 * <h3>Microservices Error Propagation</h3>
 *
 * <p>When propagating errors across service boundaries:
 *
 * <ul>
 *   <li><strong>Preserve Transaction IDs:</strong> Maintain correlation IDs for distributed tracing
 *   <li><strong>Transform Appropriately:</strong> Convert internal errors to client-appropriate
 *       messages
 *   <li><strong>Add Context:</strong> Enrich errors with service-specific diagnostic information
 *   <li><strong>Maintain Structured Codes:</strong> Preserve error codes for programmatic handling
 * </ul>
 *
 * <h3>Logging and Monitoring Integration</h3>
 *
 * <pre>{@code
 * @Component
 * public class ErrorLoggingService {
 *
 *     private static final Logger logger = LoggerFactory.getLogger(ErrorLoggingService.class);
 *
 *     public void logError(Error error, String transactionId, String serviceName) {
 *         // Structured logging for error monitoring
 *         MDC.put("transactionId", transactionId);
 *         MDC.put("errorCode", error.getErrorCode().getCode());
 *         MDC.put("serviceName", serviceName);
 *
 *         logger.error("Service error occurred: {} | Code: {} | Native: {}",
 *             error.getErrorDescription(),
 *             error.getErrorCode().getCode(),
 *             error.getNativeErrorText());
 *
 *         MDC.clear();
 *     }
 * }
 * }</pre>
 *
 * <h3>Testing Error Scenarios</h3>
 *
 * <pre>{@code
 * @Test
 * public class ErrorHandlingTest {
 *
 *     @Test
 *     public void testErrorCreationAndValidation() {
 *         ErrorCode testCode = ApplicationErrorCodes.VALIDATION_REQUIRED_FIELD;
 *         ApplicationError error = new ApplicationError(
 *             "Test error description",
 *             testCode,
 *             "Test native error text"
 *         );
 *
 *         assertThat(error.getErrorDescription()).isEqualTo("Test error description");
 *         assertThat(error.getErrorCode().getCode()).isEqualTo("VALGN001");
 *         assertThat(error.getNativeErrorText()).isEqualTo("Test native error text");
 *     }
 *
 *     @Test
 *     public void testErrorResponseCreation() {
 *         ErrorCode code = ApplicationErrorCodes.SYSTEM_DATABASE_ERROR;
 *         ApplicationError error = new ApplicationError("Database error", code, null);
 *
 *         ApplicationErrorResponse response = new ApplicationErrorResponse(
 *             "test-client",
 *             "txn-123",
 *             Status.ERROR,
 *             error
 *         );
 *
 *         assertThat(response.getError()).isNotNull();
 *         assertThat(response.getStatus()).isEqualTo(Status.ERROR);
 *     }
 * }
 * }</pre>
 *
 * @author Rubens Gomes
 * @since 0.0.1
 * @see com.rubensgomes.msreqresplib.BaseResponse
 * @see com.rubensgomes.msreqresplib.dto.ApplicationErrorResponse
 * @see com.rubensgomes.msreqresplib.error.ApplicationError
 * @see jakarta.validation.constraints
 * @see lombok.Data
 */
package com.rubensgomes.msreqresplib.error;
