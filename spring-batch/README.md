# Spring Batch — CSV Employee Salary Processing Tutorial

This module demonstrates a classic **Spring Batch** use case: reading records from a CSV file, transforming each record with business logic, and writing the results to a relational database.

## What This Example Does

The batch job processes employee salary data through a three-step pipeline:

1. **Read** — A `FlatFileItemReader` parses `employees.csv`, mapping each row into an `EmployeeSalaryInput` POJO.
2. **Process** — A custom `EmployeeSalaryProcessor` applies two transformations:
   - Increases the salary by **10%**
   - Converts the department name to **UPPERCASE**
3. **Write** — A `RepositoryItemWriter` persists the transformed `EmployeeSalary` entities to an H2 in-memory database.

### Sample Data

Input CSV (`src/main/resources/employees.csv`):

| name           | department  | salary    |
|----------------|-------------|-----------|
| John Doe       | engineering | 75000.00  |
| Jane Smith     | marketing   | 68000.00  |
| Bob Johnson    | engineering | 82000.00  |
| Alice Williams | finance     | 71000.00  |
| Charlie Brown  | marketing   | 65000.00  |
| Diana Prince   | finance     | 90000.00  |

After processing, John Doe's record becomes: `ENGINEERING`, `82500.00` (75000 × 1.10).

## Spring Batch Concepts Covered

| Concept                   | Where                                         |
|---------------------------|-----------------------------------------------|
| Chunk-oriented processing | `BatchConfiguration` — step with chunk size 5  |
| FlatFileItemReader        | `BatchConfiguration.reader()` — CSV parsing    |
| ItemProcessor             | `EmployeeSalaryProcessor` — business logic      |
| RepositoryItemWriter      | `BatchConfiguration.writer()` — JPA persistence |
| Job / Step configuration  | `BatchConfiguration` — builder API              |
| Job auto-configuration    | `SpringBatchApplication` — no `@EnableBatchProcessing` needed |
| Batch testing             | `SpringBatchJobIntegrationTest` — `@SpringBatchTest` + `JobLauncherTestUtils` |

## Project Structure

```
spring-batch/
├── pom.xml
├── src/main/java/com/hovispace/javacommons/springbatch/
│   ├── SpringBatchApplication.java          # @SpringBootApplication entry point
│   ├── config/
│   │   └── BatchConfiguration.java          # Job, Step, Reader, Processor, Writer beans
│   ├── dto/
│   │   └── EmployeeSalaryInput.java         # Plain POJO matching CSV columns (input type)
│   ├── entity/
│   │   └── EmployeeSalary.java              # JPA @Entity (output type, persisted to DB)
│   ├── processor/
│   │   └── EmployeeSalaryProcessor.java     # ItemProcessor with transformation logic
│   └── repository/
│       └── EmployeeSalaryRepository.java    # Spring Data JPA repository
├── src/main/resources/
│   ├── application.properties               # H2, JPA, and batch configuration
│   └── employees.csv                        # Sample input data
└── src/test/java/com/hovispace/javacommons/springbatch/
    ├── SpringBatchJobIntegrationTest.java    # Full job integration test
    └── processor/
        └── EmployeeSalaryProcessorUnitTest.java  # Unit test for processor logic
```

## Prerequisites

- **Java 25** (or set `-Djava.compiler.release=21` for local builds)
- **Apache Maven 3.9+**

No external services are required — the module uses an H2 in-memory database for both batch metadata and application data.

## How to Build

```bash
# Build the module (skip tests)
mvn clean install -DskipTests -pl spring-batch

# Build the entire project
mvn clean install -DskipTests
```

## How to Run the Application

By default, the job does **not** auto-launch on startup (`spring.batch.job.name=` is empty in `application.properties`). To run the job on startup, set the job name:

```bash
# Run with the job enabled
mvn spring-boot:run -pl spring-batch -Dspring-boot.run.arguments="--spring.batch.job.name=employeeSalaryJob"
```

Or edit `src/main/resources/application.properties` and change:

```properties
spring.batch.job.name=employeeSalaryJob
```

Then start normally:

```bash
mvn spring-boot:run -pl spring-batch
```

The console output will show processing logs for each record:

```
Processed: John Doe | Department: engineering -> ENGINEERING | Salary: 75000.0 -> 82500.0
Processed: Jane Smith | Department: marketing -> MARKETING | Salary: 68000.0 -> 74800.0
...
```

## How to Run Tests

```bash
# Run all tests in this module
mvn test -pl spring-batch

# Run only unit tests
mvn test -pl spring-batch -Dtest="**/*UnitTest"

# Run only integration tests
mvn test -pl spring-batch -Dtest="**/*IntegrationTest"
```

### Unit Tests

`EmployeeSalaryProcessorUnitTest` — verifies the processor logic in isolation (no Spring context):
- Salary is increased by exactly 10%
- Department name is converted to uppercase
- Employee name is preserved unchanged

### Integration Tests

`SpringBatchJobIntegrationTest` — launches the full batch job with `@SpringBootTest` and `@SpringBatchTest`:
- Job completes with `COMPLETED` status
- All 6 CSV records are written to the database
- Salary values are correctly increased (e.g., 75000 → 82500)
- Department names are uppercased (e.g., `engineering` → `ENGINEERING`)

## Key Design Decisions

- **No `@EnableBatchProcessing`** — Spring Boot 3+ auto-configures batch infrastructure. Using `@EnableBatchProcessing` would actually disable the auto-configuration.
- **`spring-boot-starter-batch-jdbc`** — Spring Boot 4 split the batch starter. The JDBC variant provides the JDBC-backed `JobRepository` needed for persisting batch metadata.
- **DTO + Entity separation** — `EmployeeSalaryInput` (CSV input) and `EmployeeSalary` (JPA output) are separate classes, following the common pattern of decoupling the input format from the storage model.
- **Spring Batch 6 APIs** — Uses the restructured `org.springframework.batch.infrastructure.item.*` packages and the updated chunk builder API (`.chunk(size).transactionManager(txManager)`).
