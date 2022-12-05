package com.iitrab.hrtool.assignment.internal;

import com.iitrab.hrtool.assignment.api.ProjectAssignment;
import com.iitrab.hrtool.client.api.Client;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.project.api.Project;
import com.iitrab.hrtool.SampleTestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectAssignmentServiceTest {

    @InjectMocks
    private ProjectAssignmentServiceImpl projectAssignmentService;

    @Mock
    private ProjectAssignmentRepository repositoryMock;
    @Mock
    private ProjectAssignmentChecker assignmentCheckerMock;

    @Test
    void shouldReturnFalse_whenCheckingIfEmployeeIsAssignedToProject_andEmployeeDoesNotHaveAssignmentsToThisProject() {
        Employee employee = SampleTestDataFactory.employee();
        Client client = SampleTestDataFactory.client(employee);
        Project project = SampleTestDataFactory.project(client);
        when(repositoryMock.findByEmployeeAndProject(employee, project)).thenReturn(List.of());

        boolean assignedToProject = projectAssignmentService.isAssignedToProject(employee, project);

        assertThat(assignedToProject).isFalse();
    }

    @Test
    void shouldReturnFalse_whenCheckingIfEmployeeIsAssignedToProject_andEmployeeHasNotActiveAssignment() {
        Employee employee = SampleTestDataFactory.employee();
        Client client = SampleTestDataFactory.client(employee);
        Project project = SampleTestDataFactory.project(client);
        ProjectAssignment assignment = new ProjectAssignment(employee,
                                                             project,
                                                             SampleTestDataFactory.DATE_IN_FUTURE,
                                                             SampleTestDataFactory.DATE_IN_FUTURE,
                                                             "Developer");
        when(repositoryMock.findByEmployeeAndProject(employee, project)).thenReturn(List.of(assignment));
        when(assignmentCheckerMock.isActiveAssignment(assignment)).thenReturn(Boolean.FALSE);

        boolean assignedToProject = projectAssignmentService.isAssignedToProject(employee, project);

        assertThat(assignedToProject).isFalse();
    }

    @Test
    void shouldReturnTrue_whenCheckingIfEmployeeIsAssignedToProject_andEmployeeHasActiveAssignment() {
        Employee employee = SampleTestDataFactory.employee();
        Client client = SampleTestDataFactory.client(employee);
        Project project = SampleTestDataFactory.project(client);
        ProjectAssignment assignment = new ProjectAssignment(employee,
                                                             project,
                                                             SampleTestDataFactory.DATE_IN_PAST,
                                                             SampleTestDataFactory.DATE_IN_FUTURE,
                                                             "Developer");
        when(repositoryMock.findByEmployeeAndProject(employee, project)).thenReturn(List.of(assignment));
        when(assignmentCheckerMock.isActiveAssignment(assignment)).thenReturn(Boolean.TRUE);

        boolean assignedToProject = projectAssignmentService.isAssignedToProject(employee, project);

        assertThat(assignedToProject).isTrue();
    }

    @Test
    void shouldReturnTrue_whenCheckingIfEmployeeIsAssignedToProject_andEmployeeHasActiveAndNotActiveAssignments() {
        Employee employee = SampleTestDataFactory.employee();
        Client client = SampleTestDataFactory.client(employee);
        Project project = SampleTestDataFactory.project(client);
        ProjectAssignment assignment1 = new ProjectAssignment(employee,
                                                              project,
                                                              SampleTestDataFactory.DATE_IN_PAST,
                                                              SampleTestDataFactory.DATE_IN_FUTURE,
                                                              "Developer");
        ProjectAssignment assignment2 = new ProjectAssignment(employee,
                                                              project,
                                                              SampleTestDataFactory.DATE_IN_PAST,
                                                              SampleTestDataFactory.DATE_IN_PAST,
                                                              "Developer");
        when(repositoryMock.findByEmployeeAndProject(employee, project)).thenReturn(List.of(assignment1, assignment2));
        when(assignmentCheckerMock.isActiveAssignment(assignment1)).thenReturn(Boolean.FALSE);
        when(assignmentCheckerMock.isActiveAssignment(assignment2)).thenReturn(Boolean.TRUE);

        boolean assignedToProject = projectAssignmentService.isAssignedToProject(employee, project);

        assertThat(assignedToProject).isTrue();
    }

    @Test
    void shouldReturnEmployeesWithCurrentAssignments_whenGettingEmployeesAssignedToProject() {
        Employee employee1 = SampleTestDataFactory.employee();
        Employee employee2 = SampleTestDataFactory.employee();
        Client client = SampleTestDataFactory.client(employee1);
        Project project = SampleTestDataFactory.project(client);
        ProjectAssignment assignment1 = new ProjectAssignment(employee1,
                                                              project,
                                                              SampleTestDataFactory.DATE_IN_PAST,
                                                              SampleTestDataFactory.DATE_IN_FUTURE,
                                                              "Developer");
        ProjectAssignment assignment2 = new ProjectAssignment(employee2,
                                                              project,
                                                              SampleTestDataFactory.DATE_IN_PAST,
                                                              SampleTestDataFactory.DATE_IN_PAST,
                                                              "Developer");
        when(repositoryMock.findByProject(project)).thenReturn(List.of(assignment1, assignment2));
        when(assignmentCheckerMock.isActiveAssignment(assignment1)).thenReturn(Boolean.TRUE);
        when(assignmentCheckerMock.isActiveAssignment(assignment2)).thenReturn(Boolean.TRUE);

        List<Employee> employees = projectAssignmentService.getEmployeesAssignedToProject(project);

        assertThat(employees).containsExactly(employee1, employee2);
    }

    @Test
    void shouldFilterEmployeesWithNotActiveAssignments_whenGettingEmployeesAssignedToProject() {
        Employee employee1 = SampleTestDataFactory.employee();
        Employee employee2 = SampleTestDataFactory.employee();
        Client client = SampleTestDataFactory.client(employee1);
        Project project = SampleTestDataFactory.project(client);
        ProjectAssignment assignment1 = new ProjectAssignment(employee1,
                                                              project,
                                                              SampleTestDataFactory.DATE_IN_PAST,
                                                              SampleTestDataFactory.DATE_IN_FUTURE,
                                                              "Developer");
        ProjectAssignment assignment2 = new ProjectAssignment(employee2,
                                                              project,
                                                              SampleTestDataFactory.DATE_IN_PAST,
                                                              SampleTestDataFactory.DATE_IN_PAST,
                                                              "Developer");
        when(repositoryMock.findByProject(project)).thenReturn(List.of(assignment1, assignment2));
        when(assignmentCheckerMock.isActiveAssignment(assignment1)).thenReturn(Boolean.TRUE);
        when(assignmentCheckerMock.isActiveAssignment(assignment2)).thenReturn(Boolean.FALSE);

        List<Employee> employees = projectAssignmentService.getEmployeesAssignedToProject(project);

        assertThat(employees).containsExactly(employee1);
    }

    @Test
    void shouldReturnEmployeeOnlyOnce_whenGettingEmployeesAssignedToProject_andEmployeeHasMultipleActiveAssignments() {
        Employee employee = SampleTestDataFactory.employee();
        Client client = SampleTestDataFactory.client(employee);
        Project project = SampleTestDataFactory.project(client);
        ProjectAssignment assignment1 = new ProjectAssignment(employee,
                                                              project,
                                                              SampleTestDataFactory.DATE_IN_PAST,
                                                              SampleTestDataFactory.DATE_IN_FUTURE,
                                                              "Developer");
        ProjectAssignment assignment2 = new ProjectAssignment(employee,
                                                              project,
                                                              SampleTestDataFactory.DATE_IN_PAST,
                                                              SampleTestDataFactory.DATE_IN_PAST,
                                                              "Developer");
        when(repositoryMock.findByProject(project)).thenReturn(List.of(assignment1, assignment2));
        when(assignmentCheckerMock.isActiveAssignment(assignment1)).thenReturn(Boolean.TRUE);
        when(assignmentCheckerMock.isActiveAssignment(assignment2)).thenReturn(Boolean.TRUE);

        List<Employee> employees = projectAssignmentService.getEmployeesAssignedToProject(project);

        assertThat(employees).containsExactly(employee);
    }

    @Test
    void shouldPersistAssignment_whenSavingProjectAssignment() {
        Employee employee = SampleTestDataFactory.employee();
        Client client = SampleTestDataFactory.client(employee);
        Project project = SampleTestDataFactory.project(client);
        ProjectAssignment assignment = new ProjectAssignment(employee,
                                                             project,
                                                             SampleTestDataFactory.DATE_IN_PAST,
                                                             SampleTestDataFactory.DATE_IN_FUTURE,
                                                             "Developer");

        projectAssignmentService.create(assignment);

        verify(repositoryMock).save(assignment);
    }

    @Test
    void shouldReturnPersistedAssignment_whenSavingProjectAssignment() {
        Employee employee = SampleTestDataFactory.employee();
        Client client = SampleTestDataFactory.client(employee);
        Project project = SampleTestDataFactory.project(client);
        ProjectAssignment assignment = new ProjectAssignment(employee,
                                                             project,
                                                             SampleTestDataFactory.DATE_IN_PAST,
                                                             SampleTestDataFactory.DATE_IN_FUTURE,
                                                             "Developer");
        when(repositoryMock.save(assignment)).then(returnsFirstArg());

        ProjectAssignment saved = projectAssignmentService.create(assignment);

        assertThat(saved).isSameAs(assignment);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenSavingProjectAssignment_withId() {
        ProjectAssignment assignmentMock = mock(ProjectAssignment.class);
        when(assignmentMock.getId()).thenReturn(1L);

        assertThatThrownBy(() -> projectAssignmentService.create(assignmentMock)).isInstanceOf(IllegalArgumentException.class);
    }

}