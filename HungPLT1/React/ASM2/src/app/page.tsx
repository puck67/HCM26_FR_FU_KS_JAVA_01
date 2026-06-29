import Link from "next/link";
import { tasksRepo } from "@/lib/tasksRepo";
import { CheckCircle2, Circle, Clock, ListTodo, PlusCircle } from "lucide-react";

export default async function Home() {
  const tasks = await tasksRepo.getAll();
  const totalTasks = tasks.length;
  const completedTasks = tasks.filter((t) => t.completed).length;
  const pendingTasks = totalTasks - completedTasks;
  const completionRate = totalTasks > 0 ? Math.round((completedTasks / totalTasks) * 100) : 0;

  return (
    <div className="flex-1 bg-slate-950 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-4xl mx-auto space-y-12">
        <div className="text-center space-y-4">
          <h1 className="text-4xl font-extrabold tracking-tight sm:text-5xl bg-gradient-to-r from-violet-400 via-indigo-200 to-indigo-400 bg-clip-text text-transparent">
            Manage Tasks Efficiently
          </h1>
          <p className="max-w-2xl mx-auto text-lg text-slate-400">
            A premium, robust Next.js 15 application utilizing App Router, Server Actions, Formik validation, and TypeScript generics.
          </p>
        </div>

        <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
          <div className="bg-slate-900 border border-slate-800 rounded-2xl p-5 shadow-xl flex flex-col justify-between">
            <div className="flex items-center justify-between text-violet-400 mb-2">
              <span className="text-sm font-semibold text-slate-400">Total Tasks</span>
              <ListTodo size={20} />
            </div>
            <span className="text-3xl font-bold text-slate-100">{totalTasks}</span>
          </div>

          <div className="bg-slate-900 border border-slate-800 rounded-2xl p-5 shadow-xl flex flex-col justify-between">
            <div className="flex items-center justify-between text-emerald-400 mb-2">
              <span className="text-sm font-semibold text-slate-400">Completed</span>
              <CheckCircle2 size={20} />
            </div>
            <span className="text-3xl font-bold text-slate-100">{completedTasks}</span>
          </div>

          <div className="bg-slate-900 border border-slate-800 rounded-2xl p-5 shadow-xl flex flex-col justify-between">
            <div className="flex items-center justify-between text-amber-400 mb-2">
              <span className="text-sm font-semibold text-slate-400">Pending</span>
              <Clock size={20} />
            </div>
            <span className="text-3xl font-bold text-slate-100">{pendingTasks}</span>
          </div>

          <div className="bg-slate-900 border border-slate-800 rounded-2xl p-5 shadow-xl flex flex-col justify-between">
            <div className="flex items-center justify-between text-indigo-400 mb-2">
              <span className="text-sm font-semibold text-slate-400">Completion</span>
              <Circle size={20} />
            </div>
            <span className="text-3xl font-bold text-slate-100">{completionRate}%</span>
          </div>
        </div>

        <div className="bg-slate-900 border border-slate-800 rounded-2xl p-6 shadow-xl space-y-6">
          <h2 className="text-xl font-bold text-slate-100">Quick Operations</h2>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <Link
              href="/tasks"
              className="flex items-center justify-between p-4 bg-slate-950 border border-slate-800 rounded-xl hover:border-violet-500 hover:bg-slate-900/50 transition duration-150 group"
            >
              <div className="space-y-1">
                <p className="font-semibold text-slate-200 group-hover:text-violet-400 transition duration-150">
                  View Tasks List
                </p>
                <p className="text-xs text-slate-500">
                  Inspect, delete, edit, and toggle completion statuses.
                </p>
              </div>
              <ListTodo className="text-slate-500 group-hover:text-violet-400 transition duration-150" />
            </Link>

            <Link
              href="/tasks/new"
              className="flex items-center justify-between p-4 bg-slate-950 border border-slate-800 rounded-xl hover:border-violet-500 hover:bg-slate-900/50 transition duration-150 group"
            >
              <div className="space-y-1">
                <p className="font-semibold text-slate-200 group-hover:text-violet-400 transition duration-150">
                  Create New Task
                </p>
                <p className="text-xs text-slate-500">
                  Add custom tasks with rigorous data validation.
                </p>
              </div>
              <PlusCircle className="text-slate-500 group-hover:text-violet-400 transition duration-150" />
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}
