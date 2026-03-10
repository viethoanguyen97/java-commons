# CLAUDE.md — AI Assistant Guide for java-commons

This document provides context for AI assistants working on this repository. It covers project structure, development workflows, conventions, and key patterns to follow.

## Project Overview

**java-commons** is a multi-module Maven project demonstrating common Java/Spring patterns and integrations. It serves as an educational/reference repository covering Kafka, GraphQL, Elasticsearch, cryptographic security, barcode generation, and serialization utilities.

- **Group ID:** `com.hovispace`
- **Java Version:** 11
- **Spring Boot:** 2.3.0.RELEASE
- **Build Tool:** Apache Maven (wrapper included)

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
| `common-libraries` | `commonlibraries` | Guava, Kryo 5, Barcode4j 2.1, ZXing 3.4.1 |
| `elasticsearch` | `elasticsearch` | Elasticsearch 7.14.0 (RestHighLevelClient) |
| `java-security` | `javasecurity` | Guava, Apache Commons Codec |
| `spring-graphql` | `springgraphql` | graphql-spring-boot-starter 5.0.2, graphql-java-tools 5.2.4 |
| `spring-kafka` | `springkafka` | Spring Kafka, Spring Boot |
| `spring-testing` | `springtesting` | Spring Boot Test, Spring Data JPA, H2 |

---

## Build & Development Commands

```bash
# Build all modules (skip tests)
./mvnw clean install -DskipTests

# Build a single module
./mvnw clean install -DskipTests -pl spring-kafka

# Run all unit tests
./mvnw test -Dtest="**/*UnitTest"

# Run all integration tests
./mvnw test -Dtest="**/*IntegrationTest"

# Run all tests
./mvnw test

# Run tests in a specific module
./mvnw test -pl spring-testing

# Generate JaCoCo coverage report
./mvnw jacoco:report
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

- **JUnit 4** via `junit-vintage-engine` (5.7.0) — not JUnit 5 native
- **Mockito** 3.3.3 with `mockito-junit-jupiter`
- **AssertJ** 3.16.1 for fluent assertions
- **Hamcrest** 2.2
- **Awaitility** 4.0.3 for async assertions (useful in Kafka tests)
- **Spring Boot Test** with `@SpringBootTest`
- **H2** in-memory database for JPA tests

When writing tests, prefer AssertJ (`assertThat(...)`) for assertions. Use Awaitility for testing async operations (Kafka consumers).

---

## Key Dependencies

```xml
<!-- Core -->
Spring Boot 2.3.0.RELEASE
Spring Framework 5.2.6.RELEASE
Spring Security 5.3.9.RELEASE

<!-- Data / Messaging -->
Spring Data JPA
Spring Kafka
Hibernate 5.4.15.Final
H2 1.4.200

<!-- Elasticsearch -->
org.elasticsearch:elasticsearch:7.14.0
org.elasticsearch.client:elasticsearch-rest-high-level-client

<!-- GraphQL -->
com.graphql-java-kickstart:graphql-spring-boot-starter:5.0.2
com.graphql-java-kickstart:graphql-java-tools:5.2.4

<!-- Utilities -->
com.google.guava:guava:29.0-jre
org.apache.commons:commons-lang3:3.10
commons-codec:commons-codec:1.15
com.google.code.gson:gson:2.6.2
com.esotericsoftware:kryo:5.0.0-RC7

<!-- Barcode/QR -->
net.sf.barcode4j:barcode4j:2.1
com.google.zxing:core:3.4.1
com.google.zxing:javase:3.4.1

<!-- JSON -->
com.fasterxml.jackson.core:jackson-core:2.11.1
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

- `GreetingKafkaProducer` — sends `Greeting` objects; uses `ListenableFuture` callbacks
- `GreetingKafkaConsumer` — receives on topic, stores messages for test verification
- Configuration via `KafkaProducerConfiguration` and `KafkaConsumerConfiguration`

Integration tests require a running Kafka instance (configured via `application.properties`).

### spring-graphql
GraphQL API with two schemas:
- `blog.graphqls` — Post/Author types
- `vehicle.graphqls` — Vehicle types

Resolver classes implement `GraphQLQueryResolver` or `GraphQLMutationResolver`.

Access GraphiQL UI at `http://localhost:8080/graphiql` when running locally.

### elasticsearch
Uses the deprecated (but functional for 7.x) `RestHighLevelClient` API.

- `ElasticConfig` — configures the client bean
- `PersonStore` / `ElasticsearchPersonStore` — document CRUD abstraction

Requires a running Elasticsearch 7.x instance. See `elasticsearch/README.md` for Docker setup.

### common-libraries
Utility demonstrations:
- **Barcode4j:** `Barcode4jBarcodeGenerator` — generates UPCA, EAN13, Code128, PDF417 barcodes
- **ZXing:** `ZxingBarcodeGenerator` — generates various barcode formats and QR codes
- **Kryo:** `Person`, `OtherPerson`, `ComplexObject` serialization; `OtherPersonSerializer` custom serializer

### java-security
Pure utility tests demonstrating:
- MD5 hashing via `JavaMD5UnitTest` (Guava and Apache Commons Codec)
- SHA-256 hashing via `JavaSHA256UnitTest`

No production source code — only test classes.

---

## CI/CD (GitHub Actions)

`.github/workflows/ci.yml` defines four jobs triggered on pushes and pull requests to `master`:
1. **Compile:** `./mvnw clean install -DskipTests`
2. **Unit Tests:** `./mvnw '-Dtest=**/*UnitTest' test`
3. **Integration Tests:** `./mvnw '-Dtest=**/*IntegrationTest' test`
4. **Code Quality** (master push only): JaCoCo coverage, SonarCloud analysis, Codecov upload

Elasticsearch 7.8.1 runs as a Docker service container for integration tests (heap limited to 128m).

Required GitHub secrets: `SONAR_TOKEN`, `CODECOV_TOKEN`.

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
- **JUnit version:** The project uses JUnit 4 via the vintage engine — avoid JUnit 5 `@Test` from `org.junit.jupiter`
- **Elasticsearch version:** Module targets 7.x API; `RestHighLevelClient` is deprecated in 8.x
- **Spring Boot version:** 2.3.0.RELEASE is intentional; do not upgrade without testing all modules
