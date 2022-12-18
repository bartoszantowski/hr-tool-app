package com.iitrab.hrtool.projectmessage.internal;

import com.iitrab.hrtool.assignment.api.ProjectAssignmentNotFoundException;
import com.iitrab.hrtool.assignment.api.ProjectAssignmentProvider;
import com.iitrab.hrtool.client.api.Client;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.employee.api.EmployeeNotFoundException;
import com.iitrab.hrtool.employee.api.EmployeeProvider;
import com.iitrab.hrtool.mail.api.EmailDto;
import com.iitrab.hrtool.project.api.Project;
import com.iitrab.hrtool.project.api.ProjectNotFoundException;
import com.iitrab.hrtool.project.api.ProjectProvider;
import com.iitrab.hrtool.SampleTestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.iitrab.hrtool.projectmessage.internal.ProjectMessageDtoAssert.assertThatProjectMessageDto;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectMessageDetailCreatorTest {

    @Mock
    private ProjectAssignmentProvider projectAssignmentProvider;
    @Mock
    private ProjectProvider projectProvider;
    @Mock
    private EmployeeProvider employeeProvider;
    @Mock
    private ProjectMessageMapper projectMessageMapper;
    @InjectMocks
    private ProjectMessageDetailCreator projectMessageDetailCreator;

    @Captor
    private ArgumentCaptor<Project> projectCaptor;

    @Test
    void shouldReturnNotFound_whenCreating_thatAuthorDoesNotExist() {
        Long authorId = 2L;
        Long projectId = 5L;
        String subject = "subject_test";
        String content = "content_test";

        SendProjectMessageRequest sendProjectMessageRequest = new SendProjectMessageRequest(authorId, projectId, subject, content);
        ProjectMessageEvent projectMessageEvent = new ProjectMessageEvent(sendProjectMessageRequest, sendProjectMessageRequest);

        when(employeeProvider.getEmployee(authorId)).thenThrow(new EmployeeNotFoundException(authorId));

        assertThatThrownBy(() -> projectMessageDetailCreator.create(projectMessageEvent))
                .isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    void shouldReturnNotFound_whenCreating_thatProjectDoesNotExist() {
        Long authorId = 2L;
        Long projectId = 5L;
        String subject = "subject_test";
        String content = "content_test";
        Employee employee = SampleTestDataFactory.employee();

        SendProjectMessageRequest sendProjectMessageRequest = new SendProjectMessageRequest(authorId, projectId, subject, content);
        ProjectMessageEvent projectMessageEvent = new ProjectMessageEvent(sendProjectMessageRequest, sendProjectMessageRequest);

        when(employeeProvider.getEmployee(authorId)).thenReturn(Optional.of(employee));
        when(projectProvider.getProject(projectId)).thenThrow(new ProjectNotFoundException(projectId));

        assertThatThrownBy(() -> projectMessageDetailCreator.create(projectMessageEvent))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void shouldReturnNotFound_whenCreating_thatProjectAssignmentNotExist() {
        Long authorId = 2L;
        Long projectId = 5L;
        String subject = "subject_test";
        String content = "content_test";
        Employee author = SampleTestDataFactory.employee();
        Employee manager = SampleTestDataFactory.employee();
        Client client = SampleTestDataFactory.client(manager);
        Project project = SampleTestDataFactory.project(client);

        SendProjectMessageRequest sendProjectMessageRequest = new SendProjectMessageRequest(authorId, projectId, subject, content);
        ProjectMessageEvent projectMessageEvent = new ProjectMessageEvent(sendProjectMessageRequest, sendProjectMessageRequest);

        when(employeeProvider.getEmployee(authorId)).thenReturn(Optional.of(author));
        when(projectProvider.getProject(projectId)).thenReturn(Optional.of(project));

        assertThatThrownBy(() -> projectMessageDetailCreator.create(projectMessageEvent))
                .isInstanceOf(ProjectAssignmentNotFoundException.class);
    }

    @Test
    void shouldReturnProjectMessageDto_whenCreating() {
        Long authorId = 2L;
        Long projectId = 5L;
        String subject = "subject_test";
        String content = "content_test";
        Employee author = SampleTestDataFactory.employee();
        Employee employee = SampleTestDataFactory.employee();
        Employee manager = SampleTestDataFactory.employee();
        Client client = SampleTestDataFactory.client(manager);
        Project project = SampleTestDataFactory.project(client);

        SendProjectMessageRequest sendProjectMessageRequest = new SendProjectMessageRequest(authorId, projectId, subject, content);
        ProjectMessageEvent projectMessageEvent = new ProjectMessageEvent(sendProjectMessageRequest, sendProjectMessageRequest);

        String messageContent = "SENDER: " + author.getName() + " " + author.getEmail() +
                "\nPROJECT NAME: " + project.getName() +
                "\nMESSAGE:\n" + content;
        List<Employee> recipients = List.of(manager, employee);
        List<String> recipientsEmails = List.of(manager.getEmail(), employee.getEmail());
        ProjectMessageDto projectMessageDto = new ProjectMessageDto(recipientsEmails, messageContent, projectMessageEvent.getSubject());

        when(employeeProvider.getEmployee(authorId)).thenReturn(Optional.of(author));
        when(projectProvider.getProject(projectId)).thenReturn(Optional.of(project));
        when(projectAssignmentProvider.isAssignedToProject(author, project)).thenReturn(true);
        when(projectAssignmentProvider.getEmployeesAssignedToProject(project)).thenReturn(recipients);
        when(projectMessageMapper.toDto(recipientsEmails, messageContent, projectMessageEvent)).thenReturn(projectMessageDto);

        ProjectMessageDto expected = projectMessageDetailCreator.create(projectMessageEvent);

        verify(projectAssignmentProvider).getEmployeesAssignedToProject(projectCaptor.capture());
        assertThat(expected).isEqualTo(projectMessageDto);
        assertThatObject(projectCaptor.getValue()).isEqualTo(project);
    }
}
