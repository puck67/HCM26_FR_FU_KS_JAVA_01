package com.example.demo.config;

import com.example.demo.interceptor.InstructorInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final InstructorInterceptor instructorInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(instructorInterceptor)
                .addPathPatterns("/instructor/**")
                .excludePathPatterns("/instructor/login", "/instructor/logout");
    }
}
