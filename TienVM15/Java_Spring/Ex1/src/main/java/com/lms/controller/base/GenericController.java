package com.lms.controller.base;

import com.lms.service.base.GenericService;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

public abstract class GenericController<T, ID> {

    protected final GenericService<T, ID> service;
    protected final String viewFolder;       // e.g., "courses"
    protected final String modelName;        // e.g., "course"

    protected GenericController(GenericService<T, ID> service, String viewFolder, String modelName) {
        this.service = service;
        this.viewFolder = viewFolder;
        this.modelName = modelName;
    }

    // Helper method to wrap templates in layout
    protected String renderLayout(Model model, String contentFragment) {
        model.addAttribute("activePage", viewFolder + "-management");
        model.addAttribute("content", viewFolder + "/" + contentFragment);
        return "layout";
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute(modelName + "s", service.findAll());
        return renderLayout(model, "list :: body");
    }

    @GetMapping("/create")
    public String createForm(Model model) throws Exception {
        model.addAttribute(modelName, getEntityClass().getDeclaredConstructor().newInstance());
        return renderLayout(model, "form :: body");
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable ID id, Model model) {
        service.findById(id).ifPresent(entity -> model.addAttribute(modelName, entity));
        return renderLayout(model, "form :: body");
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute T entity, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return renderLayout(model, "form :: body");
        }
        service.save(entity);
        return "redirect:/" + viewFolder;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable ID id) {
        service.deleteById(id);
        return "redirect:/" + viewFolder;
    }

    protected abstract Class<T> getEntityClass();
}
