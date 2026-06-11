package com.example.ex1.model;

/**
 * Một Model/DTO đơn giản để mô phỏng truyền dữ liệu (Data Transfer Object).
 * Các phương thức constructor, getter, setter được viết thủ công để đảm bảo 
 * tương thích 100% với IDE mà không cần cài đặt plugin Lombok.
 */
public class Greeting {
    private long id;
    private String message;

    // Constructor không tham số (No-args constructor)
    public Greeting() {
    }

    // Constructor nhận đầy đủ tham số (All-args constructor)
    public Greeting(long id, String message) {
        this.id = id;
        this.message = message;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
