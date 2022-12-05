package com.iitrab.hrtool.employee.api;

import javax.persistence.EntityManager;
import java.util.Optional;

/**
 * API interface for query operations on the {@link Employee} entities.
 * Implementation does not have to open new DB transaction. If the returned data has to be managed by the {@link EntityManager},
 * then the caller must open the transaction itself.
 */
public interface EmployeeProvider {

    /**
     * Returns the employee based on its ID.
     * If the employee with given ID is not found, then {@link Optional#empty()} will be returned.
     *
     * @param employeeId id of the employee to search
     * @return {@link Optional} containing found employee or {@link Optional#empty()} if not found
     */
    Optional<Employee> getEmployee(Long employeeId);

    /**
     * Returns the employee based on its email.
     * If the employee with given email is not found, then {@link Optional#empty()} will be returned.
     *
     * @param email email of the employee to search
     * @return {@link Optional} containing found employee or {@link Optional#empty()} if not found
     */
    Optional<Employee> getEmployeeByEmail(String email);

}
