package com.example.ex1.controller;

import com.example.ex1.model.Greeting;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Controller phục vụ API trả về dữ liệu thuần (JSON).
 * Đánh dấu bằng @RestController kết hợp @Controller và @ResponseBody.
 * Mọi request handler ở đây sẽ trả dữ liệu trực tiếp dưới dạng JSON (nhờ Jackson mapper tích hợp sẵn).
 */
@RestController
public class GreetingRestController {

    private static final String template = "Chào mừng bạn đến với Spring Boot, %s!";
    private final AtomicLong counter = new AtomicLong();

    /**
     * API Endpoint trả về một object Greeting dạng JSON.
     * URL ví dụ: http://localhost:8080/api/greeting?name=Java
     *
     * @param name tham số nhận từ query string, mặc định là "Beginner" nếu không truyền
     * @return Đối tượng Greeting sẽ tự động được Spring Boot chuyển đổi sang JSON
     */
    @GetMapping("/api/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "Beginner") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}
