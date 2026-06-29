import { Link } from 'react-router-dom';
import { ArrowRight, CheckCircle, LayoutDashboard, Zap } from 'lucide-react';

export const Home = () => {
  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
      <div className="text-center">
        <h1 className="text-4xl tracking-tight font-extrabold text-gray-900 sm:text-5xl md:text-6xl">
          <span className="block">Manage your tasks with</span>
          <span className="block text-primary mt-2">Ultimate Efficiency</span>
        </h1>
        <p className="mt-6 max-w-md mx-auto text-base text-gray-500 sm:text-lg md:mt-8 md:text-xl md:max-w-3xl">
          TaskMaster is a modern, fast, and responsive task management application designed to help you stay organized and productive throughout your day.
        </p>
        <div className="mt-10 max-w-sm mx-auto sm:max-w-none sm:flex sm:justify-center gap-4">
          <Link
            to="/tasks"
            className="flex items-center justify-center gap-2 px-8 py-3 border border-transparent text-base font-medium rounded-xl text-white bg-primary hover:bg-blue-600 transition-all shadow-md hover:shadow-lg w-full sm:w-auto"
          >
            View Tasks <ArrowRight className="w-5 h-5" />
          </Link>
          <Link
            to="/tasks/create"
            className="flex items-center justify-center gap-2 px-8 py-3 border border-gray-300 text-base font-medium rounded-xl text-gray-700 bg-white hover:bg-gray-50 transition-all shadow-sm w-full sm:w-auto mt-3 sm:mt-0"
          >
            Create New Task
          </Link>
        </div>
      </div>

      <div className="mt-24 grid grid-cols-1 md:grid-cols-3 gap-8">
        <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 flex flex-col items-center text-center">
          <div className="w-12 h-12 bg-blue-50 text-primary rounded-xl flex items-center justify-center mb-4">
            <CheckCircle className="w-6 h-6" />
          </div>
          <h3 className="text-xl font-bold text-gray-900">Simple Tracking</h3>
          <p className="mt-2 text-gray-500">Keep track of your tasks easily with a clean, distraction-free interface.</p>
        </div>
        <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 flex flex-col items-center text-center">
          <div className="w-12 h-12 bg-green-50 text-green-600 rounded-xl flex items-center justify-center mb-4">
            <Zap className="w-6 h-6" />
          </div>
          <h3 className="text-xl font-bold text-gray-900">Fast & Responsive</h3>
          <p className="mt-2 text-gray-500">Built with Vite and React for a lightning-fast experience on any device.</p>
        </div>
        <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 flex flex-col items-center text-center">
          <div className="w-12 h-12 bg-purple-50 text-purple-600 rounded-xl flex items-center justify-center mb-4">
            <LayoutDashboard className="w-6 h-6" />
          </div>
          <h3 className="text-xl font-bold text-gray-900">Structured Data</h3>
          <p className="mt-2 text-gray-500">Organize your thoughts with structured naming and detailed descriptions.</p>
        </div>
      </div>
    </div>
  );
};
