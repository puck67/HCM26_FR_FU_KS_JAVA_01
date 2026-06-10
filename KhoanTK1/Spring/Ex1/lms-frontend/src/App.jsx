import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import MainLayout from './components/MainLayout';
import HomePage from './components/HomePage';
import AddCoursePage from './components/AddCoursePage';
import CoursesPage from './components/CoursesPage';
import StudentsPage from './components/StudentsPage';
import LessonDetailPage from './components/LessonDetailPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<MainLayout />}>
          <Route index element={<HomePage />} />
          <Route path="courses/new" element={<AddCoursePage />} />
          <Route path="courses" element={<CoursesPage />} />
          <Route path="students" element={<StudentsPage />} />
          <Route path="courses/:courseCode/:startDate/lessons" element={<LessonDetailPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
