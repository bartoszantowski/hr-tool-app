package com.iitrab.hrtool.project.internal;

import com.iitrab.hrtool.IntegrationTest;
import com.iitrab.hrtool.IntegrationTestBase;
import com.iitrab.hrtool.client.api.Client;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.project.api.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.iitrab.hrtool.SampleTestDataFactory.*;
import static com.iitrab.hrtool.project.api.ProjectAssert.assertThatProject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@Transactional
class ProjectApiIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllProjects_whenGettingAllProjects() throws Exception {
        Employee employee = existingEmployee(employee());
        Client client = existingClient(client(employee));
        Project project = existingProject(project(client));

        mockMvc.perform(get("/v1/projects"))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].id").value(project.getId()))
               .andExpect(jsonPath("$[0].name").value(project.getName()))
               .andExpect(jsonPath("$[0].clientId").value(client.getId()))
               .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void shouldReturnProject_whenGettingProjectById() throws Exception {
        Employee employee = existingEmployee(employee());
        Client client = existingClient(client(employee));
        Project project = existingProject(project(client));

        mockMvc.perform(get("/v1/projects/{projectId}", project.getId()))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(project.getId()))
               .andExpect(jsonPath("$.name").value(project.getName()))
               .andExpect(jsonPath("$.clientId").value(client.getId()));
    }

    @Test
    void shouldReturnCreatedProject_whenCreatingProject() throws Exception {
        Employee employee = existingEmployee(employee());
        Client client = existingClient(client(employee));
        String projectName = "Glacier";
        String requestBody = """
                             {
                               "clientId": %s,
                               "name": "%s"
                             }
                             """.formatted(client.getId(), projectName);

        mockMvc.perform(post("/v1/projects").contentType(MediaType.APPLICATION_JSON)
                                            .content(requestBody))
               .andDo(log())
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").exists())
               .andExpect(jsonPath("$.name").value(projectName))
               .andExpect(jsonPath("$.clientId").value(client.getId()));
    }

    @Test
    void shouldPersistCreatedProject_whenCreatingProject() throws Exception {
        Employee employee = existingEmployee(employee());
        Client client = existingClient(client(employee));
        String projectName = "Glacier";
        String requestBody = """
                             {
                               "clientId": %s,
                               "name": "%s"
                             }
                             """.formatted(client.getId(), projectName);

        mockMvc.perform(post("/v1/projects").contentType(MediaType.APPLICATION_JSON)
                                            .content(requestBody))
               .andDo(log())
               .andExpect(status().isCreated());

        List<Project> allProjects = getAllProjects();
        assertThat(allProjects).hasSize(1)
                               .anySatisfy(project -> assertThatProject(project).hasName(projectName)
                                                                                .isForClientThat(clientAssert -> clientAssert.hasId(
                                                                                        client.getId())));
    }

}
