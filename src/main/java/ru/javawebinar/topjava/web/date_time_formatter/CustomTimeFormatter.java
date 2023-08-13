package ru.javawebinar.topjava.web.date_time_formatter;

import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomTimeFormatter implements Formatter<LocalTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        if (StringUtils.hasLength(text)) {
            return LocalTime.parse(text, FORMATTER);
        } else {
            return null;
        }
    }

    @Override
    public String print(LocalTime object, Locale locale) {
        return object.toString();
    }
}
