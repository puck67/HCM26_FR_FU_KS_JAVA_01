import { Link } from 'react-router-dom';

export function Home() {
  return (
    <div className="flex flex-col items-center justify-center min-h-[60vh] text-center">
      <h1 className="text-4xl font-extrabold text-blue-900 mb-4">Welcome to React Task Dashboard</h1>
      <p className="text-lg text-gray-600 mb-8 max-w-2xl">
        This is a simple task management application built with React, Vite, Tailwind CSS, Formik, and Custom Hooks to demonstrate modern frontend development practices.
      </p>
      <Link 
        to="/tasks" 
        className="px-6 py-3 bg-blue-600 text-white font-semibold rounded-lg shadow-md hover:bg-blue-700 transition"
      >
        Go to Task Dashboard
      </Link>
    </div>
  );
}
