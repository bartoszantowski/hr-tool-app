package com.iitrab.hrtool.util;

import java.time.*;

/**
 * Utility interface over the Java Time API, that reduce the necessity of calling its static methods and use the injectable beans instead for obtaining the date-time information.
 * Interface implementation has to implement only {@link TimeProvider#now()} method, the other ones have default implementations.
 */
@FunctionalInterface
public interface TimeProvider {

    /**
     * Returns the current {@link Instant}
     *
     * @return now
     */
    Instant now();

    default ZonedDateTime zonedDateTimeNow() {
        return ZonedDateTime.ofInstant(now(), ZoneId.systemDefault());
    }

    default LocalDate dateNow() {
        return LocalDate.ofInstant(now(), ZoneId.systemDefault());
    }

    default LocalDateTime dateTimeNow() {
        return LocalDateTime.ofInstant(now(), ZoneId.systemDefault());
    }

}
