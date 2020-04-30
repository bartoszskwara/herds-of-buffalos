package com.buffalosoftware.infrastructure;

import com.buffalosoftware.api.ITimeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static java.lang.String.format;

@Service
public class TimeService implements ITimeService {

    public LocalDateTime now() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }

    public long nowMillis() {
        ZonedDateTime zdt = now().atZone(ZoneId.of("UTC"));
        return zdt.toInstant().toEpochMilli();
    }

    @Override
    public long toMillis(LocalDateTime dateTime) {
        return Optional.ofNullable(dateTime).map(d -> d.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli()).orElse(0L);
    }

    @Override
    public String toSecondsISOCamundaFormat(Long millis) {
        if(millis == null) {
            return null;
        }
        long seconds = millis / 1000;
        return format("PT%sS", seconds);
    }
}
