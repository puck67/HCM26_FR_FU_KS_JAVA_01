import Link from "next/link";
import { getTasks } from "@/lib/store";

export default function HomePage() {
  const tasks = getTasks();
  const total = tasks.length;
  const done = tasks.filter((t) => t.completed).length;
  const pending = total - done;

  return (
    <div className="min-h-[80vh] flex flex-col items-center justify-center text-center gap-8">
      {/* Hero */}
      <div className="space-y-3">
        <span className="inline-block text-xs font-semibold uppercase tracking-widest text-indigo-500 bg-indigo-50 px-3 py-1 rounded-full">
          React Assignment 02 — Next.js
        </span>
        <h1 className="text-5xl font-extrabold text-gray-900 leading-tight">
          Task Dashboard
        </h1>
        <p className="text-gray-500 max-w-lg mx-auto text-base">
          Built with Next.js 14 App Router, Server Actions, Formik validation, and Tailwind CSS.
        </p>
      </div>

      {/* Stats */}
      <div className="flex gap-4 flex-wrap justify-center">
        {[
          { label: "Total Tasks", value: total, color: "bg-indigo-50 text-indigo-700 border-indigo-100" },
          { label: "Completed", value: done, color: "bg-green-50 text-green-700 border-green-100" },
          { label: "Pending", value: pending, color: "bg-amber-50 text-amber-700 border-amber-100" },
        ].map((s) => (
          <div
            key={s.label}
            className={`flex flex-col items-center px-8 py-5 rounded-2xl border ${s.color}`}
          >
            <span className="text-3xl font-bold">{s.value}</span>
            <span className="text-xs font-medium mt-1">{s.label}</span>
          </div>
        ))}
      </div>

      {/* Actions */}
      <div className="flex gap-3 flex-wrap justify-center">
        <Link
          href="/tasks"
          className="px-6 py-3 bg-indigo-600 text-white rounded-xl font-semibold text-sm hover:bg-indigo-700 transition-colors shadow-md shadow-indigo-200"
        >
          View All Tasks →
        </Link>
        <Link
          href="/tasks/new"
          className="px-6 py-3 bg-white text-indigo-600 border border-indigo-200 rounded-xl font-semibold text-sm hover:bg-indigo-50 transition-colors"
        >
          + Create Task
        </Link>
      </div>

      {/* Tech badges */}
      <div className="pt-4 border-t border-gray-100 w-full max-w-xl">
        <p className="text-xs text-gray-400 uppercase tracking-widest mb-3">Built with</p>
        <div className="flex flex-wrap justify-center gap-2">
          {["Next.js 14", "App Router", "Server Actions", "TypeScript", "Formik + Yup", "Tailwind CSS"].map(
            (t) => (
              <span
                key={t}
                className="px-3 py-1 bg-gray-100 text-gray-600 rounded-full text-xs font-medium"
              >
                {t}
              </span>
            )
          )}
        </div>
      </div>
    </div>
  );
}
