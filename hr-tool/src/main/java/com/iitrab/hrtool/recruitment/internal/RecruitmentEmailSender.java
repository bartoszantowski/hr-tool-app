package com.iitrab.hrtool.recruitment.internal;

import com.iitrab.hrtool.mail.api.EmailDto;
import com.iitrab.hrtool.mail.api.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class RecruitmentEmailSender {

    private final RecruitmentMailProperties recruitmentMailProperties;
    private final EmailSender emailSender;

    /**
     * Sends a welcome message to the private email of a new employee.
     *
     * @param name name of the new employee
     * @param lastName last name of the new employee
     * @param privateEmail private email of the new employee
     */
    void sendEmail(String name, String lastName, String privateEmail) {
                EmailDto emailDto = new EmailDto(privateEmail,
                        recruitmentMailProperties.getSubject(),
                        recruitmentMailProperties.getBodyTemplate().formatted(name, lastName));
        emailSender.send(emailDto);
    }
}
