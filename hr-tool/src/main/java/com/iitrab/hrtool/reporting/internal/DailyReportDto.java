package com.iitrab.hrtool.reporting.internal;

import javax.validation.constraints.NotNull;

record DailyReportDto (@NotNull String managerEmail,
                       @NotNull String reportSubject,
                       @NotNull String reportMessage) {
}
