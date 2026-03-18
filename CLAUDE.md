# CLAUDE.md — AI Assistant Guide for java-commons

This document provides context for AI assistants working on this repository. It covers project structure, development workflows, conventions, and key patterns to follow.

## Project Overview

**java-commons** is a multi-module Maven project demonstrating common Java/Spring patterns and integrations. It serves as an educational/reference repository covering Kafka, GraphQL, Elasticsearch, cryptographic security, barcode generation, and serialization utilities.

- **Group ID:** `com.hovispace`
- **Java Version:** 25
- **Spring Boot:** 4.0.3
- **Build Tool:** Apache Maven 3.9.9 (no wrapper; CI installs Maven via `actions/setup-java`)

---

## Module Structure

```
java-commons/
├── pom.xml                  # Root POM (multi-module parent)
├── common-libraries/        # Guava, Kryo, Barcode4j, ZXing utilities
├── elasticsearch/           # Elasticsearch REST client integration
├── java-security/           # Cryptographic hashing (MD5, SHA-256)
├── spring-data/             # Placeholder module (no source yet)
├── spring-graphql/          # GraphQL API with Spring Boot
├── spring-kafka/            # Kafka producer/consumer examples
└── spring-testing/          # Spring Boot testing patterns
```

### Module Details

| Module | Package Suffix | Key Tech |
|--------|---------------|----------|
| `common-libraries` | `commonlibraries` | Guava 33.4.0-jre, Kryo 5.5.0, Barcode4j 2.1, ZXing 3.5.3 |
| `elasticsearch` | `elasticsearch` | Elasticsearch 9.x (co.elastic.clients:elasticsearch-java + elasticsearch-rest5-client) |
| `java-security` | `javasecurity` | Guava, Apache Commons Codec |
| `spring-graphql` | `springgraphql` | spring-boot-starter-graphql (native), Spring Boot 4.0.3 |
| `spring-kafka` | `springkafka` | Spring Kafka (managed by Boot BOM), Spring Boot 4.0.3 |
| `spring-testing` | `springtesting` | Spring Boot Test, Spring Data JPA, H2 |

---

## Build & Development Commands

```bash
# Build all modules (skip tests)
mvn clean install -DskipTests

# Build a single module
mvn clean install -DskipTests -pl spring-kafka

# Run all unit tests
mvn test -Dtest="**/*UnitTest"

# Run all integration tests
mvn test -Dtest="**/*IntegrationTest"

# Run all tests
mvn test

# Run tests in a specific module
mvn test -pl spring-testing

# Generate JaCoCo coverage report
mvn jacoco:report
```

---

## Package & Directory Conventions

All source code lives under:
```
src/main/java/com/hovispace/javacommons/{module-packages}/
src/test/java/com/hovispace/javacommons/{module-packages}/
```

Standard sub-package layout within each module:
```
controller/    # @RestController classes
service/       # Service interfaces and *ServiceImpl classes
dao/           # Repository/DAO interfaces
entity/        # JPA @Entity classes
config/        # @Configuration classes
resolver/      # GraphQL resolver classes (spring-graphql)
store/         # Storage abstraction classes (elasticsearch)
producer/      # Kafka producers
consumer/      # Kafka consumers
```

---

## Naming Conventions

### Fields
Private fields use an **underscore prefix**:
```java
private EmployeeRepository _employeeRepository;
private KafkaTemplate<String, Greeting> _kafkaTemplate;
```

### Classes
- Entities: plain noun (e.g., `Employee`, `Greeting`, `Post`)
- Service interfaces: `*Service` (e.g., `EmployeeService`)
- Service implementations: `*ServiceImpl` (e.g., `EmployeeServiceImpl`)
- Repositories: `*Repository` (e.g., `EmployeeRepository`)
- Controllers: `*Controller` (e.g., `EmployeeController`)
- Configurations: `*Configuration` (e.g., `KafkaProducerConfiguration`)
- GraphQL resolvers: `*Query`, `*Mutation`, `*Resolver`

### Tests
- Unit tests: `*UnitTest.java`
- Integration tests: `*IntegrationTest.java`

