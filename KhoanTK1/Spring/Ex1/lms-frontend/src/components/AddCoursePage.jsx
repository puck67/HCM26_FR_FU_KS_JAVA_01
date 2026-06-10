import React, { useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

function AddCoursePage() {
  const [courseCode, setCourseCode] = useState('');
  const [startDate, setStartDate] = useState('');
  const [courseName, setCourseName] = useState('');
  const [category, setCategory] = useState('Technology');
  const [instructor, setInstructor] = useState('');
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');

  const handleSave = async (e) => {
    e.preventDefault();
    setSuccess(false);
    setError('');

    const payload = {
      id: {
        courseCode: courseCode.trim(),
        startDate: startDate
      },
      courseName: courseName.trim(),
      category: category,
      instructor: instructor.trim()
    };

    try {
      const response = await axios.post('http://localhost:8080/api/courses', payload);
      if (response.status === 201 || response.status === 200) {
        setSuccess(true);
      } else {
        setError('Failed to save course. Please check inputs.');
      }
    } catch (err) {
      console.error(err);
      setError(err.response?.data?.message || 'An error occurred while saving the course.');
    }
  };

  const handleCancel = () => {
    setCourseCode('');
    setStartDate('');
    setCourseName('');
    setCategory('Technology');
    setInstructor('');
    setSuccess(false);
    setError('');
  };

  return (
    <div className="page-container">
      <h2>Add a new Course</h2>

      {success && (
        <div className="alert alert-success">
          <span>Add a new Course successfully!</span>
          <Link 
            to={`/courses/${courseCode}/${startDate}/lessons`} 
            className="alert-link"
          >
            Lesson Detail
          </Link>
        </div>
      )}

      {error && (
        <div className="alert alert-error">
          <span>{error}</span>
        </div>
      )}

      <form onSubmit={handleSave} className="form-container">
        <div className="form-group">
          <label htmlFor="courseCode">Course ID</label>
          <input
            type="text"
            id="courseCode"
            value={courseCode}
            onChange={(e) => setCourseCode(e.target.value)}
            required
            placeholder="e.g. CS101"
            disabled={success}
          />
        </div>

        <div className="form-group">
          <label htmlFor="startDate">Start Date</label>
          <input
            type="date"
            id="startDate"
            value={startDate}
            onChange={(e) => setStartDate(e.target.value)}
            required
            disabled={success}
          />
        </div>

        <div className="form-group">
          <label htmlFor="courseName">Course Name</label>
          <input
            type="text"
            id="courseName"
            value={courseName}
            onChange={(e) => setCourseName(e.target.value)}
            required
            placeholder="e.g. Introduction to Java"
          />
        </div>

        <div className="form-group">
          <label htmlFor="category">Category</label>
          <select
            id="category"
            value={category}
            onChange={(e) => setCategory(e.target.value)}
            required
          >
            <option value="Technology">Technology</option>
            <option value="Business">Business</option>
            <option value="Design">Design</option>
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="instructor">Instructor</label>
          <input
            type="text"
            id="instructor"
            value={instructor}
            onChange={(e) => setInstructor(e.target.value)}
            required
            placeholder="e.g. Prof. John Doe"
          />
        </div>

        <div className="form-actions">
          <button type="submit" className="btn btn-primary">Save Course</button>
          <button type="button" className="btn btn-secondary" onClick={handleCancel}>Cancel</button>
        </div>
      </form>
    </div>
  );
}

export default AddCoursePage;
