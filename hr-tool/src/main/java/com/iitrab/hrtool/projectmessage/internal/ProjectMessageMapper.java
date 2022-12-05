package com.iitrab.hrtool.projectmessage.internal;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectMessageMapper {

    ProjectMessageDto toDto(List<String> recipients, String messageContent, ProjectMessageEvent projectMessageEvent) {
        return new ProjectMessageDto(recipients,
                                     messageContent,
                                     projectMessageEvent.getSubject());
    }
}
