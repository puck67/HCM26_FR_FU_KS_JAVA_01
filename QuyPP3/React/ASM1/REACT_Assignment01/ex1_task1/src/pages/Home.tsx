import { Link } from "react-router-dom";

export default function Home() {
  return (
    <div className="flex flex-col items-center justify-center min-h-[60vh] text-center p-6">
      <h1 className="text-4xl font-extrabold text-slate-900 tracking-tight mb-4">
        Welcome to your Task Manager
      </h1>
      <p className="text-lg text-slate-600 max-w-md mb-8">
        An enterprise dashboard architecture featuring modular data tables, Formik validation forms, and global contexts.
      </p>
      <Link
        to="/tasks"
        className="px-6 py-3 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-xl shadow-md shadow-blue-200 transition-all transform hover:-translate-y-0.5"
      >
        Go to Task Workspace &rarr;
      </Link>
    </div>
  );
}
