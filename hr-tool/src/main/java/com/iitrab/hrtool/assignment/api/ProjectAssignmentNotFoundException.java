package com.iitrab.hrtool.assignment.api;

import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.exception.api.NotFoundException;
import com.iitrab.hrtool.project.api.Project;

/**
 * Exception indicating that the {@link Employee}
 * assigned to {@link Project}
 * was not found.
 */
public class ProjectAssignmentNotFoundException extends NotFoundException {

    private ProjectAssignmentNotFoundException(String message) {
        super(message);
    }

    public ProjectAssignmentNotFoundException(Long employeeId, Long projectId) {
        this("Employee with ID=%s assignment to project with ID=%s was not found".formatted(employeeId, projectId));
    }
}
