import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

function CoursesPage() {
  const [courses, setCourses] = useState([]);

  useEffect(() => {
    axios.get('http://localhost:8080/api/courses')
      .then((response) => {
        setCourses(response.data);
      })
      .catch((error) => {
        console.error(error);
      });
  }, []);

  return (
    <div className="page-container">
      <h2>Courses Management</h2>
      <div className="table-container">
        {courses.length === 0 ? (
          <div style={{ padding: '24px', textAlign: 'center', color: '#64748b' }}>
            No courses found.
          </div>
        ) : (
          <table className="lessons-table">
            <thead>
              <tr>
                <th>Course Code</th>
                <th>Start Date</th>
                <th>Course Name</th>
                <th>Category</th>
                <th>Instructor</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {courses.map((course) => (
                <tr key={`${course.id.courseCode}-${course.id.startDate}`}>
                  <td>{course.id.courseCode}</td>
                  <td>{course.id.startDate}</td>
                  <td>{course.courseName}</td>
                  <td>{course.category}</td>
                  <td>{course.instructor}</td>
                  <td>
                    <Link
                      to={`/courses/${course.id.courseCode}/${course.id.startDate}/lessons`}
                      className="action-link"
                    >
                      Manage Lessons
                    </Link>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

export default CoursesPage;
