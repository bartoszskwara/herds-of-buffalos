package com.buffalosoftware.api;

import java.time.LocalDateTime;

public interface TimeService {
    LocalDateTime now();
    long nowMillis();
    long toMillis(LocalDateTime dateTime);
}
