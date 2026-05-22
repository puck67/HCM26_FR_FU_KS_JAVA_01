package org.example.ui;

import org.example.dao.GradeDAO;
import org.example.dao.StudentDAO;
import org.example.dao.SubjectDAO;
import org.example.model.Grade;
import org.example.model.Student;
import org.example.model.Subject;
import org.example.util.InputHelper;

import java.util.List;

public class ConsoleMenu {
    private final StudentDAO studentDAO = new StudentDAO();
    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final GradeDAO gradeDAO = new GradeDAO();

    public void showMainMenu() {
        while (true) {
            System.out.println("\n============================================");
            System.out.println("   STUDENT & GRADE MANAGEMENT SYSTEM (H2)   ");
            System.out.println("============================================");
            System.out.println("1. Student Management");
            System.out.println("2. Subject Management");
//            System.out.println("3. Grade Management");
            System.out.println("0. Exit Application");
            System.out.println("============================================");

            int choice = InputHelper.promptInt("Enter your choice: ", 0, 3);
            switch (choice) {
                case 1:
                    showStudentMenu();
                    break;
                case 2:
                    showSubjectMenu();
                    break;
//                case 3:
//                    showGradeMenu();
//                    break;
                case 0:
                    System.out.println("Exiting application. Goodbye!");
                    return;
            }
        }
    }

