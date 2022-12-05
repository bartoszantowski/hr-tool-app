package com.iitrab.hrtool.projectmessage.internal;

import com.iitrab.hrtool.mail.api.EmailDto;
import com.iitrab.hrtool.mail.api.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Service is responsible for handling project messages.
 */
@Slf4j
@Service
@RequiredArgsConstructor
class ProjectMessageService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final EmailSender emailSender;


    /**
     * Publishes a project message event.
     *
     * @param sendProjectMessageRequest data of the project message
     */
    void publishProjectMessageEvent(SendProjectMessageRequest sendProjectMessageRequest) {
        final ProjectMessageEvent event = new ProjectMessageEvent(this, sendProjectMessageRequest);
        applicationEventPublisher.publishEvent(event);
    }

    /**
     * Sends project message.
     *
     * @param projectMessageDto prepared project message data
     * @param projectMessageEvent
     */
    void send(ProjectMessageDto projectMessageDto, ProjectMessageEvent projectMessageEvent) {
        log.info("Sending mail with event : %s".formatted(projectMessageEvent));
        projectMessageDto.recipients().forEach(receiver -> {
            emailSender.send(getEmailDto(projectMessageDto, receiver));
        });
    }

    private EmailDto getEmailDto(ProjectMessageDto projectMessageDto, String receiver) {
        return new EmailDto(
                receiver,
                projectMessageDto.messageSubject(),
                projectMessageDto.messageContent());
    }
}
