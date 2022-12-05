package com.iitrab.hrtool.projectmessage.internal;

import org.assertj.core.api.AbstractAssert;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectMessageDtoAssert extends AbstractAssert<ProjectMessageDtoAssert, ProjectMessageDto> {

    private ProjectMessageDtoAssert(ProjectMessageDto projectMessageDto){
        super(projectMessageDto, ProjectMessageDtoAssert.class);
    }

    public static ProjectMessageDtoAssert assertThatProjectMessageDto(ProjectMessageDto projectMessageDto) {
        return new ProjectMessageDtoAssert(projectMessageDto);
    }

    public ProjectMessageDtoAssert hasRecipients(List<String> recipients) {
        isNotNull();
        assertThat(actual.recipients()).isNotNull();
        assertThat(actual.recipients()).isEqualTo(recipients);
        return this;
    }

    public ProjectMessageDtoAssert hasMessageContent(String messageContent) {
        isNotNull();
        assertThat(actual.messageContent()).isEqualTo(messageContent);
        return this;
    }

    public ProjectMessageDtoAssert hasMessageSubject(String messageSubject) {
        isNotNull();
        assertThat(actual.messageSubject()).isEqualTo(messageSubject);
        return this;
    }
}
