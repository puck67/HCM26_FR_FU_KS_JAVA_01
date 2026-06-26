import { useEffect, useState } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import { api } from "../api";
import type { Task } from "../taskReducer";

export default function TaskDetail() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [task, setTask] = useState<Task | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) return;
    let cancelled = false;
    setLoading(true);
    api
      .getTask(id)
      .then((data) => {
        if (cancelled) return;
        if (!data) {
          setError("Task not found");
        } else {
          setTask(data);
        }
        setLoading(false);
      })
      .catch((err) => {
        if (!cancelled) {
          setError(err instanceof Error ? err.message : "Failed to load task");
          setLoading(false);
        }
      });
    return () => { cancelled = true; };
  }, [id]);

  const handleDelete = async () => {
    if (!id || !confirm("Delete this task?")) return;
    await api.deleteTask(id);
    navigate("/tasks");
  };

  if (loading) {
    return (
      <div className="max-w-2xl mx-auto space-y-4 animate-pulse">
        <div className="h-4 w-24 bg-slate-200 rounded" />
        <div className="h-8 w-64 bg-slate-200 rounded-lg" />
        <div className="h-40 bg-white rounded-(--radius-card) border border-slate-200/60" />
      </div>
    );
  }

  if (error || !task) {
    return (
      <div className="flex flex-col items-center justify-center py-20 text-center">
        <div className="w-12 h-12 rounded-full bg-danger-light flex items-center justify-center mb-4">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#dc2626" strokeWidth="2" strokeLinecap="round">
            <circle cx="12" cy="12" r="10" />
            <path d="M12 8v4M12 16h.01" />
          </svg>
        </div>
        <h2 className="text-lg font-semibold text-slate-900 mb-1">{error || "Task not found"}</h2>
        <Link to="/tasks" className="text-sm text-accent hover:underline mt-2">
          Back to tasks
        </Link>
      </div>
    );
  }

  const statusStyles = {
    todo: { bg: "bg-slate-100", text: "text-slate-600", label: "To Do" },
    "in-progress": { bg: "bg-amber-50", text: "text-amber-700", label: "In Progress" },
    done: { bg: "bg-emerald-50", text: "text-emerald-700", label: "Done" },
  };
  const s = statusStyles[task.status];

  return (
    <div className="max-w-2xl mx-auto">
      {/* Breadcrumb */}
      <nav className="flex items-center gap-2 text-sm text-slate-400 mb-6">
        <Link to="/tasks" className="hover:text-accent transition-colors">Tasks</Link>
        <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round">
          <path d="M9 18l6-6-6-6" />
        </svg>
        <span className="text-slate-600 truncate max-w-[200px]">{task.name}</span>
      </nav>

      {/* Card */}
      <article className="bg-white rounded-(--radius-card) border border-slate-200/60 shadow-sm overflow-hidden">
        <div className="p-6 sm:p-8 space-y-6">
          {/* Header */}
          <div className="flex items-start justify-between gap-4">
            <div className="space-y-2">
              <h1 className="text-xl sm:text-2xl font-bold text-slate-900 tracking-tight">
                {task.name}
              </h1>
              <span className={`inline-flex items-center px-2.5 py-1 rounded-md text-xs font-medium ${s.bg} ${s.text}`}>
                {s.label}
              </span>
            </div>
          </div>

          {/* Description */}
          <div className="space-y-1.5">
            <h2 className="text-xs font-medium text-slate-400 uppercase tracking-wider">Description</h2>
            <p className="text-sm text-slate-600 leading-relaxed">
              {task.description || "No description provided."}
            </p>
          </div>

          {/* Meta */}
          <div className="pt-4 border-t border-slate-100">
            <div className="flex flex-wrap gap-6 text-xs text-slate-400">
              <div>
                <span className="block font-medium text-slate-500 mb-0.5">Created</span>
                {new Date(task.createdAt).toLocaleDateString("en-US", {
                  year: "numeric",
                  month: "short",
                  day: "numeric",
                })}
              </div>
              <div>
                <span className="block font-medium text-slate-500 mb-0.5">ID</span>
                {task.id}
              </div>
            </div>
          </div>
        </div>

        {/* Actions */}
        <div className="px-6 sm:px-8 py-4 bg-slate-50/50 border-t border-slate-100 flex flex-wrap gap-2">
          <Link
            to={`/tasks/${task.id}/edit`}
            className="px-4 py-2 rounded-(--radius-btn) bg-accent text-white text-sm font-medium hover:bg-accent-dark transition-colors active:scale-[0.98]"
          >
            Edit Task
          </Link>
          <button
            onClick={handleDelete}
            className="px-4 py-2 rounded-(--radius-btn) bg-white border border-slate-200 text-sm font-medium text-danger hover:bg-danger-light transition-colors active:scale-[0.98]"
          >
            Delete
          </button>
          <Link
            to="/tasks"
            className="px-4 py-2 rounded-(--radius-btn) bg-white border border-slate-200 text-sm font-medium text-slate-600 hover:bg-slate-50 transition-colors ml-auto"
          >
            Back to List
          </Link>
        </div>
      </article>
    </div>
  );
}
