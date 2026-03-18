package com.hovispace.javacommons.springbatch.processor;

import com.hovispace.javacommons.springbatch.dto.EmployeeSalaryInput;
import com.hovispace.javacommons.springbatch.entity.EmployeeSalary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * Custom ItemProcessor that transforms raw CSV input into a processed entity.
 *
 * In Spring Batch's chunk-oriented processing model, the ItemProcessor sits between
 * the ItemReader and ItemWriter. It receives one item at a time from the reader,
 * applies business logic, and returns the transformed item for the writer.
 *
 * This processor applies two transformations:
 * 1. Salary increase: applies a 10% raise to the original salary
 * 2. Department normalization: converts the department name to uppercase
 *
 * The processor also demonstrates type conversion — the input type (EmployeeSalaryInput)
 * differs from the output type (EmployeeSalary), which is a common pattern when the
 * raw data model differs from the persistence model.
 *
 * Returning null from process() would signal Spring Batch to skip (filter out) that item.
 */
public class EmployeeSalaryProcessor implements ItemProcessor<EmployeeSalaryInput, EmployeeSalary> {

    private static final Logger _logger = LoggerFactory.getLogger(EmployeeSalaryProcessor.class);

    // The salary increase multiplier: 1.10 = 10% raise
    static final double SALARY_INCREASE_MULTIPLIER = 1.10;

    @Override
    public EmployeeSalary process(EmployeeSalaryInput input) {
        // Apply business transformations: 10% salary increase and uppercase department
        String uppercasedDepartment = input.getDepartment().toUpperCase();
        Double increasedSalary = input.getSalary() * SALARY_INCREASE_MULTIPLIER;

        EmployeeSalary result = new EmployeeSalary(input.getName(), uppercasedDepartment, increasedSalary);

        _logger.info("Processed: {} | Department: {} -> {} | Salary: {} -> {}",
                input.getName(), input.getDepartment(), uppercasedDepartment,
                input.getSalary(), increasedSalary);

        return result;
    }
}
