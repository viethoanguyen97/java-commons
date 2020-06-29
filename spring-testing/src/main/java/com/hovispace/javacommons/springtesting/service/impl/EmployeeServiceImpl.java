package com.hovispace.javacommons.springtesting.service.impl;

import com.hovispace.javacommons.springtesting.entity.Employee;
import com.hovispace.javacommons.springtesting.repository.EmployeeRepository;
import com.hovispace.javacommons.springtesting.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository _employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        _employeeRepository = employeeRepository;
    }

    @Override
    public Employee getEmployeeByName(String name) {
        return _employeeRepository.findByName(name);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return _employeeRepository.findAll();
    }
}
