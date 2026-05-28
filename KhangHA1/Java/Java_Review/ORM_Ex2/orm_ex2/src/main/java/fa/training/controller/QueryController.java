package fa.training.controller;

import fa.training.entity.Course;
import fa.training.entity.Student;
import fa.training.service.CourseService;
import fa.training.service.StudentService;
import fa.training.service.impl.CourseServiceImpl;
import fa.training.service.impl.StudentServiceImpl;
import fa.training.util.ConsoleUtil;
import fa.training.view.QueryView;

import java.util.List;

public class QueryController {
    private final StudentService studentService = new StudentServiceImpl();
    private final CourseService courseService = new CourseServiceImpl();
    private final QueryView view = new QueryView();

    public QueryView getView() {
        return view;
    }

    public void findStudentsOlderThan() {
        int age = ConsoleUtil.readInt("Enter age: ");
        List<Student> olderStudents = studentService.getStudentsOlderThan(age);
        if (olderStudents.isEmpty()) {
            System.out.println("No students found older than " + age);
        } else {
            olderStudents.forEach(System.out::println);
        }
    }

    public void findStudentsByName() {
        String name = ConsoleUtil.readString("Enter name to search: ");
        List<Student> studentsByName = studentService.getStudentsByName(name);
        if (studentsByName.isEmpty()) {
            System.out.println("No students found with name: " + name);
        } else {
            studentsByName.forEach(System.out::println);
        }
    }

    public void listStudentsWithCourses() {
        List<Object[]> studentCourses = studentService.getStudentsWithCourseTitles();
        if (studentCourses.isEmpty()) {
            System.out.println("No enrollments found.");
        } else {
            for (Object[] row : studentCourses) {
                Student s = (Student) row[0];
                String courseTitle = (String) row[1];
                System.out.println("Student: " + s.getName() + " | Course: " + courseTitle);
            }
        }
    }

    public void findCoursesWithHighCredit() {
        int credit = ConsoleUtil.readInt("Enter minimum credit value: ");
        List<Course> courses = courseService.getCoursesWithCreditGreaterThan(credit);
        if (courses.isEmpty()) {
            System.out.println("No courses found with credit > " + credit);
        } else {
            courses.forEach(System.out::println);
        }
    }

    public void countStudentsPerCourse() {
        List<Object[]> counts = courseService.getStudentCountPerCourse();
        if (counts.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            for (Object[] row : counts) {
                String title = (String) row[0];
                Long count = (Long) row[1];
                System.out.println("Course: " + title + " | Students enrolled: " + count);
            }
        }
    }

    public void findStudentsEnrolledInCourse() {
        int courseId = ConsoleUtil.readInt("Enter Course ID: ");
        List<Student> enrolledStudents = studentService.getStudentsEnrolledInCourse(courseId);
        if (enrolledStudents.isEmpty()) {
            System.out.println("No students found in this course.");
        } else {
            enrolledStudents.forEach(System.out::println);
        }
    }
}
