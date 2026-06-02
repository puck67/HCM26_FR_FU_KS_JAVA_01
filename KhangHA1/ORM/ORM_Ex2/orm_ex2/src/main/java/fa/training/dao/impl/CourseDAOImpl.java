package fa.training.dao.impl;

import fa.training.dao.CourseDAO;
import fa.training.entity.Course;

public class CourseDAOImpl extends BaseDAOImpl<Course, Integer> implements CourseDAO {

    public CourseDAOImpl() {
        super(Course.class);
    }
}
