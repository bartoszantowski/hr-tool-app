package com.iitrab.hrtool.mail.api;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class MimeMessageAssert extends AbstractAssert<MimeMessageAssert, MimeMessage> {

    private MimeMessageAssert(MimeMessage mimeMessage) {
        super(mimeMessage, MimeMessageAssert.class);
    }

    public static MimeMessageAssert assertThatMimeMessage(MimeMessage message) {
        return new MimeMessageAssert(message);
    }

    public MimeMessageAssert wasSentTo(String recipient) {
        isNotNull();
        try {
            boolean matched = Arrays.stream(actual.getAllRecipients())
                                    .filter(InternetAddress.class::isInstance)
                                    .map(InternetAddress.class::cast)
                                    .map(InternetAddress::getAddress)
                                    .anyMatch(recipient::equals);
            assertThat(matched).isTrue();
        } catch (MessagingException exception) {
            Assertions.fail("Could not read recipients", exception);
        }
        return this;
    }

    public MimeMessageAssert wasSentBy(String sender) {
        isNotNull();
        try {
            boolean matched = Arrays.stream(actual.getFrom())
                                    .filter(InternetAddress.class::isInstance)
                                    .map(InternetAddress.class::cast)
                                    .map(InternetAddress::getAddress)
                                    .anyMatch(sender::equals);
            assertThat(matched).isTrue();
        } catch (MessagingException exception) {
            Assertions.fail("Could not read senders", exception);
        }
        return this;
    }

    public MimeMessageAssert hasSubject(String subject) {
        isNotNull();
        try {
            assertThat(actual.getSubject()).isEqualTo(subject);
        } catch (MessagingException exception) {
            Assertions.fail("Could not read subject", exception);
        }
        return this;
    }

    public MimeMessageAssert hasContent(String content) {
        isNotNull();
        try {
            final String actualContent = (String) actual.getContent();
            assertThat(sanitizeContent(actualContent)).isEqualTo(content);
        } catch (IOException | MessagingException exception) {
            Assertions.fail("Could not read content", exception);
        }
        return this;
    }

    /**
     * Replaces all `\r` characters in a {@link String} to change Windows CRLF line
     * ends to Linux LF.
     * 
     * @param content string to sanitize
     * @return content without `\r` characters
     */
    @SuppressWarnings("HardcodedLineSeparator")
    private String sanitizeContent(String content) {
        return content.replaceAll("\r", "");
    }

}
