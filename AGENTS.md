# AGENTS.md - Developer Guidelines for This Repository

## Project Overview

This is a **Spring Boot 4.0** application with **Java 17** using **Gradle** as the build tool. The project uses Spring AI for OpenAI integration.

## Build Commands

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Run tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.silvionetto.ai.AiApplicationTests"

# Run a single test method
./gradlew test --tests "com.silvionetto.ai.AiApplicationTests.contextLoads"

# Clean build
./gradlew clean build

# Run with verbose test output
./gradlew test --info
```

## Code Style Guidelines

### General Principles
- Use **4 spaces** for indentation (no tabs)
- Use **K&R-style braces** (opening brace on same line)
- Keep lines under **120 characters** when practical
- Add a **blank line** between imports and class declaration

### Naming Conventions
- **Classes**: `PascalCase` (e.g., `ChatController`, `AiApplication`)
- **Methods/Variables**: `camelCase` (e.g., `chatClient`, `generateResponse`)
- **Constants**: `SCREAMING_SNAKE_CASE`
- **Packages**: lowercase, single words or dotted (e.g., `com.silvionetto.ai`)

### Import Organization
1. **Java standard library** (`java.*`, `javax.*`)
2. **Third-party libraries** (Spring, etc.)
3. **Project imports** (`com.silvionetto.*`)

Use **static imports** for constants and static methods when it improves readability. Group imports without blank lines between groups.

### Types and Generics
- Use **interface types** over concrete implementations in public APIs (e.g., `List<T>` not `ArrayList<T>`)
- Prefer **generics** for type safety
- Use **primitive types** (`int`, `boolean`) over wrapper classes unless nullability is needed

### Annotations
- Place annotations on their own line above the declaration
- Common annotations: `@RestController`, `@Service`, `@Repository`, `@Configuration`, `@Bean`
- Use `@Autowired` on **constructors** (preferred) or **fields** (less common in modern Spring)

### Error Handling
- Use **Spring's exception handling** (`@ControllerAdvice`) for global exception mapping
- Throw **specific exceptions** with meaningful messages
- Avoid swallowing exceptions without logging
- Return appropriate **HTTP status codes** (4xx for client errors, 5xx for server errors)

### Logging
- Use **SLF4J** (`Logger` from `org.slf4j`)
- Use appropriate log levels: `ERROR` for failures, `WARN` for recoverable issues, `INFO` for important events
- Don't log sensitive data (passwords, API keys)

### Spring Boot Best Practices
- Use **constructor injection** (recommended) or `@Autowired` on constructors
- Keep configuration in `application.properties` or `@ConfigurationProperties`
- Use `@RestController` for REST APIs
- Use `@SpringBootTest` for integration tests

### Testing
- Place tests in `src/test/java` mirroring the main source structure
- Test class naming: `ClassNameTests` or `ClassNameIT` for integration tests
- Use `@SpringBootTest` for integration tests requiring Spring context
- Use `@MockitoBean` (Spring AI test annotation) to mock beans
- Keep tests focused and independent

### Documentation
- Use Javadoc for public APIs when behavior is non-obvious
- Keep Javadoc concise and meaningful
- Document `@Configuration` and `@Bean` methods

## Project Structure

```
src/
├── main/
│   ├── java/com/silvionetto/ai/
│   │   ├── AiApplication.java        # Main entry point
│   │   ├── ChatController.java       # REST endpoints
│   │   └── ChatClientConfig.java     # Bean configuration
│   └── resources/
│       └── application.properties    # App configuration
└── test/
    └── java/com/silvionetto/ai/
        └── AiApplicationTests.java   # Integration tests
```

## Environment Variables

- `SPRING_AI_OPENAI_API_KEY` - OpenAI API key (do not commit to version control)

## IDE Notes

- This project is configured for **IntelliJ IDEA** (see `.idea/`)
- Gradle wrapper is included at `gradle/wrapper/`
- Use `./gradlew` on Unix/macOS or `gradlew.bat` on Windows
