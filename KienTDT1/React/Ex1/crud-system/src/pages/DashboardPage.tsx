import type { Task } from "./TaskPage";
import type { Student } from "./StudentPage";

type DashboardPageProps = {
  tasks: Task[];
  students: Student[];
  setActiveTab: (tab: "dashboard" | "tasks" | "students") => void;
  onQuickAddTask: () => void;
  onQuickAddStudent: () => void;
};

export function DashboardPage({
  tasks,
  students,
  setActiveTab,
  onQuickAddTask,
  onQuickAddStudent,
}: DashboardPageProps) {
  // Stats calculations
  const totalTasks = tasks.length;
  const completedTasks = tasks.filter((t) => t.status === "Done").length;
  const progressPercent = totalTasks > 0 ? Math.round((completedTasks / totalTasks) * 100) : 0;

  const totalStudents = students.length;
  const itStudents = students.filter((s) => s.class.toUpperCase() === "IT").length;
  const seStudents = students.filter((s) => s.class.toUpperCase() === "SE").length;

  return (
    <div className="space-y-6 animate-fade-in">
      {/* Header Title */}
      <div className="border-b border-brand-border pb-5 flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h2 className="text-2xl md:text-3xl font-extrabold tracking-tight text-brand-text">Portal Overview</h2>
          <p className="text-sm text-brand-text-muted mt-1">Real-time indicators across operations and academic metrics.</p>
        </div>
        <div className="text-xs text-brand-text-muted font-mono bg-slate-900 border border-brand-border px-3 py-1.5 rounded-lg shrink-0 w-fit">
          REFRESHED: {new Date().toLocaleTimeString()}
        </div>
      </div>

      {/* Statistics Grid */}
      <section className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4.5">
        {/* Card 1: Active Tasks */}
        <div className="bg-brand-card border border-brand-border rounded-2xl p-5 shadow-lg flex items-center justify-between hover:border-brand-border-hover transition">
          <div>
            <p className="text-xs font-bold text-brand-text-muted uppercase tracking-wider">Active Tasks</p>
            <h3 className="text-2xl font-extrabold mt-1 text-brand-text">{totalTasks}</h3>
          </div>
          <div className="p-3 rounded-xl bg-slate-800 border border-brand-border text-brand-text-muted">
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2" />
            </svg>
          </div>
        </div>

        {/* Card 2: Task Completion */}
        <div className="bg-brand-card border border-brand-border rounded-2xl p-5 shadow-lg flex items-center justify-between hover:border-brand-border-hover transition">
          <div>
            <p className="text-xs font-bold text-brand-text-muted uppercase tracking-wider">Completion Rate</p>
            <h3 className="text-2xl font-extrabold mt-1 text-emerald-400">{progressPercent}%</h3>
          </div>
          <div className="p-3 rounded-xl bg-emerald-500/10 border border-emerald-500/20 text-emerald-400">
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2.5" d="M9 12l2 2 4-4" />
            </svg>
          </div>
        </div>

        {/* Card 3: Total Students */}
        <div className="bg-brand-card border border-brand-border rounded-2xl p-5 shadow-lg flex items-center justify-between hover:border-brand-border-hover transition">
          <div>
            <p className="text-xs font-bold text-brand-text-muted uppercase tracking-wider">Total Students</p>
            <h3 className="text-2xl font-extrabold mt-1 text-cyan-400">{totalStudents}</h3>
          </div>
          <div className="p-3 rounded-xl bg-cyan-500/10 border border-cyan-500/20 text-cyan-400">
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 14l9-5-9-5-9 5 9 5zm0 0l6.16-3.422a12.083 12.083 0 01.665 6.479A11.952 11.952 0 0012 20.055a11.952 11.952 0 00-6.824-2.998 12.078 12.078 0 01.665-6.479L12 14zm-4 6v-7.5l4-2.222" />
            </svg>
          </div>
        </div>

        {/* Card 4: Class Split */}
        <div className="bg-brand-card border border-brand-border rounded-2xl p-5 shadow-lg flex items-center justify-between hover:border-brand-border-hover transition">
          <div>
            <p className="text-xs font-bold text-brand-text-muted uppercase tracking-wider">Class Split (IT/SE)</p>
            <h3 className="text-2xl font-extrabold mt-1 text-purple-400">{itStudents} / {seStudents}</h3>
          </div>
          <div className="p-3 rounded-xl bg-purple-500/10 border border-purple-500/20 text-purple-400">
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
            </svg>
          </div>
        </div>
      </section>

      {/* Combined Progress Indicators */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Welcome Card & Shortcuts */}
        <div className="lg:col-span-2 bg-gradient-to-br from-indigo-950/40 via-slate-900/60 to-purple-950/30 border border-brand-border p-6 rounded-2xl flex flex-col justify-between space-y-4 shadow-xl">
          <div>
            <h4 className="text-xl font-bold text-brand-text">Welcome back, Administrator</h4>
            <p className="text-sm text-brand-text-muted mt-1 leading-relaxed">
              You are viewing the consolidated control workspace. Here you can track tasks statuses, student records, batch process items via CSV, and monitor the overall progress rate.
            </p>
          </div>
          <div className="grid grid-cols-2 gap-3 pt-2">
            <button 
              onClick={onQuickAddTask}
              className="px-4 py-3 bg-indigo-600 hover:bg-indigo-500 rounded-xl text-xs font-bold text-white shadow-lg shadow-indigo-600/20 text-center transition cursor-pointer"
            >
              + Create New Task
            </button>
            <button 
              onClick={onQuickAddStudent}
              className="px-4 py-3 bg-purple-600 hover:bg-purple-500 rounded-xl text-xs font-bold text-white shadow-lg shadow-purple-600/20 text-center transition cursor-pointer"
            >
              + Enroll Student
            </button>
          </div>
        </div>

        {/* Progress Chart Representation */}
        <div className="bg-brand-card border border-brand-border p-6 rounded-2xl flex flex-col justify-between shadow-xl">
          <div>
            <span className="text-xs font-bold text-brand-text-muted tracking-wider uppercase">Task Progress Breakdown</span>
            <div className="flex items-baseline gap-2 mt-2">
              <h4 className="text-3xl font-extrabold text-brand-text">{completedTasks}</h4>
              <span className="text-xs text-brand-text-muted">/ {totalTasks} completed</span>
            </div>
          </div>

          <div className="relative flex items-center justify-center my-4">
            <div className="w-24 h-24 rounded-full border-8 border-slate-800 flex items-center justify-center relative">
              <span className="text-sm font-extrabold text-brand-text">{progressPercent}%</span>
              <div className="absolute inset-0 rounded-full border-8 border-transparent border-t-indigo-500 border-r-emerald-500 animate-spin opacity-40 pointer-events-none" />
            </div>
          </div>

          <div className="flex justify-between text-2xs text-brand-text-muted border-t border-brand-border/40 pt-3">
            <span className="flex items-center gap-1"><span className="w-2.5 h-2.5 rounded-full bg-emerald-400"></span> {completedTasks} Done</span>
            <span className="flex items-center gap-1"><span className="w-2.5 h-2.5 rounded-full bg-amber-400"></span> {totalTasks - completedTasks} Doing</span>
          </div>
        </div>
      </div>

      {/* Data Previews */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Recent Tasks */}
        <div className="bg-brand-card border border-brand-border rounded-2xl p-5 shadow-lg space-y-4">
          <div className="flex items-center justify-between border-b border-brand-border/60 pb-3">
            <h4 className="text-sm font-extrabold text-brand-text flex items-center gap-2">
              <span className="w-2 h-2 rounded-full bg-indigo-500" /> Recent Tasks
            </h4>
            <button 
              onClick={() => setActiveTab("tasks")} 
              className="text-xs text-indigo-400 hover:text-indigo-300 font-semibold cursor-pointer"
            >
              View All
            </button>
          </div>
          {tasks.length === 0 ? (
            <p className="text-xs text-brand-text-muted py-2">No tasks created yet.</p>
          ) : (
            <ul className="divide-y divide-brand-border/20">
              {tasks.slice(0, 3).map((task) => (
                <li key={task.id} className="py-2.5 flex items-center justify-between">
                  <span className="text-xs font-semibold text-brand-text truncate max-w-xs">{task.name}</span>
                  <span className={`px-2 py-0.5 text-3xs font-extrabold rounded-full ${
                    task.status === "Done" ? "bg-emerald-500/10 text-emerald-400" : "bg-amber-500/10 text-amber-400"
                  }`}>
                    {task.status}
                  </span>
                </li>
              ))}
            </ul>
          )}
        </div>

        {/* Recent Students */}
        <div className="bg-brand-card border border-brand-border rounded-2xl p-5 shadow-lg space-y-4">
          <div className="flex items-center justify-between border-b border-brand-border/60 pb-3">
            <h4 className="text-sm font-extrabold text-brand-text flex items-center gap-2">
              <span className="w-2 h-2 rounded-full bg-purple-500" /> Enrolled Students
            </h4>
            <button 
              onClick={() => setActiveTab("students")} 
              className="text-xs text-purple-400 hover:text-purple-300 font-semibold cursor-pointer"
            >
              View All
            </button>
          </div>
          {students.length === 0 ? (
            <p className="text-xs text-brand-text-muted py-2">No students enrolled yet.</p>
          ) : (
            <ul className="divide-y divide-brand-border/20">
              {students.slice(0, 3).map((stud) => (
                <li key={stud.id} className="py-2.5 flex items-center justify-between">
                  <span className="text-xs font-semibold text-brand-text">{stud.name} (Age {stud.age})</span>
                  <span className={`px-2 py-0.5 text-3xs font-extrabold rounded-full ${
                    stud.class.toUpperCase() === "IT" ? "bg-purple-500/10 text-purple-400" : "bg-cyan-500/10 text-cyan-400"
                  }`}>
                    {stud.class}
                  </span>
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    </div>
  );
}
