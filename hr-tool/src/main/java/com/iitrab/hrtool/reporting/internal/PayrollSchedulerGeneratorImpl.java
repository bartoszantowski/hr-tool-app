package com.iitrab.hrtool.reporting.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayrollSchedulerGeneratorImpl implements PayrollSchedulerGenerator{

    private final DailyReportGenerator dailyReportGenerator;
    private final DailyReportSender dailyReportSender;

    /**
     * Generates a daily report.
     * Generates the exact date and time of the report generation and
     * on the basis of the received data forwards them for sending.
     */
    @Override
    public void generateDailyReport() {
        log.info("Generating Daily Report...");
        LocalDateTime localDateTime = LocalDateTime.now();
        log.info("Preparing Daily Report data...");
        List<DailyReportDto> dailyReportDtos = dailyReportGenerator.generate(localDateTime);
        log.info("Daily Report is ready.");
        dailyReportSender.send(dailyReportDtos);
    }
}
