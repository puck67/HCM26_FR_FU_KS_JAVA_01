import { NavLink } from "react-router-dom";
import type { PropsWithChildren } from "react";

export default function Layout({ children }: PropsWithChildren) {
  const getLinkClass = ({ isActive }: { isActive: boolean }) =>
    `rounded-xl px-4 py-2 text-sm font-medium transition duration-200 border ${
      isActive
        ? "bg-indigo-50 text-indigo-700 border-indigo-100/80 shadow-sm"
        : "text-slate-600 border-transparent hover:text-slate-900 hover:bg-slate-50 hover:border-slate-100"
    }`;

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 via-slate-100/60 to-indigo-50/20 text-slate-900 flex flex-col font-sans antialiased">
      <header className="sticky top-0 z-40 bg-white/75 backdrop-blur-lg border-b border-slate-200/60 shadow-sm">
        <div className="mx-auto flex max-w-6xl items-center justify-between px-4 py-3.5 sm:px-6 lg:px-8">
          <div className="flex items-center gap-3">
            <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-gradient-to-tr from-indigo-600 to-violet-600 text-white shadow-md shadow-indigo-200">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="2.5" stroke="currentColor" className="w-5 h-5">
                <path strokeLinecap="round" strokeLinejoin="round" d="M9 12h3.75M9 15h3.375c.621 0 1.125-.504 1.125-1.125V11.25c0-.621-.504-1.125-1.125-1.125H9.75M8.25 21h8.25c.621 0 1.125-.504 1.125-1.125V5.625c0-.621-.504-1.125-1.125-1.125H8.25c-.621 0-1.125.504-1.125 1.125v14.25c0 .621.504 1.125 1.125 1.125z" />
              </svg>
            </div>
            <div>
              <p className="text-[10px] font-bold uppercase tracking-[0.25em] text-indigo-600/80 leading-none">Vite + React</p>
              <h1 className="mt-0.5 text-lg font-bold text-slate-800 tracking-tight leading-none">Task Dashboard</h1>
            </div>
          </div>
          <nav className="flex items-center gap-2">
            <NavLink to="/" className={getLinkClass}>
              Home
            </NavLink>
            <NavLink to="/tasks" className={getLinkClass}>
              Tasks
            </NavLink>
          </nav>
        </div>
      </header>

      <main className="mx-auto max-w-6xl px-4 py-8 sm:px-6 lg:px-8 flex-1 w-full">
        {children}
      </main>

      <footer className="border-t border-slate-200/60 bg-white/60 backdrop-blur-sm py-5 mt-auto">
        <div className="mx-auto max-w-6xl px-4 text-center sm:text-left sm:px-6 lg:px-8 flex flex-col sm:flex-row items-center justify-between gap-3 text-xs text-slate-500">
          <p>Built with React, React Router, Formik, and Tailwind CSS.</p>
          <p className="font-semibold text-slate-400">REACT_Assignment01 &copy; 2026</p>
        </div>
      </footer>
    </div>
  );
}

