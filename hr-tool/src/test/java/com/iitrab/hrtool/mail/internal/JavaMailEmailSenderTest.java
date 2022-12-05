package com.iitrab.hrtool.mail.internal;

import com.iitrab.hrtool.mail.api.EmailDto;
import com.iitrab.hrtool.mail.api.SimpleMailMessageAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JavaMailEmailSenderTest {

    private static final String FROM = "application-noreply@company.com";
    private static final String RECIPIENT = "john.smith@company.com";
    private static final String SUBJECT = "Important!";
    private static final String CONTENT = "Happy Birthday!";
    private JavaMailEmailSender emailSender;

    @Mock
    private JavaMailSender javaMailSenderMock;
    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;

    @BeforeEach
    void setUp() {
        MailProperties mailProperties = new MailProperties(FROM);
        emailSender = new JavaMailEmailSender(javaMailSenderMock, mailProperties);
    }

    @Test
    void shouldSendMailMessage() {
        EmailDto emailDto = new EmailDto(RECIPIENT, SUBJECT, CONTENT);

        emailSender.send(emailDto);

        verify(javaMailSenderMock).send(messageCaptor.capture());
        SimpleMailMessageAssert.assertThatSimpleMailMessage(messageCaptor.getValue())
                               .wasSentBy(FROM)
                               .wasSentTo(RECIPIENT)
                               .hasSubject(SUBJECT)
                               .hasContent(CONTENT);
    }

}
