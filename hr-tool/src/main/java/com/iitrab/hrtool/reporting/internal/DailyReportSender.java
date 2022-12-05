package com.iitrab.hrtool.reporting.internal;

import com.iitrab.hrtool.mail.api.EmailDto;
import com.iitrab.hrtool.mail.api.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
class DailyReportSender {

    private final EmailSender emailSender;

    /**
     * Sends reports by e-mail based on the provided data.
     *
     * @param dailyReportDtos list od DailyReportDto
     */
    void send(List<DailyReportDto> dailyReportDtos) {
        log.info("Sending daily report...");

        dailyReportDtos.forEach(report -> {
            EmailDto emailDto = new EmailDto(
                    report.managerEmail(),
                    report.reportSubject(),
                    report.reportMessage());
            emailSender.send(emailDto);
        });

        log.info("Daily report has been sent.");
    }
}
