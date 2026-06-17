package fa.training.ex5.service;

import fa.training.ex5.dto.request.CourseRequestDTO;
import fa.training.ex5.dto.response.CourseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {
    List<CourseResponse> findAll();
    Page<CourseResponse> findAll(Pageable pageable);
    CourseResponse findById(Long id);
    CourseResponse createCourse(CourseRequestDTO request);
    CourseResponse updateCourse(Long id, CourseRequestDTO request);
    void deleteCourse(Long id);
    Page<CourseResponse> searchCourses(String keyword, Pageable pageable);
}
