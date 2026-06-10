package fa.training.lms.controllers.base;

import fa.training.lms.services.GenericService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

public abstract class GenericController<T, ID> {

    protected final GenericService<T, ID> service;
    protected final String viewFolder; // Thư mục chứa view Thymeleaf (VD: "courses")
    protected final String modelName; // Tên biến dùng trong form/list (VD: "course")

    protected GenericController(GenericService<T, ID> service, String viewFolder, String modelName) {
        this.service = service;
        this.viewFolder = viewFolder;
        this.modelName = modelName;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute(modelName + "s", service.findAll());
        return viewFolder + "/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) throws Exception {
        // Tự động khởi tạo instance mới cho Model (Yêu cầu Class có No-Args
        // Constructor)
        model.addAttribute(modelName, getEntityClass().getDeclaredConstructor().newInstance());
        return viewFolder + "/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable ID id, Model model) {
        service.findById(id).ifPresent(entity -> model.addAttribute(modelName, entity));
        return viewFolder + "/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute T entity) {
        service.save(entity);
        return "redirect:/" + viewFolder;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable ID id) {
        service.deleteById(id);
        return "redirect:/" + viewFolder;
    }

    // Phương thức bổ trợ để lấy Class type của Entity phục vụ tạo mới instance
    protected abstract Class<T> getEntityClass();
}
