import Link from 'next/link';

export default function Home() {
  return (
    <div className="flex flex-col items-center justify-center min-h-[60vh] text-center">
      <h1 className="text-4xl font-extrabold text-blue-900 mb-4">Welcome to Next.js Task Dashboard</h1>
      <p className="text-lg text-gray-600 mb-8 max-w-2xl">
        This is a task management application built with Next.js App Router, Server Actions, Tailwind CSS, Formik, and reusable React Components to demonstrate modern frontend development practices.
      </p>
      <Link 
        href="/tasks" 
        className="px-6 py-3 bg-blue-600 text-white font-semibold rounded-lg shadow-md hover:bg-blue-700 transition"
      >
        Go to Task Dashboard
      </Link>
    </div>
  );
}
