package com.iitrab.hrtool.projectassigning.internal;

import com.iitrab.hrtool.IntegrationTest;
import com.iitrab.hrtool.IntegrationTestBase;
import com.iitrab.hrtool.assignment.api.ProjectAssignment;
import com.iitrab.hrtool.client.api.Client;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.project.api.Project;
import com.iitrab.hrtool.SampleTestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.iitrab.hrtool.assignment.api.ProjectAssignmentAssert.assertThatProjectAssignment;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@Transactional
class AssignmentApiIntegrationTest extends IntegrationTestBase {

    private static final String ROLE = "Dev";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnCreatedAssignmentInfo_whenAssigningEmployeeToProject() throws Exception {
        Employee employee = existingEmployee(SampleTestDataFactory.employee());
        Client client = existingClient(SampleTestDataFactory.client(employee));
        Project project = existingProject(SampleTestDataFactory.project(client));
        String request = """
                         {
                           "employeeId": %s,
                           "role": "%s",
                           "startDate": "%s",
                           "endDate": "%s"
                         }
                         """.formatted(employee.getId(),
                                       ROLE,
                                       ISO_DATE.format(SampleTestDataFactory.DATE_IN_PAST),
                                       ISO_DATE.format(SampleTestDataFactory.DATE_IN_FUTURE));

        mockMvc.perform(post("/v1/projects/{projectId}/assignments", project.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").isNotEmpty())
               .andExpect(jsonPath("$.project.id").value(project.getId()))
               .andExpect(jsonPath("$.project.name").value(project.getName()))
               .andExpect(jsonPath("$.employee.id").value(employee.getId()))
               .andExpect(jsonPath("$.employee.name").value(employee.getName()))
               .andExpect(jsonPath("$.employee.lastName").value(employee.getLastName()))
               .andExpect(jsonPath("$.employee.email").value(employee.getEmail()))
               .andExpect(jsonPath("$.role").value(ROLE))
               .andExpect(jsonPath("$.startDate").value(ISO_DATE.format(SampleTestDataFactory.DATE_IN_PAST)))
               .andExpect(jsonPath("$.endDate").value(ISO_DATE.format(SampleTestDataFactory.DATE_IN_FUTURE)));
    }

    @Test
    void shouldPersistAssignment_whenAssigningEmployeeToProject() throws Exception {
        Employee employee = existingEmployee(SampleTestDataFactory.employee());
        Client client = existingClient(SampleTestDataFactory.client(employee));
        Project project = existingProject(SampleTestDataFactory.project(client));
        String request = """
                         {
                           "employeeId": %s,
                           "role": "%s",
                           "startDate": "%s",
                           "endDate": "%s"
                         }
                         """.formatted(employee.getId(),
                                       ROLE,
                                       ISO_DATE.format(SampleTestDataFactory.DATE_IN_PAST),
                                       ISO_DATE.format(SampleTestDataFactory.DATE_IN_FUTURE));

        mockMvc.perform(post("/v1/projects/{projectId}/assignments", project.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
               .andDo(log())
               .andExpect(status().isOk());

        List<ProjectAssignment> assignments = getAllProjectAssignments();
        assertThat(assignments).hasSize(1)
                               .anySatisfy(assignment -> assertThatProjectAssignment(assignment).isForEmployeeThat(emp -> emp.hasId(
                                                                                                        employee.getId()))
                                                                                                .isToProject(proj -> proj.hasId(
                                                                                                        project.getId()))
                                                                                                .hasRole(ROLE)
                                                                                                .startsOn(SampleTestDataFactory.DATE_IN_PAST)
                                                                                                .endsOn(SampleTestDataFactory.DATE_IN_FUTURE));
    }

}