package com.hovispace.javacommons.springtesting.service;

import com.hovispace.javacommons.springtesting.entity.Employee;
import com.hovispace.javacommons.springtesting.repository.EmployeeRepository;
import com.hovispace.javacommons.springtesting.service.impl.EmployeeServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EmployeeServiceIntegrationTest {

    @TestConfiguration
    static class EmployeeServiceIntegrationTestContextConfiguration {

        @Bean
        public EmployeeService employeeService(EmployeeRepository employeeRepository) {
            return new EmployeeServiceImpl(employeeRepository);
        }
    }

    @Resource
    private EmployeeRepository _employeeRepository;

    @Resource
    private EmployeeService _employeeService;

    @Test
    public void test_that_getEmployeeByName_returns_employee_if_name_is_valid() {
        //arrange
        Employee employee = new Employee("grapes");
        _employeeRepository.save(employee);

        //act
        Employee found = _employeeService.getEmployeeByName("grapes");

        //assert
        assertThat(found.getName()).isEqualTo("grapes");
    }

    @Test
    public void test_that_getAllEmployees_returns_all_existed_employee() {
        //arrange
        Employee employee = new Employee("grapes");
        _employeeRepository.saveAndFlush(employee);

        //assert
        assertThat(_employeeService.getAllEmployees()).containsExactly(employee);
    }

}
