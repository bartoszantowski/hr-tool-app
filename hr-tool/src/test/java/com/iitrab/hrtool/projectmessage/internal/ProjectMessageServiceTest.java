package com.iitrab.hrtool.projectmessage.internal;

import com.iitrab.hrtool.mail.api.EmailDto;
import com.iitrab.hrtool.mail.api.EmailSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProjectMessageServiceTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisherMock;

    @Mock
    private EmailSender emailSenderMock;

    @InjectMocks
    private ProjectMessageService projectMessageService;

    @Captor
    private ArgumentCaptor<ProjectMessageEvent> eventCaptor;

    @Captor
    private ArgumentCaptor<EmailDto> emailDtoCaptors;

    @Test
    void shouldPublishEvent_whenPublishingEvent() {
        Long authorId = 11L;
        Long projectId = 23L;
        String subject = "testSubject";
        String content = "testContent";

        SendProjectMessageRequest sendProjectMessageRequest =
                new SendProjectMessageRequest(authorId, projectId, subject, content);

        projectMessageService.publishProjectMessageEvent(sendProjectMessageRequest);

        verify(applicationEventPublisherMock).publishEvent(eventCaptor.capture());

        assertThatObject(eventCaptor.getValue().getProjectId()).isEqualTo(projectId);
        assertThatObject(eventCaptor.getValue().getAuthorId()).isEqualTo(authorId);
        assertThatObject(eventCaptor.getValue().getSubject()).isEqualTo(subject);
        assertThatObject(eventCaptor.getValue().getContent()).isEqualTo(content);
    }

    @Test
    void shouldSendProjectMessage_whenSendingProjectMessage() {
        Long authorId = 11L;
        Long projectId = 23L;
        String subject = "testSubject";
        String content = "testContent";

        SendProjectMessageRequest sendProjectMessageRequest =
                new SendProjectMessageRequest(authorId, projectId, subject, content);

        ProjectMessageEvent event = new ProjectMessageEvent(this, sendProjectMessageRequest);

        String recipient1 = "xxx";
        String recipient2 = "yyy";
        List<String> recipients = List.of(recipient1, recipient2);
        ProjectMessageDto projectMessageDto = new ProjectMessageDto(recipients, content, subject);

        projectMessageService.send(projectMessageDto, event);

        verify(emailSenderMock, times(2)).send(emailDtoCaptors.capture());

        assertThatObject(emailDtoCaptors.getAllValues().get(0).toAddress()).isEqualTo(recipient1);
        assertThatObject(emailDtoCaptors.getAllValues().get(1).toAddress()).isEqualTo(recipient2);
        assertThatObject(emailDtoCaptors.getValue().content()).isEqualTo(content);
        assertThatObject(emailDtoCaptors.getValue().subject()).isEqualTo(subject);
    }

}
