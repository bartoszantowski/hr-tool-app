package com.iitrab.hrtool.mail.api;

import org.assertj.core.api.AbstractAssert;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class EmailDtoAssert extends AbstractAssert<EmailDtoAssert, EmailDto> {

    private EmailDtoAssert(EmailDto emailDto) {
        super(emailDto, EmailDtoAssert.class);
    }

    public static EmailDtoAssert assertThatEmail(EmailDto emailDto) {
        return new EmailDtoAssert(emailDto);
    }

    public EmailDtoAssert isSentTo(String recipient) {
        isNotNull();
        assertThat(actual.toAddress()).isEqualTo(recipient);
        return this;
    }

    public EmailDtoAssert hasSubject(String subject) {
        isNotNull();
        assertThat(actual.subject()).isEqualTo(subject);
        return this;
    }

    public EmailDtoAssert hasContent(String content) {
        isNotNull();
        assertThat(actual.content()).isEqualTo(content);
        return this;
    }

}
