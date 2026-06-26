import { Link } from "react-router-dom";

export default function Home() {
  return (
    <section className="py-12 sm:py-20">
      <div className="grid lg:grid-cols-2 gap-12 items-center">
        {/* Left - Hero text */}
        <div className="space-y-6">
          <h1 className="text-4xl sm:text-5xl font-bold tracking-tight text-slate-900 leading-[1.1]">
            Manage your tasks with clarity
          </h1>
          <p className="text-lg text-slate-500 leading-relaxed max-w-[50ch]">
            A clean, fast task dashboard built with React, TypeScript, and Tailwind CSS. Create, track, and complete your work efficiently.
          </p>
          <div className="flex flex-wrap gap-3 pt-2">
            <Link
              to="/tasks"
              className="inline-flex items-center gap-2 px-6 py-3 rounded-(--radius-btn) bg-accent text-white font-medium text-sm hover:bg-accent-dark transition-colors active:scale-[0.98]"
            >
              View Tasks
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                <path d="M5 12h14M12 5l7 7-7 7" />
              </svg>
            </Link>
            <Link
              to="/tasks/new"
              className="inline-flex items-center gap-2 px-6 py-3 rounded-(--radius-btn) bg-slate-100 text-slate-700 font-medium text-sm hover:bg-slate-200 transition-colors active:scale-[0.98]"
            >
              Create Task
            </Link>
          </div>
        </div>

        {/* Right - Visual card */}
        <div className="relative">
          <div className="bg-white rounded-(--radius-card) border border-slate-200/80 shadow-xl shadow-slate-200/50 p-6 space-y-4">
            {/* Mini task cards */}
            {[
              { name: "Design system setup", status: "done" as const },
              { name: "API integration", status: "in-progress" as const },
              { name: "Write documentation", status: "todo" as const },
            ].map((item) => (
              <div
                key={item.name}
                className="flex items-center gap-3 p-3 rounded-lg bg-slate-50 border border-slate-100"
              >
                <StatusDot status={item.status} />
                <span className="text-sm font-medium text-slate-700 flex-1">
                  {item.name}
                </span>
                <StatusBadge status={item.status} />
              </div>
            ))}
          </div>
          {/* Decorative blur */}
          <div className="absolute -z-10 inset-0 translate-x-4 translate-y-4 rounded-(--radius-card) bg-accent/10 blur-xl" />
        </div>
      </div>

      {/* Stats */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 mt-16 pt-12 border-t border-slate-200/60">
        {[
          { label: "React + Vite", detail: "Fast dev server" },
          { label: "TypeScript", detail: "Type safety" },
          { label: "Tailwind CSS", detail: "Utility-first" },
          { label: "Formik + Yup", detail: "Form validation" },
        ].map((stat) => (
          <div key={stat.label} className="text-center sm:text-left">
            <div className="text-sm font-semibold text-slate-900">{stat.label}</div>
            <div className="text-xs text-slate-400 mt-0.5">{stat.detail}</div>
          </div>
        ))}
      </div>
    </section>
  );
}

function StatusDot({ status }: { status: "todo" | "in-progress" | "done" }) {
  const colors = {
    todo: "bg-slate-300",
    "in-progress": "bg-amber-400",
    done: "bg-emerald-500",
  };
  return <div className={`w-2.5 h-2.5 rounded-full ${colors[status]}`} />;
}

function StatusBadge({ status }: { status: "todo" | "in-progress" | "done" }) {
  const styles = {
    todo: "bg-slate-100 text-slate-500",
    "in-progress": "bg-amber-50 text-amber-700",
    done: "bg-emerald-50 text-emerald-700",
  };
  const labels = { todo: "To Do", "in-progress": "In Progress", done: "Done" };
  return (
    <span className={`text-xs font-medium px-2 py-0.5 rounded-md ${styles[status]}`}>
      {labels[status]}
    </span>
  );
}
