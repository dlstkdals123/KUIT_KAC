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

    private static LocalDateTime getMonthStart() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.withDayOfMonth(1).withHour(3).withMinute(0).withSecond(0).withNano(0);
        return start;
    }

    public static boolean isToday(LocalDateTime dateTime) {
        LocalDateTime todayStart = getTodayStart();
        return !dateTime.isBefore(todayStart) && dateTime.isBefore(todayStart.plusDays(1));
    }

    public static boolean isMonth(LocalDateTime dateTime) {
        LocalDateTime monthStart = getMonthStart();
        return !dateTime.isBefore(monthStart) && dateTime.isBefore(monthStart.plusMonths(1));
    }
}
