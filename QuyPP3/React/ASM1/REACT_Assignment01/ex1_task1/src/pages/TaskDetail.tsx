import { useParams, useNavigate, Link } from "react-router-dom";
import { useTasks } from "../context/TaskContext";

export default function TaskDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { state, dispatch } = useTasks();

  const task = state.tasks.find((t) => t.id === id);

  if (!task) {
    return (
      <div className="text-center py-12">
        <h3 className="text-lg font-bold text-slate-800">Target missing</h3>
        <p className="text-slate-500 mt-1 mb-4">The task entry you requested could not be located.</p>
        <Link to="/tasks" className="text-blue-600 hover:underline font-semibold">&larr; Return to Workspace</Link>
      </div>
    );
  }

  return (
    <div className="bg-white border border-slate-200 shadow-sm rounded-2xl p-6 max-w-2xl mx-auto">
      <div className="flex justify-between items-start gap-4 mb-6">
        <div>
          <span className="text-xs font-bold tracking-wide uppercase bg-blue-50 text-blue-700 px-2.5 py-1 rounded-md">
            ID: #{task.id}
          </span>
          <h1 className="text-2xl font-black text-slate-900 mt-2">{task.name}</h1>
        </div>
        <button
          onClick={() => {
            dispatch({ type: "DELETE_TASK", payload: task.id });
            navigate("/tasks");
          }}
          className="px-3 py-1.5 bg-rose-50 hover:bg-rose-100 text-rose-600 font-bold text-xs rounded-lg transition"
        >
          Remove Record
        </button>
      </div>

      <div className="border-t border-slate-100 pt-4 mb-6">
        <h4 className="text-xs font-bold text-slate-400 uppercase tracking-wider mb-2">Scope & Documentation</h4>
        <p className="text-slate-700 leading-relaxed bg-slate-50 p-4 rounded-xl border border-slate-100 whitespace-pre-wrap">
          {task.description || "No metadata description specified for this system entry."}
        </p>
      </div>

      <Link to="/tasks" className="inline-flex items-center text-sm font-semibold text-slate-600 hover:text-slate-900 transition">
        &larr; Back to System Dashboard
      </Link>
    </div>
  );
}
