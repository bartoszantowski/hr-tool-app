package com.iitrab.hrtool.recruitment.internal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * Configuration of the {@link RecruitmentEmailSender}
 */
@ConfigurationProperties(prefix = "mail.recruitment.hired")
@ConstructorBinding
@Getter
@RequiredArgsConstructor
public class RecruitmentMailProperties {

    /**
     * Email subject to be sent.
     */
    private final String subject;
    /**
     * Email body template to be sent.
     */
    private final String bodyTemplate;

}
