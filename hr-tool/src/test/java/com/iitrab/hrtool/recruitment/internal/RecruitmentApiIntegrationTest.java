package com.iitrab.hrtool.recruitment.internal;

import com.iitrab.hrtool.IntegrationTest;
import com.iitrab.hrtool.IntegrationTestBase;
import com.iitrab.hrtool.SampleTestDataFactory;
import com.iitrab.hrtool.candidate.api.Candidate;
import com.iitrab.hrtool.candidate.api.RecruitmentStatus;
import com.iitrab.hrtool.contract.api.Grade;
import com.iitrab.hrtool.department.api.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.iitrab.hrtool.SampleTestDataFactory.*;
import static com.iitrab.hrtool.mail.api.MimeMessageAssert.assertThatMimeMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@Transactional
class RecruitmentApiIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecruitmentMailProperties recruitmentMailProperties;

    @Test
    void shouldSendEmail_whenHiringEmployee() throws Exception {
        Long hrEmployeeId = 2L;
        LocalDate contractStartDate = LocalDate.of(2023, 1, 1);
        LocalDate contractEndDate = LocalDate.of(2024, 1, 1);
        BigDecimal salary = BigDecimal.valueOf(5000);
        Grade grade = Grade.B;
        String position = "test";
        Department department = existingDepartment(department(existingEmployee(employee())));
        Candidate candidate = existingCandidate(candidate(RecruitmentStatus.ACCEPTED));

        String requestBody = """
                                 {
                                   "hrEmployeeId": %s,
                                   "candidateId": %s,
                                   "contractStartDate": "%s",
                                   "contractEndDate": "%s",
                                   "departmentId": %s,
                                   "salary": %s,
                                   "grade": "%s",
                                   "position": "%s"
                                 }
                                 """.formatted(
                                         hrEmployeeId,
                candidate.getId(),
                contractStartDate,
                contractEndDate,
                department.getId(),
                salary,
                grade.toString(),
                position);

        mockMvc.perform(post("/v1/recruitment")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andDo(log())
                .andExpect(status().isCreated());

        MimeMessage[] receivedMessage = GREEN_MAIL.getReceivedMessages();
        String expectedSubject = recruitmentMailProperties.getSubject();
        String expectedContent = recruitmentMailProperties.getBodyTemplate().formatted(candidate.getName(), candidate.getLastName()) + "\n";

        assertThat(receivedMessage).hasSize(1)
                .anySatisfy(message -> assertThatMimeMessage(message)
                        .wasSentTo(candidate.getPrivateEmail())
                        .hasSubject(expectedSubject)
                        .hasContent(expectedContent));

    }

}
