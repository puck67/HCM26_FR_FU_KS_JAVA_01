import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';

function LessonDetailPage() {
  const { courseCode, startDate } = useParams();

  // Lessons list state
  const [lessons, setLessons] = useState([]);
  
  // Form states
  const [lessonName, setLessonName] = useState('');
  const [duration, setDuration] = useState('');
  const [contentType, setContentType] = useState('Video');
  const [status, setStatus] = useState('Bản nháp');
  
  // Edit mode state
  const [editingLessonId, setEditingLessonId] = useState(null);
  
  // Feedback states
  const [successMsg, setSuccessMsg] = useState('');
  const [errorMsg, setErrorMsg] = useState('');

  // Fetch all lessons for this course
  const fetchLessons = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/api/courses/${courseCode}/${startDate}/lessons`);
      setLessons(response.data);
    } catch (err) {
      console.error('Error fetching lessons:', err);
      setErrorMsg('Failed to load lessons. Make sure the course exists.');
    }
  };

  useEffect(() => {
    fetchLessons();
  }, [courseCode, startDate]);

  // Handle form submission (Create or Edit)
  const handleSaveLesson = async (e) => {
    e.preventDefault();
    setSuccessMsg('');
    setErrorMsg('');

    const parsedDuration = parseInt(duration, 10);
    if (isNaN(parsedDuration) || parsedDuration <= 0) {
      setErrorMsg('Duration must be a positive number of minutes.');
      return;
    }

    const payload = {
      lessonName: lessonName.trim(),
      duration: parsedDuration,
      contentType: contentType,
      status: status,
      course: {
        id: {
          courseCode: courseCode,
          startDate: startDate
        }
      }
    };

    try {
      if (editingLessonId) {
        // Edit Mode (PUT /api/lessons/{id})
        // Include the lessonId in payload for BaseServiceImpl reflection save or update
        const editPayload = {
          ...payload,
          lessonId: editingLessonId
        };
        const response = await axios.put(`http://localhost:8080/api/lessons/${editingLessonId}`, editPayload);
        if (response.status === 200 || response.status === 204) {
          setSuccessMsg('Lesson updated successfully!');
          resetForm();
          fetchLessons();
        } else {
          setErrorMsg('Failed to update lesson.');
        }
      } else {
        // Create Mode (POST /api/lessons)
        const response = await axios.post('http://localhost:8080/api/lessons', payload);
        if (response.status === 201 || response.status === 200) {
          setSuccessMsg('Lesson added successfully!');
          resetForm();
          fetchLessons();
        } else {
          setErrorMsg('Failed to create lesson.');
        }
      }
    } catch (err) {
      console.error(err);
      setErrorMsg(err.response?.data?.message || 'An error occurred while saving the lesson.');
    }
  };

  // Populate form for editing
  const handleEditClick = (lesson) => {
    setEditingLessonId(lesson.lessonId);
    setLessonName(lesson.lessonName);
    setDuration(lesson.duration.toString());
    setContentType(lesson.contentType);
    setStatus(lesson.status);
    setSuccessMsg('');
    setErrorMsg('');
  };

  // Handle lesson deletion
  const handleDeleteClick = async (lessonId) => {
    if (window.confirm("Do you want to delete this lesson?")) {
      setSuccessMsg('');
      setErrorMsg('');
      try {
        const response = await axios.delete(`http://localhost:8080/api/lessons/${lessonId}`);
        if (response.status === 200 || response.status === 204 || response.status === 202) {
          setSuccessMsg('Lesson deleted successfully.');
          fetchLessons();
          // If we were editing this deleted lesson, reset form
          if (editingLessonId === lessonId) {
            resetForm();
          }
        }
      } catch (err) {
        console.error(err);
        setErrorMsg('Failed to delete lesson.');
      }
    }
  };

  const resetForm = () => {
    setLessonName('');
    setDuration('');
    setContentType('Video');
    setStatus('Bản nháp');
    setEditingLessonId(null);
  };

  // Map status values to appropriate badge style class name
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
      <h2>Lesson Detail</h2>

      {/* Top UI: Course Summary (Read-Only) */}
      <div className="course-info-card">
        <div className="info-item">
          <span className="label">Course ID</span>
          <span className="value">{courseCode}</span>
        </div>
        <div className="info-item">
          <span className="label">Start Date</span>
          <span className="value">{startDate}</span>
        </div>
      </div>

      {/* Messages */}
      {successMsg && <div className="alert alert-success"><span>{successMsg}</span></div>}
      {errorMsg && <div className="alert alert-error"><span>{errorMsg}</span></div>}

      {/* Middle UI: Add/Edit Form */}
      <h3>{editingLessonId ? 'Edit Lesson' : 'Add New Lesson'}</h3>
      <form onSubmit={handleSaveLesson} className="form-container" style={{ marginTop: '12px', marginBottom: '32px' }}>
        <div className="form-group">
          <label htmlFor="lessonName">Lesson Name</label>
          <input
            type="text"
            id="lessonName"
            value={lessonName}
            onChange={(e) => setLessonName(e.target.value)}
            required
            placeholder="e.g. Overview of Spring Boot"
          />
        </div>

        <div className="form-group">
          <label htmlFor="duration">Duration (mins)</label>
          <input
            type="number"
            id="duration"
            value={duration}
            onChange={(e) => setDuration(e.target.value)}
            required
            placeholder="e.g. 45"
            min="1"
          />
        </div>

        <div className="form-group">
          <label htmlFor="contentType">Content Type</label>
          <select
            id="contentType"
            value={contentType}
            onChange={(e) => setContentType(e.target.value)}
            required
          >
            <option value="Video">Video</option>
            <option value="Lý thuyết">Lý thuyết</option>
            <option value="Thực hành">Thực hành</option>
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="status">Status</label>
          <select
            id="status"
            value={status}
            onChange={(e) => setStatus(e.target.value)}
            required
          >
            <option value="Bản nháp">Bản nháp</option>
            <option value="Đang mở">Đang mở</option>
            <option value="Đã khóa">Đã khóa</option>
          </select>
        </div>

        <div className="form-actions">
          <button type="submit" className="btn btn-primary">
            {editingLessonId ? 'Update Lesson' : 'Save Lesson'}
          </button>
          {editingLessonId && (
            <button type="button" className="btn btn-secondary" onClick={resetForm}>
              Cancel Edit
            </button>
          )}
        </div>
      </form>

      {/* Bottom UI: Lessons Table */}
      <h3>Lessons List</h3>
      <div className="table-container">
        {lessons.length === 0 ? (
          <div style={{ padding: '24px', textAlign: 'center', color: '#64748b' }}>
            No lessons created yet for this course. Use the form above to add a lesson.
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
                      className="action-link" 
                      onClick={() => handleEditClick(lesson)}
                    >
                      Edit
                    </span>
                    <span 
                      className="action-link delete" 
                      onClick={() => handleDeleteClick(lesson.lessonId)}
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

export default LessonDetailPage;
