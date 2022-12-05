package com.iitrab.hrtool.recruitment.internal;

import com.iitrab.hrtool.mail.api.EmailDto;
import com.iitrab.hrtool.mail.api.EmailSender;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
class RecruitmentEmailSenderTest {

    @Mock
    private RecruitmentMailProperties recruitmentMailPropertiesMock;

    @Mock
    private EmailSender emailSenderMock;

    @InjectMocks
    private RecruitmentEmailSender recruitmentEmailSender;


    @RegisterExtension
    protected static final GreenMailExtension GREEN_MAIL = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig()
                    .withUser("user", "pass"))
            .withPerMethodLifecycle(true);



    @Test
    void test() throws MessagingException, IOException {

        String name = "Bob";
        String lastName = "Test";
        String privateMail = "bob.test@gmail.com";

        String subject = "subject";
        String bodyTemplate = "Hello %s %s";

        when(recruitmentMailPropertiesMock.getSubject()).thenReturn(subject);
        when(recruitmentMailPropertiesMock.getBodyTemplate()).thenReturn(bodyTemplate);


        EmailDto emailDto = new EmailDto(privateMail, subject, bodyTemplate);

        emailSenderMock.send(emailDto);

        MimeMessage[] receivedMessage = GREEN_MAIL.getReceivedMessages();

        assertThat(receivedMessage).hasSize(1);
        assertThat(receivedMessage[0].getAllRecipients())
                .hasSize(1)
                .singleElement()
                .extracting(Address::toString)
                .isEqualTo(privateMail);

        String content = (String) receivedMessage[0].getContent();

        assertThat(content).isEqualTo("Hello Bob Test");
    }
}
