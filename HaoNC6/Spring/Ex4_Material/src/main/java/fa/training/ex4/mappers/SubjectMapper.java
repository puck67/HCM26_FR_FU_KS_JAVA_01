package fa.training.ex4.mappers;

import fa.training.ex4.dto.request.CreateSubjectRequest;
import fa.training.ex4.dto.request.UpdateSubjectRequest;
import fa.training.ex4.dto.response.SubjectResponse;
import fa.training.ex4.entities.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SubjectMapper extends GenericMapper<Subject, CreateSubjectRequest, UpdateSubjectRequest, SubjectResponse> {
    @Override
    @Mapping(target = "materials", ignore = true)
    Subject toEntity(CreateSubjectRequest request);

    @Override
    @Mapping(target = "subject_id", ignore = true)
    @Mapping(target = "materials", ignore = true)
    void updateEntityFromRequest(UpdateSubjectRequest request, @MappingTarget Subject subject);
}
