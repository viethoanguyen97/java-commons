package com.hovispace.javacommons.springbatch.repository;

import com.hovispace.javacommons.springbatch.entity.EmployeeSalary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for EmployeeSalary entities.
 *
 * This repository is used by the RepositoryItemWriter in the batch job to persist
 * processed employee salary records. It also provides query methods useful for
 * verifying batch job results in integration tests.
 */
public interface EmployeeSalaryRepository extends JpaRepository<EmployeeSalary, Long> {

    /**
     * Find all employee salary records by employee name.
     */
    List<EmployeeSalary> findByName(String name);
}
