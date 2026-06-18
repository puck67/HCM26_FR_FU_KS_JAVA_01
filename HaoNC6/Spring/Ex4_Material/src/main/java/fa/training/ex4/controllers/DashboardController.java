package fa.training.ex4.controllers;

import fa.training.ex4.services.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final SubjectService subjectService;

    @GetMapping({"/", "/dashboard"})
    public String viewDashboard(Model model) {
        model.addAttribute("stats", subjectService.getDashboardStats());
        model.addAttribute("pageTitle", "Dashboard");
        return "dashboard";
    }
}
