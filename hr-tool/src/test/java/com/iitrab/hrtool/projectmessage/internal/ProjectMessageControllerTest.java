package com.iitrab.hrtool.projectmessage.internal;

import com.iitrab.hrtool.assignment.api.ProjectAssignmentNotFoundException;
import com.iitrab.hrtool.employee.api.EmployeeNotFoundException;
import com.iitrab.hrtool.project.api.ProjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProjectMessageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProjectMessageService projectMessageServiceMock;

    @BeforeEach
    void setUp() {
        ProjectMessageController projectMessageController = new ProjectMessageController(projectMessageServiceMock);
        mockMvc = MockMvcBuilders.standaloneSetup(projectMessageController)
                .build();
    }

    @Test
    void shouldSendProjectMessage_whenSendingProjectMessage() throws Exception{
        Long authorId = 2L;
        Long projectId = 5L;
        String subject = "subject_test";
        String content = "content_test";

        String requestBody = """
                             {
                               "authorId": %s,
                               "projectId": %s,
                               "subject": "%s",
                               "content": "%s"
                             }
                             """.formatted(authorId, projectId, subject, content);

        mockMvc.perform(post("/v1/projectmessage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(log())
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFound_whenSendingProjectMessage_thatAuthorDoesNotExist() throws Exception {
        Long authorId = 2L;
        Long projectId = 5L;
        String subject = "subject_test";
        String content = "content_test";

        SendProjectMessageRequest sendProjectMessageRequest = new SendProjectMessageRequest(authorId, projectId, subject, content);

        Mockito.doThrow(new EmployeeNotFoundException(authorId)).when(projectMessageServiceMock).publishProjectMessageEvent(sendProjectMessageRequest);

        String requestBody = """
                             {
                               "authorId": %s,
                               "projectId": %s,
                               "subject": "%s",
                               "content": "%s"
                             }
                             """.formatted(authorId, projectId, subject, content);

        mockMvc.perform(post("/v1/projectmessage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(log())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFound_whenSendingProjectMessage_thatProjectDoesNotExist() throws Exception {
        Long authorId = 2L;
        Long projectId = 5L;
        String subject = "subject_test";
        String content = "content_test";

        SendProjectMessageRequest sendProjectMessageRequest = new SendProjectMessageRequest(authorId, projectId, subject, content);

        Mockito.doThrow(new ProjectNotFoundException(projectId)).when(projectMessageServiceMock).publishProjectMessageEvent(sendProjectMessageRequest);

        String requestBody = """
                             {
                               "authorId": %s,
                               "projectId": %s,
                               "subject": "%s",
                               "content": "%s"
                             }
                             """.formatted(authorId, projectId, subject, content);

        mockMvc.perform(post("/v1/projectmessage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(log())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFound_whenSendingProjectMessage_thatProjectAssignmentNotExist() throws Exception {
        Long authorId = 2L;
        Long projectId = 5L;
        String subject = "subject_test";
        String content = "content_test";

        SendProjectMessageRequest sendProjectMessageRequest = new SendProjectMessageRequest(authorId, projectId, subject, content);

        Mockito.doThrow(new ProjectAssignmentNotFoundException(authorId, projectId)).when(projectMessageServiceMock).publishProjectMessageEvent(sendProjectMessageRequest);

        String requestBody = """
                             {
                               "authorId": %s,
                               "projectId": %s,
                               "subject": "%s",
                               "content": "%s"
                             }
                             """.formatted(authorId, projectId, subject, content);

        mockMvc.perform(post("/v1/projectmessage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(log())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequest_whenSendingProjectMessage_withEmptyRequestBody() throws Exception {
        String requestBody = "{}";

        mockMvc.perform(post("/v1/projectmessage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(log())
                .andExpect(status().isBadRequest());
    }
}
