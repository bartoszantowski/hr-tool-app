package com.iitrab.hrtool.employee.internal;

import com.iitrab.hrtool.IntegrationTestBase;
import com.iitrab.hrtool.employee.api.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.iitrab.hrtool.SampleTestDataFactory.employee;
import static com.iitrab.hrtool.employee.api.EmployeeAssert.assertThatEmployee;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class EmployeeRepositoryTest extends IntegrationTestBase {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void shouldReturnEmpty_whenSearchingEmployeesByEmail_andThereAreNone() {
        Optional<Employee> found = employeeRepository.findByEmail("email");

        assertThat(found).isEmpty();
    }

    @Test
    void shouldReturnEmpty_whenSearchingEmployeesByEmail_andNoneIsMatching() {
        existingEmployee(employee());

        Optional<Employee> found = employeeRepository.findByEmail("email");

        assertThat(found).isEmpty();
    }

    @Test
    void shouldReturnEmployee_whenSearchingEmployeesByEmail_andOneMatches() {
        Employee employee = existingEmployee(employee());

        Optional<Employee> found = employeeRepository.findByEmail(employee.getEmail());

        assertThat(found).hasValueSatisfying(emp -> assertThatEmployee(emp).hasId(employee.getId())
                                                                           .hasName(employee.getName())
                                                                           .hasLastName(employee.getLastName())
                                                                           .wasBornOn(employee.getBirthdate())
                                                                           .hasEmail(employee.getEmail()));
    }

}