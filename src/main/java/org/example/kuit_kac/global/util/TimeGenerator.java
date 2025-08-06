package org.example.kuit_kac.global.util;

import java.time.LocalDateTime;

public class TimeGenerator {
    private static LocalDateTime getTodayStart() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.withHour(3).withMinute(0).withSecond(0).withNano(0);
        if (now.getHour() < 3) {
            start = start.minusDays(1);
        }
        return start;
    }

    public static boolean isToday(LocalDateTime dateTime) {
        LocalDateTime todayStart = getTodayStart();
        return !dateTime.isBefore(todayStart) && dateTime.isBefore(todayStart.plusDays(1));
    }
}
