package com.hovispace.javacommons.springbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Batch Tutorial Application.
 *
 * This application demonstrates a classic Spring Batch use case: reading data from a CSV file,
 * processing/transforming each record, and writing the results to a relational database.
 *
 * Key concepts covered:
 * - Chunk-oriented processing (read → process → write in configurable chunks)
 * - FlatFileItemReader for CSV parsing
 * - Custom ItemProcessor for business logic transformation
 * - RepositoryItemWriter for JPA-based persistence
 * - Job and Step configuration using Spring Batch's builder API
 *
 * Note: We do NOT use @EnableBatchProcessing here. Spring Boot 3+ auto-configures batch
 * infrastructure (JobRepository, JobLauncher, etc.) when spring-boot-starter-batch is on
 * the classpath. Using @EnableBatchProcessing would actually disable this auto-configuration.
 */
@SpringBootApplication
public class SpringBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchApplication.class, args);
    }
}
