package fa.training.ex5.mapper;

import java.util.List;

public interface GenericMapper<E, R, D> {
    D toResponseDTO(E entity);

    E toEntity(R dto);

    List<D> toResponseDTOList(List<E> entityList);
}

