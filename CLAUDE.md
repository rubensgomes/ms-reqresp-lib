# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/claude-code) when working with this repository.

## Project Overview

This is a Java library (`ms-reqresp-lib`) providing standardized request/response DTOs and base types for microservices architectures. It provides:
- Immutable `BaseRequest` and `BaseResponse` abstract classes
- Distributed tracing support via `clientId` and `transactionId` fields
- Jakarta Bean Validation integration
- `ApplicationErrorResponse` for guaranteed error information scenarios

## Build Commands

```bash
# Build with tests
./gradlew --info clean build

# Run tests only (--info required to see test output)
./gradlew --info clean test

# Apply code formatting (Spotless with Google Java Format)
./gradlew :lib:spotlessApply

# Create JAR artifact
./gradlew --info jar

# Clean build artifacts
./gradlew --info clean
```

## Project Structure

- `lib/src/main/java/com/rubensgomes/msreqresplib/` - Main source code
  - `BaseRequest.java` - Abstract base for all request DTOs
  - `BaseResponse.java` - Abstract base for all response DTOs
  - `dto/ApplicationErrorResponse.java` - Specialized error response
- `lib/src/test/java/` - Unit tests (JUnit 5)

## Key Dependencies

- `com.rubensgomes:ms-base-lib` - Provides `Status` enum, `ApplicationError`, `ErrorCode`
- Jakarta Validation API for `@NotBlank`, `@NotNull` annotations
- Lombok for `@Data`, `@Slf4j` boilerplate reduction
- SLF4J for logging

## Code Style

- **Formatter**: Google Java Format (enforced via Spotless)
- **Run `./gradlew :lib:spotlessApply` before committing**
- Java 25 (Amazon Corretto)
- Immutable design: core fields should be `final`
- Use Lombok annotations to reduce boilerplate

## Testing

- Framework: JUnit 5 (Jupiter)
- Test fixtures: `TestRequest` and `TestResponse` in test source
- Some tests may be `@Disabled` - check before enabling
