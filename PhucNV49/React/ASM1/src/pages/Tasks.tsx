import { useEffect, useReducer, useState } from "react";
import { Link } from "react-router-dom";
import { api } from "../api";
import { taskReducer, type Task } from "../taskReducer";

export default function Tasks() {
  const [tasks, dispatch] = useReducer(taskReducer, []);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [filter, setFilter] = useState<"all" | Task["status"]>("all");
  const [deletingId, setDeletingId] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    setError(null);
    api
      .getTasks()
      .then((data) => {
        if (!cancelled) {
          dispatch({ type: "SET_TASKS", payload: data });
          setLoading(false);
        }
      })
      .catch((err) => {
        if (!cancelled) {
          setError(err instanceof Error ? err.message : "Failed to load tasks");
          setLoading(false);
        }
      });
    return () => { cancelled = true; };
  }, []);

  const handleDelete = async (id: string) => {
    if (!confirm("Delete this task?")) return;
    setDeletingId(id);
    try {
      await api.deleteTask(id);
      dispatch({ type: "DELETE_TASK", payload: id });
    } catch {
      alert("Failed to delete task");
    } finally {
      setDeletingId(null);
    }
  };

  const filtered = filter === "all" ? tasks : tasks.filter((t) => t.status === filter);

  // Loading skeleton
  if (loading) {
    return (
      <div className="space-y-4">
        <div className="h-8 w-48 bg-slate-200 rounded-lg animate-pulse" />
        <div className="space-y-3">
          {Array.from({ length: 4 }).map((_, i) => (
            <div key={i} className="h-20 bg-white rounded-(--radius-card) border border-slate-200/60 animate-pulse" />
          ))}
        </div>
      </div>
    );
  }

  // Error state
  if (error) {
    return (
      <div className="flex flex-col items-center justify-center py-20 text-center">
        <div className="w-12 h-12 rounded-full bg-danger-light flex items-center justify-center mb-4">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#dc2626" strokeWidth="2" strokeLinecap="round">
            <circle cx="12" cy="12" r="10" />
            <path d="M12 8v4M12 16h.01" />
          </svg>
        </div>
        <h2 className="text-lg font-semibold text-slate-900 mb-1">Something went wrong</h2>
        <p className="text-sm text-slate-500 mb-4">{error}</p>
        <button
          onClick={() => window.location.reload()}
          className="px-4 py-2 rounded-(--radius-btn) bg-accent text-white text-sm font-medium hover:bg-accent-dark transition-colors"
        >
          Retry
        </button>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-900 tracking-tight">Tasks</h1>
          <p className="text-sm text-slate-500 mt-0.5">
            {tasks.length} task{tasks.length !== 1 ? "s" : ""} total
          </p>
        </div>
        <Link
          to="/tasks/new"
          className="inline-flex items-center gap-2 px-4 py-2.5 rounded-(--radius-btn) bg-accent text-white text-sm font-medium hover:bg-accent-dark transition-colors active:scale-[0.98] self-start sm:self-auto"
        >
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round">
            <path d="M12 5v14M5 12h14" />
          </svg>
          New Task
        </Link>
      </div>

      {/* Filter tabs */}
      <div className="flex gap-1 p-1 bg-slate-100 rounded-lg w-fit">
        {(["all", "todo", "in-progress", "done"] as const).map((f) => (
          <button
            key={f}
            onClick={() => setFilter(f)}
            className={`px-3 py-1.5 rounded-md text-xs font-medium transition-all ${
              filter === f
                ? "bg-white text-slate-900 shadow-sm"
                : "text-slate-500 hover:text-slate-700"
            }`}
          >
            {f === "all" ? "All" : f === "todo" ? "To Do" : f === "in-progress" ? "In Progress" : "Done"}
          </button>
        ))}
      </div>

      {/* Task list */}
      {filtered.length === 0 ? (
        <div className="text-center py-16">
          <div className="w-12 h-12 rounded-full bg-slate-100 flex items-center justify-center mx-auto mb-3">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#94a3b8" strokeWidth="1.5" strokeLinecap="round">
              <path d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2" />
              <rect x="9" y="3" width="6" height="4" rx="1" />
            </svg>
          </div>
          <p className="text-sm text-slate-500">No tasks found</p>
        </div>
      ) : (
        <ul className="space-y-2">
          {filtered.map((task) => (
            <li
              key={task.id}
              className={`group relative bg-white rounded-(--radius-card) border border-slate-200/60 p-4 sm:p-5 hover:border-slate-300 hover:shadow-sm transition-all ${
                deletingId === task.id ? "opacity-50 pointer-events-none" : ""
              }`}
            >
              <div className="flex items-start gap-3">
                <StatusDot status={task.status} />
                <div className="flex-1 min-w-0">
                  <Link
                    to={`/tasks/${task.id}`}
                    className="text-sm font-semibold text-slate-900 hover:text-accent transition-colors"
                  >
                    {task.name}
                  </Link>
                  {task.description && (
                    <p className="text-xs text-slate-400 mt-0.5 truncate max-w-md">
                      {task.description}
                    </p>
                  )}
                </div>
                <div className="flex items-center gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
                  <Link
                    to={`/tasks/${task.id}/edit`}
                    className="p-1.5 rounded-md text-slate-400 hover:text-accent hover:bg-accent-light transition-colors"
                    title="Edit"
                  >
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round">
                      <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7" />
                      <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z" />
                    </svg>
                  </Link>
                  <button
                    onClick={() => handleDelete(task.id)}
                    className="p-1.5 rounded-md text-slate-400 hover:text-danger hover:bg-danger-light transition-colors"
                    title="Delete"
                  >
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round">
                      <path d="M3 6h18M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2" />
                    </svg>
                  </button>
                </div>
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

function StatusDot({ status }: { status: Task["status"] }) {
  const colors = {
    todo: "bg-slate-300",
    "in-progress": "bg-amber-400",
    done: "bg-emerald-500",
  };
  return <div className={`w-2.5 h-2.5 rounded-full mt-1 shrink-0 ${colors[status]}`} />;
}
