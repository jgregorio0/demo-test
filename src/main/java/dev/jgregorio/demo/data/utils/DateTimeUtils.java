package dev.jgregorio.demo.data.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class DateTimeUtils {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final DateTimeFormatter dateFormatter =
            new DateTimeFormatterBuilder().appendPattern(DATE_FORMAT).toFormatter();
    public static final String TIME_FORMAT = "HH:mm";
    public static final DateTimeFormatter timeFormatter =
            new DateTimeFormatterBuilder().appendPattern(TIME_FORMAT).toFormatter();
    public static final String DATETIME_UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private DateTimeUtils() {
        throw new IllegalStateException("Class is not instantiable.");
    }

    public static LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
