package com.hovispace.javacommons.springbatch.entity;

import jakarta.persistence.*;

/**
 * JPA entity representing a processed employee salary record.
 *
 * This entity is the OUTPUT of the batch job. After the ItemProcessor transforms the raw CSV
 * input (EmployeeSalaryInput), the result is persisted as an EmployeeSalary record in the database.
 *
 * The separation between EmployeeSalaryInput (DTO) and EmployeeSalary (entity) follows a common
 * Spring Batch pattern: the reader produces one type, the processor transforms it into another,
 * and the writer persists the final type. This decouples the input format from the storage model.
 */
@Entity
@Access(AccessType.PROPERTY)
@Table(name = "employee_salary")
public class EmployeeSalary {

    private Long _id;

    private String _name;

    private String _department;

    private Double _salary;

    public EmployeeSalary() {
    }

    public EmployeeSalary(String name, String department, Double salary) {
        _name = name;
        _department = department;
        _salary = salary;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return _id;
    }

    public void setId(Long id) {
        _id = id;
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
