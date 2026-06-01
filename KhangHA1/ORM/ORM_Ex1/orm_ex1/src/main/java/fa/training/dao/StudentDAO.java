package fa.training.dao;

import fa.training.entity.Student;

import java.util.List;

/**
 * Student-specific DAO — CRUD from BaseDAO + pagination bonus.
 */
public interface StudentDAO extends BaseDAO<Student, Integer> {

    /** Returns one page of students ordered by id. pageNumber is 1-based. */
    List<Student> getPage(int pageNumber, int pageSize);
}
