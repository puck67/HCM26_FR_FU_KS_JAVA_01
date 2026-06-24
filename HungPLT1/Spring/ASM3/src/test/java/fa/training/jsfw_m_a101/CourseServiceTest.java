package fa.training.jsfw_m_a101;

import fa.training.jsfw_m_a101.model.Course;
import fa.training.jsfw_m_a101.service.CourseService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CourseServiceTest {

    private final CourseService courseService = new CourseService();

    @AfterEach
    void tearDown() {
        File file = new File("courses.dat");
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testSaveAndGetCourses() {
        List<Course> list = new ArrayList<>();
        list.add(new Course("Test Course", "Instructor", "Description", 10));

        courseService.saveCourses(list);

        List<Course> loaded = courseService.getCourses();
        assertEquals(1, loaded.size());
        assertEquals("Test Course", loaded.get(0).getTitle());
        assertEquals("Instructor", loaded.get(0).getInstructorName());
        assertEquals("Description", loaded.get(0).getDescription());
        assertEquals(10, loaded.get(0).getDurationHours());
    }
}
