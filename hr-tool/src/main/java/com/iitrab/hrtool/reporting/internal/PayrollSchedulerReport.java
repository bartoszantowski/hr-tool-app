package com.iitrab.hrtool.reporting.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayrollSchedulerReport {

    private final PayrollSchedulerGenerator payrollSchedulerGenerator;

    /**
     * sends reports according to a given schedule
     */
    @Scheduled(cron = "${reports.salary.scheduling.cron}", zone = "${reports.salary.scheduling.timezone}")
    void sendDailyReport() {
        this.payrollSchedulerGenerator.generateDailyReport();
    }
}
