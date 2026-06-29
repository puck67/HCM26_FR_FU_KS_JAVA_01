import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { TaskProvider } from './context/TaskContext';
import { Navbar } from './components/Navbar';
import { Home } from './pages/Home';
import { Tasks } from './pages/Tasks';
import { TaskDetail } from './pages/TaskDetail';

function App() {
  return (
    <TaskProvider>
      <BrowserRouter>
        <div className="min-h-screen bg-[#070a13] text-gray-100 flex flex-col">
          {/* Header Navigation */}
          <Navbar />

          {/* Main Content Area */}
          <main className="flex-1 w-full">
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/tasks" element={<Tasks />} />
              <Route path="/tasks/:id" element={<TaskDetail />} />
              {/* Fallback to Home if unknown route */}
              <Route path="*" element={<Home />} />
            </Routes>
          </main>

          {/* Premium Footer */}
          <footer className="border-t border-white/5 py-8 text-center text-xs text-gray-600 bg-slate-950/20">
            <p>© {new Date().getFullYear()} TaskSphere. Developed for JSFW Assignment 1.</p>
          </footer>
        </div>
      </BrowserRouter>
    </TaskProvider>
  );
}

export default App;
