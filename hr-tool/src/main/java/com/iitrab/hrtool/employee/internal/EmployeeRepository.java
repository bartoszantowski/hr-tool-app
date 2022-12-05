package com.iitrab.hrtool.employee.internal;

import com.iitrab.hrtool.employee.api.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Objects;
import java.util.Optional;

interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Query searching employees by email address. It matches by exact match.
     *
     * @param email email of the employee to search
     * @return {@link Optional} containing found employee or {@link Optional#empty()} if none matched
     */
    default Optional<Employee> findByEmail(String email) {
        return findAll().stream()
                        .filter(employee -> Objects.equals(employee.getEmail(), email))
                        .findFirst();
    }

}
