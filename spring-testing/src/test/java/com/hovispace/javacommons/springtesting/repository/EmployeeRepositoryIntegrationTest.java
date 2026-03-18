package com.hovispace.javacommons.springtesting.repository;

import com.hovispace.javacommons.springtesting.entity.Employee;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryIntegrationTest {

    @Resource
    private TestEntityManager _entityManager;

    @Resource
    private EmployeeRepository _employeeRepository;

    @Test
    public void test_that_findByName_returns_expected_data() throws Exception {
        //arrange
        Employee employee = new Employee("grapes");
        _entityManager.persist(employee);
        _entityManager.flush();

        //act
        Employee found = _employeeRepository.findByName("grapes");

        //assert
        assertThat(found.getName()).isEqualTo("grapes");
    }

    @Test
    public void test_that_findAll_returns_expected_data() throws Exception {
        //arrange
        Employee employee = new Employee("grapes");
        _entityManager.persist(employee);
        _entityManager.flush();

        //act
        assertThat(_employeeRepository.findAll()).containsExactly(employee);
    }
}
