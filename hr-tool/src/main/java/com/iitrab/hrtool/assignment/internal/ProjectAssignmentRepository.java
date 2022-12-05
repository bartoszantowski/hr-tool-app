package com.iitrab.hrtool.assignment.internal;

import com.iitrab.hrtool.assignment.api.ProjectAssignment;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.project.api.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

interface ProjectAssignmentRepository extends JpaRepository<ProjectAssignment, Long> {

    /**
     * Query for finding all assignments (past, current and future) of the employee in the project.
     *
     * @param employee employee to check
     * @param project  project to check
     * @return all assignments of the employee in the project
     */
    default List<ProjectAssignment> findByEmployeeAndProject(Employee employee, Project project) {
        return findAll().stream()
                        .filter(isForEmployee(employee).and(isInProject(project)))
                        .toList();
    }

    /**
     * Query for finding all assignments (past, current and future) to the project.
     *
     * @param project project to check
     * @return all assignments to the project
     */
    default List<ProjectAssignment> findByProject(Project project) {
        return findAll().stream()
                        .filter(isInProject(project))
                        .toList();
    }

    private Predicate<ProjectAssignment> isForEmployee(final Employee employee) {
        return assignment -> Objects.equals(assignment.getEmployee()
                                                      .getId(), employee.getId());
    }

    private Predicate<ProjectAssignment> isInProject(Project project) {
        return assignment -> Objects.equals(assignment.getProject()
                                                      .getId(), project.getId());
    }

}
