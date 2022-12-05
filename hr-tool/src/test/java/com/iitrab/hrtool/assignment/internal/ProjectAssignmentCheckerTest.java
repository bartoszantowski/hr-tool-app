package com.iitrab.hrtool.assignment.internal;

import com.iitrab.hrtool.assignment.api.ProjectAssignment;
import com.iitrab.hrtool.client.api.Client;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.project.api.Project;
import com.iitrab.hrtool.util.TimeProvider;
import com.iitrab.hrtool.SampleTestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectAssignmentCheckerTest {

    private static final String ROLE = "Engineer";
    private static final LocalDate NOW = LocalDate.now();
    private static final LocalDate DATE_IN_PAST = NOW.minusDays(1L);
    private static final LocalDate DATE_IN_FUTURE = NOW.plusDays(1L);
    @InjectMocks
    private ProjectAssignmentChecker projectAssignmentChecker;
    @Mock
    private TimeProvider timeProviderMock;

    @Test
    void shouldReturnTrue_whenCheckingIfAssignmentIsActive_andItStartedInThePast_andHasNoEndDate() {
        Employee employee = SampleTestDataFactory.employee();
        Client client = SampleTestDataFactory.client(employee);
        Project project = SampleTestDataFactory.project(client);
        ProjectAssignment projectAssignment = new ProjectAssignment(employee, project, DATE_IN_PAST, null, ROLE);
        when(timeProviderMock.dateNow()).thenReturn(NOW);

        boolean activeAssignment = projectAssignmentChecker.isActiveAssignment(projectAssignment);

        assertThat(activeAssignment).isTrue();
    }

    @Test
    void shouldReturnTrue_whenCheckingIfAssignmentIsActive_andItStartedNow_andHasNoEndDate() {
        Employee employee = SampleTestDataFactory.employee();
        Client client = SampleTestDataFactory.client(employee);
        Project project = SampleTestDataFactory.project(client);
        ProjectAssignment projectAssignment = new ProjectAssignment(employee, project, NOW, null, ROLE);
        when(timeProviderMock.dateNow()).thenReturn(NOW);

        boolean activeAssignment = projectAssignmentChecker.isActiveAssignment(projectAssignment);

        assertThat(activeAssignment).isTrue();
    }

    @Test
    void shouldReturnTrue_whenCheckingIfAssignmentIsActive_andItStartedInThePast_andHasEndDateInTheFuture() {
        Employee employee = SampleTestDataFactory.employee();
        Client client = SampleTestDataFactory.client(employee);
        Project project = SampleTestDataFactory.project(client);
        ProjectAssignment projectAssignment = new ProjectAssignment(employee,
                                                                    project,
                                                                    DATE_IN_PAST,
                                                                    DATE_IN_FUTURE,
                                                                    ROLE);
        when(timeProviderMock.dateNow()).thenReturn(NOW);

        boolean activeAssignment = projectAssignmentChecker.isActiveAssignment(projectAssignment);

        assertThat(activeAssignment).isTrue();
    }

    @Test
    void shouldReturnTrue_whenCheckingIfAssignmentIsActive_andItStartedInThePast_andHasEndDateNow() {
        Employee employee = SampleTestDataFactory.employee();
        Client client = SampleTestDataFactory.client(employee);
        Project project = SampleTestDataFactory.project(client);
        ProjectAssignment projectAssignment = new ProjectAssignment(employee, project, DATE_IN_PAST, NOW, ROLE);
        when(timeProviderMock.dateNow()).thenReturn(NOW);

        boolean activeAssignment = projectAssignmentChecker.isActiveAssignment(projectAssignment);

        assertThat(activeAssignment).isTrue();
    }

    @Test
    void shouldReturnFalse_whenCheckingIfAssignmentIsActive_andItStartsInTheFuture_andHasNoEndDate() {
        Employee employee = SampleTestDataFactory.employee();
        Client client = SampleTestDataFactory.client(employee);
        Project project = SampleTestDataFactory.project(client);
        ProjectAssignment projectAssignment = new ProjectAssignment(employee, project, DATE_IN_FUTURE, null, ROLE);
        when(timeProviderMock.dateNow()).thenReturn(NOW);

        boolean activeAssignment = projectAssignmentChecker.isActiveAssignment(projectAssignment);

        assertThat(activeAssignment).isFalse();
    }

    @Test
    void shouldReturnFalse_whenCheckingIfAssignmentIsActive_andItStartsInTheFuture_andEndsInFuture() {
        Employee employee = SampleTestDataFactory.employee();
        Client client = SampleTestDataFactory.client(employee);
        Project project = SampleTestDataFactory.project(client);
        ProjectAssignment projectAssignment = new ProjectAssignment(employee,
                                                                    project,
                                                                    DATE_IN_FUTURE,
                                                                    DATE_IN_FUTURE,
                                                                    ROLE);
        when(timeProviderMock.dateNow()).thenReturn(NOW);

        boolean activeAssignment = projectAssignmentChecker.isActiveAssignment(projectAssignment);

        assertThat(activeAssignment).isFalse();
    }

    @Test
    void shouldReturnFalse_whenCheckingIfAssignmentIsActive_andItStartsInThePast_andEndsInThePast() {
        Employee employee = SampleTestDataFactory.employee();
        Client client = SampleTestDataFactory.client(employee);
        Project project = SampleTestDataFactory.project(client);
        ProjectAssignment projectAssignment = new ProjectAssignment(employee,
                                                                    project,
                                                                    DATE_IN_PAST,
                                                                    DATE_IN_PAST,
                                                                    ROLE);
        when(timeProviderMock.dateNow()).thenReturn(NOW);

        boolean activeAssignment = projectAssignmentChecker.isActiveAssignment(projectAssignment);

        assertThat(activeAssignment).isFalse();
    }

}