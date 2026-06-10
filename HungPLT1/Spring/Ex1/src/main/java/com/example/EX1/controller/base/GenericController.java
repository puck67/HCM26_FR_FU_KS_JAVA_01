package com.example.EX1.controller.base;

import com.example.EX1.service.base.GenericService;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

/**
 * GenericController - Lớp Controller trừu tượng dùng chung.
 *
 * Cung cấp các phương thức helper tái sử dụng cho CRUD:
 *   - handleList()   : đưa danh sách entity vào model
 *   - handleCreate() : đưa entity mới (rỗng) vào model
 *   - handleEdit()   : đưa entity đang sửa vào model
 *   - handleSave()   : gọi service.save()
 *   - handleDelete() : gọi service.deleteById()
 *
 * Mỗi Controller cụ thể tự định nghĩa @RequestMapping và gọi các hàm helper này.
 * Phù hợp với LMS vì các entity có key phức tạp (CourseId) và URL lồng nhau.
 *
 * @param <T>  Loại Entity (VD: Course, Lesson)
 * @param <ID> Loại khóa chính (VD: CourseId, Long)
 */
public abstract class GenericController<T, ID> {

    protected final GenericService<T, ID> service;
    protected final String viewFolder;   // Thư mục chứa view Thymeleaf (VD: "courses")
    protected final String modelName;    // Tên biến dùng trong template (VD: "course")

    protected GenericController(GenericService<T, ID> service,
                                String viewFolder,
                                String modelName) {
        this.service = service;
        this.viewFolder = viewFolder;
        this.modelName = modelName;
    }

    // ─── Helper Methods ────────────────────────────────────────────────────────

    /**
     * Đưa toàn bộ danh sách entity vào Model và trả về tên view danh sách.
     */
    protected String handleList(Model model) {
        List<T> entities = service.findAll();
        model.addAttribute(modelName + "s", entities);
        return viewFolder + "/list";
    }

    /**
     * Tạo instance entity mới (rỗng) và đưa vào Model để hiển thị form tạo mới.
     */
    protected String handleCreate(Model model) throws Exception {
        T newEntity = getEntityClass().getDeclaredConstructor().newInstance();
        model.addAttribute(modelName, newEntity);
        return viewFolder + "/form";
    }

    /**
     * Tìm entity theo ID và đưa vào Model để hiển thị form chỉnh sửa.
     * Nếu không tìm thấy, redirect về trang danh sách.
     */
    protected String handleEdit(ID id, Model model) {
        Optional<T> entityOpt = service.findById(id);
        if (entityOpt.isEmpty()) {
            return "redirect:/" + viewFolder;
        }
        model.addAttribute(modelName, entityOpt.get());
        return viewFolder + "/form";
    }

    /**
     * Lưu entity (thêm mới hoặc cập nhật) và redirect về trang danh sách.
     */
    protected String handleSave(T entity) {
        service.save(entity);
        return "redirect:/" + viewFolder;
    }

    /**
     * Xóa entity theo ID và redirect về trang danh sách.
     */
    protected String handleDelete(ID id) {
        service.deleteById(id);
        return "redirect:/" + viewFolder;
    }

    /**
     * Phương thức trừu tượng: mỗi Controller con cung cấp Class của Entity.
     * Dùng để tạo instance mới trong handleCreate().
     */
    protected abstract Class<T> getEntityClass();
}
