package com.iitrab.hrtool.mail.api;

import org.assertj.core.api.AbstractAssert;
import org.springframework.mail.SimpleMailMessage;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class SimpleMailMessageAssert extends AbstractAssert<SimpleMailMessageAssert, SimpleMailMessage> {

    private SimpleMailMessageAssert(SimpleMailMessage simpleMailMessage) {
        super(simpleMailMessage, SimpleMailMessageAssert.class);
    }

    public static SimpleMailMessageAssert assertThatSimpleMailMessage(SimpleMailMessage message) {
        return new SimpleMailMessageAssert(message);
    }

    public SimpleMailMessageAssert wasSentBy(String sender) {
        isNotNull();
        assertThat(actual.getFrom()).isEqualTo(sender);
        return this;
    }

    public SimpleMailMessageAssert wasSentTo(String recipient) {
        isNotNull();
        assertThat(actual.getTo()).contains(recipient);
        return this;
    }

    public SimpleMailMessageAssert hasSubject(String subject) {
        isNotNull();
        assertThat(actual.getSubject()).isEqualTo(subject);
        return this;
    }

    public SimpleMailMessageAssert hasContent(String content) {
        isNotNull();
        assertThat(actual.getText()).isEqualTo(content);
        return this;
    }

}
