package fa.training.ex4.mappers;

import fa.training.ex4.dto.response.MaterialResponse;
import fa.training.ex4.entities.Material;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MaterialMapper {
    MaterialResponse toResponse(Material material);
}
