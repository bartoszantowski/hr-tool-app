package com.iitrab.hrtool.util;

import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Utility component providing the real-time based implementation of the {@link TimeProvider} interface.
 */
@Component
class RealTimeProvider implements TimeProvider {

    @Override
    public Instant now() {
        return Instant.now();
    }

}
