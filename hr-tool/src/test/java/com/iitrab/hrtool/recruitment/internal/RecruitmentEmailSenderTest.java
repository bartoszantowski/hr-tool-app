package com.iitrab.hrtool.recruitment.internal;

import com.iitrab.hrtool.candidate.api.Candidate;
import com.iitrab.hrtool.candidate.api.RecruitmentStatus;
import com.iitrab.hrtool.mail.api.EmailDto;
import com.iitrab.hrtool.mail.api.EmailSender;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static com.iitrab.hrtool.SampleTestDataFactory.candidate;
import static com.iitrab.hrtool.mail.api.EmailDtoAssert.assertThatEmail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecruitmentEmailSenderTest {

    @Mock
    private RecruitmentMailProperties recruitmentMailPropertiesMock;

    @Mock
    private EmailSender emailSenderMock;

    @InjectMocks
    private RecruitmentEmailSender recruitmentEmailSender;

    @Captor
    private ArgumentCaptor<EmailDto> emailCaptor;

    @Test
    void shouldSendEmail_whenSendingEmail() {
        Candidate candidate = candidate(RecruitmentStatus.HIRED);
        String subject = "subject";
        String bodyTemplate = "Hello %s %s";

        when(recruitmentMailPropertiesMock.getSubject()).thenReturn(subject);
        when(recruitmentMailPropertiesMock.getBodyTemplate()).thenReturn(bodyTemplate);

        recruitmentEmailSender.sendEmail(candidate.getName(), candidate.getLastName(), candidate.getPrivateEmail());
        verify(emailSenderMock).send(emailCaptor.capture());

        String expectedContent = bodyTemplate.formatted(candidate.getName(), candidate.getLastName());
        assertThatEmail(emailCaptor.getValue())
                .isSentTo(candidate.getPrivateEmail())
                .hasSubject(subject)
                .hasContent(expectedContent);

    }
}
