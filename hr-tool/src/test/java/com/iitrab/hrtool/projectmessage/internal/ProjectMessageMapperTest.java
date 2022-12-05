package com.iitrab.hrtool.projectmessage.internal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.iitrab.hrtool.projectmessage.internal.ProjectMessageDtoAssert.assertThatProjectMessageDto;

@ExtendWith(MockitoExtension.class)
class ProjectMessageMapperTest {

    @InjectMocks
    private ProjectMessageMapper projectMessageMapper;

    @Test
    void shouldReturnProjectMessageDto_whenMappingToDto() {
        List<String> recipients = List.of();
        String messageContent = "test";
        String messageSubject = "test";
        SendProjectMessageRequest sendProjectMessageRequest = new SendProjectMessageRequest(2L, 3L, messageSubject, messageContent);

        ProjectMessageEvent projectMessageEvent = new ProjectMessageEvent(this, sendProjectMessageRequest);

        ProjectMessageDto projectMessageDto = projectMessageMapper.toDto(recipients, messageContent, projectMessageEvent);


        assertThatProjectMessageDto(projectMessageDto)
                .hasRecipients(recipients)
                .hasMessageContent(messageContent)
                .hasMessageSubject(messageSubject);
    }
}
