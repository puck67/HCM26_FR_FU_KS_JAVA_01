package fa.training.lms.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fa.training.lms.entities.Course;
import fa.training.lms.entities.CourseId;
import fa.training.lms.entities.Lesson;
import fa.training.lms.repositories.CourseRepository;
import fa.training.lms.repositories.LessonRepository;
import fa.training.lms.entities.ContentType;
import fa.training.lms.entities.LessonStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CourseAndLessonControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    private final ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
            .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @BeforeEach
    void setUp() {
        lessonRepository.deleteAll();
        courseRepository.deleteAll();
    }

    @Test
    void testCourseCRUD() throws Exception {
        CourseId courseId = new CourseId("CS101", LocalDate.of(2026, 6, 10));
        Course course = new Course(courseId, "Computer Science 101", "IT", "Dr. John", new ArrayList<>());

        // 1. Create Course
        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id.courseCode").value("CS101"))
                .andExpect(jsonPath("$.data.id.startDate").value("2026-06-10"))
                .andExpect(jsonPath("$.data.courseName").value("Computer Science 101"));

        // 2. Get All Courses
        String allCoursesJson = mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andReturn().getResponse().getContentAsString();
        System.out.println("ALL COURSES IN DB: " + allCoursesJson);

        // 3. Get Course By ID (Composite ID format: courseCode_startDate)
        mockMvc.perform(get("/api/courses/CS101_2026-06-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.courseName").value("Computer Science 101"));

        // 4. Update Course
        course.setCourseName("Intro to Computer Science");
        mockMvc.perform(put("/api/courses/CS101_2026-06-10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.courseName").value("Intro to Computer Science"));

        // 4.5. Patch Course
        java.util.Map<String, Object> courseUpdates = new java.util.HashMap<>();
        courseUpdates.put("category", "Computer Science");
        mockMvc.perform(patch("/api/courses/CS101_2026-06-10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseUpdates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.category").value("Computer Science"))
                .andExpect(jsonPath("$.data.courseName").value("Intro to Computer Science"));

        // 5. Delete Course
        mockMvc.perform(delete("/api/courses/CS101_2026-06-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Xóa thành công"));

        // Verify deleted
        mockMvc.perform(get("/api/courses/CS101_2026-06-10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testLessonCRUD() throws Exception {
        // Create Course first for lesson reference
        CourseId courseId = new CourseId("JAVA101", LocalDate.of(2026, 6, 10));
        Course course = new Course(courseId, "Java Core", "IT", "Instructor", new ArrayList<>());
        courseRepository.save(course);

        Lesson lesson = new Lesson(null, "Introduction to OOP", 45, ContentType.VIDEO, LessonStatus.DRAFT, course);

        // 1. Create Lesson
        String responseContent = mockMvc.perform(post("/api/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lesson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", notNullValue()))
                .andExpect(jsonPath("$.data.lessonName").value("Introduction to OOP"))
                .andReturn().getResponse().getContentAsString();

        java.util.Map<?, ?> responseMap = objectMapper.readValue(responseContent, java.util.Map.class);
        Lesson savedLesson = objectMapper.convertValue(responseMap.get("data"), Lesson.class);
        Long lessonId = savedLesson.getId();

        // 2. Get Lesson By ID
        mockMvc.perform(get("/api/lessons/" + lessonId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.lessonName").value("Introduction to OOP"));

        // 3. Update Lesson
        savedLesson.setLessonName("Advanced OOP Concepts");
        mockMvc.perform(put("/api/lessons/" + lessonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedLesson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.lessonName").value("Advanced OOP Concepts"));

        // 3.5. Patch Lesson
        java.util.Map<String, Object> lessonUpdates = new java.util.HashMap<>();
        lessonUpdates.put("duration", 120);
        mockMvc.perform(patch("/api/lessons/" + lessonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lessonUpdates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.duration").value(120))
                .andExpect(jsonPath("$.data.lessonName").value("Advanced OOP Concepts"));

        // 3.6. Patch Lesson Enum fields
        java.util.Map<String, Object> lessonEnumUpdates = new java.util.HashMap<>();
        lessonEnumUpdates.put("status", "ACTIVE");
        lessonEnumUpdates.put("contentType", "PRACTICE");
        mockMvc.perform(patch("/api/lessons/" + lessonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lessonEnumUpdates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ACTIVE"))
                .andExpect(jsonPath("$.data.contentType").value("PRACTICE"));

        // 4. Delete Lesson
        mockMvc.perform(delete("/api/lessons/" + lessonId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Xóa thành công"));

        // Verify deleted
        mockMvc.perform(get("/api/lessons/" + lessonId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCustomEndpointsAndValidations() throws Exception {
        // Create Course and Lesson
        CourseId courseId = new CourseId("JAVA101", LocalDate.of(2026, 6, 10));
        Course course = new Course(courseId, "Java Core", "IT", "Instructor", new ArrayList<>());
        courseRepository.save(course);

        Lesson lesson1 = new Lesson(null, "OOP Intro", 45, ContentType.VIDEO, LessonStatus.ACTIVE, course);
        Lesson lesson2 = new Lesson(null, "OOP Advanced", 60, ContentType.THEORY, LessonStatus.DRAFT, course);
        lessonRepository.save(lesson1);
        lessonRepository.save(lesson2);

        // 1. Test Course Category endpoint
        mockMvc.perform(get("/api/courses/category/IT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id.courseCode").value("JAVA101"));

        mockMvc.perform(get("/api/courses/category/Math"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(0)));

        // 2. Test Lesson Course ID endpoint
        mockMvc.perform(get("/api/lessons/course/JAVA101_2026-06-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[*].lessonName", containsInAnyOrder("OOP Intro", "OOP Advanced")));

        // 3. Test Validation: Invalid Course Name (Empty)
        Course invalidCourse = new Course(new CourseId("BAD101", LocalDate.of(2026, 6, 10)), "", "IT", "Dr. Bad", new ArrayList<>());
        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCourse)))
                .andExpect(status().isBadRequest());

        // 4. Test Validation: Invalid Lesson Duration (<= 0)
        Lesson invalidLesson = new Lesson(null, "Bad Lesson", -10, ContentType.VIDEO, LessonStatus.DRAFT, course);
        mockMvc.perform(post("/api/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidLesson)))
                .andExpect(status().isBadRequest());

        // 5. Test PATCH Validation: Empty Course Name
        java.util.Map<String, Object> emptyCourseNameUpdate = new java.util.HashMap<>();
        emptyCourseNameUpdate.put("courseName", "   ");
        mockMvc.perform(patch("/api/courses/JAVA101_2026-06-10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyCourseNameUpdate)))
                .andExpect(status().isBadRequest());

        // 6. Test PATCH Validation: Negative Lesson Duration
        java.util.Map<String, Object> negativeDurationUpdate = new java.util.HashMap<>();
        negativeDurationUpdate.put("duration", -5);
        mockMvc.perform(patch("/api/lessons/" + lesson1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(negativeDurationUpdate)))
                .andExpect(status().isBadRequest());

        // 7. Test PATCH Validation: Invalid Lesson Status
        java.util.Map<String, Object> invalidStatusUpdate = new java.util.HashMap<>();
        invalidStatusUpdate.put("status", "SuperActive");
        mockMvc.perform(patch("/api/lessons/" + lesson1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidStatusUpdate)))
                .andExpect(status().isBadRequest());

        // 8. Test PUT Validation: Empty Course Name on Update
        Course courseForPut = new Course(courseId, "  ", "IT", "Instructor", new ArrayList<>());
        mockMvc.perform(put("/api/courses/JAVA101_2026-06-10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseForPut)))
                .andExpect(status().isBadRequest());

        // 9. Test PUT Validation: Negative Lesson Duration on Update
        Lesson lessonForPut = new Lesson(lesson1.getId(), "Valid Name", -5, ContentType.VIDEO, LessonStatus.ACTIVE, course);
        mockMvc.perform(put("/api/lessons/" + lesson1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lessonForPut)))
                .andExpect(status().isBadRequest());
    }
}
