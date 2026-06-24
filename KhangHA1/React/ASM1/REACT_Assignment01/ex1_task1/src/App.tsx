import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import { Home } from './pages/Home';
import { Tasks } from './pages/Tasks';
import { TaskDetail } from './pages/TaskDetail';

function Layout({ children }: { children: React.ReactNode }) {
  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      <nav className="bg-blue-800 text-white shadow-md">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center">
              <span className="font-bold text-xl mr-8">Task Dashboard ASM1</span>
              <div className="hidden md:block">
                <div className="flex items-baseline space-x-4">
                  <Link to="/" className="px-3 py-2 rounded-md text-sm font-medium hover:bg-blue-700">Home</Link>
                  <Link to="/tasks" className="px-3 py-2 rounded-md text-sm font-medium hover:bg-blue-700">Tasks</Link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </nav>
      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {children}
      </main>
      <footer className="bg-gray-800 text-gray-300 text-center py-4">
        &copy; {new Date().getFullYear()} React Assignment 1
      </footer>
    </div>
  );
}

function App() {
  return (
    <Router>
      <Layout>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/tasks" element={<Tasks />} />
          <Route path="/tasks/:id" element={<TaskDetail />} />
        </Routes>
      </Layout>
    </Router>
  );
}

export default App;
