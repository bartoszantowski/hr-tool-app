package com.iitrab.hrtool.employee.api;

import com.iitrab.hrtool.exception.api.NotFoundException;

/**
 * Exception indicating that the {@link Employee} was not found.
 */
@SuppressWarnings("squid:S110")
public class EmployeeNotFoundException extends NotFoundException {

    private EmployeeNotFoundException(String message) {
        super(message);
    }

    public EmployeeNotFoundException(Long id) {
        this("Employee with ID=%s was not found".formatted(id));
    }

}
