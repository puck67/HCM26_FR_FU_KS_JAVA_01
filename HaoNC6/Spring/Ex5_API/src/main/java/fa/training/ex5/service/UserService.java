package fa.training.ex5.service;

import fa.training.ex5.dto.request.UserRequestDTO;
import fa.training.ex5.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponse> findAll();

    UserResponse createUser(UserRequestDTO request);

    UserResponse updateUser(UUID id, UserRequestDTO request);

    void deleteUser(UUID id);
}
