package com.hovispace.javacommons.springtesting.controller;

import com.hovispace.javacommons.springtesting.entity.Employee;
import com.hovispace.javacommons.springtesting.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    private final EmployeeService _employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        _employeeService = employeeService;
    }

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return _employeeService.getAllEmployees();
    }
}
