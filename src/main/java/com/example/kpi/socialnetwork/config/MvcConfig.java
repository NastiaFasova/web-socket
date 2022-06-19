package com.example.kpi.socialnetwork.config;

import com.example.kpi.socialnetwork.config.converters.DateTimeToStringConverter;
import com.example.kpi.socialnetwork.config.converters.DateToStringConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    private static String DIR_NAME = "user-photos";


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory(registry);
    }

    private void exposeDirectory(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(DIR_NAME);
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        if (DIR_NAME.startsWith("../")) {
            DIR_NAME = DIR_NAME.replace("../", "");
        }
        registry.addResourceHandler("/" + DIR_NAME + "/**").addResourceLocations("file:/"+ uploadPath + "/");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter (new DateTimeToStringConverter());
        registry.addConverter(new DateToStringConverter());
    }
}
