import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Layout from './components/layout/Layout';
import Home from './pages/Home';
import Courses from './pages/Courses';
import CourseAdd from './pages/CourseAdd';
import CourseEdit from './pages/CourseEdit';
import Students from './pages/Students';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          {/* Home Landing Page */}
          <Route index element={<Home />} />
          
          {/* Courses (Khóa học) */}
          <Route path="courses" element={<Courses />} />
          <Route path="courses/add" element={<CourseAdd />} />
          <Route path="courses/edit/:id" element={<CourseEdit />} />
          
          {/* Students (Học viên) */}
          <Route path="students" element={<Students />} />
          
          {/* Catch-all Route: Redirect to Home */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
