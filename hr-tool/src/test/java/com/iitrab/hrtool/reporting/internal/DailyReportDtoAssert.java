package com.iitrab.hrtool.reporting.internal;

import org.assertj.core.api.AbstractAssert;

import static org.assertj.core.api.Assertions.assertThat;

public class DailyReportDtoAssert extends AbstractAssert<DailyReportDtoAssert, DailyReportDto> {

    private DailyReportDtoAssert(DailyReportDto dailyReportDto) {
        super(dailyReportDto, DailyReportDtoAssert.class);
    }

    public static DailyReportDtoAssert assertThatDailyReportDto(DailyReportDto dailyReportDto) {
        return new DailyReportDtoAssert(dailyReportDto);
    }

    public DailyReportDtoAssert hasManagerEmail(String managerEmail) {
        isNotNull();
        assertThat(actual.managerEmail()).isEqualTo(managerEmail);
        return this;
    }

    public DailyReportDtoAssert hasReportSubject(String reportSubject) {
        isNotNull();
        assertThat(actual.reportSubject()).isEqualTo(reportSubject);
        return this;
    }

    public DailyReportDtoAssert hasReportMessage(String reportMessage) {
        isNotNull();
        assertThat(actual.reportMessage()).isEqualTo(reportMessage);
        return this;
    }
}
