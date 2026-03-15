package com.hovispace.javacommons.springtesting.repository;

import com.hovispace.javacommons.springtesting.entity.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
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
