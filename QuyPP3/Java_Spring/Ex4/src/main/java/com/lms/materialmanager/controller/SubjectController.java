package com.lms.materialmanager.controller;

import com.lms.materialmanager.entity.Subject;
import com.lms.materialmanager.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public String listSubjects(Model model) {
        model.addAttribute("subjects", subjectService.getAllSubjects());
        model.addAttribute("activePage", "subjects");
        return "subjects/subject-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("subject", new Subject());
        model.addAttribute("activePage", "subjects");
        return "subjects/subject-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            Subject subject = subjectService.getSubjectById(id);
            model.addAttribute("subject", subject);
            model.addAttribute("activePage", "subjects");
            return "subjects/subject-form";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/subjects";
        }
    }

    @PostMapping("/save")
    public String saveSubject(@Valid @ModelAttribute("subject") Subject subject, 
                              BindingResult result, 
                              Model model, 
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("activePage", "subjects");
            return "subjects/subject-form";
        }
        try {
            subjectService.saveSubject(subject);
            redirectAttributes.addFlashAttribute("success", "Subject saved successfully.");
            return "redirect:/subjects";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("activePage", "subjects");
            return "subjects/subject-form";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteSubject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            subjectService.deleteSubject(id);
            redirectAttributes.addFlashAttribute("success", "Subject deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete subject: " + e.getMessage());
        }
        return "redirect:/subjects";
    }
}
