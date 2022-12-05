package com.iitrab.hrtool.projectassigning.internal;

import com.iitrab.hrtool.assignment.api.ProjectAssignment;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.project.api.Project;
import com.iitrab.hrtool.SampleTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static java.time.format.DateTimeFormatter.ISO_DATE;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AssignmentControllerTest {

    private static final long PROJECT_ID = 1L;
    private static final long EMPLOYEE_ID = 2L;
    private static final String ROLE = "Dev";

    private MockMvc mockMvc;
    @Mock
    private AssignmentService assignmentServiceMock;

    @BeforeEach
    void setUp() {
        AssignmentController controller = new AssignmentController(assignmentServiceMock, new AssignmentMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .build();
    }

    @Test
    void shouldReturnCreatedAssignmentInfo_whenAssigningEmployeeToProject() throws Exception {
        Employee employee = SampleTestDataFactory.employee();
        Project project = SampleTestDataFactory.project(SampleTestDataFactory.client(employee));
        ProjectAssignment projectAssignment = SampleTestDataFactory.activeAssignment(employee, project);
        String request = """
                         {
                           "employeeId": %s,
                           "role": "%s",
                           "startDate": "%s",
                           "endDate": "%s"
                         }
                         """.formatted(EMPLOYEE_ID,
                                       ROLE,
                                       ISO_DATE.format(SampleTestDataFactory.DATE_IN_PAST),
                                       ISO_DATE.format(SampleTestDataFactory.DATE_IN_FUTURE));
        AssignToProjectRequest expectedRequest = new AssignToProjectRequest(EMPLOYEE_ID,
                                                                            ROLE,
                                                                            SampleTestDataFactory.DATE_IN_PAST,
                                                                            SampleTestDataFactory.DATE_IN_FUTURE);
        when(assignmentServiceMock.assignToProject(PROJECT_ID, expectedRequest)).thenReturn(projectAssignment);

        mockMvc.perform(post("/v1/projects/{projectId}/assignments", PROJECT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.project.name").value(project.getName()))
               .andExpect(jsonPath("$.employee.name").value(employee.getName()))
               .andExpect(jsonPath("$.employee.lastName").value(employee.getLastName()))
               .andExpect(jsonPath("$.employee.email").value(employee.getEmail()))
               .andExpect(jsonPath("$.role").value(projectAssignment.getRole()))
               .andExpect(jsonPath("$.startDate").value(ISO_DATE.format(projectAssignment.getStartDate())))
               .andExpect(jsonPath("$.endDate").value(ISO_DATE.format(projectAssignment.getEndDate())));
    }

    @Test
    void shouldReturnBadRequest_whenAssigningEmployeeToProject_withEmptyRequestBody() throws Exception {
        String request = "{}";

        mockMvc.perform(post("/v1/projects/{projectId}/assignments", PROJECT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
               .andDo(log())
               .andExpect(status().isBadRequest());
        verifyNoInteractions(assignmentServiceMock);
    }

}