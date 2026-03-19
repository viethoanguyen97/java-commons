package com.hovispace.javacommons.springbatch.config;

import com.hovispace.javacommons.springbatch.dto.EmployeeSalaryInput;
import com.hovispace.javacommons.springbatch.entity.EmployeeSalary;
import com.hovispace.javacommons.springbatch.processor.EmployeeSalaryProcessor;
import com.hovispace.javacommons.springbatch.repository.EmployeeSalaryRepository;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
// Spring Batch 6 moved item reader/writer/processor classes to the
// org.springframework.batch.infrastructure.item package (from org.springframework.batch.item)
import org.springframework.batch.infrastructure.item.data.RepositoryItemWriter;
import org.springframework.batch.infrastructure.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Spring Batch job configuration for the Employee Salary CSV processing tutorial.
 *
 * This configuration defines the complete batch pipeline:
 *
 * 1. READER (FlatFileItemReader) — reads CSV rows and maps them to EmployeeSalaryInput POJOs
 * 2. PROCESSOR (EmployeeSalaryProcessor) — transforms input: 10% salary raise + uppercase department
 * 3. WRITER (RepositoryItemWriter) — persists the processed EmployeeSalary entities to the database
 * 4. STEP — combines reader, processor, and writer into a chunk-oriented step
 * 5. JOB — orchestrates one or more steps (this tutorial uses a single step)
 *
 * Chunk-oriented processing:
 * Spring Batch reads items one at a time, but writes them in "chunks" (batches).
 * The chunk size (set to 5 here) determines how many items are read and processed
 * before a single write/commit operation. This improves performance and provides
 * transaction boundaries — if a chunk fails, only that chunk is rolled back.
 */
@Configuration
public class BatchConfiguration {

    private final EmployeeSalaryRepository _employeeSalaryRepository;

    @Autowired
    public BatchConfiguration(EmployeeSalaryRepository employeeSalaryRepository) {
        _employeeSalaryRepository = employeeSalaryRepository;
    }

    /**
     * FlatFileItemReader — reads from a delimited (CSV) file.
     *
     * Configuration:
     * - name: identifies this reader in the batch metadata (for restart support)
     * - resource: the CSV file on the classpath
     * - linesToSkip: skips the header row
     * - delimited().names(): maps CSV columns to POJO fields by name
     * - targetType: the class to map each row into (uses BeanWrapperFieldSetMapper internally)
     */
    @Bean
    public FlatFileItemReader<EmployeeSalaryInput> reader() throws Exception {
        return new FlatFileItemReaderBuilder<EmployeeSalaryInput>()
                .name("employeeSalaryReader")
                .resource(new ClassPathResource("employees.csv"))
                .linesToSkip(1) // Skip the CSV header row
                .delimited()
                .names("name", "department", "salary") // CSV column names, mapped to POJO setters
                .targetType(EmployeeSalaryInput.class)
                .build();
    }

    /**
     * ItemProcessor — applies business logic to transform each item.
     *
     * The processor is a simple POJO (not a Spring-managed bean by default),
     * so we create it as a @Bean to make it available in the Step definition.
     */
    @Bean
    public EmployeeSalaryProcessor processor() {
        return new EmployeeSalaryProcessor();
    }

    /**
     * RepositoryItemWriter — writes processed items to the database via JPA.
     *
     * Uses the Spring Data repository's save method to persist each EmployeeSalary entity.
     * The RepositoryItemWriter internally calls repository.saveAll() for the entire chunk,
     * providing efficient batch inserts within a single transaction.
     */
    @Bean
    public RepositoryItemWriter<EmployeeSalary> writer() {
        return new RepositoryItemWriterBuilder<EmployeeSalary>()
                .repository(_employeeSalaryRepository)
                .methodName("save")
                .build();
    }

    /**
     * Step definition — the fundamental unit of work in Spring Batch.
     *
     * A step encapsulates a reader, processor, and writer with a chunk size.
     * The chunk size (5) means: read 5 items, process them, then write all 5 in one transaction.
     *
     * Parameters:
     * - jobRepository: stores step execution metadata (status, read/write counts, etc.)
     * - transactionManager: manages the transaction boundary around each chunk
     *
     * Note: In Spring Batch 6, the chunk API changed from chunk(size, txManager) to
     * chunk(size).transactionManager(txManager) as separate builder calls.
     */
    @Bean
    public Step employeeSalaryStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("employeeSalaryStep", jobRepository)
                .<EmployeeSalaryInput, EmployeeSalary>chunk(5)
                .transactionManager(transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    /**
     * Job definition — the top-level batch execution unit.
     *
     * A job consists of one or more steps executed in sequence. This tutorial uses a single step.
     * The job name ("employeeSalaryJob") identifies this job in the batch metadata tables,
     * enabling restart, status tracking, and execution history.
     *
     * Spring Boot auto-configuration will automatically launch this job on application startup
     * (unless spring.batch.job.enabled=false is set).
     */
    @Bean
    public Job employeeSalaryJob(JobRepository jobRepository, Step employeeSalaryStep) {
        return new JobBuilder("employeeSalaryJob", jobRepository)
                .start(employeeSalaryStep)
                .build();
    }
}
