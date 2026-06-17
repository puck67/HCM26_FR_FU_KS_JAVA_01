package com.material.controller;

import com.material.entity.Subject;
import com.material.repository.SubjectRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectRepository subjectRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("allSubjects", subjectRepository.findAll());
        return "subject-list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("subject", new Subject());
        return "subject-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Optional<Subject> subjectOpt = subjectRepository.findById(id);
        if (subjectOpt.isPresent()) {
            model.addAttribute("subject", subjectOpt.get());
            return "subject-form";
        }
        return "redirect:/subjects";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("subject") Subject subject,
                       BindingResult result,
                       Model model) {

        if (result.hasErrors()) {
            return "subject-form";
        }

        // Unique code validation
        Optional<Subject> existingSubject = subjectRepository.findByCode(subject.getCode());
        if (existingSubject.isPresent()) {
            if (subject.getId() == null || !existingSubject.get().getId().equals(subject.getId())) {
                model.addAttribute("errorMessage", "Subject code already exists!");
                return "subject-form";
            }
        }

        subjectRepository.save(subject);
        return "redirect:/subjects";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        subjectRepository.deleteById(id);
        return "redirect:/subjects";
    }
}
