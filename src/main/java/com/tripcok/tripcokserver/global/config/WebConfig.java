package com.tripcok.tripcokserver.global.config;

import com.tripcok.tripcokserver.global.interceptor.AdminInterceptor;
import com.tripcok.tripcokserver.global.interceptor.LoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AdminInterceptor adminInterceptor;
    private final LoggingInterceptor loggingInterceptor;

    public WebConfig(AdminInterceptor adminInterceptor, LoggingInterceptor loggingInterceptor) {
        this.adminInterceptor = adminInterceptor;
        this.loggingInterceptor = loggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**") // Interceptor가 적용될 URL 패턴
                .excludePathPatterns("/admin/login", "/admin/css/**", "/admin/js/**"); // 제외할 URL

        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/**")  // 인터셉터를 적용할 기본 URL 패턴
                .excludePathPatterns(
                        "/api/v1/member/login",      // 로그인 URL 제외
                        "/api/v1/member/login/*",      // 로그인 URL 제외
                        "/api/v1/member/register",     // 회원가입 URL 제외
                        "/api/v1/member/register/*"
                );
    }
}
