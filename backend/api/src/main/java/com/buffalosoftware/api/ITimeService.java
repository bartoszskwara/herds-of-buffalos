package com.buffalosoftware.api;

import java.time.LocalDateTime;

public interface ITimeService {
    LocalDateTime now();
    long nowMillis();
    long toMillis(LocalDateTime dateTime);
    String toSecondsISOCamundaFormat(Long millis);
}
