package fa.training.ex3.services;

import fa.training.ex3.dto.request.CreateUserRequest;
import fa.training.ex3.dto.request.ResetPasswordRequest;
import fa.training.ex3.dto.request.UpdateUserRequest;
import fa.training.ex3.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(CreateUserRequest request);

    User update(UpdateUserRequest request);

    void delete(Long id);

    List<User> findAll();

    Optional<User> findById(Long id);

    void resetPassword(ResetPasswordRequest request);

    Optional<User> findByUsername(String username);
}
