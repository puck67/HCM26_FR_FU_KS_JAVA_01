package com.lms.securitymanager;

import com.lms.securitymanager.config.CustomAuthenticationSuccessHandler;
import com.lms.securitymanager.entity.Role;
import com.lms.securitymanager.entity.User;
import com.lms.securitymanager.repository.RoleRepository;
import com.lms.securitymanager.repository.UserRepository;
import com.lms.securitymanager.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserSecurityTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Test
    public void testDatabaseSeeded() {
        // Assert roles seeded
        assertTrue(roleRepository.count() >= 3, "Roles should be seeded");
        assertTrue(roleRepository.findByRoleName("SUPER_ADMIN").isPresent(), "SUPER_ADMIN role should exist");
        assertTrue(roleRepository.findByRoleName("ADMIN").isPresent(), "ADMIN role should exist");
        assertTrue(roleRepository.findByRoleName("TEACHER").isPresent(), "TEACHER role should exist");

        // Assert superadmin user seeded
        assertTrue(userRepository.findByUsername("superadmin").isPresent(), "superadmin user should exist");
        User superAdmin = userRepository.findByUsername("superadmin").get();
        assertTrue(superAdmin.getRoles().stream().anyMatch(r -> r.getRoleName().equals("SUPER_ADMIN")), "superadmin should have SUPER_ADMIN role");
        assertTrue(superAdmin.getStatus(), "superadmin should be active");
    }

    @Test
    public void testInactiveUserCannotLogin() {
        // Create an inactive user
        Role teacherRole = roleRepository.findByRoleName("TEACHER").orElseThrow();
        
        User inactiveUser = new User();
        inactiveUser.setUsername("testteacher");
        inactiveUser.setPassword("password123");
        inactiveUser.setFullName("Test Teacher");
        inactiveUser.setStatus(false); // Inactive
        Set<Role> roles = new HashSet<>();
        roles.add(teacherRole);
        inactiveUser.setRoles(roles);
        
        userRepository.save(inactiveUser);

        // Load details via CustomUserDetailsService
        UserDetails details = userDetailsService.loadUserByUsername("testteacher");
        
        // Inactive maps to disabled account
        assertFalse(details.isEnabled(), "Inactive status should map to a disabled account in UserDetails");
    }

    @Test
    public void testActiveUserCanLogin() {
        // Create an active user
        Role teacherRole = roleRepository.findByRoleName("TEACHER").orElseThrow();

        User activeUser = new User();
        activeUser.setUsername("activeteacher");
        activeUser.setPassword("password123");
        activeUser.setFullName("Active Teacher");
        activeUser.setStatus(true); // Active
        Set<Role> roles = new HashSet<>();
        roles.add(teacherRole);
        activeUser.setRoles(roles);

        userRepository.save(activeUser);

        // Load details via CustomUserDetailsService
        UserDetails details = userDetailsService.loadUserByUsername("activeteacher");

        // Active maps to enabled account
        assertTrue(details.isEnabled(), "Active status should map to an enabled account in UserDetails");
    }

    @Test
    public void testSuccessHandlerRedirects() throws Exception {
        CustomAuthenticationSuccessHandler handler = new CustomAuthenticationSuccessHandler();
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        // 1. Test SUPER_ADMIN redirects to /users
        MockHttpServletResponse responseSuperAdmin = new MockHttpServletResponse();
        Authentication superAdminAuth = new UsernamePasswordAuthenticationToken(
                "superadmin", "pass", List.of(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN")));
        handler.onAuthenticationSuccess(request, responseSuperAdmin, superAdminAuth);
        assertEquals("/users", responseSuperAdmin.getRedirectedUrl(), "SUPER_ADMIN should redirect to /users");

        // 2. Test ADMIN redirects to /users
        MockHttpServletResponse responseAdmin = new MockHttpServletResponse();
        Authentication adminAuth = new UsernamePasswordAuthenticationToken(
                "admin", "pass", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        handler.onAuthenticationSuccess(request, responseAdmin, adminAuth);
        assertEquals("/users", responseAdmin.getRedirectedUrl(), "ADMIN should redirect to /users");

        // 3. Test TEACHER redirects to /dashboard
        MockHttpServletResponse responseTeacher = new MockHttpServletResponse();
        Authentication teacherAuth = new UsernamePasswordAuthenticationToken(
                "teacher", "pass", List.of(new SimpleGrantedAuthority("ROLE_TEACHER")));
        handler.onAuthenticationSuccess(request, responseTeacher, teacherAuth);
        assertEquals("/dashboard", responseTeacher.getRedirectedUrl(), "TEACHER should redirect to /dashboard");
    }
}
