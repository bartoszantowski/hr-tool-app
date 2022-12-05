package com.iitrab.hrtool.projectassigning.internal;

import com.iitrab.hrtool.assignment.api.ProjectAssignment;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.project.api.Project;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class AssignmentMapper {

    AssignmentDto toDto(ProjectAssignment assignment) {
        return new AssignmentDto(assignment.getId(),
                                 Optional.ofNullable(assignment.getProject())
                                         .map(this::toDto)
                                         .orElse(null),
                                 Optional.ofNullable(assignment.getEmployee())
                                         .map(this::toDto)
                                         .orElse(null),
                                 assignment.getRole(),
                                 assignment.getStartDate(),
                                 assignment.getEndDate());
    }

    private ProjectDto toDto(Project project) {
        return new ProjectDto(project.getId(), project.getName());
    }

    private EmployeeDto toDto(Employee employee) {
        return new EmployeeDto(employee.getId(), employee.getName(), employee.getLastName(), employee.getEmail());
    }

}