Both patterns are required by maven-surefire-plugin includes:
```xml
<include>**/*UnitTest.java</include>
<include>**/*IntegrationTest.java</include>
```

---

## Testing Stack

- **JUnit 5** (`junit-jupiter`) — managed by Spring Boot BOM via `spring-boot-starter-test`
- **Mockito** (latest, managed by Boot BOM) with `@ExtendWith(MockitoExtension.class)`
- **AssertJ** (managed by Boot BOM) for fluent assertions
- **Hamcrest** 2.2
- **Awaitility** (managed by Boot BOM) for async assertions (useful in Kafka tests)
- **Spring Boot Test** with `@SpringBootTest`
- **H2** in-memory database for JPA tests

When writing tests, prefer AssertJ (`assertThat(...)`) for assertions. Use Awaitility for testing async operations (Kafka consumers).

---

## Key Dependencies

```xml
<!-- Core (managed by Spring Boot 4.0.3 BOM) -->
Spring Boot 4.0.3
Spring Framework 7.x
Spring Security 7.x

<!-- Data / Messaging (managed by BOM) -->
Spring Data JPA 4.x
Spring Kafka 4.x
Hibernate 6.x (org.hibernate.orm:hibernate-core)
H2 2.x

<!-- Elasticsearch (managed by Spring Boot 4 BOM) -->
co.elastic.clients:elasticsearch-java
org.elasticsearch.client:elasticsearch-rest5-client

<!-- GraphQL (Spring Boot native) -->
org.springframework.boot:spring-boot-starter-graphql

<!-- Utilities -->
com.google.guava:guava:33.4.0-jre
org.apache.commons:commons-lang3 (managed by BOM)
commons-codec:commons-codec:1.17.1
com.google.code.gson:gson (managed by BOM)
com.esotericsoftware:kryo:5.5.0

<!-- Barcode/QR -->
net.sf.barcode4j:barcode4j:2.1
com.google.zxing:core:3.5.3
com.google.zxing:javase:3.5.3

<!-- JSON (managed by BOM) -->
com.fasterxml.jackson.core:jackson-core (managed by BOM)
```

---

## Module-Specific Notes

### spring-testing
Reference module for Spring Boot testing patterns. Demonstrates:
- `@SpringBootTest` with `MockMvc`
- Service layer mocking with Mockito
- JPA repository integration tests with H2

Key classes: `Employee`, `EmployeeService`/`EmployeeServiceImpl`, `EmployeeController`, `EmployeeRepository`

### spring-kafka
Demonstrates Kafka producer/consumer patterns with serialization via Jackson and Guava.

- `GreetingKafkaProducer` — sends `Greeting` objects; uses `CompletableFuture` callbacks
- `GreetingKafkaConsumer` — receives on topic, stores messages for test verification
- Configuration via `KafkaProducerConfiguration` and `KafkaConsumerConfiguration`

Integration tests require a running Kafka instance (configured via `application.properties`).

### spring-graphql
GraphQL API with two schemas:
- `blog.graphqls` — Post/Author types
- `vehicle.graphqls` — Vehicle types

Resolver classes are `@Controller` beans using Spring Boot native GraphQL annotations:
- `@QueryMapping` for query fields
- `@MutationMapping` for mutation fields
- `@SchemaMapping(typeName = "...")` for type-level field resolvers
- `@Argument` to bind GraphQL arguments to method parameters

Access GraphiQL UI at `http://localhost:8080/graphiql` when running locally (enabled via `spring.graphql.graphiql.enabled=true`).

### elasticsearch
Uses the Elasticsearch Java API Client (`co.elastic.clients:elasticsearch-java`) targeting ES 9.x.

- `ElasticConfig` — configures `ElasticsearchClient` via `Rest5Client` + `Rest5ClientTransport`
- `PersonStore` / `ElasticsearchPersonStore` — document CRUD abstraction

Requires a running Elasticsearch 9.x instance with security disabled (`xpack.security.enabled=false`).

