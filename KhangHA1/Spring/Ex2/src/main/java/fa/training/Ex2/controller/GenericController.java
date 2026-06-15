package fa.training.Ex2.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

public abstract class GenericController<DTO, ID> {

    protected abstract String getViewFolder();

    protected abstract void loadCreateData(Model model);

    protected abstract void loadEditData(Model model, ID id);

    protected abstract void saveEntity(DTO dto);

    protected abstract void updateEntity(ID id, DTO dto);

    protected abstract void deleteEntity(ID id);

    protected String getBaseUrl() {
        RequestMapping mapping = this.getClass().getAnnotation(RequestMapping.class);
        if (mapping != null && mapping.value().length > 0) {
            return mapping.value()[0];
        }
        return "/" + getViewFolder();
    }

    @GetMapping("/create")
    public String create(Model model) {

        loadCreateData(model);

        return getViewFolder() + "/create";
    }

    @PostMapping
    public String save(@ModelAttribute DTO dto, Model model) {

        try {
            saveEntity(dto);
            return "redirect:" + getBaseUrl();
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            loadCreateData(model);
            return getViewFolder() + "/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable ID id,
            Model model) {

        loadEditData(model, id);

        return getViewFolder() + "/edit";
    }

    @PostMapping("/update/{id}")
    public String update(
            @PathVariable ID id,
            @ModelAttribute DTO dto,
            Model model) {

        try {
            updateEntity(id, dto);
            return "redirect:" + getBaseUrl();
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            loadEditData(model, id);
            return getViewFolder() + "/edit";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable ID id) {

        deleteEntity(id);

        return "redirect:" + getBaseUrl();
    }
}