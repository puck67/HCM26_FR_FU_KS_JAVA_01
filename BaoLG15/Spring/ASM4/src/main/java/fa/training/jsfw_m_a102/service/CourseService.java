package fa.training.jsfw_m_a102.service;

import fa.training.jsfw_m_a102.model.Course;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {
    private final String FILE_NAME = "course.dat";

    public void saveCourses(List<Course> courses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(courses);
            System.out.println(">>> Đã lưu danh sách khóa học vào file thành công.");
        } catch (IOException ex) {
            System.err.println("Lỗi khi ghi file: " + ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<Course> getCourses() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Course>) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Lỗi khi đọc file: " + ex.getMessage());
            return new ArrayList<>();
        }
    }
}
