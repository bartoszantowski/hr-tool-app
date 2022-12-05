package com.iitrab.hrtool.reporting.internal;

import org.springframework.stereotype.Component;

@Component
class DailyReportMapper {

    DailyReportDto toDto(String managerEmail, String reportSubject, String reportContent) {
        return new DailyReportDto(managerEmail,
                                  reportSubject,
                                  reportContent);
    }
}
