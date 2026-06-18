package fa.training.ex5.mapper;

import fa.training.ex5.dto.request.UserRequestDTO;
import fa.training.ex5.dto.response.UserResponse;
import fa.training.ex5.entity.User;
import fa.training.ex5.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper extends GenericMapper<User, UserRequestDTO, UserResponse> {
    @Override
    @Mapping(target = "roles", source = "roles", qualifiedByName = "roleSetToStringSet")
    UserResponse toResponseDTO(User user);

    @Override
    @Mapping(target = "roles", ignore = true)
    User toEntity(UserRequestDTO dto);

    @org.mapstruct.Named("roleSetToStringSet")
    default Set<String> roleSetToStringSet(Set<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream().map(Role::getName).collect(Collectors.toSet());
    }
}
