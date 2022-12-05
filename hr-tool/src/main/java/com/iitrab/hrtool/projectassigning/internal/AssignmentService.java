package com.iitrab.hrtool.projectassigning.internal;

import com.iitrab.hrtool.assignment.api.ProjectAssignment;
import com.iitrab.hrtool.assignment.api.ProjectAssignmentService;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.employee.api.EmployeeNotFoundException;
import com.iitrab.hrtool.employee.api.EmployeeProvider;
import com.iitrab.hrtool.project.api.Project;
import com.iitrab.hrtool.project.api.ProjectNotFoundException;
import com.iitrab.hrtool.project.api.ProjectProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for operations related to the employee to project assignments.
 */
@Service
@RequiredArgsConstructor
@Slf4j
class AssignmentService {

    private final ProjectProvider projectProvider;
    private final EmployeeProvider employeeProvider;
    private final ProjectAssignmentService projectAssignmentService;
    private final AssignmentValidator assignmentValidator;

    /**
     * Assigns the employee to the project.
     * Implementation might throw {@link AssignmentNotPossibleException} if the assignment is not permitted.
     *
     * @param projectId project the employee should be assigned to
     * @param request   assignment information
     * @return created assignment
     */
    @Transactional
    public ProjectAssignment assignToProject(Long projectId, AssignToProjectRequest request) {
        log.debug("Assigning employee with ID={} to the project with ID={}", request.employeeId(), projectId);
        Project project = projectProvider.getProject(projectId)
                                         .orElseThrow(() -> new ProjectNotFoundException(projectId));
        Employee employee = employeeProvider.getEmployee(request.employeeId())
                                            .orElseThrow(() -> new EmployeeNotFoundException(
                                                    request.employeeId()));
        assignmentValidator.canBeAssigned(employee, project);
        ProjectAssignment projectAssignment = createAssignment(request, project, employee);
        log.info("Employee was assigned to the project {}", projectAssignment);
        return projectAssignmentService.create(projectAssignment);
    }

    private ProjectAssignment createAssignment(AssignToProjectRequest request,
                                               Project project,
                                               Employee employee) {
        return new ProjectAssignment(employee,
                                     project,
                                     request.startDate(),
                                     request.endDate(),
                                     request.role());
    }

}
