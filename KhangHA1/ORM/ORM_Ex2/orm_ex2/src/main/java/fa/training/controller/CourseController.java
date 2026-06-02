package fa.training.controller;

import fa.training.entity.Course;
import fa.training.service.CourseService;
import fa.training.service.impl.CourseServiceImpl;
import fa.training.util.ConsoleUtil;
import fa.training.view.CourseView;

import java.util.List;

public class CourseController {
    private final CourseService courseService = new CourseServiceImpl();
    private final CourseView view = new CourseView();

    public CourseView getView() {
        return view;
    }

    public void addCourse() {
        String title = ConsoleUtil.readString("Enter course title: ");
        int credit = ConsoleUtil.readInt("Enter course credit: ");
        courseService.createCourse(title, credit);
    }

    public void displayAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            courses.forEach(System.out::println);
        }
    }

    public void findCourseById() {
        int viewId = ConsoleUtil.readInt("Enter course ID to view: ");
        Course course = courseService.getCourseById(viewId);
        if (course != null) {
            System.out.println(course);
        } else {
            System.out.println("Course not found.");
        }
    }

    public void updateCourse() {
        int updateId = ConsoleUtil.readInt("Enter course ID to update: ");
        String newTitle = ConsoleUtil.readString("Enter new title: ");
        int newCredit = ConsoleUtil.readInt("Enter new credit: ");
        courseService.updateCourse(updateId, newTitle, newCredit);
    }

    public void deleteCourse() {
        int deleteId = ConsoleUtil.readInt("Enter course ID to delete: ");
        courseService.deleteCourse(deleteId);
    }
}
