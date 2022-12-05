package com.iitrab.hrtool.util;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.assertj.core.api.Assertions.assertThat;

class TimeProviderTest {

    private static final String DATE_NOW_AS_STRING = "2022-07-02";
    private static final String TIME_NOW_AS_STRING = "19:12:47.085256824";
    private static final String ZONED_NOW_AS_STRING = DATE_NOW_AS_STRING + "T" + TIME_NOW_AS_STRING + "Z";
    private static final Instant NOW = Instant.parse(ZONED_NOW_AS_STRING);

    private final TimeProvider timeProvider = () -> NOW;

    @Test
    void shouldReturnCurrentZonedDateTime() {
        ZonedDateTime zonedDateTime = timeProvider.zonedDateTimeNow();

        assertThat(zonedDateTime).isEqualTo(ZONED_NOW_AS_STRING);
    }

    @Test
    void shouldReturnCurrentLocalDateTime() {
        LocalDateTime localDateTime = timeProvider.dateTimeNow();

        assertThat(localDateTime.atZone(ZoneId.systemDefault())).isEqualTo(ZONED_NOW_AS_STRING);
    }

    @Test
    void shouldReturnCurrentLocalDate() {
        LocalDate localDate = timeProvider.dateNow();

        assertThat(localDate).isEqualTo(DATE_NOW_AS_STRING);
    }

}
