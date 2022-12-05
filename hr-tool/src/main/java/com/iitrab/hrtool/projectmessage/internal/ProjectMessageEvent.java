package com.iitrab.hrtool.projectmessage.internal;

import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Getter
@ToString
class ProjectMessageEvent extends ApplicationEvent {

    private final Long authorId;
    private final Long projectId;
    private final String subject;
    private final String content;

    public ProjectMessageEvent(Object source, SendProjectMessageRequest request) {
        super(source);
        this.authorId = request.authorId();
        this.projectId = request.projectId();
        this.subject = request.subject();
        this.content = request.content();
    }
}
