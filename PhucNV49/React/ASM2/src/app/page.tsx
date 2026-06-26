import Link from "next/link";

export default function Home() {
  return (
    <section className="py-12 sm:py-20">
      <div className="grid lg:grid-cols-2 gap-12 items-center">
        {/* Hero text */}
        <div className="space-y-6">
          <h1 className="text-4xl sm:text-5xl font-bold tracking-tight text-slate-900 leading-[1.1]">
            Task management, server-side
          </h1>
          <p className="text-lg text-slate-500 leading-relaxed max-w-[50ch]">
            Built with Next.js App Router, Server Actions, and Tailwind CSS. Full CRUD with real server-side data handling and Formik validation.
          </p>
          <div className="flex flex-wrap gap-3 pt-2">
            <Link
              href="/tasks"
              className="inline-flex items-center gap-2 px-6 py-3 rounded-(--radius-btn) bg-accent text-white font-medium text-sm hover:bg-accent-dark transition-colors active:scale-[0.98]"
            >
              View Tasks
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                <path d="M5 12h14M12 5l7 7-7 7" />
              </svg>
            </Link>
            <Link
              href="/tasks/new"
              className="inline-flex items-center gap-2 px-6 py-3 rounded-(--radius-btn) bg-slate-100 text-slate-700 font-medium text-sm hover:bg-slate-200 transition-colors active:scale-[0.98]"
            >
              Create Task
            </Link>
          </div>
        </div>

        {/* Visual card */}
        <div className="relative">
          <div className="bg-white rounded-(--radius-card) border border-slate-200/80 shadow-xl shadow-slate-200/50 p-6 space-y-4">
            {[
              { name: "Set up Next.js project", done: true },
              { name: "Implement server actions", done: true },
              { name: "Add Formik validation", done: false },
            ].map((item) => (
              <div
                key={item.name}
                className={`flex items-center gap-3 p-3 rounded-lg border ${
                  item.done
                    ? "bg-emerald-50/50 border-emerald-100"
                    : "bg-slate-50 border-slate-100"
                }`}
              >
                <div className={`w-5 h-5 rounded-md border-2 flex items-center justify-center ${
                  item.done ? "bg-emerald-500 border-emerald-500" : "border-slate-300"
                }`}>
                  {item.done && (
                    <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round">
                      <path d="M5 12l5 5L20 7" />
                    </svg>
                  )}
                </div>
                <span className={`text-sm font-medium flex-1 ${
                  item.done ? "text-slate-400 line-through" : "text-slate-700"
                }`}>
                  {item.name}
                </span>
              </div>
            ))}
          </div>
          <div className="absolute -z-10 inset-0 translate-x-4 translate-y-4 rounded-(--radius-card) bg-accent/10 blur-xl" />
        </div>
      </div>

      {/* Tech stack */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 mt-16 pt-12 border-t border-slate-200/60">
        {[
          { label: "Next.js 15", detail: "App Router + RSC" },
          { label: "Server Actions", detail: "CRUD mutations" },
          { label: "Tailwind CSS", detail: "Responsive UI" },
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
