package com.hovispace.javacommons.springtesting.service;

import com.hovispace.javacommons.springtesting.entity.Employee;

import java.util.List;

public interface EmployeeService {

    Employee getEmployeeByName(String name);

    List<Employee> getAllEmployees();
}
