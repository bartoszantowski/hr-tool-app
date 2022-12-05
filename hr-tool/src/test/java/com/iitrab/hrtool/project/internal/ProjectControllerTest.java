package com.iitrab.hrtool.project.internal;

import com.iitrab.hrtool.client.api.Client;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.project.api.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static com.iitrab.hrtool.SampleTestDataFactory.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    @Mock
    private ProjectServiceImpl projectServiceMock;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ProjectController controller = new ProjectController(projectServiceMock,
                                                             new ProjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .build();
    }

    @Test
    void shouldReturnAllProjects_whenGettingAllProjects() throws Exception {
        Employee employee = employee();
        Client client = client(employee);
        Project project = project(client);
        when(projectServiceMock.findAllProjects()).thenReturn(List.of(project));

        mockMvc.perform(get("/v1/projects"))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].name").value(project.getName()))
               .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void shouldReturnEmptyList_whenGettingProjects_andNoneIsPresent() throws Exception {
        when(projectServiceMock.findAllProjects()).thenReturn(List.of());

        mockMvc.perform(get("/v1/projects"))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldReturnProject_whenGettingProjectById() throws Exception {
        Employee employee = employee();
        Client client = client(employee);
        Project project = project(client);
        long projectId = 1L;
        when(projectServiceMock.getProject(projectId)).thenReturn(Optional.of(project));

        mockMvc.perform(get("/v1/projects/{projectId}", projectId))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value(project.getName()));
    }

    @Test
    void shouldReturnNotFound_whenGettingProjectById_thatDoesNotExist() throws Exception {
        long projectId = 1L;
        when(projectServiceMock.getProject(projectId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/projects/{projectId}", projectId))
               .andDo(log())
               .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnCreatedProject_whenCreatingProject() throws Exception {
        Employee employee = employee();
        Client client = client(employee);
        String projectName = "Glacier";
        Project project = new Project(projectName, client);
        long clientId = 1L;
        CreateProjectRequest expectedRequest = new CreateProjectRequest(clientId, projectName);
        when(projectServiceMock.create(expectedRequest)).thenReturn(project);
        String requestBody = """
                             {
                               "clientId": %s,
                               "name": "%s"
                             }
                             """.formatted(clientId, projectName);

        mockMvc.perform(post("/v1/projects").contentType(MediaType.APPLICATION_JSON)
                                            .content(requestBody))
               .andDo(log())
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.name").value(project.getName()));
    }

    @Test
    void shouldReturnBadRequest_whenCreatingProject_withEmptyRequestBody() throws Exception {
        String requestBody = "{}";

        mockMvc.perform(post("/v1/projects").contentType(MediaType.APPLICATION_JSON)
                                            .content(requestBody))
               .andDo(log())
               .andExpect(status().isBadRequest());
    }

}