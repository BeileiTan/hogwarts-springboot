package com.springdemo.hogwartsartifactsonline.system.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.boot.actuate.health.Status;

import java.io.File;

@Component
public class UsableMemoryHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        File path = new File("."); //path used to compute the usable disc space
        long diskUsableInBytes = path.getUsableSpace();
        boolean isHealth = diskUsableInBytes >= 10 * 1024 * 1024; //10MB
        Status status = isHealth ? Status.UP : Status.DOWN;
        return Health
                .status(status)
                .withDetail("usable memory",diskUsableInBytes )
                .withDetail("threshold", 10 * 1024 * 1024)
                .build();
    }
}
