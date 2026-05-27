import dao.CourseDAO;
import dao.StudentDAO;
import model.Course;
import model.Student;
import util.HibernateUtil;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        StudentDAO studentDAO = new StudentDAO();
        CourseDAO courseDAO = new CourseDAO();


        // 1. Insert mock  Task 6
        System.out.println("--- 1. Them du lieu mau ---");
        
        Student s1 = new Student("Nguyen", 21);
        Student s2 = new Student("Ngoc", 22);
        Student s3 = new Student("Quynh", 19);
        Student s4 = new Student("Nhu", 25);

        studentDAO.saveStudent(s1);
        studentDAO.saveStudent(s2);
        studentDAO.saveStudent(s3);
        studentDAO.saveStudent(s4);

        Course c1 = new Course("Java Programming", 4);
        Course c2 = new Course("Database Systems", 3);
        Course c3 = new Course("Web Development", 2);


        courseDAO.saveCourse(c1);
        courseDAO.saveCourse(c2);
        courseDAO.saveCourse(c3);

        System.out.println("Đã thêm 4 hs và 4 khóa học!");


        // 2. DĂNG KÝ HỌC (ENROLLMENT)
        System.out.println("\n--- 2. Dang ky mon hoc ---");
        
        // Nguyen enroll Java va Database
        studentDAO.enrollStudentInCourse(s1.getId(), c1.getId());
        studentDAO.enrollStudentInCourse(s1.getId(), c2.getId());

        // Ngoc hoc ca 3 mon
        studentDAO.enrollStudentInCourse(s2.getId(), c1.getId());
        studentDAO.enrollStudentInCourse(s2.getId(), c2.getId());
        studentDAO.enrollStudentInCourse(s2.getId(), c3.getId());

        // Quynh hoc mon Web Development
        studentDAO.enrollStudentInCourse(s3.getId(), c3.getId());

        System.out.println("Da dang ky mon hoc cho cac hoc sinh xong!");


        // 3. Execute query (TASK 5)

        System.out.println("\n--- 3. Thuc hien cac truy van (Task 5) ---");

        // 3.1. HQL Query: Find all students older than a given age.
        System.out.println("\n[HQL] Hoc sinh lon hon 20 tuoi:");
        List<Student> studentsOlder20 = studentDAO.findStudentsOlderThan(20);
        for (Student s : studentsOlder20) {
            System.out.println(s);
        }

        // 3.2. HQL with Join: List students and the courses they are enrolled in.
        System.out.println("\n[HQL Join] Danh sach hoc sinh va mon hoc da dang ky:");
        List<Student> studentsAndCourses = studentDAO.getStudentsAndCoursesHQLJoin();
        for (Student s : studentsAndCourses) {
            System.out.println("Hoc sinh: " + s.getName() + " | Cac mon hoc: " + s.getCourses());
        }

        // 3.3. Named Query:Create a named query in the Student entity to find students by name.
        // Tim hoc sinh theo ten "Nguyen"
        System.out.println("\n[Named Query] Tim hoc sinh co ten 'Nguyen':");
        List<Student> foundStudents = studentDAO.findStudentsByName("Nguyen");
        for (Student s : foundStudents) {
            System.out.println(s);
        }

        // 3.4. Criteria API: Find courses that have credit greater than a given value.
        System.out.println("\n[Criteria API] Khoa hoc co tin chi > 2:");
        List<Course> coursesAbove2Credits = courseDAO.findCoursesWithCreditsGreaterThan(2);
        for (Course c : coursesAbove2Credits) {
            System.out.println(c);
        }

        // 3.5. Aggregation Query: Count how many students are enrolled in each course
        System.out.println("\n[Aggregation] Thong ke so hoc sinh moi mon hoc:");
        List<Object[]> stats = studentDAO.countStudentsPerCourse();
        for (Object[] row : stats) {
            System.out.println("Mon hoc: " + row[0] + " | So luong: " + row[1]);
        }


        // 4. CRUD (UPDATE & DELETE)
        System.out.println("\n--- 4. Demo Update va Delete ---");

        //  Get student by ID
        Student testStudent = studentDAO.getStudentById(s4.getId());
        System.out.println("Lay hoc sinh ID 4 (Nhu): " + testStudent);

        // Update age of student
        if (testStudent != null) {
            testStudent.setAge(26);
            studentDAO.updateStudent(testStudent);
            System.out.println("Da cap nhat tuoi cua Nhu thanh 26: " + studentDAO.getStudentById(s4.getId()));
        }

        //  Delete student
        System.out.println("Xoa hoc sinh ID 3 (Quynh)...");
        studentDAO.deleteStudent(s3.getId());

        // Show all student after delete
        System.out.println("Danh sach hoc sinh con lai:");
        List<Student> remains = studentDAO.getAllStudents(1, 10);
        for (Student s : remains) {
            System.out.println(s);
        }


        HibernateUtil.shutdown();
        System.out.println("\n CHUONG TRINH KET THUC");
    }
}
