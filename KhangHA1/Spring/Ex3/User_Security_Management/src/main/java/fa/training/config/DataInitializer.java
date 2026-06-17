package fa.training.config;

import fa.training.entity.Role;
import fa.training.entity.User;
import fa.training.entity.UserStatus;
import fa.training.repository.RoleRepository;
import fa.training.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        if (roleRepository.count() == 0) {
            Role superAdminRole = new Role();
            superAdminRole.setRoleName("SUPER_ADMIN");
            roleRepository.save(superAdminRole);

            Role adminRole = new Role();
            adminRole.setRoleName("ADMIN");
            roleRepository.save(adminRole);

            Role teacherRole = new Role();
            teacherRole.setRoleName("TEACHER");
            roleRepository.save(teacherRole);
        }

        if (userRepository.count() == 0) {
            Role superAdminRole = roleRepository.findByRoleName("SUPER_ADMIN").orElse(null);
            
            if (superAdminRole != null) {
                User superAdmin = new User();
                superAdmin.setUsername("superadmin");
                superAdmin.setPassword(passwordEncoder.encode("admin123"));
                superAdmin.setFullName("System Super Admin");
                superAdmin.setStatus(UserStatus.ACTIVE);
                superAdmin.getRoles().add(superAdminRole);
                
                userRepository.save(superAdmin);
            }
        }
    }
}
