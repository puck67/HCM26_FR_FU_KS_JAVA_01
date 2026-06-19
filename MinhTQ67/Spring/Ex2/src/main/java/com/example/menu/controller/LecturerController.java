package com.example.menu.controller;

import com.example.menu.service.LecturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lecturers")
@RequiredArgsConstructor
public class LecturerController {
    private final LecturerService lecturerService;

    @GetMapping({"", "/"})
    public String listLecturers(Model model) {
        model.addAttribute("lecturers", lecturerService.getAllLecturers());
        return "lecturer/list";
    }
}
