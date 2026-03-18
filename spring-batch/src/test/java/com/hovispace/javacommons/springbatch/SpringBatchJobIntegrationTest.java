package com.hovispace.javacommons.springbatch;

import com.hovispace.javacommons.springbatch.entity.EmployeeSalary;
import com.hovispace.javacommons.springbatch.repository.EmployeeSalaryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for the Spring Batch employee salary processing job.
 *
 * This test launches the actual batch job in a full Spring Boot context with an H2 database.
 * It verifies the entire pipeline: CSV reading → processing → database writing.
 *
 * @SpringBatchTest provides JobLauncherTestUtils and JobRepositoryTestUtils beans
 * for convenient batch job testing. It automatically detects the Job bean in the context.
 *
 * @SpringBootTest loads the full application context including:
 * - BatchConfiguration (job, step, reader, processor, writer)
 * - JPA/H2 auto-configuration
 * - Spring Batch auto-configuration (JobRepository, JobLauncher)
 */
@SpringBatchTest
@SpringBootTest
class SpringBatchJobIntegrationTest {

    @Autowired
    private JobLauncherTestUtils _jobLauncherTestUtils;

    @Autowired
    private EmployeeSalaryRepository _employeeSalaryRepository;

    @Test
    void employeeSalaryJob_shouldCompleteSuccessfully() throws Exception {
        // When: the batch job is launched
        JobExecution jobExecution = _jobLauncherTestUtils.launchJob();

        // Then: the job should complete with COMPLETED status
        assertThat(jobExecution.getExitStatus().getExitCode())
                .isEqualTo(ExitStatus.COMPLETED.getExitCode());
    }

    @Test
    void employeeSalaryJob_shouldProcessAllRecordsFromCsv() throws Exception {
        // Given: the CSV file contains 6 employee records (see employees.csv)
        _employeeSalaryRepository.deleteAll();

        // When: the batch job is launched
        _jobLauncherTestUtils.launchJob(new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .toJobParameters());

        // Then: all 6 records should be persisted in the database
        List<EmployeeSalary> results = _employeeSalaryRepository.findAll();
        assertThat(results).hasSize(6);
    }

    @Test
    void employeeSalaryJob_shouldApplySalaryIncrease() throws Exception {
        // Given: John Doe has a salary of 75000.00 in the CSV
        _employeeSalaryRepository.deleteAll();

        // When: the batch job is launched
        _jobLauncherTestUtils.launchJob(new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .toJobParameters());

        // Then: John Doe's salary should be increased by 10% (75000 * 1.10 = 82500)
        List<EmployeeSalary> johnRecords = _employeeSalaryRepository.findByName("John Doe");
        assertThat(johnRecords).hasSize(1);
        assertThat(johnRecords.getFirst().getSalary()).isEqualTo(82500.00);
    }

    @Test
    void employeeSalaryJob_shouldUppercaseDepartments() throws Exception {
        // Given: departments in the CSV are lowercase (engineering, marketing, finance)
        _employeeSalaryRepository.deleteAll();

        // When: the batch job is launched
        _jobLauncherTestUtils.launchJob(new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .toJobParameters());

        // Then: all department names should be uppercased
        List<EmployeeSalary> results = _employeeSalaryRepository.findAll();
        assertThat(results)
                .extracting(EmployeeSalary::getDepartment)
                .allMatch(dept -> dept.equals(dept.toUpperCase()));

        // Verify specific departments
        assertThat(results)
                .extracting(EmployeeSalary::getDepartment)
                .contains("ENGINEERING", "MARKETING", "FINANCE");
    }
}
