package com.iitrab.hrtool.employee.api;

/**
 * API interface for performing modifying operations on {@link Employee} entities.
 * Implementation should perform the change in the database transaction either by continuing the existing one or creating new if needed.
 */
public interface EmployeeService {

    /**
     * Persists the passed employee.
     * If the employee has already DB ID assigned, then the implementation might throw an {@link IllegalArgumentException}.
     *
     * @param employee employee to create
     * @return created employee
     */
    Employee createEmployee(Employee employee);

}
