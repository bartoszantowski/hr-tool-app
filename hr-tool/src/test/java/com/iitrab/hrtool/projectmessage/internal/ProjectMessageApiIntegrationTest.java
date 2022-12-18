package com.iitrab.hrtool.projectmessage.internal;

import com.iitrab.hrtool.IntegrationTest;
import com.iitrab.hrtool.IntegrationTestBase;

import com.iitrab.hrtool.client.api.Client;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.mail.api.EmailSender;
import com.iitrab.hrtool.project.api.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.mail.internet.MimeMessage;

import static com.iitrab.hrtool.SampleTestDataFactory.*;
import static com.iitrab.hrtool.mail.api.MimeMessageAssert.assertThatMimeMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@Transactional
class ProjectMessageApiIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmailSender emailSender;

    @Test
    void shouldSendProjectMessage_whenSendingProjectMessage() throws Exception{
        Employee author = existingEmployee(employee());
        Employee employee1 = existingEmployee(employee());
        Employee employee2 = existingEmployee(employee());
        Client client = existingClient(client(employee1));
        Project project = existingProject(project(client));

        existingProjectAssignment(activeAssignment(author, project));
        existingProjectAssignment(activeAssignment(employee1, project));
        existingProjectAssignment(activeAssignment(employee2, project));

        Long authorId = author.getId();
        Long projectId = project.getId();
        String subject = "subject_test";
        String content = "content_test";
        String projectMessageContent = "SENDER: " + author.getName() + " " + author.getEmail() +
                "\nPROJECT NAME: " + project.getName() +
                "\nMESSAGE:\n" + content + "\n";

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

        MimeMessage[] receivedMessage = GREEN_MAIL.getReceivedMessages();

        assertThat(receivedMessage).hasSize(2)
                .anySatisfy(message -> assertThatMimeMessage(message)
                        .wasSentTo(employee1.getEmail())
                        .hasSubject(subject)
                        .hasContent(projectMessageContent))
                .anySatisfy(message -> assertThatMimeMessage(message)
                        .wasSentTo(employee2.getEmail())
                        .hasSubject(subject)
                        .hasContent(projectMessageContent));
    }
}