### common-libraries
Utility demonstrations:
- **Barcode4j:** `Barcode4jBarcodeGenerator` — generates UPCA, EAN13, Code128, PDF417 barcodes
- **ZXing:** `ZxingBarcodeGenerator` — generates various barcode formats and QR codes
- **Kryo:** `Person`, `OtherPerson`, `ComplexObject` serialization; `OtherPersonSerializer` custom serializer

### java-security
Pure utility tests demonstrating:
- MD5 hashing via `JavaMD5UnitTest` (Guava and Apache Commons Codec)
- SHA-256 hashing via `JavaSHA256UnitTest`

Uses `HexFormat` (Java 17+) for byte-to-hex conversion.

No production source code — only test classes.

---

## CI/CD (GitHub Actions)

The pipeline is defined in `.github/workflows/ci.yml` and runs on pushes and pull requests to `master`. It has four jobs:

1. **Compile** — `mvn clean install -DskipTests`
2. **Unit Tests** — `mvn '-Dtest=**/*UnitTest' test` (runs after compile)
3. **Integration Tests** — `mvn '-Dtest=**/*IntegrationTest' test` (runs after compile, with Elasticsearch 9.0.1 service container, heap limited to 128m)
4. **Code Quality** — runs only on `master` push after both test jobs pass:
   - JaCoCo coverage report generation
   - SonarCloud analysis (requires `SONAR_TOKEN` secret)
   - Codecov upload

### Required GitHub Secrets

| Secret | Description |
|--------|-------------|
| `SONAR_TOKEN` | SonarCloud authentication token for project `viethoanguyen97_java-commons` |

`GITHUB_TOKEN` is provided automatically by GitHub Actions.

---

## Code Quality

- **SonarCloud** — project key `viethoanguyen97_java-commons`
- **Codecov** — coverage tracking after CI test runs
- **JaCoCo** — generates `target/site/jacoco/` reports locally

---

## Adding a New Module

1. Create the module directory with standard Maven layout
2. Add a `pom.xml` with parent reference:
   ```xml
   <parent>
     <groupId>com.hovispace</groupId>
     <artifactId>java-commons</artifactId>
     <version>1.0</version>
   </parent>
   ```
3. Register the module in the root `pom.xml` `<modules>` section
4. Follow the package naming convention: `com.hovispace.javacommons.{modulename}`
5. Name test classes `*UnitTest` or `*IntegrationTest` to be picked up by Surefire

---

## Common Pitfalls

- **Test naming:** Tests not ending in `UnitTest` or `IntegrationTest` will not run in CI
- **Private field prefix:** Follow the `_fieldName` convention for private instance fields
- **JUnit version:** The project uses JUnit 5 (`junit-jupiter`) — use `org.junit.jupiter.api.Test`, `@ExtendWith(MockitoExtension.class)`, `@BeforeEach`, etc.
- **Elasticsearch version:** Module uses ES 9.x Java API Client (`co.elastic.clients:elasticsearch-java` + `elasticsearch-rest5-client`), managed by the Spring Boot BOM. The old `RestHighLevelClient` has been removed.
- **GraphQL resolvers:** Use Spring Boot native annotations (`@QueryMapping`, `@MutationMapping`, `@SchemaMapping`) — not the old graphql-java-kickstart interface-based approach
- **Kafka futures:** `KafkaTemplate.send()` returns `CompletableFuture` in Spring Kafka 4.x — `ListenableFuture` is removed
- **Jakarta namespace:** All JPA entities and injection annotations use `jakarta.*` (not `javax.*`)
- **Spring Boot 4 test module split:** `@WebMvcTest` moved to `spring-boot-webmvc-test` (`org.springframework.boot.webmvc.test.autoconfigure`); `@DataJpaTest` to `spring-boot-data-jpa-test` (`org.springframework.boot.data.jpa.test.autoconfigure`); `TestEntityManager` to `spring-boot-jpa-test` (`org.springframework.boot.jpa.test.autoconfigure`). `@MockBean` removed — use `@MockitoBean` from `org.springframework.test.context.bean.override.mockito`.
