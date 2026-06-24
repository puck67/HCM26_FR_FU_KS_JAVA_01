package fa.training.ex4.controllers;

import fa.training.ex4.dto.request.CreateSubjectRequest;
import fa.training.ex4.dto.request.UpdateSubjectRequest;
import fa.training.ex4.dto.response.SubjectResponse;
import fa.training.ex4.services.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    public String listSubjects(Model model) {
        model.addAttribute("subjects", subjectService.findAll());
        model.addAttribute("pageTitle", "Subject Management");
        return "subject/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("subjectRequest", new CreateSubjectRequest());
        return "subject/add";
    }

    @PostMapping("/add")
    public String addSubject(@Valid @ModelAttribute("subjectRequest") CreateSubjectRequest request,
                             BindingResult result) {
        if (result.hasErrors()) return "subject/add";
        subjectService.create(request);
        return "redirect:/subjects";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        SubjectResponse subjectOldData = subjectService.findById(id);
        model.addAttribute("subject", subjectOldData);

        model.addAttribute("subjectRequest", new UpdateSubjectRequest());
        return "subject/edit";
    }

    @PostMapping("/edit/{id}")
    public String editSubject(@PathVariable Long id,
                              @Valid @ModelAttribute("subjectRequest") UpdateSubjectRequest request,
                              BindingResult result) {
        if (result.hasErrors()) return "subject/edit";
        subjectService.update(id, request);
        return "redirect:/subjects";
    }

    @GetMapping("/delete/{id}")
    public String deleteSubject(@PathVariable Long id) {
        subjectService.delete(id);
        return "redirect:/subjects";
    }
}
