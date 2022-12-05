package com.iitrab.hrtool.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RealTimeProviderTest {

    @InjectMocks
    private RealTimeProvider realTimeProvider;

    @Test
    void shouldReturnCurrentTime_whenPointingNow() {
        Instant before = Instant.now();
        Instant now = realTimeProvider.now();
        Instant after = Instant.now();

        assertThat(now).isAfterOrEqualTo(before)
                       .isBeforeOrEqualTo(after);
    }

}
