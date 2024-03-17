package com.dragonguard.backend.config.timezone;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Configuration
public class TimeZoneConfig {
    private static final String SEOUL_TIMEZONE = "Asia/Seoul";

    @PostConstruct
    public void timeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone(SEOUL_TIMEZONE));
    }
}
