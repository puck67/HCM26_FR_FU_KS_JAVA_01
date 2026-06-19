package com.example.lms.controller;

import com.example.lms.entity.Course;
import com.example.lms.entity.Student;
import com.example.lms.repository.AccountRepository;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final AccountRepository accountRepository;

    @GetMapping("/")
    public String home(Model model) {
        long totalStudents = studentRepository.count();
        long totalCourses = courseRepository.count();
        long totalUsers = accountRepository.count();

        List<Student> recentStudents = studentRepository.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"))).getContent();

        List<Course> allCourses = courseRepository.findAll();
        Map<String, Long> courseCategoryData = allCourses.stream()
                .collect(Collectors.groupingBy(c -> c.getCategory() != null && !c.getCategory().isEmpty() ? c.getCategory() : "Uncategorized", Collectors.counting()));

        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("totalCourses", totalCourses);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("recentStudents", recentStudents);
        model.addAttribute("courseCategoryData", courseCategoryData);

        return "home";
    }
}
