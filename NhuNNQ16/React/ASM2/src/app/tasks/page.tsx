import Link from 'next/link';
import { getTasks } from '@/lib/store';
import TaskActions from '@/components/TaskActions';
import TaskFilters from '@/components/TaskFilters';

export const dynamic = 'force-dynamic';

interface PageProps {
  searchParams?: {
    q?: string;
    status?: string;
  };
}

export default function TasksPage({ searchParams }: PageProps) {
  const allTasks = getTasks();
  const query = (searchParams?.q || '').toLowerCase().trim();
  const statusFilter = searchParams?.status || 'all';

  // Apply filters
  let filteredTasks = [...allTasks];
  
  if (query) {
    filteredTasks = filteredTasks.filter(
      (t) =>
        t.name.toLowerCase().includes(query) ||
        t.description.toLowerCase().includes(query)
    );
  }

  if (statusFilter === 'completed') {
    filteredTasks = filteredTasks.filter((t) => t.completed);
  } else if (statusFilter === 'active') {
    filteredTasks = filteredTasks.filter((t) => !t.completed);
  }

  // Sort: completed tasks at the bottom, active tasks ordered by createdAt descending
  filteredTasks.sort((a, b) => {
    if (a.completed !== b.completed) {
      return a.completed ? 1 : -1;
    }
    return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
  });

  // Calculate metrics
  const doneCount = allTasks.filter((t) => t.completed).length;
  const completionRate = allTasks.length > 0 ? Math.round((doneCount / allTasks.length) * 100) : 0;

  return (
    <div className="space-y-8 max-w-4xl mx-auto">
      {/* Page Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-3xl font-extrabold tracking-tight text-slate-900">Tasks Workspace</h1>
          <p className="text-slate-500 text-sm mt-1">Organize and keep track of your daily operations</p>
        </div>
        <Link
          href="/tasks/new"
          className="px-5 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-700 text-white font-medium text-sm transition-all active:scale-95 shadow-md flex items-center gap-1.5 self-start sm:self-auto"
        >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2.5} stroke="currentColor" className="w-4 h-4">
            <path strokeLinecap="round" strokeLinejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
          </svg>
          Add Task
        </Link>
      </div>

      {/* Progress & Quick Stats */}
      {allTasks.length > 0 && (
        <div className="bg-white border border-slate-200 p-6 rounded-2xl space-y-4 shadow-sm">
          <div className="flex justify-between items-center text-sm font-medium">
            <span className="text-slate-500">Workspace Progress</span>
            <span className="text-indigo-600 font-semibold">{doneCount} of {allTasks.length} tasks completed ({completionRate}%)</span>
          </div>
          <div className="w-full bg-slate-100 rounded-full h-2.5 overflow-hidden">
            <div
              className="bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500 h-2.5 rounded-full transition-all duration-500"
              style={{ width: `${completionRate}%` }}
            />
          </div>
        </div>
      )}

      {/* Filter component */}
      <TaskFilters />

      {/* Tasks list */}
      {filteredTasks.length === 0 ? (
        <div className="text-center py-16 bg-white border border-slate-200 border-dashed rounded-2xl text-slate-400 shadow-sm animate-fade-in">
          <div className="text-4xl mb-3">📂</div>
          <p className="text-base font-semibold text-slate-700">No tasks found</p>
          <p className="text-slate-500 text-xs mt-1">Try modifying your filters or create a new task to get started.</p>
          {allTasks.length > 0 && (
            <Link
              href="/tasks"
              className="text-xs text-indigo-600 font-semibold hover:underline mt-4 inline-block"
            >
              Reset Filters
            </Link>
          )}
        </div>
      ) : (
        <div className="space-y-4">
          {filteredTasks.map((task) => (
            <div
              key={task.id}
              className={`group bg-white border rounded-2xl p-6 flex flex-col sm:flex-row sm:items-center justify-between gap-6 transition-all duration-200 hover:shadow-md ${
                task.completed
                  ? 'border-slate-200 border-l-4 border-l-emerald-500 text-slate-400 bg-slate-50/40'
                  : 'border-slate-200 border-l-4 border-l-indigo-600'
              }`}
            >
              <div className="flex-1 min-w-0 space-y-2">
                <div className="flex flex-wrap items-center gap-2">
                  <h3
                    className={`font-bold text-lg leading-snug break-words ${
                      task.completed ? 'line-through text-slate-400' : 'text-slate-800'
                    }`}
                  >
                    {task.name}
                  </h3>
                  
                  <span
                    className={`text-[10px] px-2.5 py-0.5 rounded-full font-bold uppercase tracking-wider inline-block ${
                      task.completed
                        ? 'bg-emerald-50 text-emerald-700 border border-emerald-100'
                        : 'bg-indigo-50 text-indigo-700 border border-indigo-100'
                    }`}
                  >
                    {task.completed ? 'Done' : 'Active'}
                  </span>
                </div>

                {task.description && (
                  <p className={`text-sm leading-relaxed ${task.completed ? 'text-slate-400' : 'text-slate-600'}`}>
                    {task.description}
                  </p>
                )}

                <div className="flex items-center gap-3 pt-1 text-[11px] text-slate-400 font-medium">
                  <span className="flex items-center gap-1">
                    📅 {new Date(task.createdAt).toLocaleDateString('en-US', {
                      month: 'short',
                      day: 'numeric',
                      year: 'numeric',
                      hour: '2-digit',
                      minute: '2-digit'
                    })}
                  </span>
                  <span>•</span>
                  <span>ID: {task.id}</span>
                </div>
              </div>

              {/* Action Buttons Client Component */}
              <TaskActions id={task.id} completed={task.completed} />
            </div>
          ))}
        </div>
      )}

      {/* Footer text */}
      {filteredTasks.length > 0 && (
        <div className="text-right text-[11px] text-slate-400 font-medium">
          Showing {filteredTasks.length} of {allTasks.length} total tasks
        </div>
      )}
    </div>
  );
}
