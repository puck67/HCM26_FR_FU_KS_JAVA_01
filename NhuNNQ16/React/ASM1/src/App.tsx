import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import Tasks from './pages/Tasks';
import TaskDetail from './pages/TaskDetail';
import { TaskProvider } from './context/TaskContext';

// Component gốc - cấu hình routing và Context Provider cho toàn ứng dụng
function App() {
  return (
    <TaskProvider>
      <BrowserRouter>
        {/* Thanh điều hướng */}
        <Navbar />

        {/* Nội dung chính của ứng dụng */}
        <main className="min-h-screen bg-slate-900 text-slate-100 selection:bg-indigo-500 selection:text-white pb-20">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/tasks" element={<Tasks />} />
            <Route path="/tasks/:id" element={<TaskDetail />} />
          </Routes>
        </main>
      </BrowserRouter>
    </TaskProvider>
  );
}

export default App;
