package com.iitrab.hrtool.projectmessage.internal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectMessageEventListenerTest {

    @InjectMocks
    private ProjectMessageEventListener projectMessageEventListenerMock;

    @Mock
    private ProjectMessageDetailCreator projectMessageDetailCreator;

    @Mock
    private ProjectMessageService projectMessageService;

    @Test
    void shouldSendProjectMessageOnApplicationEvent_whenSendingProjectMessageOnApplicationEvent() {
        Long authorId = 2L;
        Long projectId = 5L;
        String subject = "subject_test";
        String content = "content_test";
        SendProjectMessageRequest sendProjectMessageRequest = new SendProjectMessageRequest(authorId, projectId, subject, content);
        ProjectMessageEvent event = new ProjectMessageEvent(this, sendProjectMessageRequest);

        String recipient1 = "xxx";
        String recipient2 = "yyy";
        List<String> recipients = List.of(recipient1, recipient2);
        ProjectMessageDto projectMessageDto = new ProjectMessageDto(recipients, content, subject);

        when(projectMessageDetailCreator.create(event)).thenReturn(projectMessageDto);

        projectMessageEventListenerMock.sendProjectMessageOnApplicationEvent(event);

        verify(projectMessageService).send(projectMessageDto, event);
    }
}