package fa.training.TrainingMaterialManagementSystem.controller.base;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import fa.training.TrainingMaterialManagementSystem.service.base.GenericService;

public abstract class GenericController<T, ID> {
    protected abstract GenericService<T, ID> getService();

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", getService().findAll());
        return getViewPrefix() + "/list";
    }

    protected abstract String getViewPrefix();
}
