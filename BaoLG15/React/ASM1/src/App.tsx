import { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { TaskProvider } from './context/TaskContext';
import { Navbar } from './components/Navbar';
import { Home } from './pages/Home';
import { Tasks } from './pages/Tasks';
import { TaskDetail } from './pages/TaskDetail';
import { TaskForm } from './components/TaskForm';

function App() {
  const [isCreateOpen, setIsCreateOpen] = useState(false);

  return (
    <TaskProvider>
      <Router>
        <div className="min-h-screen bg-[#0b111e] text-slate-100 flex flex-col">
          {/* Global Navigation Header */}
          <Navbar onOpenCreateModal={() => setIsCreateOpen(true)} />
          
          {/* Main Dashboard Pages */}
          <main className="flex-grow">
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/tasks" element={<Tasks />} />
              <Route path="/tasks/:id" element={<TaskDetail />} />
            </Routes>
          </main>

          {/* Footer branding */}
          <footer className="py-6 border-t border-white/5 bg-[#070b14] text-center text-xs text-slate-500 font-semibold tracking-wider">
            <div>&copy; 2026 ASM1 Task Management. Built with React, TypeScript & Tailwind CSS.</div>
          </footer>

          {/* Global New Task Modal */}
          <TaskForm isOpen={isCreateOpen} onClose={() => setIsCreateOpen(false)} />
        </div>
      </Router>
    </TaskProvider>
  );
}

export default App;
