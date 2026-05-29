package fa.training.main;

import fa.training.dao.CourseDAO;
import fa.training.dao.StudentDAO;
import fa.training.entities.Course;
import fa.training.entities.Student;
import fa.training.utils.HibernateUtils;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        StudentDAO studentDAO =
                new StudentDAO();

        CourseDAO courseDAO =
                new CourseDAO();

        while (true) {

            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Student management");
            System.out.println("2. Course management");
            System.out.println("3. Exit");

            System.out.print("Choose: ");

            int mainChoice =
                    Integer.parseInt(sc.nextLine());

            switch (mainChoice) {

                // =========================
                // STUDENT MENU
                // =========================

                case 1:

                    while (true) {

                        System.out.println(
                                "\n===== STUDENT MENU ====="
                        );

                        System.out.println("1. Add student");
                        System.out.println("2. Show all students");
                        System.out.println("3. Find student by id");
                        System.out.println("4. Find student by name");
                        System.out.println("5. Update student");
                        System.out.println("6. Delete student");
                        System.out.println("7. Enroll course");
                        System.out.println("8. Show student courses");
                        System.out.println("9. Remove course");
                        System.out.println("10. Back");

                        System.out.print("Choose: ");

                        int studentChoice =
                                Integer.parseInt(sc.nextLine());

                        switch (studentChoice) {

                            // ADD STUDENT
                            case 1:

                                System.out.print("Enter name: ");

                                String name =
                                        sc.nextLine();

                                System.out.print("Enter age: ");

                                int age =
                                        Integer.parseInt(sc.nextLine());

                                Student student =
                                        new Student(name, age);

                                studentDAO.save(student);

                                System.out.println("Add success");

                                break;

                            // SHOW ALL
                            case 2:

                                System.out.println(
                                        "\n=== STUDENT LIST ==="
                                );

                                studentDAO
                                        .getAll()
                                        .forEach(System.out::println);

                                break;

                            // FIND BY ID
                            case 3:

                                System.out.print("Enter id: ");

                                int findId =
                                        Integer.parseInt(sc.nextLine());

                                Student findStudent =
                                        studentDAO.getById(findId);

                                if (findStudent != null) {

                                    System.out.println(findStudent);

                                } else {

                                    System.out.println(
                                            "Student not found"
                                    );
                                }

                                break;

                            // FIND BY NAME
                            case 4:

                                System.out.print("Enter name: ");

                                String searchName =
                                        sc.nextLine();

                                studentDAO
                                        .findByName(searchName)
                                        .forEach(System.out::println);

                                break;

                            // UPDATE
                            case 5:

                                System.out.print(
                                        "Enter student id: "
                                );

                                int updateId =
                                        Integer.parseInt(sc.nextLine());

                                Student updateStudent =
                                        studentDAO.getById(updateId);

                                if (updateStudent == null) {

                                    System.out.println(
                                            "Student not found"
                                    );

                                    break;
                                }

                                System.out.print(
                                        "Enter new name: "
                                );

                                String newName =
                                        sc.nextLine();

                                System.out.print(
                                        "Enter new age: "
                                );

                                int newAge =
                                        Integer.parseInt(sc.nextLine());

                                updateStudent.setName(newName);
                                updateStudent.setAge(newAge);

                                studentDAO.update(updateStudent);

                                System.out.println(
                                        "Update success"
                                );

                                break;

                            // DELETE
                            case 6:

                                System.out.print(
                                        "Enter id to delete: "
                                );

                                int deleteId =
                                        Integer.parseInt(sc.nextLine());

                                Student deleteStudent =
                                        studentDAO.getById(deleteId);

                                if (deleteStudent == null) {

                                    System.out.println(
                                            "Student not found"
                                    );

                                    break;
                                }

                                studentDAO.delete(deleteStudent);

                                System.out.println(
                                        "Delete success"
                                );

                                break;

                            // ENROLL COURSE
                            case 7:

                                System.out.print(
                                        "Enter student id: "
                                );

                                int studentId =
                                        Integer.parseInt(sc.nextLine());

                                Student enrollStudent =
                                        studentDAO.getById(studentId);

                                if (enrollStudent == null) {

                                    System.out.println(
                                            "Student not found"
                                    );

                                    break;
                                }

                                System.out.println(
                                        "\n=== COURSE LIST ==="
                                );

                                courseDAO
                                        .getAll()
                                        .forEach(System.out::println);

                                System.out.print(
                                        "Enter course id: "
                                );

                                int courseId =
                                        Integer.parseInt(sc.nextLine());

                                Course enrollCourse =
                                        courseDAO.getById(courseId);

                                if (enrollCourse == null) {

                                    System.out.println(
                                            "Course not found"
                                    );

                                    break;
                                }

                                boolean exists =
                                        enrollStudent
                                                .getCourses()
                                                .stream()
                                                .anyMatch(c ->
                                                        c.getId()
                                                                == courseId);

                                if (exists) {

                                    System.out.println(
                                            "Student already enrolled"
                                    );

                                    break;
                                }

                                enrollStudent
                                        .getCourses()
                                        .add(enrollCourse);

                                studentDAO.update(enrollStudent);

                                System.out.println(
                                        "Enroll success"
                                );

                                break;

                            // SHOW STUDENT COURSES
                            case 8:

                                System.out.print(
                                        "Enter student id: "
                                );

                                int courseStudentId =
                                        Integer.parseInt(sc.nextLine());

                                Student courseStudent =
                                        studentDAO.getById(courseStudentId);

                                if (courseStudent == null) {

                                    System.out.println(
                                            "Student not found"
                                    );

                                    break;
                                }

                                System.out.println(
                                        "\n=== COURSES OF "
                                                + courseStudent.getName()
                                                + " ==="
                                );

                                if (courseStudent
                                        .getCourses()
                                        .isEmpty()) {

                                    System.out.println(
                                            "No courses"
                                    );

                                } else {

                                    courseStudent
                                            .getCourses()
                                            .forEach(System.out::println);
                                }

                                break;

                            // REMOVE COURSE
                            case 9:

                                System.out.print(
                                        "Enter student id: "
                                );

                                int removeStudentId =
                                        Integer.parseInt(sc.nextLine());

                                Student removeStudent =
                                        studentDAO.getById(removeStudentId);

                                if (removeStudent == null) {

                                    System.out.println(
                                            "Student not found"
                                    );

                                    break;
                                }

                                if (removeStudent
                                        .getCourses()
                                        .isEmpty()) {

                                    System.out.println(
                                            "Student has no courses"
                                    );

                                    break;
                                }

                                System.out.println(
                                        "\n=== STUDENT COURSES ==="
                                );

                                removeStudent
                                        .getCourses()
                                        .forEach(System.out::println);

                                System.out.print(
                                        "Enter course id to remove: "
                                );

                                int removeCourseId =
                                        Integer.parseInt(sc.nextLine());

                                boolean removed =
                                        removeStudent
                                                .getCourses()
                                                .removeIf(c ->
                                                        c.getId()
                                                                == removeCourseId);

                                if (!removed) {

                                    System.out.println(
                                            "Course not found in student"
                                    );

                                    break;
                                }

                                studentDAO.update(removeStudent);

                                System.out.println(
                                        "Remove success"
                                );

                                break;

                            // BACK
                            case 10:

                                break;

                            default:

                                System.out.println(
                                        "Invalid choice"
                                );
                        }

                        if (studentChoice == 10) {
                            break;
                        }
                    }

                    break;

                // =========================
                // COURSE MENU
                // =========================

                case 2:

                    while (true) {

                        System.out.println(
                                "\n===== COURSE MENU ====="
                        );

                        System.out.println("1. Add course");
                        System.out.println("2. Show all courses");
                        System.out.println("3. Show students of course");
                        System.out.println("4. Update course");
                        System.out.println("5. Delete course");
                        System.out.println("6. Back");

                        System.out.print("Choose: ");

                        int courseChoice =
                                Integer.parseInt(sc.nextLine());

                        switch (courseChoice) {

                            // ADD COURSE
                            case 1:

                                System.out.print(
                                        "Enter course title: "
                                );

                                String title =
                                        sc.nextLine();

                                System.out.print(
                                        "Enter credit: "
                                );

                                int credit =
                                        Integer.parseInt(sc.nextLine());

                                Course course =
                                        new Course(title, credit);

                                courseDAO.save(course);

                                System.out.println(
                                        "Add course success"
                                );

                                break;

                            // SHOW ALL
                            case 2:

                                System.out.println(
                                        "\n=== COURSE LIST ==="
                                );

                                courseDAO
                                        .getAll()
                                        .forEach(System.out::println);

                                break;

                            // SHOW STUDENTS OF COURSE
                            case 3:

                                System.out.print(
                                        "Enter course id: "
                                );

                                int courseId =
                                        Integer.parseInt(sc.nextLine());

                                Course showCourse =
                                        courseDAO.getById(courseId);

                                if (showCourse == null) {

                                    System.out.println(
                                            "Course not found"
                                    );

                                    break;
                                }

                                System.out.println(
                                        "\n=== STUDENTS OF "
                                                + showCourse.getTitle()
                                                + " ==="
                                );

                                if (showCourse
                                        .getStudents()
                                        .isEmpty()) {

                                    System.out.println(
                                            "No students"
                                    );

                                } else {

                                    showCourse
                                            .getStudents()
                                            .forEach(System.out::println);
                                }

                                break;

                            // UPDATE COURSE
                            case 4:

                                System.out.print(
                                        "Enter course id: "
                                );

                                int updateCourseId =
                                        Integer.parseInt(sc.nextLine());

                                Course updateCourse =
                                        courseDAO.getById(updateCourseId);

                                if (updateCourse == null) {

                                    System.out.println(
                                            "Course not found"
                                    );

                                    break;
                                }

                                System.out.print(
                                        "Enter new title: "
                                );

                                String newTitle =
                                        sc.nextLine();

                                System.out.print(
                                        "Enter new credit: "
                                );

                                int newCredit =
                                        Integer.parseInt(sc.nextLine());

                                updateCourse.setTitle(newTitle);
                                updateCourse.setCredit(newCredit);

                                courseDAO.update(updateCourse);

                                System.out.println(
                                        "Update success"
                                );

                                break;

                            // DELETE COURSE
                            case 5:

                                System.out.print(
                                        "Enter course id: "
                                );

                                int deleteCourseId =
                                        Integer.parseInt(sc.nextLine());

                                Course deleteCourse =
                                        courseDAO.getById(deleteCourseId);

                                if (deleteCourse == null) {

                                    System.out.println(
                                            "Course not found"
                                    );

                                    break;
                                }

                                courseDAO.delete(deleteCourse);

                                System.out.println(
                                        "Delete success"
                                );

                                break;

                            // BACK
                            case 6:

                                break;

                            default:

                                System.out.println(
                                        "Invalid choice"
                                );
                        }

                        if (courseChoice == 6) {
                            break;
                        }
                    }

                    break;

                // EXIT
                case 3:

                    HibernateUtils.shutdown();

                    System.out.println("Program closed");

                    System.exit(0);

                    break;

                default:

                    System.out.println("Invalid choice");
            }
        }
    }
}