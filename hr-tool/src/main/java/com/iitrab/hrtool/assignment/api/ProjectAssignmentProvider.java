package com.iitrab.hrtool.assignment.api;

import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.project.api.Project;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * API interface for query operations on the {@link ProjectAssignment} entities.
 * Implementation does not have to open new DB transaction. If the returned data has to be managed by the {@link EntityManager},
 * then the caller must open the transaction itself.
 */
public interface ProjectAssignmentProvider {

    /**
     * Returns all {@link Employee}, that are currently assigned to the provided {@link Project}. It does not return the employees,
     * that have been assigned to the project in the past or will be assigned in the future.
     *
     * @param project project, that the employees are searched for
     * @return all employees currently assigned to the project
     */
    List<Employee> getEmployeesAssignedToProject(Project project);

    /**
     * Checks whether the employee is currently assigned to the project. It does not check past assignments nor the future ones.
     *
     * @param employee checked employee
     * @param project  checked project
     * @return true if employee is currently assigned to the project, false otherwise
     */
    boolean isAssignedToProject(Employee employee, Project project);

}
