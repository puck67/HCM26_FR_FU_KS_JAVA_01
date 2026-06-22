package com.example.ASM6.config;

import com.example.ASM6.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor interceptor;

    public WebMvcConfig(AuthInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/instructor/**")
                .excludePathPatterns(
                        "/instructor/login",
                        "/instructor/css/**",
                        "/instructor/js/**",
                        "/instructor/images/**"
                );
    }
}
