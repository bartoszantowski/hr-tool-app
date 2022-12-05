package com.iitrab.hrtool.reporting.internal;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DailyReportMessageGeneratorTest {

    @Mock
    private DailyReportMapper dailyReportMapper;

    @InjectMocks
    private DailyReportMessageGenerator dailyReportMessageGenerator;


}
