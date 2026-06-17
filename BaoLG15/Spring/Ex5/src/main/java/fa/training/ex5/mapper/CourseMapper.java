package fa.training.ex5.mapper;

import fa.training.ex5.dto.request.CourseRequestDTO;
import fa.training.ex5.dto.response.CourseResponse;
import fa.training.ex5.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper extends GenericMapper<Course, CourseRequestDTO, CourseResponse> {
    @Override
    @Mapping(target = "courseId", source = "id")
    CourseResponse toResponseDTO(Course entity);
}
