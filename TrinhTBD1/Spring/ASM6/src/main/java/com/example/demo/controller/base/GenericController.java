package com.example.demo.controller.base;

import com.example.demo.service.base.GenericService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public abstract class GenericController<T, ID> {

    protected final GenericService<T, ID> service;
    protected final String viewFolder;
    protected final String modelName;

    protected GenericController(GenericService<T, ID> service, String viewFolder, String modelName) {
        this.service = service;
        this.viewFolder = viewFolder;
        this.modelName = modelName;
    }

    @GetMapping
    public String list(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<T> entityPage = getEntityPage(pageable);
        model.addAttribute(modelName + "Page", entityPage);
        return viewFolder + "/manage-" + modelName + "s";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute(modelName)) {
            model.addAttribute(modelName, createEmptyEntity());
        }
        addFormAttributes(model);
        return viewFolder + "/" + modelName + "-form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute T entity, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            addFormAttributes(model);
            return viewFolder + "/" + modelName + "-form";
        }
        saveEntity(entity);
        return new StringBuilder()
                .append("redirect:/")
                .append(viewFolder)
                .append("/")
                .append(modelName)
                .append("s?created=true")
                .toString();
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") ID id, Model model) {
        T entity = service.findById(id);
        model.addAttribute(modelName, entity);
        addFormAttributes(model);
        return viewFolder + "/" + modelName + "-form";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable("id") ID id,
            @Valid @ModelAttribute T entity,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            addFormAttributes(model);
            return viewFolder + "/" + modelName + "-form";
        }
        setEntityId(entity, id);
        beforeUpdate(entity, id);
        saveEntity(entity);
        return new StringBuilder()
                .append("redirect:/")
                .append(viewFolder)
                .append("/")
                .append(modelName)
                .append("s?updated=true")
                .toString();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") ID id) {
        deleteEntity(id);
        return new StringBuilder()
                .append("redirect:/")
                .append(viewFolder)
                .append("/")
                .append(modelName)
                .append("s?deleted=true")
                .toString();
    }

    protected Page<T> getEntityPage(Pageable pageable) {
        return service.findAll(pageable);
    }

    protected void saveEntity(T entity) {
        service.save(entity);
    }

    protected void deleteEntity(ID id) {
        service.deleteById(id);
    }

    protected void beforeUpdate(T entity, ID id) {
    }

    protected abstract T createEmptyEntity();
    protected abstract void setEntityId(T entity, ID id);
    protected abstract void addFormAttributes(Model model);
}
