package com.iitrab.hrtool.assignment.internal;

import com.iitrab.hrtool.assignment.api.ProjectAssignment;
import com.iitrab.hrtool.assignment.api.ProjectAssignmentProvider;
import com.iitrab.hrtool.assignment.api.ProjectAssignmentService;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.project.api.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
class ProjectAssignmentServiceImpl implements ProjectAssignmentService, ProjectAssignmentProvider {

    private final ProjectAssignmentRepository projectAssignmentRepository;
    private final ProjectAssignmentChecker assignmentChecker;

    @Override
    public boolean isAssignedToProject(Employee employee, Project project) {
        return projectAssignmentRepository.findByEmployeeAndProject(employee, project)
                                          .stream()
                                          .anyMatch(assignmentChecker::isActiveAssignment);
    }

    @Override
    public List<Employee> getEmployeesAssignedToProject(Project project) {
        return projectAssignmentRepository.findByProject(project)
                                          .stream()
                                          .filter(assignmentChecker::isActiveAssignment)
                                          .map(ProjectAssignment::getEmployee)
                                          .distinct()
                                          .toList();
    }

    @Override
    @Transactional
    public ProjectAssignment create(ProjectAssignment projectAssignment) {
        log.info("Saving project assignment {}", projectAssignment);
        if (projectAssignment.getId() != null) {
            throw new IllegalArgumentException("Assignment has already DB ID, update is not permitted!");
        }
        return projectAssignmentRepository.save(projectAssignment);
    }

}
