import Link from 'next/link';
import { getTasks } from '@/lib/store';

export default function HomePage() {
  const tasks = getTasks();
  const doneCount = tasks.filter((t) => t.completed).length;
  const pendingCount = tasks.length - doneCount;
  const completionRate = tasks.length > 0 ? Math.round((doneCount / tasks.length) * 100) : 0;

  // Get 3 most recent tasks
  const recentTasks = [...tasks]
    .sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
    .slice(0, 3);

  return (
    <div className="space-y-12">
      {/* Hero Section */}
      <section className="relative overflow-hidden rounded-3xl bg-gradient-to-br from-indigo-50 via-white to-purple-50 border border-slate-200 p-8 md:p-12 shadow-sm">
        <div className="absolute top-0 right-0 -mt-12 -mr-12 w-64 h-64 bg-indigo-500/5 rounded-full blur-3xl pointer-events-none" />
        
        <div className="max-w-3xl space-y-6">
          <div className="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full border border-indigo-200 bg-indigo-50 text-indigo-700 text-xs font-semibold tracking-wide">
            <span>🚀</span> Next.js 14 App Router & Server Actions
          </div>
          
          <h1 className="text-4xl md:text-5xl lg:text-6xl font-extrabold tracking-tight text-slate-900">
            Master Your Focus, <br />
            <span className="bg-gradient-to-r from-indigo-600 via-purple-600 to-pink-600 bg-clip-text text-transparent">
              Elevate Your Execution
            </span>
          </h1>
          
          <p className="text-slate-600 text-lg md:text-xl max-w-2xl font-light leading-relaxed">
            ZenTask is a performance-optimized task management application powered by Next.js server components, client-side Formik validation, and real-time server actions.
          </p>

          <div className="flex flex-wrap gap-4 pt-4">
            <Link
              href="/tasks"
              className="px-6 py-3.5 rounded-xl bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-700 hover:to-purple-700 text-white font-semibold shadow-md transition-all active:scale-98 flex items-center gap-2 group"
            >
              Go to Tasks Board
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="currentColor" className="w-4 h-4 transform group-hover:translate-x-1 transition-transform">
                <path strokeLinecap="round" strokeLinejoin="round" d="M13.5 4.5L21 12m0 0l-7.5 7.5M21 12H3" />
              </svg>
            </Link>
            <Link
              href="/tasks/new"
              className="px-6 py-3.5 rounded-xl bg-white hover:bg-slate-50 border border-slate-200 text-slate-700 font-semibold transition-all active:scale-98 flex items-center gap-2 shadow-sm"
            >
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="currentColor" className="w-4 h-4">
                <path strokeLinecap="round" strokeLinejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
              </svg>
              Create New Task
            </Link>
          </div>
        </div>
      </section>

      {/* Stats Grid */}
      <section className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <div className="bg-white border border-slate-200 rounded-2xl p-6 flex items-center gap-4 hover:shadow-md transition-all">
          <div className="p-3 bg-blue-50 text-blue-600 rounded-xl text-2xl font-bold">📋</div>
          <div>
            <p className="text-3xl font-extrabold text-slate-900">{tasks.length}</p>
            <p className="text-slate-500 text-sm font-medium">Total Tasks</p>
          </div>
        </div>
        
        <div className="bg-white border border-slate-200 rounded-2xl p-6 flex items-center gap-4 hover:shadow-md transition-all">
          <div className="p-3 bg-emerald-50 text-emerald-600 rounded-xl text-2xl font-bold">✅</div>
          <div>
            <p className="text-3xl font-extrabold text-emerald-600">{doneCount}</p>
            <p className="text-slate-500 text-sm font-medium">Completed</p>
          </div>
        </div>

        <div className="bg-white border border-slate-200 rounded-2xl p-6 flex items-center gap-4 hover:shadow-md transition-all">
          <div className="p-3 bg-amber-50 text-amber-600 rounded-xl text-2xl font-bold">⏳</div>
          <div>
            <p className="text-3xl font-extrabold text-amber-600">{pendingCount}</p>
            <p className="text-slate-500 text-sm font-medium">In Progress</p>
          </div>
        </div>

        <div className="bg-white border border-slate-200 rounded-2xl p-6 flex items-center gap-4 hover:shadow-md transition-all">
          <div className="p-3 bg-indigo-50 text-indigo-600 rounded-xl text-2xl font-bold">🎯</div>
          <div className="flex-1">
            <div className="flex justify-between items-baseline">
              <span className="text-3xl font-extrabold text-indigo-600">{completionRate}%</span>
            </div>
            <div className="w-full bg-slate-100 rounded-full h-2 mt-1.5 overflow-hidden">
              <div 
                className="bg-gradient-to-r from-indigo-500 to-purple-500 h-2 rounded-full transition-all duration-500" 
                style={{ width: `${completionRate}%` }} 
              />
            </div>
          </div>
        </div>
      </section>

      {/* Main Grid: Recent Tasks & Next.js Architecture */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-8">
        {/* Left: Recent Tasks */}
        <div className="lg:col-span-7 bg-white border border-slate-200 rounded-2xl p-6 space-y-6 shadow-sm">
          <div className="flex justify-between items-center">
            <div>
              <h2 className="text-xl font-extrabold text-slate-900">Recent Activities</h2>
              <p className="text-slate-500 text-xs">Latest updates on your workspace</p>
            </div>
            <Link href="/tasks" className="text-xs text-indigo-600 hover:text-indigo-700 font-bold flex items-center gap-1">
              View All Tasks <span>→</span>
            </Link>
          </div>

          <div className="space-y-4">
            {recentTasks.length === 0 ? (
              <div className="text-center py-12 text-slate-400 border border-dashed border-slate-200 rounded-xl">
                <p className="text-sm">No tasks created yet.</p>
                <Link href="/tasks/new" className="text-xs text-indigo-600 hover:underline mt-1 inline-block">
                  Add task now
                </Link>
              </div>
            ) : (
              recentTasks.map((task) => (
                <div 
                  key={task.id} 
                  className={`p-4 rounded-xl border transition-all duration-200 flex items-center justify-between ${
                    task.completed 
                      ? 'bg-slate-50/50 border-slate-100 text-slate-400' 
                      : 'bg-white border-slate-200 hover:border-slate-300 hover:shadow-sm'
                  }`}
                >
                  <div className="min-w-0 flex-1 pr-4">
                    <h3 className={`font-bold text-sm truncate ${task.completed ? 'line-through text-slate-400' : 'text-slate-800'}`}>
                      {task.name}
                    </h3>
                    <p className="text-slate-500 text-xs truncate mt-0.5">
                      {task.description || 'No description provided'}
                    </p>
                  </div>
                  <div className="flex items-center gap-3">
                    <span className={`text-[10px] px-2.5 py-1 rounded-full font-bold uppercase ${
                      task.completed 
                        ? 'bg-emerald-50 text-emerald-700 border border-emerald-100' 
                        : 'bg-amber-50 text-amber-700 border border-amber-100'
                    }`}>
                      {task.completed ? 'Done' : 'Active'}
                    </span>
                    <Link 
                      href={`/tasks/${task.id}`}
                      className="p-1.5 hover:bg-slate-100 rounded-lg text-slate-500 hover:text-slate-800 transition-colors"
                      title="Edit Task"
                    >
                      ✏️
                    </Link>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>

        {/* Right: Architectural Concept Showcase */}
        <div className="lg:col-span-5 bg-white border border-slate-200 rounded-2xl p-6 space-y-6 shadow-sm">
          <div>
            <h2 className="text-xl font-extrabold text-slate-900">Next.js App Engine</h2>
            <p className="text-slate-500 text-xs">How this application runs under the hood</p>
          </div>

          <div className="space-y-4">
            <div className="p-4 rounded-xl bg-slate-50 border border-slate-200 hover:border-indigo-500/20 transition-all group">
              <h3 className="font-bold text-sm text-indigo-700 flex items-center gap-2">
                <span className="p-1.5 rounded-lg bg-indigo-50 text-indigo-600 text-xs">SSR</span>
                Server Components
              </h3>
              <p className="text-slate-500 text-xs mt-1.5 leading-relaxed">
                The home & list routes are fetched directly on the server. Zero client-side JS is shipped for initial renders, guaranteeing lightning-fast speeds.
              </p>
            </div>

            <div className="p-4 rounded-xl bg-slate-50 border border-slate-200 hover:border-purple-500/20 transition-all group">
              <h3 className="font-bold text-sm text-purple-700 flex items-center gap-2">
                <span className="p-1.5 rounded-lg bg-purple-50 text-purple-600 text-xs">HYD</span>
                Interactive Client Forms
              </h3>
              <p className="text-slate-500 text-xs mt-1.5 leading-relaxed">
                Task Creation & Edit forms leverage Client Components to run Formik + Yup validations locally before shipping payloads to the server.
              </p>
            </div>

            <div className="p-4 rounded-xl bg-slate-50 border border-slate-200 hover:border-pink-500/20 transition-all group">
              <h3 className="font-bold text-sm text-pink-700 flex items-center gap-2">
                <span className="p-1.5 rounded-lg bg-pink-50 text-pink-600 text-xs">ACT</span>
                Typesafe Server Actions
              </h3>
              <p className="text-slate-500 text-xs mt-1.5 leading-relaxed">
                Mutations are executed securely using Next.js Server Actions. Revalidation is driven by <code className="text-pink-600 font-mono text-[10px] bg-pink-50 px-1 py-0.5 rounded border border-pink-100">revalidatePath</code> to refresh page states.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
