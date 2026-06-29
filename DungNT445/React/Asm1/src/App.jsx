import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { TaskProvider } from './context/TaskContext';
import { Navbar } from './components/Navbar';
import { Home } from './pages/Home';
import { Tasks } from './pages/Tasks';
import { TaskDetail } from './pages/TaskDetail';
import { TaskForm } from './pages/TaskForm';
import { CheckSquare } from 'lucide-react';

const App = () => {
  return (
    <TaskProvider>
      <Router>
        <div className="flex flex-col min-h-screen bg-slate-50">
          {/* Header */}
          <Navbar />

          {/* Main Content Area */}
          <main className="flex-grow">
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/tasks" element={<Tasks />} />
              <Route path="/tasks/new" element={<TaskForm />} />
              <Route path="/tasks/:id" element={<TaskDetail />} />
              <Route path="/tasks/edit/:id" element={<TaskForm />} />
              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </main>

          {/* Footer */}
          <footer className="bg-white border-t border-slate-200 py-6 mt-12">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex flex-col sm:flex-row items-center justify-between gap-4 text-sm text-slate-500">
              <div className="flex items-center gap-2 font-bold text-indigo-600">
                <CheckSquare className="w-5 h-5" />
                <span>TaskFlow Inc.</span>
              </div>
              <div>
                <p>&copy; {new Date().getFullYear()} React Task Dashboard Assignment. Created with Vite + React.</p>
              </div>
            </div>
          </footer>
        </div>
      </Router>
    </TaskProvider>
  );
};

export default App;
