package fa.training.ex4.mappers;

import org.mapstruct.MappingTarget;

public interface GenericMapper<T, REQ_C, REQ_U, RES> {
    RES toResponse(T entity);

    T toEntity(REQ_C request);

    void updateEntityFromRequest(REQ_U request, @MappingTarget T entity);
}
