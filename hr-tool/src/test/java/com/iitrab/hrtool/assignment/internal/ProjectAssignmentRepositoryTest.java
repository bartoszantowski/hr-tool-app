package com.iitrab.hrtool.assignment.internal;

import com.iitrab.hrtool.IntegrationTestBase;
import com.iitrab.hrtool.assignment.api.ProjectAssignment;
import com.iitrab.hrtool.client.api.Client;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.project.api.Project;
import com.iitrab.hrtool.SampleTestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.iitrab.hrtool.assignment.api.ProjectAssignmentAssert.assertThatProjectAssignment;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class ProjectAssignmentRepositoryTest extends IntegrationTestBase {

    @Autowired
    private ProjectAssignmentRepository projectAssignmentRepository;

    @Test
    void shouldFindMatchingAssignments_whenFindingAssignmentsByProject() {
        Employee employee1 = existingEmployee(SampleTestDataFactory.employee());
        Client client = existingClient(SampleTestDataFactory.client(employee1));
        Project project1 = existingProject(SampleTestDataFactory.project(client));
        Project project2 = existingProject(SampleTestDataFactory.project(client));
        ProjectAssignment assignment1 = existingProjectAssignment(SampleTestDataFactory.activeAssignment(employee1, project1));
        Employee employee2 = existingEmployee(SampleTestDataFactory.employee());
        existingProjectAssignment(SampleTestDataFactory.activeAssignment(employee2, project2));

        List<ProjectAssignment> assignments = projectAssignmentRepository.findByProject(project1);

        assertThat(assignments).hasSize(1)
                               .anySatisfy(assignment -> assertThatProjectAssignment(assignment).hasId(assignment1.getId())
                                                                                                .isForEmployeeThat(
                                                                                                        employee -> employee.hasId(
                                                                                                                employee1.getId()))
                                                                                                .isToProject(project -> project.hasId(
                                                                                                        project1.getId()))
                                                                                                .startsOn(assignment1.getStartDate())
                                                                                                .endsOn(assignment1.getEndDate())
                                                                                                .hasRole(assignment1.getRole()));
    }

    @Test
    void shouldNotFindAssignments_whenFindingAssignmentsByProject_andProjectHasNone() {
        Employee employee1 = existingEmployee(SampleTestDataFactory.employee());
        Client client = existingClient(SampleTestDataFactory.client(employee1));
        Project project1 = existingProject(SampleTestDataFactory.project(client));
        Project project2 = existingProject(SampleTestDataFactory.project(client));
        existingProjectAssignment(SampleTestDataFactory.activeAssignment(employee1, project1));

        List<ProjectAssignment> assignments = projectAssignmentRepository.findByProject(project2);

        assertThat(assignments).isEmpty();
    }

    @Test
    void shouldFindMatchingAssignments_whenFindingAssignmentsByEmployeeAndProject() {
        Employee employee1 = existingEmployee(SampleTestDataFactory.employee());
        Client client = existingClient(SampleTestDataFactory.client(employee1));
        Project project1 = existingProject(SampleTestDataFactory.project(client));
        Project project2 = existingProject(SampleTestDataFactory.project(client));
        ProjectAssignment assignment1 = existingProjectAssignment(SampleTestDataFactory.activeAssignment(employee1, project1));
        Employee employee2 = existingEmployee(SampleTestDataFactory.employee());
        existingProjectAssignment(SampleTestDataFactory.activeAssignment(employee2, project2));

        List<ProjectAssignment> assignments = projectAssignmentRepository.findByEmployeeAndProject(employee1, project1);

        assertThat(assignments).hasSize(1)
                               .anySatisfy(assignment -> assertThatProjectAssignment(assignment).hasId(assignment1.getId())
                                                                                                .isForEmployeeThat(
                                                                                                        employee -> employee.hasId(
                                                                                                                employee1.getId()))
                                                                                                .isToProject(project -> project.hasId(
                                                                                                        project1.getId()))
                                                                                                .startsOn(assignment1.getStartDate())
                                                                                                .endsOn(assignment1.getEndDate())
                                                                                                .hasRole(assignment1.getRole()));
    }

    @Test
    void shouldFindAllEmployeeAssignmentsInProject_whenFindingAssignmentsByEmployeeAndProject_andEmployeeHasMultipleAssignmentsInTheProject() {
        Employee employee1 = existingEmployee(SampleTestDataFactory.employee());
        Client client = existingClient(SampleTestDataFactory.client(employee1));
        Project project1 = existingProject(SampleTestDataFactory.project(client));
        ProjectAssignment assignment1 = existingProjectAssignment(SampleTestDataFactory.activeAssignment(employee1, project1));
        ProjectAssignment assignment2 = existingProjectAssignment(SampleTestDataFactory.activeAssignment(employee1, project1));

        List<ProjectAssignment> assignments = projectAssignmentRepository.findByEmployeeAndProject(employee1, project1);

        assertThat(assignments).hasSize(2)
                               .anySatisfy(assignment -> assertThatProjectAssignment(assignment).hasId(assignment1.getId())
                                                                                                .isForEmployeeThat(
                                                                                                        employee -> employee.hasId(
                                                                                                                employee1.getId()))
                                                                                                .isToProject(project -> project.hasId(
                                                                                                        project1.getId()))
                                                                                                .startsOn(assignment1.getStartDate())
                                                                                                .endsOn(assignment1.getEndDate())
                                                                                                .hasRole(assignment1.getRole()))
                               .anySatisfy(assignment -> assertThatProjectAssignment(assignment).hasId(assignment2.getId())
                                                                                                .isForEmployeeThat(
                                                                                                        employee -> employee.hasId(
                                                                                                                employee1.getId()))
                                                                                                .isToProject(project -> project.hasId(
                                                                                                        project1.getId()))
                                                                                                .startsOn(assignment2.getStartDate())
                                                                                                .endsOn(assignment2.getEndDate())
                                                                                                .hasRole(assignment2.getRole()));
    }

    @Test
    void shouldFindOnlyAssignmentsFromTheProject_whenFindingAssignmentsByEmployeeAndProject_andEmployeeHasAssignmentsInTheMultipleProjects() {
        Employee employee1 = existingEmployee(SampleTestDataFactory.employee());
        Client client = existingClient(SampleTestDataFactory.client(employee1));
        Project project1 = existingProject(SampleTestDataFactory.project(client));
        Project project2 = existingProject(SampleTestDataFactory.project(client));
        ProjectAssignment assignment1 = existingProjectAssignment(SampleTestDataFactory.activeAssignment(employee1, project1));
        existingProjectAssignment(SampleTestDataFactory.activeAssignment(employee1, project2));

        List<ProjectAssignment> assignments = projectAssignmentRepository.findByEmployeeAndProject(employee1, project1);

        assertThat(assignments).hasSize(1)
                               .anySatisfy(assignment -> assertThatProjectAssignment(assignment).hasId(assignment1.getId())
                                                                                                .isForEmployeeThat(
                                                                                                        employee -> employee.hasId(
                                                                                                                employee1.getId()))
                                                                                                .isToProject(project -> project.hasId(
                                                                                                        project1.getId()))
                                                                                                .startsOn(assignment1.getStartDate())
                                                                                                .endsOn(assignment1.getEndDate())
                                                                                                .hasRole(assignment1.getRole()));
    }

    @Test
    void shouldNotFindEmployeeAssignments_whenFindingAssignmentsByEmployeeAndProject_andEmployeeDoesNotHaveAssignmentsInTheProject() {
        Employee employee1 = existingEmployee(SampleTestDataFactory.employee());
        Client client = existingClient(SampleTestDataFactory.client(employee1));
        Project project1 = existingProject(SampleTestDataFactory.project(client));

        List<ProjectAssignment> assignments = projectAssignmentRepository.findByEmployeeAndProject(employee1, project1);

        assertThat(assignments).isEmpty();
    }

}