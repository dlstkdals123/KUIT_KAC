package org.example.kuit_kac.global.util;

import java.time.LocalDateTime;

public record TimeRange(LocalDateTime start, LocalDateTime end) {
    public static TimeRange getTodayTimeRange() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.withHour(3).withMinute(0).withSecond(0).withNano(0);
        if (now.getHour() < 3) {
            start = start.minusDays(1);
        }
        LocalDateTime end = start.plusDays(1).withHour(2).withMinute(59).withSecond(59).withNano(999_999_999);
        return new TimeRange(start, end);
    }

    public static TimeRange getPastWeekDietTimeRange() {
        // 오늘 기준 하루 시작 시점(오전 3시)
        LocalDateTime todayStart = getTodayTimeRange().start();
        LocalDateTime start = todayStart.minusDays(6);
        LocalDateTime end = todayStart.plusDays(1).withHour(2).withMinute(59).withNano(999_999_999);
        return new TimeRange(start, end);
    }
} 