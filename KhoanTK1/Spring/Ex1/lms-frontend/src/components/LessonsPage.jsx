import React, { useState, useEffect } from 'react';
import axios from 'axios';

function LessonsPage() {
  const [lessons, setLessons] = useState([]);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const fetchLessons = () => {
    axios.get('http://localhost:8080/api/lessons')
      .then((response) => {
        setLessons(response.data);
      })
      .catch((err) => {
        console.error(err);
        setError('Failed to fetch lessons.');
      });
  };

  useEffect(() => {
    fetchLessons();
  }, []);

  const handleDelete = (lessonId) => {
    if (window.confirm('Do you want to delete this lesson?')) {
      setError('');
      setSuccess('');
      axios.delete(`http://localhost:8080/api/lessons/${lessonId}`)
        .then(() => {
          setSuccess('Lesson deleted successfully.');
          fetchLessons();
        })
        .catch((err) => {
          console.error(err);
          setError('Failed to delete lesson.');
        });
    }
  };

  const getBadgeClass = (statusVal) => {
    switch (statusVal) {
      case 'Bản nháp':
        return 'badge badge-draft';
      case 'Đang mở':
        return 'badge badge-open';
      case 'Đã khóa':
        return 'badge badge-locked';
      default:
        return 'badge';
    }
  };

  return (
    <div className="page-container">
      <h2>Lessons Management</h2>

      {success && <div className="alert alert-success"><span>{success}</span></div>}
      {error && <div className="alert alert-error"><span>{error}</span></div>}

      <div className="table-container">
        {lessons.length === 0 ? (
          <div style={{ padding: '24px', textAlign: 'center', color: '#64748b' }}>
            No lessons found.
          </div>
        ) : (
          <table className="lessons-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Lesson Name</th>
                <th>Duration</th>
                <th>Content Type</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {lessons.map((lesson) => (
                <tr key={lesson.lessonId}>
                  <td>{lesson.lessonId}</td>
                  <td>{lesson.lessonName}</td>
                  <td>{lesson.duration} mins</td>
                  <td>{lesson.contentType}</td>
                  <td>
                    <span className={getBadgeClass(lesson.status)}>
                      {lesson.status}
                    </span>
                  </td>
                  <td>
                    <span
                      className="action-link delete"
                      onClick={() => handleDelete(lesson.lessonId)}
                    >
                      Delete
                    </span>
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

export default LessonsPage;
