package fa.training.ex3.services.impl;

import fa.training.ex3.dto.request.CreateUserRequest;
import fa.training.ex3.dto.request.ResetPasswordRequest;
import fa.training.ex3.dto.request.UpdateUserRequest;
import fa.training.ex3.entities.Role;
import fa.training.ex3.entities.User;
import fa.training.ex3.repositories.RoleRepository;
import fa.training.ex3.repositories.UserRepository;
import fa.training.ex3.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User create(CreateUserRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + request.getUsername());
        }

        Set<Role> roles = new HashSet<>();
        for (Long roleId : request.getRoleIds()) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + roleId));
            roles.add(role);
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .full_name(request.getFull_name())
                .status(request.getStatus())
                .roles(roles)
                .build();

        return userRepository.save(user);
    }

    @Override
    public User update(UpdateUserRequest request) {
        User user = userRepository.findById(request.getUser_id())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + request.getUser_id()));

        Optional<User> existing = userRepository.findByUsername(request.getUsername());
        if (existing.isPresent() && !existing.get().getUser_id().equals(user.getUser_id())) {
            throw new IllegalArgumentException("Username already exists: " + request.getUsername());
        }

        user.setUsername(request.getUsername());
        user.setFull_name(request.getFull_name());
        user.setStatus(request.getStatus());

        Set<Role> newRoles = new HashSet<>();
        for (Long roleId : request.getRoleIds()) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + roleId));
            newRoles.add(role);
        }
        user.setRoles(newRoles);

        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        
        user.getRoles().clear();
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAllWithRoles();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findByIdWithRoles(id);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findById(request.getUser_id())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + request.getUser_id()));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