    // --- STUDENT SUB-MENU ---
    private void showStudentMenu() {
        while (true) {
            System.out.println("\n--------------------------------------------");
            System.out.println("             STUDENT MANAGEMENT             ");
            System.out.println("--------------------------------------------");
            System.out.println("1. List All Students");
            System.out.println("2. Add New Student");
            System.out.println("3. Update Student Info");
            System.out.println("4. Delete Student");
            System.out.println("5. Find Student by Code");
            System.out.println("0. Back to Main Menu");
            System.out.println("--------------------------------------------");

            int choice = InputHelper.promptInt("Enter your choice: ", 0, 5);
            switch (choice) {
                case 1:
                    listStudents();
                    break;
                case 2:
                    addStudent();
                    break;
                case 3:
                    updateStudent();
                    break;
                case 4:
                    deleteStudent();
                    break;
                case 5:
                    findStudentByCode();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void listStudents() {
        List<Student> students = studentDAO.findAll();
        if (students.isEmpty()) {
            System.out.println("No students found in the system.");
            return;
        }
        printStudentTable(students);
    }

    private void addStudent() {
        System.out.println("\n[Add New Student]");
        String code;
        while (true) {
            code = InputHelper.promptStudentCode("Enter Student Code (e.g. SV004): ");
            if (studentDAO.findByCode(code) == null) {
                break;
            }
            System.out.println("Error: Student code already exists. Please choose a different one.");
        }

        String name = InputHelper.promptName("Enter Full Name: ");

        String email;
        while (true) {
            email = InputHelper.promptEmail("Enter Email: ");
            // In a real application, check duplicate email too
            boolean duplicate = false;
            for (Student s : studentDAO.findAll()) {
                if (s.getEmail().equalsIgnoreCase(email)) {
                    duplicate = true;
                    break;
                }
            }
            if (!duplicate) {
                break;
            }
            System.out.println("Error: Email already exists. Please choose a different one.");
        }

        String phone = InputHelper.promptPhone("Enter Phone Number: ");

        Student newStudent = new Student(code, name, email, phone);
        if (studentDAO.insert(newStudent)) {
            System.out.println("Student added successfully! Generated ID: " + newStudent.getId());
        } else {
            System.out.println("Failed to add student.");
        }
    }

    private void updateStudent() {
        System.out.println("\n[Update Student Info]");
        String code = InputHelper.promptStudentCode("Enter Student Code to update: ");
        Student student = studentDAO.findByCode(code);
        if (student == null) {
            System.out.println("Error: Student with code " + code + " not found.");
            return;
        }

        System.out.println("Updating student (Press Enter to keep current values):");

        String newCode = InputHelper.promptStringForUpdate("Enter Student Code", student.getStudentCode()).toUpperCase();
        if (!newCode.equals(student.getStudentCode())) {
            // Check if format is valid
            if (!org.example.util.InputValidator.isValidStudentCode(newCode)) {
                System.out.println("Error: Invalid Student Code format. Aborting update.");
                return;
            }
            // Check uniqueness
            if (studentDAO.findByCode(newCode) != null) {
                System.out.println("Error: New Student Code already exists. Aborting update.");
                return;
            }
            student.setStudentCode(newCode);
        }

        student.setName(InputHelper.promptNameForUpdate("Enter Full Name", student.getName()));

        String newEmail = InputHelper.promptEmailForUpdate("Enter Email", student.getEmail());
        if (!newEmail.equalsIgnoreCase(student.getEmail())) {
            boolean duplicate = false;
            for (Student s : studentDAO.findAll()) {
                if (s.getEmail().equalsIgnoreCase(newEmail) && s.getId() != student.getId()) {
                    duplicate = true;
                    break;
                }
            }
            if (duplicate) {
                System.out.println("Error: Email already exists. Aborting update.");
                return;
            }
            student.setEmail(newEmail);
        }

        student.setPhone(InputHelper.promptPhoneForUpdate("Enter Phone Number", student.getPhone()));

        if (studentDAO.update(student)) {
            System.out.println("Student updated successfully!");
        } else {
            System.out.println("Failed to update student.");
        }
    }

    private void deleteStudent() {
        System.out.println("\n[Delete Student]");
        String code = InputHelper.promptStudentCode("Enter Student Code to delete: ");
        Student student = studentDAO.findByCode(code);
        if (student == null) {
            System.out.println("Error: Student with code " + code + " not found.");
            return;
        }

        System.out.println("WARNING: Deleting this student will also delete all their academic grades!");
        String confirm = InputHelper.promptString("Are you sure you want to delete student " + student.getName() + "? (Y/N): ");
        if (confirm.equalsIgnoreCase("Y")) {
            if (studentDAO.delete(student.getId())) {
                System.out.println("Student and their associated grades deleted successfully.");
            } else {
                System.out.println("Failed to delete student.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void findStudentByCode() {
        System.out.println("\n[Find Student]");
        String code = InputHelper.promptStudentCode("Enter Student Code to search: ");
        Student student = studentDAO.findByCode(code);
        if (student == null) {
            System.out.println("No student found with code " + code);
        } else {
            System.out.println("\nStudent Found:");
            System.out.println("+------+--------------+----------------------+---------------------------+--------------+");
            System.out.println("| ID   | Student Code | Name                 | Email                     | Phone        |");
            System.out.println("+------+--------------+----------------------+---------------------------+--------------+");
            System.out.println(student);
            System.out.println("+------+--------------+----------------------+---------------------------+--------------+");
        }
    }

    private void printStudentTable(List<Student> list) {
        System.out.println("+------+--------------+----------------------+---------------------------+--------------+");
        System.out.println("| ID   | Student Code | Name                 | Email                     | Phone        |");
        System.out.println("+------+--------------+----------------------+---------------------------+--------------+");
        for (Student s : list) {
            System.out.println(s);
        }
        System.out.println("+------+--------------+----------------------+---------------------------+--------------+");
    }

    // --- SUBJECT SUB-MENU ---
    private void showSubjectMenu() {
        while (true) {
            System.out.println("\n--------------------------------------------");
            System.out.println("             SUBJECT MANAGEMENT             ");
            System.out.println("--------------------------------------------");
            System.out.println("1. List All Subjects");
            System.out.println("2. Add New Subject");
            System.out.println("3. Update Subject Info");
            System.out.println("4. Delete Subject");
            System.out.println("5. Find Subject by Code");
            System.out.println("0. Back to Main Menu");
            System.out.println("--------------------------------------------");

            int choice = InputHelper.promptInt("Enter your choice: ", 0, 5);
            switch (choice) {
                case 1:
                    listSubjects();
                    break;
                case 2:
                    addSubject();
                    break;
                case 3:
                    updateSubject();
                    break;
                case 4:
                    deleteSubject();
                    break;
                case 5:
                    findSubjectByCode();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void listSubjects() {
        List<Subject> subjects = subjectDAO.findAll();
        if (subjects.isEmpty()) {
            System.out.println("No subjects found in the system.");
            return;
        }
        printSubjectTable(subjects);
    }

    private void addSubject() {
        System.out.println("\n[Add New Subject]");
        String code;
        while (true) {
            code = InputHelper.promptSubjectCode("Enter Subject Code (e.g. MH04): ");
            if (subjectDAO.findByCode(code) == null) {
                break;
            }
            System.out.println("Error: Subject code already exists. Please choose a different one.");
        }

        String name = InputHelper.promptString("Enter Subject Name: ");
        int credits = InputHelper.promptInt("Enter Credits (1-12): ", 1, 12);

        Subject newSubject = new Subject(code, name, credits);
        if (subjectDAO.insert(newSubject)) {
            System.out.println("Subject added successfully! Generated ID: " + newSubject.getId());
        } else {
            System.out.println("Failed to add subject.");
        }
    }


    private void updateSubject() {
        System.out.println("\n[Update Subject Info]");
        String code = InputHelper.promptSubjectCode("Enter Subject Code to update: ");
        Subject subject = subjectDAO.findByCode(code);
        if (subject == null) {
            System.out.println("Error: Subject with code " + code + " not found.");
            return;
        }

        System.out.println("Updating subject (Press Enter to keep current values):");

        String newCode = InputHelper.promptStringForUpdate("Enter Subject Code", subject.getSubjectCode()).toUpperCase();
        if (!newCode.equals(subject.getSubjectCode())) {
            if (!org.example.util.InputValidator.isValidSubjectCode(newCode)) {
                System.out.println("Error: Invalid Subject Code format. Aborting update.");
                return;
            }
            if (subjectDAO.findByCode(newCode) != null) {
                System.out.println("Error: New Subject Code already exists. Aborting update.");
                return;
            }
            subject.setSubjectCode(newCode);
        }

        subject.setName(InputHelper.promptStringForUpdate("Enter Subject Name", subject.getName()));
        subject.setCredits(InputHelper.promptIntForUpdate("Enter Credits (1-12)", subject.getCredits(), 1, 12));

        if (subjectDAO.update(subject)) {
            System.out.println("Subject updated successfully!");
        } else {
            System.out.println("Failed to update subject.");
        }
    }

    private void deleteSubject() {
        System.out.println("\n[Delete Subject]");
        String code = InputHelper.promptSubjectCode("Enter Subject Code to delete: ");
        Subject subject = subjectDAO.findByCode(code);
        if (subject == null) {
            System.out.println("Error: Subject with code " + code + " not found.");
            return;
        }

        System.out.println("WARNING: Deleting this subject will also delete all student grades registered for it!");
        String confirm = InputHelper.promptString("Are you sure you want to delete subject " + subject.getName() + "? (Y/N): ");
        if (confirm.equalsIgnoreCase("Y")) {
            if (subjectDAO.delete(subject.getId())) {
                System.out.println("Subject and associated grades deleted successfully.");
            } else {
                System.out.println("Failed to delete subject.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void findSubjectByCode() {
        System.out.println("\n[Find Subject]");
        String code = InputHelper.promptSubjectCode("Enter Subject Code to search: ");
        Subject subject = subjectDAO.findByCode(code);
        if (subject == null) {
            System.out.println("No subject found with code " + code);
        } else {
            System.out.println("\nSubject Found:");
            System.out.println("+------+--------------+---------------------------+---------+");
            System.out.println("| ID   | Subject Code | Name                      | Credits |");
            System.out.println("+------+--------------+---------------------------+---------+");
            System.out.println(subject);
            System.out.println("+------+--------------+---------------------------+---------+");
        }
    }

    private void printSubjectTable(List<Subject> list) {
        System.out.println("+------+--------------+---------------------------+---------+");
        System.out.println("| ID   | Subject Code | Name                      | Credits |");
        System.out.println("+------+--------------+---------------------------+---------+");
        for (Subject s : list) {
            System.out.println(s);
        }
        System.out.println("+------+--------------+---------------------------+---------+");
    }

    // --- GRADE SUB-MENU ---
    private void showGradeMenu() {
        while (true) {
            System.out.println("\n--------------------------------------------");
            System.out.println("              GRADE MANAGEMENT              ");
            System.out.println("--------------------------------------------");
            System.out.println("1. View All Grades");
            System.out.println("2. Enter/Update Grade for Student");
            System.out.println("3. Remove Grade record");
            System.out.println("4. View Grade Sheet of a Student");
            System.out.println("5. View Subject Grade Report & Stats");
            System.out.println("0. Back to Main Menu");
            System.out.println("--------------------------------------------");

            int choice = InputHelper.promptInt("Enter your choice: ", 0, 5);
            switch (choice) {
                case 1:
                    listAllGrades();
                    break;
                case 2:
                    enterOrUpdateGrade();
                    break;
                case 3:
                    removeGrade();
                    break;
                case 4:
                    viewStudentGradeSheet();
                    break;
                case 5:
                    viewSubjectGradeReport();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void listAllGrades() {
        List<Grade> grades = gradeDAO.findAllWithDetails();
        if (grades.isEmpty()) {
            System.out.println("No grades recorded yet.");
            return;
        }
        printGradeTable(grades);
    }

    private void enterOrUpdateGrade() {
        System.out.println("\n[Enter/Update Grade]");
        String studentCode = InputHelper.promptStudentCode("Enter Student Code (e.g. SV001): ");
        Student student = studentDAO.findByCode(studentCode);
        if (student == null) {
            System.out.println("Error: Student with code " + studentCode + " does not exist.");
            return;
        }

        String subjectCode = InputHelper.promptSubjectCode("Enter Subject Code (e.g. MH01): ");
        Subject subject = subjectDAO.findByCode(subjectCode);
        if (subject == null) {
            System.out.println("Error: Subject with code " + subjectCode + " does not exist.");
            return;
        }

        double score = InputHelper.promptDouble("Enter Score (0.0 to 10.0): ", 0.0, 10.0);

        if (gradeDAO.saveOrUpdate(student.getId(), subject.getId(), score)) {
            System.out.printf("Success: Grade %.1f saved/updated for student %s in subject %s.%n",
                    score, student.getName(), subject.getName());
        } else {
            System.out.println("Failed to save grade.");
        }
    }

    private void removeGrade() {
        System.out.println("\n[Remove Grade Record]");
        String studentCode = InputHelper.promptStudentCode("Enter Student Code: ");
        Student student = studentDAO.findByCode(studentCode);
        if (student == null) {
            System.out.println("Error: Student with code " + studentCode + " not found.");
            return;
        }

        String subjectCode = InputHelper.promptSubjectCode("Enter Subject Code: ");
        Subject subject = subjectDAO.findByCode(subjectCode);
        if (subject == null) {
            System.out.println("Error: Subject with code " + subjectCode + " not found.");
            return;
        }

        if (!gradeDAO.exists(student.getId(), subject.getId())) {
            System.out.println("No grade record exists for this student in this subject.");
            return;
        }

        String confirm = InputHelper.promptString("Are you sure you want to delete this grade record? (Y/N): ");
        if (confirm.equalsIgnoreCase("Y")) {
            if (gradeDAO.delete(student.getId(), subject.getId())) {
                System.out.println("Grade record removed successfully.");
            } else {
                System.out.println("Failed to remove grade.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void viewStudentGradeSheet() {
        System.out.println("\n[Student Grade Sheet]");
        String studentCode = InputHelper.promptStudentCode("Enter Student Code: ");
        Student student = studentDAO.findByCode(studentCode);
        if (student == null) {
            System.out.println("Error: Student with code " + studentCode + " not found.");
            return;
        }

        List<Grade> grades = gradeDAO.findByStudentId(student.getId());
        System.out.println("\n========================================================");
        System.out.println("GRADE REPORT FOR STUDENT: " + student.getName() + " (" + student.getStudentCode() + ")");
        System.out.println("========================================================");

        if (grades.isEmpty()) {
            System.out.println("No grades recorded for this student.");
            return;
        }

        printGradeTable(grades);

        double totalScore = 0.0;
        int totalCredits = 0;
        for (Grade g : grades) {
            // Find credits for subject
            Subject sub = subjectDAO.findById(g.getSubjectId());
            if (sub != null) {
                totalScore += g.getScore() * sub.getCredits();
                totalCredits += sub.getCredits();
            }
        }

        if (totalCredits > 0) {
            double gpa = totalScore / totalCredits;
            System.out.printf("Total Credits: %d | Weighted GPA (10-scale): %.2f%n", totalCredits, gpa);
        }
        System.out.println("========================================================");
    }

    private void viewSubjectGradeReport() {
        System.out.println("\n[Subject Grade Report & Statistics]");
        String subjectCode = InputHelper.promptSubjectCode("Enter Subject Code: ");
        Subject subject = subjectDAO.findByCode(subjectCode);
        if (subject == null) {
            System.out.println("Error: Subject with code " + subjectCode + " not found.");
            return;
        }

        List<Grade> grades = gradeDAO.findBySubjectId(subject.getId());
        System.out.println("\n=========================================================================");
        System.out.println("CLASS REPORT FOR SUBJECT: " + subject.getName() + " (" + subject.getSubjectCode() + ")");
        System.out.println("=========================================================================");

        if (grades.isEmpty()) {
            System.out.println("No grades recorded for this subject.");
            return;
        }

        printGradeTable(grades);

        double total = 0.0;
        double max = -1.0;
        double min = 11.0;
        String bestStudent = "";
        String poorestStudent = "";

        for (Grade g : grades) {
            double score = g.getScore();
            total += score;
            if (score > max) {
                max = score;
                bestStudent = g.getStudentName();
            }
            if (score < min) {
                min = score;
                poorestStudent = g.getStudentName();
            }
        }

        double avg = total / grades.size();
        System.out.printf("Total Students Enrolled: %d%n", grades.size());
        System.out.printf("Class Average Score:     %.2f%n", avg);
        System.out.printf("Highest Score:           %.1f (Student: %s)%n", max, bestStudent);
        System.out.printf("Lowest Score:            %.1f (Student: %s)%n", min, poorestStudent);
        System.out.println("=========================================================================");
    }

    private void printGradeTable(List<Grade> list) {
        System.out.println("+------------+----------------------+------------+---------------------------+-------+");
        System.out.println("| Student    | Student Name         | Subject    | Subject Name              | Score |");
        System.out.println("+------------+----------------------+------------+---------------------------+-------+");
        for (Grade g : list) {
            System.out.println(g);
        }
        System.out.println("+------------+----------------------+------------+---------------------------+-------+");
    }
}
