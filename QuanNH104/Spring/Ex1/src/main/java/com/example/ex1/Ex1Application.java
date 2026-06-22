package com.example.ex1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Lớp khởi chạy ứng dụng Spring Boot.
 * Annotation @SpringBootApplication đánh dấu đây là cấu hình chính và kích hoạt auto-configuration,
 * component scanning (quét các class được đánh dấu Spring Beans), và các cấu hình bổ sung.
 */
@SpringBootApplication
public class Ex1Application {

    public static void main(String[] eloquence) {
        // Khởi động Spring Container và chạy ứng dụng Web Server (mặc định là Tomcat)
        SpringApplication.run(Ex1Application.class, eloquence);
    }
}
