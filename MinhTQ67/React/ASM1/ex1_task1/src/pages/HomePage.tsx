import { Link } from 'react-router-dom';

const features = [
  { icon: '📋', title: 'Manage Tasks', desc: 'View, create, edit and delete tasks easily.' },
  { icon: '🔍', title: 'Task Details', desc: 'Dive into task details including status and description.' },
  { icon: '⚡', title: 'Fast & Reactive', desc: 'Built with Vite + React for a blazing fast experience.' },
  { icon: '🎨', title: 'Clean UI', desc: 'Styled with Tailwind CSS for a responsive, modern look.' },
];

export default function HomePage() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-indigo-50 via-white to-purple-50">
      {/* Hero */}
      <section className="max-w-4xl mx-auto px-4 py-20 text-center">
        <span className="inline-block text-xs font-semibold uppercase tracking-widest text-indigo-500 bg-indigo-50 px-3 py-1 rounded-full mb-4">
          React Training Assignment
        </span>
        <h1 className="text-5xl font-extrabold text-gray-900 leading-tight mb-4">
          Task Dashboard
        </h1>
        <p className="text-lg text-gray-500 max-w-xl mx-auto mb-8">
          A modern task management app built with React, TypeScript, Vite, Formik, and Tailwind CSS.
        </p>
        <div className="flex gap-3 justify-center flex-wrap">
          <Link
            to="/tasks"
            className="px-6 py-3 bg-indigo-600 text-white rounded-xl font-semibold text-sm hover:bg-indigo-700 transition-colors shadow-md shadow-indigo-200"
          >
            View All Tasks →
          </Link>
          <Link
            to="/tasks/new"
            className="px-6 py-3 bg-white text-indigo-600 border border-indigo-200 rounded-xl font-semibold text-sm hover:bg-indigo-50 transition-colors"
          >
            + Create Task
          </Link>
        </div>
      </section>

      {/* Features */}
      <section className="max-w-4xl mx-auto px-4 pb-20">
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          {features.map((f) => (
            <div
              key={f.title}
              className="bg-white rounded-2xl p-6 border border-gray-100 shadow-sm hover:shadow-md transition-shadow"
            >
              <div className="text-3xl mb-3">{f.icon}</div>
              <h3 className="font-semibold text-gray-800 mb-1">{f.title}</h3>
              <p className="text-gray-500 text-sm">{f.desc}</p>
            </div>
          ))}
        </div>
      </section>

      {/* Tech stack badges */}
      <section className="border-t border-gray-100 bg-white py-8">
        <div className="max-w-4xl mx-auto px-4 text-center">
          <p className="text-xs text-gray-400 uppercase tracking-widest mb-3">Built with</p>
          <div className="flex flex-wrap justify-center gap-2">
            {['React 18', 'TypeScript', 'Vite', 'React Router v6', 'Formik + Yup', 'Tailwind CSS', 'JSONPlaceholder API'].map(
              (tech) => (
                <span
                  key={tech}
                  className="px-3 py-1 bg-gray-100 text-gray-600 rounded-full text-xs font-medium"
                >
                  {tech}
                </span>
              )
            )}
          </div>
        </div>
      </section>
    </div>
  );
}
