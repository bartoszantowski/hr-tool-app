package com.iitrab.hrtool.department.api;

import com.iitrab.hrtool.exception.api.NotFoundException;

/**
 * Exception indicating that the {@link Department} was not found.
 */
@SuppressWarnings("squid:S110")
public class DepartmentNotFoundException extends NotFoundException {

    public DepartmentNotFoundException(String message) {
        super(message);
    }

    public DepartmentNotFoundException(Long id) {
        this("Department with ID=%s was not found".formatted(id));
    }

}
