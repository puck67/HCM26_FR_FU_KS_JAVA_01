import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { getTaskById } from "../api/taskApi";
import type { Task } from "../types/task";

export default function TaskDetail() {
  const { id } = useParams();
  const [task, setTask] = useState<Task | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchTask = async () => {
      setLoading(true);
      setError("");

      if (!id) {
        setError("Invalid task identifier.");
        setLoading(false);
        return;
      }

      try {
        const data = await getTaskById(Number(id));
        if (!data) {
          setError("Task not found in the database.");
        } else {
          setTask(data);
        }
      } catch {
        setError("Failed to fetch task information.");
      } finally {
        setLoading(false);
      }
    };

    fetchTask();
  }, [id]);

  if (loading) {
    return (
      <div className="mx-auto max-w-3xl rounded-[32px] border border-slate-200/80 bg-white p-8 shadow-sm space-y-6 animate-pulse">
        <div className="flex items-center justify-between">
          <div className="space-y-2 w-1/2">
            <div className="h-4 w-1/4 rounded bg-slate-100" />
            <div className="h-8 w-full rounded bg-slate-100" />
          </div>
          <div className="h-10 w-28 rounded-xl bg-slate-100" />
        </div>
        <div className="h-32 rounded-2xl bg-slate-50" />
      </div>
    );
  }

  if (error) {
    return (
      <div className="mx-auto max-w-2xl rounded-[32px] border border-red-100 bg-red-50/60 p-8 text-center shadow-sm space-y-5">
        <div className="mx-auto flex h-14 w-14 items-center justify-center rounded-2xl bg-red-100 text-red-600">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="2.5" stroke="currentColor" className="w-6 h-6">
            <path strokeLinecap="round" strokeLinejoin="round" d="M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126zM12 15.75h.007v.008H12v-.008z" />
          </svg>
        </div>
        <div>
          <h2 className="text-xl font-bold text-slate-800">Error Occurred</h2>
          <p className="mt-2 text-sm text-slate-500">{error}</p>
        </div>
        <Link 
          to="/tasks" 
          className="inline-flex items-center gap-2 rounded-xl bg-indigo-600 px-5 py-2.5 text-xs font-semibold text-white shadow transition hover:bg-indigo-700"
        >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="2" stroke="currentColor" className="w-4 h-4">
            <path strokeLinecap="round" strokeLinejoin="round" d="M10.5 19.5L3 12m0 0l7.5-7.5M3 12h18" />
          </svg>
          Return to Dashboard
        </Link>
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-3xl rounded-[32px] border border-slate-200/80 bg-white p-8 shadow-sm relative overflow-hidden">
      <div className="absolute top-0 right-0 -mt-8 -mr-8 h-36 w-36 rounded-full bg-indigo-50/40 blur-2xl" />
      
      <div className="relative flex flex-col gap-6 sm:flex-row sm:items-center sm:justify-between pb-6 border-b border-slate-100">
        <div>
          <div className="flex items-center gap-2">
            <span className="inline-flex items-center rounded-full bg-indigo-50 px-2.5 py-0.5 text-[10px] font-semibold uppercase tracking-wider text-indigo-700 border border-indigo-100/40">
              Task Item #{task?.id}
            </span>
            <span className="inline-flex items-center rounded-full bg-emerald-50 px-2.5 py-0.5 text-[10px] font-semibold uppercase tracking-wider text-emerald-700 border border-emerald-100/40">
              Active
            </span>
          </div>
          <h1 className="mt-3 text-3xl font-extrabold tracking-tight text-slate-900">{task?.name}</h1>
        </div>
        <Link 
          to="/tasks" 
          className="inline-flex items-center gap-2 rounded-xl border border-slate-200 bg-slate-50 px-4 py-2.5 text-xs font-semibold text-slate-700 transition hover:bg-slate-100 hover:text-slate-900"
        >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="2" stroke="currentColor" className="w-4 h-4">
            <path strokeLinecap="round" strokeLinejoin="round" d="M10.5 19.5L3 12m0 0l7.5-7.5M3 12h18" />
          </svg>
          Back to list
        </Link>
      </div>

      <div className="relative mt-8 space-y-6">
        <div className="rounded-2xl border border-slate-100 bg-slate-50/50 p-6">
          <h3 className="text-sm font-bold uppercase tracking-wider text-slate-400">Task Description</h3>
          <p className="mt-3 text-base leading-relaxed text-slate-700 whitespace-pre-wrap">
            {task?.description || "No description was provided for this task."}
          </p>
        </div>

        <div className="grid grid-cols-2 gap-4 text-xs">
          <div className="rounded-xl border border-slate-100 p-4 bg-slate-50/30">
            <span className="block font-semibold text-slate-400">Created On</span>
            <span className="mt-1 block font-bold text-slate-700">June 29, 2026</span>
          </div>
          <div className="rounded-xl border border-slate-100 p-4 bg-slate-50/30">
            <span className="block font-semibold text-slate-400">Priority Level</span>
            <span className="mt-1 block font-bold text-slate-700">Standard</span>
          </div>
        </div>
      </div>
    </div>
  );
}

