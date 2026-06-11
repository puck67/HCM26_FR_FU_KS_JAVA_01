package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/students")
    public String students() {
        return "pages/students";
    }

    @GetMapping("/lecturers")
    public String lecturers() {
        return "pages/lecturers";
    }

    @GetMapping("/subjects")
    public String subjects() {
        return "pages/subjects";
    }

    @GetMapping("/courses")
    public String courses() {
        return "pages/courses";
    }

    @GetMapping("/courses/online")
    public String onlineCourses() {
        return "pages/online-courses";
    }

    @GetMapping("/courses/offline")
    public String offlineCourses() {
        return "pages/offline-courses";
    }

    @GetMapping("/my-courses")
    public String myCourses() {
        return "pages/my-courses";
    }

    @GetMapping("/system/roles")
    public String systemRoles() {
        return "pages/system-roles";
    }

    @GetMapping("/system/users")
    public String systemUsers() {
        return "pages/system-users";
    }

    @GetMapping("/questions")
    public String questions() {
        return "pages/questions";
    }

    @GetMapping("/exams")
    public String exams() {
        return "pages/exams";
    }

    @GetMapping("/results")
    public String results() {
        return "pages/results";
    }
}
