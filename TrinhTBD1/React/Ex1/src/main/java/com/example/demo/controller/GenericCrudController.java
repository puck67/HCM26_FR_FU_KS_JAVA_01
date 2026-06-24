package com.example.demo.controller;

import com.example.demo.service.GenericService;
import com.example.demo.util.GenericTableHelper;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

public abstract class GenericCrudController<T, ID> {
    protected final GenericService<T, ID> service;
    protected final Class<T> entityClass;
    protected final String entityName;
    protected final String pageTitle;
    protected final String createButtonText;

    protected GenericCrudController(GenericService<T, ID> service, Class<T> entityClass, String entityName, String pageTitle, String createButtonText) {
        this.service = service;
        this.entityClass = entityClass;
        this.entityName = entityName;
        this.pageTitle = pageTitle;
        this.createButtonText = createButtonText;
    }

    @GetMapping
    public String list(Model model) {
        List<T> data = service.findAll();
        model.addAttribute("entityName", entityName);
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("createButtonText", createButtonText);
        model.addAttribute("headers", GenericTableHelper.getHeaders(entityClass));
        model.addAttribute("rows", GenericTableHelper.getRows(data));
        return "crud/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        try {
            T entity = entityClass.getDeclaredConstructor().newInstance();
            model.addAttribute("entity", entity);
            model.addAttribute("fields", GenericTableHelper.getFields(entity));
        } catch (Exception e) {
        }
        model.addAttribute("entityName", entityName);
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("actionUrl", new StringBuilder("/").append(entityName).append("/create").toString());
        return "crud/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("entity") T entity, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("entityName", entityName);
            model.addAttribute("pageTitle", pageTitle);
            model.addAttribute("fields", GenericTableHelper.getFields(entity));
            model.addAttribute("actionUrl", new StringBuilder("/").append(entityName).append("/create").toString());
            return "crud/form";
        }
        service.save(entity);
        redirectAttributes.addFlashAttribute("successMessage", new StringBuilder(pageTitle).append(" created successfully!").toString());
        return new StringBuilder("redirect:/").append(entityName).toString();
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") ID id, Model model, RedirectAttributes redirectAttributes) {
        return service.findById(id)
                .map(entity -> {
                    model.addAttribute("entity", entity);
                    model.addAttribute("entityName", entityName);
                    model.addAttribute("pageTitle", pageTitle);
                    model.addAttribute("fields", GenericTableHelper.getFields(entity));
                    model.addAttribute("actionUrl", new StringBuilder("/").append(entityName).append("/edit/").append(id).toString());
                    return "crud/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Item not found!");
                    return new StringBuilder("redirect:/").append(entityName).toString();
                });
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") ID id, @Valid @ModelAttribute("entity") T entity, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("entityName", entityName);
            model.addAttribute("pageTitle", pageTitle);
            model.addAttribute("fields", GenericTableHelper.getFields(entity));
            model.addAttribute("actionUrl", new StringBuilder("/").append(entityName).append("/edit/").append(id).toString());
            return "crud/form";
        }
        setIdOfEntity(entity, id);
        service.save(entity);
        redirectAttributes.addFlashAttribute("successMessage", new StringBuilder(pageTitle).append(" updated successfully!").toString());
        return new StringBuilder("redirect:/").append(entityName).toString();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") ID id, RedirectAttributes redirectAttributes) {
        try {
            service.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Item deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting item!");
        }
        return new StringBuilder("redirect:/").append(entityName).toString();
    }

    @GetMapping("/export")
    public void export(jakarta.servlet.http.HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String fileName = new StringBuilder(entityName).append("_export.xlsx").toString();
            response.setHeader("Content-Disposition", new StringBuilder("attachment; filename=").append(fileName).toString());
            com.example.demo.util.ExcelHelper.exportToExcel(response.getOutputStream(), service.findAll(), entityClass);
        } catch (Exception e) {
            response.setStatus(500);
        }
    }

    @PostMapping("/import")
    public String importData(@RequestParam("file") org.springframework.web.multipart.MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Please select a file to import!");
                return new StringBuilder("redirect:/").append(entityName).toString();
            }
            List<T> importedItems = com.example.demo.util.ExcelHelper.importFromExcel(file.getInputStream(), entityClass);
            jakarta.validation.Validator validator = jakarta.validation.Validation.buildDefaultValidatorFactory().getValidator();
            int successCount = 0;
            int errorCount = 0;
            for (T item : importedItems) {
                if (validator.validate(item).isEmpty()) {
                    service.save(item);
                    successCount++;
                } else {
                    errorCount++;
                }
            }
            StringBuilder msg = new StringBuilder("Imported ").append(successCount).append(" items successfully!");
            if (errorCount > 0) {
                msg.append(" (Failed to validate ").append(errorCount).append(" items)");
            }
            redirectAttributes.addFlashAttribute("successMessage", msg.toString());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Tệp Excel không đúng mẫu yêu cầu. Vui lòng tải lại tệp mẫu!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi nhập dữ liệu từ Excel!");
        }
        return new StringBuilder("redirect:/").append(entityName).toString();
    }

    private void setIdOfEntity(T entity, ID id) {
        try {
            java.lang.reflect.Field idField = entityClass.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (Exception e) {
        }
    }
}
