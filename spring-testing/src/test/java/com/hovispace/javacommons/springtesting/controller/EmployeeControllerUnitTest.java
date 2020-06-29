package com.hovispace.javacommons.springtesting.controller;

import com.hovispace.javacommons.springtesting.entity.Employee;
import com.hovispace.javacommons.springtesting.service.EmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerUnitTest {

    @Autowired
    private MockMvc _mockMvc;

    @MockBean
    private EmployeeService _employeeService;

    @Test
    public void test_that_getAllEmployees_returns_all_employees_in_json_array() throws Exception {
        List<Employee> employees = singletonList(new Employee("grapes"));

        given(_employeeService.getAllEmployees()).willReturn(employees);

        _mockMvc.perform(get("/api/employees").contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("grapes")));
    }

}