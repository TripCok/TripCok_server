package com.tripcok.tripcokserver.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripcok.tripcokserver.global.interceptor.LoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LogConfig implements WebMvcConfigurer {

    private static LoggingInterceptor loggingInterceptor;
    public LogConfig(LoggingInterceptor loggingInterceptor) {
        LogConfig.loggingInterceptor = loggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor);
    }
}
