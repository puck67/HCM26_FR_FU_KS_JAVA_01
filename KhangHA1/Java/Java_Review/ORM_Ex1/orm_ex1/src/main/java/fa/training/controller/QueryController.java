package fa.training.controller;

import fa.training.dao.QueryDAO;
import fa.training.dao.impl.QueryDAOImpl;
import fa.training.util.ConsoleUtil;
import fa.training.view.QueryView;

/**
 * Controller for the five Hibernate query techniques (Task 5).
 */
public class QueryController {

    private final QueryDAO  queryDAO = new QueryDAOImpl();
    private final QueryView view     = new QueryView();

    public QueryView getView() { return view; }

    // ------------------------------------------------------------------ //
    //  1. HQL — students older than age
    // ------------------------------------------------------------------ //

    public void findStudentsOlderThan() {
        view.printHeader("HQL — Students Older Than");
        int age = ConsoleUtil.readInt("Enter minimum age: ");
        view.displayStudents(
                "Students older than " + age + ":",
                queryDAO.findStudentsOlderThan(age)
        );
    }

    // ------------------------------------------------------------------ //
    //  2. HQL with Join — student-course pairs
    // ------------------------------------------------------------------ //

    public void listStudentsWithCourses() {
        view.printHeader("HQL with Join — Students and Their Courses");
        view.displayStudentCoursePairs(queryDAO.findStudentsWithCourses());
    }

    // ------------------------------------------------------------------ //
    //  3. Named Query — find by name
    // ------------------------------------------------------------------ //

    public void findStudentsByName() {
        view.printHeader("Named Query — Find Students by Name");
        String name = ConsoleUtil.readString("Enter name (or part of name): ");
        view.displayStudents(
                "Students matching \"" + name + "\":",
                queryDAO.findStudentsByName(name)
        );
    }

    // ------------------------------------------------------------------ //
    //  4. Criteria API — courses with credit > N
    // ------------------------------------------------------------------ //

    public void findCoursesWithHighCredit() {
        view.printHeader("Criteria API — Courses with Credit > N");
        int min = ConsoleUtil.readInt("Enter minimum credit value: ");
        view.displayCourses(
                "Courses with credit > " + min + ":",
                queryDAO.findCoursesWithCreditGreaterThan(min)
        );
    }

    // ------------------------------------------------------------------ //
    //  5. Aggregation — student count per course
    // ------------------------------------------------------------------ //

    public void countStudentsPerCourse() {
        view.printHeader("Aggregation — Student Count per Course");
        view.displayStudentCountPerCourse(queryDAO.countStudentsPerCourse());
    }
}
