package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends Temporal> boolean isBetweenHalfOpen(T ldt, T start, T end) {
        if (ldt.getClass() == LocalDateTime.class) {
            return (start == null || ((LocalDateTime) ldt).compareTo(((LocalDateTime) start)) >= 0) &&
                    (end == null || ((LocalDateTime) ldt).compareTo((LocalDateTime) end) <= 0);
        } else {
            return (start == null || ((LocalTime) ldt).compareTo((LocalTime) start) >= 0) &&
                    (end == null || ((LocalTime) ldt).compareTo((LocalTime) end) < 0);
        }
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

