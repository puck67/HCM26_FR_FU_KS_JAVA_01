import Link from 'next/link';

export default function Home() {
  return (
    <main className="min-h-screen p-10 flex flex-col items-center justify-center bg-gray-100">
      <div className="bg-white p-12 rounded-xl shadow-lg text-center max-w-lg w-full">
        <h1 className="text-4xl font-extrabold mb-4 text-gray-800">Task Manager</h1>
        <p className="text-lg text-gray-600 mb-8">React Assignment 02 - Next.js App Router</p>
        <Link 
          href="/tasks" 
          className="inline-block px-8 py-4 bg-indigo-600 text-white rounded-lg shadow hover:bg-indigo-700 font-semibold transition"
        >
          Manage Tasks
        </Link>
      </div>
    </main>
  );
}
