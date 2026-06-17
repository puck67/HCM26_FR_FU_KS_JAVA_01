package fa.training.ex3.config;

import fa.training.ex3.entities.Role;
import fa.training.ex3.entities.User;
import fa.training.ex3.enums.RoleEnum;
import fa.training.ex3.enums.UserStatus;
import fa.training.ex3.repositories.RoleRepository;
import fa.training.ex3.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            for (RoleEnum roleEnum : RoleEnum.values()) {
                Role role = Role.builder()
                        .role_name(roleEnum)
                        .build();
                roleRepository.save(role);
            }
        }

        if (userRepository.count() == 0) {
            Role superAdminRole = roleRepository.findByRoleName(RoleEnum.SUPER_ADMIN)
                    .orElseThrow(() -> new IllegalStateException("SUPER_ADMIN role not found"));

            Set<Role> roles = new HashSet<>();
            roles.add(superAdminRole);

            User superAdmin = User.builder()
                    .username("superadmin")
                    .password(passwordEncoder.encode("admin123"))
                    .full_name("Super Admin")
                    .status(UserStatus.ACTIVE)
                    .roles(roles)
                    .build();

            userRepository.save(superAdmin);
        }
    }
}
