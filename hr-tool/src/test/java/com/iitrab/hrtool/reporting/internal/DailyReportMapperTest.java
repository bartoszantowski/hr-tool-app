package com.iitrab.hrtool.reporting.internal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DailyReportMapperTest {

    @InjectMocks
    private DailyReportMapper dailyReportMapper;

    @Test
    void shouldReturnDailyReportDto_whenMappingDailyReportDto() {
        String managerEmail = "mailTest";
        String reportSubject = "subjectTest";
        String reportContent = "contentTest";

        DailyReportDto dailyReportDto = dailyReportMapper.toDto(managerEmail, reportSubject, reportContent);

        DailyReportDtoAssert.assertThatDailyReportDto(dailyReportDto)
                .hasReportMessage(reportContent)
                .hasReportSubject(reportSubject)
                .hasManagerEmail(managerEmail);

    }
}
