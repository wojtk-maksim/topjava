package ru.javawebinar.topjava.web.date_time_formatter;

import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomDateFormatter implements Formatter<LocalDate> {
    private static final String FORMAT = "yyyy-MM-dd";

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        if (StringUtils.hasLength(text)) {
            return LocalDate.parse(text, DateTimeFormatter.ofPattern(FORMAT));
        } else {
            return null;
        }
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return object.toString();
    }
}
