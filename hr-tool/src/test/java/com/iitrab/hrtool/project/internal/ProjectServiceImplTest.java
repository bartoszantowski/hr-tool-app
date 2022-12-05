package com.iitrab.hrtool.project.internal;

import com.iitrab.hrtool.client.api.Client;
import com.iitrab.hrtool.client.api.ClientNotFoundException;
import com.iitrab.hrtool.client.api.ClientProvider;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.project.api.Project;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.iitrab.hrtool.SampleTestDataFactory.*;
import static com.iitrab.hrtool.project.api.ProjectAssert.assertThatProject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Mock
    private ProjectRepository projectRepositoryMock;
    @Mock
    private ClientProvider clientProviderMock;
    @Captor
    private ArgumentCaptor<Project> projectCaptor;

    @Test
    void shouldReturnAllProjects_whenFindingAllProjects() {
        Employee employee = employee();
        Client client = client(employee);
        Project project = project(client);
        when(projectRepositoryMock.findAll()).thenReturn(List.of(project));

        List<Project> projects = projectService.findAllProjects();

        assertThat(projects).containsExactly(project);
    }

    @Test
    void shouldReturnFoundProject_whenFindingProjectById() {
        Employee employee = employee();
        Client client = client(employee);
        Project project = project(client);
        long projectId = 1L;
        when(projectRepositoryMock.findById(projectId)).thenReturn(Optional.of(project));

        Optional<Project> foundProject = projectService.getProject(projectId);

        assertThat(foundProject).contains(project);
    }

    @Test
    void shouldReturnEmptyOptional_whenFindingProjectById_andItDoesNotExist() {
        long projectId = 1L;
        when(projectRepositoryMock.findById(projectId)).thenReturn(Optional.empty());

        Optional<Project> foundProject = projectService.getProject(projectId);

        assertThat(foundProject).isEmpty();
    }

    @Test
    void shouldThrowException_whenCreatingProject_forNonExistingClient() {
        long clientId = 1L;
        CreateProjectRequest createProjectRequest = new CreateProjectRequest(clientId, "Glacier");
        when(clientProviderMock.getClient(clientId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.create(createProjectRequest)).isInstanceOf(
                ClientNotFoundException.class);
    }

    @Test
    void shouldPersistProject_whenCreatingProject() {
        Employee employee = employee();
        long clientId = 1L;
        Client client = client(employee);
        String projectName = "Glacier";
        CreateProjectRequest createProjectRequest = new CreateProjectRequest(clientId, projectName);
        when(clientProviderMock.getClient(clientId)).thenReturn(Optional.of(client));

        projectService.create(createProjectRequest);

        verify(projectRepositoryMock).save(projectCaptor.capture());

        assertThatProject(projectCaptor.getValue()).hasName(projectName)
                                                   .isForClientThat(clientAssert -> clientAssert.isSameAs(client));
    }

    @Test
    void shouldReturnCreatedProject_whenCreatingProject() {
        Employee employee = employee();
        long clientId = 1L;
        Client client = client(employee);
        String projectName = "Glacier";
        CreateProjectRequest createProjectRequest = new CreateProjectRequest(clientId, projectName);
        when(clientProviderMock.getClient(clientId)).thenReturn(Optional.of(client));
        when(projectRepositoryMock.save(any())).then(returnsFirstArg());

        Project project = projectService.create(createProjectRequest);

        assertThatProject(project).hasName(projectName)
                                  .isForClientThat(clientAssert -> clientAssert.isSameAs(client));
    }

}