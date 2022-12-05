package com.iitrab.hrtool.projectmessage.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectMessageEventListener {

    private final ProjectMessageDetailCreator projectMessageDetailCreator;
    private final ProjectMessageService projectMessageService;

    /**
     * This method listens for the project message event and forwards it to be sent.
     *
     * @param projectMessageEvent event of project message
     */
    @EventListener
    void sendProjectMessageOnApplicationEvent(final ProjectMessageEvent projectMessageEvent) {
        log.info("Validating mail with event : %s".formatted(projectMessageEvent));
        projectMessageService.send(projectMessageDetailCreator.create(projectMessageEvent), projectMessageEvent);
    }
}
