package com.buffalosoftware.infrastructure;

import com.buffalosoftware.api.TimeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class TimeServiceImpl implements TimeService {

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
}
