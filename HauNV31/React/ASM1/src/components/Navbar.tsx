import { Link, useLocation } from 'react-router-dom';
import { ListTodo, Home, PlusSquare } from 'lucide-react';

export const Navbar = () => {
  const location = useLocation();

  const isActive = (path: string) => location.pathname === path;

  return (
    <nav className="bg-white shadow-sm sticky top-0 z-10 border-b border-gray-100">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex">
            <Link to="/" className="flex items-center gap-2">
              <div className="bg-primary/10 p-2 rounded-lg">
                <ListTodo className="h-6 w-6 text-primary" />
              </div>
              <span className="font-bold text-xl text-gray-900 tracking-tight">TaskMaster</span>
            </Link>
            <div className="hidden sm:ml-8 sm:flex sm:space-x-4 items-center">
              <Link
                to="/"
                className={`flex items-center gap-2 px-3 py-2 rounded-md text-sm font-medium transition-colors ${
                  isActive('/') ? 'bg-gray-100 text-gray-900' : 'text-gray-500 hover:text-gray-900 hover:bg-gray-50'
                }`}
              >
                <Home className="w-4 h-4" /> Home
              </Link>
              <Link
                to="/tasks"
                className={`flex items-center gap-2 px-3 py-2 rounded-md text-sm font-medium transition-colors ${
                  isActive('/tasks') ? 'bg-gray-100 text-gray-900' : 'text-gray-500 hover:text-gray-900 hover:bg-gray-50'
                }`}
              >
                <ListTodo className="w-4 h-4" /> Tasks
              </Link>
            </div>
          </div>
          <div className="flex items-center">
            <Link
              to="/tasks/create"
              className="flex items-center gap-2 bg-primary text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-blue-600 transition-colors shadow-sm"
            >
              <PlusSquare className="w-4 h-4" />
              <span>New Task</span>
            </Link>
          </div>
        </div>
      </div>
    </nav>
  );
};
