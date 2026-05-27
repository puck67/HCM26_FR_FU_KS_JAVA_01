package com.example.view;

import com.example.model.Course;
import com.example.model.Student;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CourseView {

    public void printCourseList(Map<Course, List<Student>> courseStudentsMap) {
        System.out.println("\n==================================== COURSES LIST ===================================");
        if (courseStudentsMap.isEmpty()) {
            System.out.println("No courses found in the system.");
            return;
        }

        String separator = "+------+---------------------------------+---------+------------------------------------------+";
        System.out.println(separator);
        System.out.printf("| %-4s | %-31s | %-7s | %-40s |\n", "ID", "Course Title", "Credits", "Enrolled Students");
        System.out.println(separator);

        courseStudentsMap.forEach((course, students) -> {
            String studentsStr = students.stream()
                    .map(Student::getName)
                    .collect(Collectors.joining(", "));
            if (studentsStr.length() > 40) {
                studentsStr = studentsStr.substring(0, 37) + "...";
            }
            System.out.printf("| %-4d | %-31s | %-7d | %-40s |\n",
                    course.getId(),
                    course.getTitle(),
                    course.getCredit(),
                    studentsStr.isEmpty() ? "(No students)" : studentsStr);
        });
        System.out.println(separator);
    }

    public void printCourseDetails(Course course, List<Student> students) {
        System.out.println("\n+-----------------------------------------------------------------------------------+");
        System.out.println("|                                   COURSE DETAILS                                  |");
        System.out.println("+-----------------------------------------------------------------------------------+");
        System.out.printf("| %-15s : %-61s |\n", "Course ID", course.getId());
        System.out.printf("| %-15s : %-61s |\n", "Course Title", course.getTitle());
        System.out.printf("| %-15s : %-61s |\n", "Course Credits", course.getCredit());
        System.out.println("+-----------------------------------------------------------------------------------+");
        System.out.println("|                                 ENROLLED STUDENTS                                 |");
        System.out.println("+------+-------------------------------------------------------------+--------------+");
        System.out.printf("| %-4s | %-59s | %-12s |\n", "ID", "Student Name", "Age");
        System.out.println("+------+-------------------------------------------------------------+--------------+");

        if (students.isEmpty()) {
            System.out.printf("| %-80s |\n", "(No students enrolled in this course)");
        } else {
            students.forEach(student -> {
                String name = student.getName();
                if (name.length() > 59) {
                    name = name.substring(0, 56) + "...";
                }
                System.out.printf("| %-4d | %-59s | %-12d |\n",
                        student.getId(),
                        name,
                        student.getAge());
            });
        }
        System.out.println("+------+-------------------------------------------------------------+--------------+");
    }
}
