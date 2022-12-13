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
@SpringBootTest
class RecruitmentEmailSenderTest {

    @Mock
    private RecruitmentMailProperties recruitmentMailPropertiesMock;

    @Mock
    private EmailSender emailSenderMock;

    @InjectMocks
    private RecruitmentEmailSender recruitmentEmailSender;

    @Captor
    private ArgumentCaptor<EmailDto> emailCaptor;


//    @RegisterExtension
//    protected static final GreenMailExtension GREEN_MAIL = new GreenMailExtension(ServerSetupTest.SMTP)
//            .withConfiguration(GreenMailConfiguration.aConfig()
//                    .withUser("user", "pass"))
//            .withPerMethodLifecycle(true);

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


//    @Test
//    void test() throws MessagingException, IOException {
//
//        String name = "Bob";
//        String lastName = "Test";
//        String privateMail = "bob.test@gmail.com";
//
//        String subject = "subject";
//        String bodyTemplate = "Hello %s %s";
//
//        when(recruitmentMailPropertiesMock.getSubject()).thenReturn(subject);
//        when(recruitmentMailPropertiesMock.getBodyTemplate()).thenReturn(bodyTemplate);
//
//
//        EmailDto emailDto = new EmailDto(privateMail, subject, bodyTemplate);
//
//        emailSenderMock.send(emailDto);
//
//        MimeMessage[] receivedMessage = GREEN_MAIL.getReceivedMessages();
//
//        assertThat(receivedMessage).hasSize(1);
//        assertThat(receivedMessage[0].getAllRecipients())
//                .hasSize(1)
//                .singleElement()
//                .extracting(Address::toString)
//                .isEqualTo(privateMail);
//
//        String content = (String) receivedMessage[0].getContent();
//
//        assertThat(content).isEqualTo("Hello Bob Test");
//    }
}
