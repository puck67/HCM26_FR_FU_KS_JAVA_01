package fa.training.TrainingMaterialManagementSystem.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fa.training.TrainingMaterialManagementSystem.controller.base.GenericController;
import fa.training.TrainingMaterialManagementSystem.entity.Subject;
import fa.training.TrainingMaterialManagementSystem.service.SubjectService;
import fa.training.TrainingMaterialManagementSystem.service.base.GenericService;

@Controller
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class AuthController extends GenericController<Subject, Long> {

    private final SubjectService service;

    @Override
    protected GenericService<Subject, Long> getService() {
        return service;
    }

    @Override
    protected String getViewPrefix() {
        return "subject";
    }
}