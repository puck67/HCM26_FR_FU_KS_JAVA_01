package com.example.Ex4.controller;

import com.example.Ex4.controller.base.GenericController;
import com.example.Ex4.entity.Subject;
import com.example.Ex4.repository.MaterialRepository;
import com.example.Ex4.service.SubjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/subjects")
public class SubjectController extends GenericController<Subject> {

    private final MaterialRepository materialRepository;

    public SubjectController(SubjectService subjectService,
                             MaterialRepository materialRepository) {
        super(subjectService, "subject", "/subjects");
        this.materialRepository = materialRepository;
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        Subject subject = service.findById(id);
        if (subject == null) {
            return "redirect:/subjects";
        }
        model.addAttribute("subject", subject);
        model.addAttribute("materials", materialRepository.findBySubjectId(id));
        model.addAttribute("view", "subject/detail");
        return "layout/main";
    }
}