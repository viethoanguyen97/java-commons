package com.hovispace.javacommons.springbatch.dto;

/**
 * Plain POJO representing a raw employee salary record from the CSV file.
 *
 * This class is the INPUT type for the batch job. The FlatFileItemReader maps each CSV row
 * into an EmployeeSalaryInput instance using BeanWrapperFieldSetMapper, which calls the
 * setter methods to populate the fields.
 *
 * Fields match the CSV column order: name, department, salary.
 *
 * Note: This is intentionally NOT a JPA entity. It represents the raw, unprocessed input data.
 * The ItemProcessor will transform this into an EmployeeSalary entity for persistence.
 */
public class EmployeeSalaryInput {

    private String _name;
    private String _department;
    private Double _salary;

    public EmployeeSalaryInput() {
    }

    public EmployeeSalaryInput(String name, String department, Double salary) {
        _name = name;
        _department = department;
        _salary = salary;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getDepartment() {
        return _department;
    }

    public void setDepartment(String department) {
        _department = department;
    }

    public Double getSalary() {
        return _salary;
    }

    public void setSalary(Double salary) {
        _salary = salary;
    }
}
