package fa.training.ex5.service.impl;

import fa.training.ex5.dto.request.CourseRequestDTO;
import fa.training.ex5.dto.response.CourseResponse;
import fa.training.ex5.entity.Course;
import fa.training.ex5.exception.ResourceNotFoundException;
import fa.training.ex5.mapper.CourseMapper;
import fa.training.ex5.repository.CourseRepository;
import fa.training.ex5.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public List<CourseResponse> findAll() {
        List<Course> courses = courseRepository.findAll();
        return courseMapper.toResponseDTOList(courses);
    }

    @Override
    public Page<CourseResponse> findAll(Pageable pageable) {
        Page<Course> coursePage = courseRepository.findAll(pageable);
        return coursePage.map(courseMapper::toResponseDTO);
    }

    @Override
    public CourseResponse findById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        return courseMapper.toResponseDTO(course);
    }

    @Override
    public CourseResponse createCourse(CourseRequestDTO request) {
        Course course = courseMapper.toEntity(request);
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toResponseDTO(savedCourse);
    }

    @Override
    public CourseResponse updateCourse(Long id, CourseRequestDTO request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));

        course.setCourseName(request.getCourseName());
        course.setDuration(request.getDuration());
        course.setDescription(request.getDescription());

        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toResponseDTO(updatedCourse);
    }

    @Override
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course", "id", id);
        }
        courseRepository.deleteById(id);
    }

    @Override
    public Page<CourseResponse> searchCourses(String keyword, Pageable pageable) {
        Page<Course> coursePage = courseRepository.findByCourseNameContainingIgnoreCase(keyword, pageable);
        return coursePage.map(courseMapper::toResponseDTO);
    }
}
