package com.example.kpi.socialnetwork.config.converters;


import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateToStringConverter implements Converter<LocalDate, String> {
    @Override
    public String convert(LocalDate from) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(from);
    }
}
