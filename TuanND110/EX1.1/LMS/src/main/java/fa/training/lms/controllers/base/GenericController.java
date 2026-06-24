package fa.training.lms.controllers.base;

import fa.training.lms.dto.ApiResponse;
import fa.training.lms.interfaces.base.GenericService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

public abstract class GenericController<T, ID> {

    protected final GenericService<T, ID> service;

    protected GenericController(GenericService<T, ID> service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<T>>> getAll() {
        List<T> data = service.findAll();
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách thành công", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<T>> getById(@PathVariable ID id) {
        T data = service.findById(id);
        return ResponseEntity.ok(new ApiResponse<>("Lấy chi tiết thành công", data));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<T>> create(@RequestBody T entity) {
        T data = service.save(entity);
        return ResponseEntity.ok(new ApiResponse<>("Tạo thành công", data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<T>> update(@PathVariable ID id, @RequestBody T entity) {
        T data = service.update(id, entity);
        return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", data));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<T>> patch(@PathVariable ID id, @RequestBody Map<String, Object> updates) {
        T data = service.patch(id, updates);
        return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable ID id) {
        service.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>("Xóa thành công", null));
    }
}