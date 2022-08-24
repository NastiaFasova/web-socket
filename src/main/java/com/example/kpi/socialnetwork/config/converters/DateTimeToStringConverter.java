package com.example.kpi.socialnetwork.config.converters;

import org.springframework.core.convert.converter.Converter;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A class-converter from LocalDateTime to String
 * */
public class DateTimeToStringConverter implements Converter<LocalDateTime, String> {
    @Override
    public String convert(LocalDateTime from) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(from);
    }
}
