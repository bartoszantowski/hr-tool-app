package com.iitrab.hrtool.projectassigning.internal;

import com.iitrab.hrtool.assignment.api.ProjectAssignment;
import com.iitrab.hrtool.assignment.api.ProjectAssignmentService;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.employee.api.EmployeeNotFoundException;
import com.iitrab.hrtool.employee.api.EmployeeProvider;
import com.iitrab.hrtool.project.api.Project;
import com.iitrab.hrtool.project.api.ProjectNotFoundException;
import com.iitrab.hrtool.project.api.ProjectProvider;
import com.iitrab.hrtool.SampleTestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.iitrab.hrtool.assignment.api.ProjectAssignmentAssert.assertThatProjectAssignment;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceTest {

    private static final long EMPLOYEE_ID = 2L;
    private static final String ROLE = "Developer";
    private static final long PROJECT_ID = 1L;
    @InjectMocks
    private AssignmentService assignmentService;
    @Mock
    private ProjectProvider projectProviderMock;
    @Mock
    private EmployeeProvider employeeProviderMock;
    @Mock
    private ProjectAssignmentService projectAssignmentServiceMock;
    @Mock
    private AssignmentValidator assignmentValidatorMock;
    @Captor
    private ArgumentCaptor<ProjectAssignment> projectAssignmentCaptor;

    @Test
    void shouldThrowProjectNotFoundException_whenAssigningToProject_withNotExistingId() {
        when(projectProviderMock.getProject(PROJECT_ID)).thenReturn(Optional.empty());
        AssignToProjectRequest request = new AssignToProjectRequest(EMPLOYEE_ID,
                                                                    ROLE,
                                                                    SampleTestDataFactory.DATE_IN_PAST,
                                                                    SampleTestDataFactory.DATE_IN_FUTURE);

        assertThatThrownBy(() -> assignmentService.assignToProject(PROJECT_ID, request))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void shouldThrowEmployeeNotFoundException_whenAssigningToProject_employeeThatDoesNotExist() {
        Project project = SampleTestDataFactory.project(SampleTestDataFactory.client(SampleTestDataFactory.employee()));
        when(projectProviderMock.getProject(PROJECT_ID)).thenReturn(Optional.of(project));
        when(employeeProviderMock.getEmployee(EMPLOYEE_ID)).thenReturn(Optional.empty());
        AssignToProjectRequest request = new AssignToProjectRequest(EMPLOYEE_ID,
                                                                    ROLE,
                                                                    SampleTestDataFactory.DATE_IN_PAST,
                                                                    SampleTestDataFactory.DATE_IN_FUTURE);

        assertThatThrownBy(() -> assignmentService.assignToProject(PROJECT_ID, request))
                .isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    void shouldThrowAssignmentNotPossibleException_whenAssigningToProject_andAssignmentIsNotPossible() {
        Employee employee = SampleTestDataFactory.employee();
        Project project = SampleTestDataFactory.project(SampleTestDataFactory.client(SampleTestDataFactory.employee()));
        when(projectProviderMock.getProject(PROJECT_ID)).thenReturn(Optional.of(project));
        when(employeeProviderMock.getEmployee(EMPLOYEE_ID)).thenReturn(Optional.of(employee));
        Mockito.doThrow(AssignmentNotPossibleException.class).when(assignmentValidatorMock)
                                                     .canBeAssigned(any(), any());
        AssignToProjectRequest request = new AssignToProjectRequest(EMPLOYEE_ID,
                                                                    ROLE,
                                                                    SampleTestDataFactory.DATE_IN_PAST,
                                                                    SampleTestDataFactory.DATE_IN_FUTURE);

        assertThatThrownBy(() -> assignmentService.assignToProject(PROJECT_ID, request))
                .isInstanceOf(AssignmentNotPossibleException.class);
    }

    @Test
    void shouldSaveCreatedAssignment_whenAssigningToProject() {
        Employee employee = SampleTestDataFactory.employee();
        Project project = SampleTestDataFactory.project(SampleTestDataFactory.client(employee));
        when(projectProviderMock.getProject(PROJECT_ID)).thenReturn(Optional.of(project));
        when(employeeProviderMock.getEmployee(EMPLOYEE_ID)).thenReturn(Optional.of(employee));
        doNothing().when(assignmentValidatorMock)
                   .canBeAssigned(employee, project);
        AssignToProjectRequest request = new AssignToProjectRequest(EMPLOYEE_ID,
                                                                    ROLE,
                                                                    SampleTestDataFactory.DATE_IN_PAST,
                                                                    SampleTestDataFactory.DATE_IN_FUTURE);

        assignmentService.assignToProject(PROJECT_ID, request);

        verify(projectAssignmentServiceMock).create(projectAssignmentCaptor.capture());
        assertThatProjectAssignment(projectAssignmentCaptor.getValue())
                .hasRole(ROLE)
                .startsOn(SampleTestDataFactory.DATE_IN_PAST)
                .endsOn(SampleTestDataFactory.DATE_IN_FUTURE)
                .isForEmployeeThat(emp -> emp.isSameAs(employee))
                .isToProject(proj -> proj.isSameAs(project));
    }

    @Test
    void shouldReturnCreatedAssignment_whenAssigningToProject() {
        Employee employee = SampleTestDataFactory.employee();
        Project project = SampleTestDataFactory.project(SampleTestDataFactory.client(employee));
        when(projectProviderMock.getProject(PROJECT_ID)).thenReturn(Optional.of(project));
        when(employeeProviderMock.getEmployee(EMPLOYEE_ID)).thenReturn(Optional.of(employee));
        when(projectAssignmentServiceMock.create(any())).then(returnsFirstArg());
        doNothing().when(assignmentValidatorMock)
                   .canBeAssigned(employee, project);
        AssignToProjectRequest request = new AssignToProjectRequest(EMPLOYEE_ID,
                                                                    ROLE,
                                                                    SampleTestDataFactory.DATE_IN_PAST,
                                                                    SampleTestDataFactory.DATE_IN_FUTURE);

        ProjectAssignment projectAssignment = assignmentService.assignToProject(PROJECT_ID, request);

        assertThatProjectAssignment(projectAssignment)
                .hasRole(ROLE)
                .startsOn(SampleTestDataFactory.DATE_IN_PAST)
                .endsOn(SampleTestDataFactory.DATE_IN_FUTURE)
                .isForEmployeeThat(emp -> emp.isSameAs(employee))
                .isToProject(proj -> proj.isSameAs(project));
    }

}