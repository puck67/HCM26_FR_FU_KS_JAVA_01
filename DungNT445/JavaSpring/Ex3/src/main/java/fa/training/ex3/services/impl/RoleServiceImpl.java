package fa.training.ex3.services.impl;

import fa.training.ex3.dto.request.CreateRoleRequest;
import fa.training.ex3.dto.request.UpdateRoleRequest;
import fa.training.ex3.entities.Role;
import fa.training.ex3.repositories.RoleRepository;
import fa.training.ex3.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role create(CreateRoleRequest request) {
        if (roleRepository.findByRoleName(request.getRole_name()).isPresent()) {
            throw new IllegalArgumentException("Role already exists: " + request.getRole_name());
        }
        Role role = Role.builder()
                .role_name(request.getRole_name())
                .build();
        return roleRepository.save(role);
    }

    @Override
    public Role update(UpdateRoleRequest request) {
        Role role = roleRepository.findById(request.getRole_id())
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + request.getRole_id()));
        
        Optional<Role> existing = roleRepository.findByRoleName(request.getRole_name());
        if (existing.isPresent() && !existing.get().getRole_id().equals(role.getRole_id())) {
            throw new IllegalArgumentException("Role name already exists: " + request.getRole_name());
        }
        
        role.setRole_name(request.getRole_name());
        return roleRepository.save(role);
    }

    @Override
    public void delete(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + id));
        if (!role.getUsers().isEmpty()) {
            throw new IllegalStateException("Cannot delete role because it is assigned to users");
        }
        roleRepository.delete(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }
}
