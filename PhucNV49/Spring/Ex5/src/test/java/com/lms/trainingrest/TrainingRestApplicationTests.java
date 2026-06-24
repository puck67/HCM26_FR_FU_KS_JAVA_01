package com.lms.trainingrest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.trainingrest.dto.CourseRequestDTO;
import com.lms.trainingrest.dto.LoginRequest;
import com.lms.trainingrest.dto.TokenRefreshRequestDTO;
import com.lms.trainingrest.dto.UserRequestDTO;
import com.lms.trainingrest.entity.Role;
import com.lms.trainingrest.entity.User;
import com.lms.trainingrest.repository.RoleRepository;
import com.lms.trainingrest.repository.UserRepository;
import com.lms.trainingrest.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class TrainingRestApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String adminToken;
    private String trainerToken;
    private String studentToken;
    private String refreshToken;

    @BeforeEach
    void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .build();

        // Clear H2 database to start clean
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();

        // Ensure roles are seeded
        Role adminRole = roleRepository.findByRoleName("ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(null, "ADMIN")));
        Role trainerRole = roleRepository.findByRoleName("TRAINER")
                .orElseGet(() -> roleRepository.save(new Role(null, "TRAINER")));
        Role studentRole = roleRepository.findByRoleName("STUDENT")
                .orElseGet(() -> roleRepository.save(new Role(null, "STUDENT")));

        // Create Default Users
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFullName("System Admin");
        admin.setEmail("admin@lms.com");
        admin.setStatus("ACTIVE");
        admin.setRoles(Set.of(adminRole));
        userRepository.save(admin);

        User trainer = new User();
        trainer.setUsername("trainer");
        trainer.setPassword(passwordEncoder.encode("trainer123"));
        trainer.setFullName("Trainer Expert");
        trainer.setEmail("trainer@lms.com");
        trainer.setStatus("ACTIVE");
        trainer.setRoles(Set.of(trainerRole));
        userRepository.save(trainer);

        User student = new User();
        student.setUsername("student");
        student.setPassword(passwordEncoder.encode("student123"));
        student.setFullName("Student Learner");
        student.setEmail("student@lms.com");
        student.setStatus("ACTIVE");
        student.setRoles(Set.of(studentRole));
        userRepository.save(student);

        // Fetch Tokens for testing authorization
        adminToken = fetchToken("admin", "admin123");
        trainerToken = fetchToken("trainer", "trainer123");
        studentToken = fetchToken("student", "student123");
    }

    private String fetchToken(String username, String password) throws Exception {
        LoginRequest request = new LoginRequest(username, password);
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        // Extract refreshToken during admin login
        if (username.equals("admin")) {
            refreshToken = objectMapper.readTree(responseString).get("refreshToken").asText();
        }
        return objectMapper.readTree(responseString).get("token").asText();
    }

    @Test
    void testAuthLoginSuccess() throws Exception {
        LoginRequest request = new LoginRequest("admin", "admin123");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.refreshToken", notNullValue()))
                .andExpect(jsonPath("$.tokenType", is("Bearer")));
    }

    @Test
    void testAuthLoginBadCredentials() throws Exception {
        LoginRequest request = new LoginRequest("admin", "wrongpassword");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
    }

    @Test
    void testTokenRefreshSuccess() throws Exception {
        TokenRefreshRequestDTO request = new TokenRefreshRequestDTO(refreshToken);
        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.refreshToken", notNullValue()));
    }

    @Test
    void testTokenRefreshInvalid() throws Exception {
        TokenRefreshRequestDTO request = new TokenRefreshRequestDTO("invalid-refresh-token");
        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
    }

    @Test
    void testUserManagementAdminOnly() throws Exception {
        // Retrieve users with ADMIN token - should succeed
        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));

        // Retrieve users with TRAINER token - should be blocked
        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + trainerToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));

        // Retrieve users without token - should be blocked
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
    }

    @Test
    void testCreateUserValidation() throws Exception {
        // Missing fields and bad email format
        UserRequestDTO badRequest = new UserRequestDTO("", "123", "", "bademail", "ACTIVE", Set.of("TRAINER"));
        mockMvc.perform(post("/api/users")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.errors", hasSize(greaterThanOrEqualTo(3))));
    }

    @Test
    void testCourseRBACRoles() throws Exception {
        CourseRequestDTO courseRequest = new CourseRequestDTO("Spring Framework REST", 36, "Advanced course in Spring Boot Rest controllers.");

        // TRAINER can create course
        MvcResult result = mockMvc.perform(post("/api/courses")
                        .header("Authorization", "Bearer " + trainerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.courseId").exists())
                .andExpect(jsonPath("$.courseName", is("Spring Framework REST")))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        long courseId = objectMapper.readTree(responseBody).get("courseId").asLong();

        // STUDENT cannot delete course
        mockMvc.perform(delete("/api/courses/" + courseId)
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));

        // ADMIN can delete course
        mockMvc.perform(delete("/api/courses/" + courseId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void testCourseSearchAndPagination() throws Exception {
        CourseRequestDTO course1 = new CourseRequestDTO("React Hooks Masterclass", 24, "Learn hooks state management.");
        CourseRequestDTO course2 = new CourseRequestDTO("Advanced Vue.js CLI", 30, "Webpack routing CLI components.");

        // Create test courses
        mockMvc.perform(post("/api/courses")
                        .header("Authorization", "Bearer " + trainerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/courses")
                        .header("Authorization", "Bearer " + trainerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course2)))
                .andExpect(status().isCreated());

        // Search courses by keyword matching
        mockMvc.perform(get("/api/courses/search?keyword=vue")
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].courseName", is("Advanced Vue.js CLI")));

        // Pagination query
        mockMvc.perform(get("/api/courses?page=0&size=1")
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalPages", greaterThanOrEqualTo(2)));
    }
}
