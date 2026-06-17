package fa.training.ex3.services;

import fa.training.ex3.dto.request.CreateRoleRequest;
import fa.training.ex3.dto.request.UpdateRoleRequest;
import fa.training.ex3.entities.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Role create(CreateRoleRequest request);

    Role update(UpdateRoleRequest request);

    void delete(Long id);

    List<Role> findAll();

    Optional<Role> findById(Long id);
}
