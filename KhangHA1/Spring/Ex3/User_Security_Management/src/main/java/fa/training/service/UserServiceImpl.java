package fa.training.service;

import fa.training.entity.User;
import fa.training.repository.UserRepository;
import fa.training.service.base.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends GenericServiceImpl<User, Long> implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected JpaRepository<User, Long> getRepository() {
        return userRepository;
    }

    @Override
    public User save(User user) {
        if (user.getUserId() == null) {
            // New user, encode password
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        } else {
            // Existing user
            User existingUser = userRepository.findById(user.getUserId()).orElse(null);
            if (existingUser != null) {
                if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                } else {
                    user.setPassword(existingUser.getPassword());
                }
            }
        }
        return super.save(user);
    }
}
