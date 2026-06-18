package service;

import model.Course;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class CourseService {

    public void saveCourses(List<Course> courses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("courses.dat"))) {
            oos.writeObject(courses);
            System.out.println("Courses saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Course> getCourses() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("courses.dat"))) {
            return (List<Course>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
