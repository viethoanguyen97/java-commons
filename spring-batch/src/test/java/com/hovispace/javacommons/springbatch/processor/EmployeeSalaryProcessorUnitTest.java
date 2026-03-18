package com.hovispace.javacommons.springbatch.processor;

import com.hovispace.javacommons.springbatch.dto.EmployeeSalaryInput;
import com.hovispace.javacommons.springbatch.entity.EmployeeSalary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for EmployeeSalaryProcessor.
 *
 * These tests verify the processor's business logic in isolation — no Spring context is loaded.
 * This makes them fast and focused on the transformation rules:
 * - 10% salary increase
 * - Department name uppercasing
 * - Name passthrough (unchanged)
 */
class EmployeeSalaryProcessorUnitTest {

    private EmployeeSalaryProcessor _processor;

    @BeforeEach
    void setUp() {
        _processor = new EmployeeSalaryProcessor();
    }

    @Test
    void process_shouldIncreaseSalaryByTenPercent() throws Exception {
        // Given: an employee earning 100,000
        EmployeeSalaryInput input = new EmployeeSalaryInput("John Doe", "engineering", 100000.00);

        // When: the processor transforms the input
        EmployeeSalary result = _processor.process(input);

        // Then: the salary should be increased by 10%
        assertThat(result.getSalary()).isEqualTo(110000.00);
    }

    @Test
    void process_shouldUppercaseDepartmentName() throws Exception {
        // Given: a lowercase department name
        EmployeeSalaryInput input = new EmployeeSalaryInput("Jane Smith", "marketing", 50000.00);

        // When: the processor transforms the input
        EmployeeSalary result = _processor.process(input);

        // Then: the department name should be uppercased
        assertThat(result.getDepartment()).isEqualTo("MARKETING");
    }

    @Test
    void process_shouldPreserveEmployeeName() throws Exception {
        // Given: an employee with a specific name
        EmployeeSalaryInput input = new EmployeeSalaryInput("Alice Williams", "finance", 70000.00);

        // When: the processor transforms the input
        EmployeeSalary result = _processor.process(input);

        // Then: the name should remain unchanged
        assertThat(result.getName()).isEqualTo("Alice Williams");
    }

    @Test
    void process_shouldHandleMixedCaseDepartment() throws Exception {
        // Given: a mixed-case department name
        EmployeeSalaryInput input = new EmployeeSalaryInput("Bob", "Engineering", 80000.00);

        // When: the processor transforms the input
        EmployeeSalary result = _processor.process(input);

        // Then: the department should be fully uppercased
        assertThat(result.getDepartment()).isEqualTo("ENGINEERING");
    }

    @Test
    void process_shouldApplyCorrectMultiplier() throws Exception {
        // Given: various salary values to verify the multiplier
        EmployeeSalaryInput input = new EmployeeSalaryInput("Test", "dept", 75000.00);

        // When
        EmployeeSalary result = _processor.process(input);

        // Then: salary * 1.10 = 82500.00
        assertThat(result.getSalary()).isEqualTo(82500.00);
    }
}
