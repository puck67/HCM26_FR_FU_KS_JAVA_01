package com.example.view;

import com.example.model.Course;
import com.example.model.Student;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentView {

    public void printStudentList(Map<Student, List<Course>> studentCoursesMap) {
        System.out.println("\n=================================== STUDENTS LIST ===================================");
        if (studentCoursesMap.isEmpty()) {
            System.out.println("No students found in the system.");
            return;
        }

        String separator = "+------+---------------------------+-----+----------------------------------------------------+";
        System.out.println(separator);
        System.out.printf("| %-4s | %-25s | %-3s | %-50s |\n", "ID", "Student Name", "Age", "Enrolled Courses");
        System.out.println(separator);

        studentCoursesMap.forEach((student, courses) -> {
            String coursesStr = courses.stream()
                    .map(Course::getTitle)
                    .collect(Collectors.joining(", "));
            if (coursesStr.length() > 50) {
                coursesStr = coursesStr.substring(0, 47) + "...";
            }
            System.out.printf("| %-4d | %-25s | %-3d | %-50s |\n",
                    student.getId(),
                    student.getName(),
                    student.getAge(),
                    coursesStr.isEmpty() ? "(Not enrolled)" : coursesStr);
        });
        System.out.println(separator);
    }

    public void printStudentDetails(Student student, List<Course> courses) {
        System.out.println("\n+-----------------------------------------------------------------------------------+");
        System.out.println("|                                  STUDENT DETAILS                                  |");
        System.out.println("+-----------------------------------------------------------------------------------+");
        System.out.printf("| %-15s : %-61s |\n", "Student ID", student.getId());
        System.out.printf("| %-15s : %-61s |\n", "Student Name", student.getName());
        System.out.printf("| %-15s : %-61s |\n", "Student Age", student.getAge());
        System.out.println("+-----------------------------------------------------------------------------------+");
        System.out.println("|                                  ENROLLED COURSES                                 |");
        System.out.println("+------+-------------------------------------------------------------+--------------+");
        System.out.printf("| %-4s | %-59s | %-12s |\n", "ID", "Course Title", "Credits");
        System.out.println("+------+-------------------------------------------------------------+--------------+");

        if (courses.isEmpty()) {
            System.out.printf("| %-80s |\n", "(Not enrolled in any courses)");
        } else {
            courses.forEach(course -> {
                String title = course.getTitle();
                if (title.length() > 59) {
                    title = title.substring(0, 56) + "...";
                }
                System.out.printf("| %-4d | %-59s | %-12d |\n",
                        course.getId(),
                        title,
                        course.getCredit());
            });
        }
        System.out.println("+------+-------------------------------------------------------------+--------------+");
    }

    public void printStudentsAndCoursesJoin(List<Object[]> results) {
        System.out.println("\n================ STUDENTS AND ENROLLED COURSES (HQL JOIN) ================");
        if (results.isEmpty()) {
            System.out.println("No enrollments found.");
            return;
        }
        String separator = "+------+---------------------------+------+------------------------------------------+---------+";
        System.out.println(separator);
        System.out.printf("| %-4s | %-25s | %-4s | %-40s | %-7s |\n", "S.ID", "Student Name", "Age", "Course Title", "Credits");
        System.out.println(separator);
        for (Object[] row : results) {
            Student s = (Student) row[0];
            Course c = (Course) row[1];
            String studentName = s.getName();
            if (studentName.length() > 25) {
                studentName = studentName.substring(0, 22) + "...";
            }
            String courseTitle = c.getTitle();
            if (courseTitle.length() > 40) {
                courseTitle = courseTitle.substring(0, 37) + "...";
            }
            System.out.printf("| %-4d | %-25s | %-4d | %-40s | %-7d |\n",
                    s.getId(), studentName, s.getAge(), courseTitle, c.getCredit());
        }
        System.out.println(separator);
    }
}
