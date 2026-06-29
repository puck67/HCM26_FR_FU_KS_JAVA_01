import { useEffect, useReducer, useState } from "react";
import { Link } from "react-router-dom";
import {
  createTask,
  deleteTask,
  getTasks,
  updateTask,
} from "../api/taskApi";
import { taskReducer } from "../store/taskReducer";
import type { Task } from "../types/task";
import TaskForm from "../components/TaskForm";

const emptyTask = { name: "", description: "" };

export default function Tasks() {
  const [tasks, dispatch] = useReducer(taskReducer, [] as Task[]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedTask, setSelectedTask] = useState<Task | null>(null);
  const [formMode, setFormMode] = useState<"Create" | "Edit">("Create");

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setError("");
      try {
        const data = await getTasks();
        dispatch({ type: "SET", payload: data });
      } catch {
        setError("Failed to load tasks from API server.");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const openCreateForm = () => {
    setFormMode("Create");
    setSelectedTask(null);
    setIsFormOpen(true);
  };

  const openEditForm = (task: Task) => {
    setFormMode("Edit");
    setSelectedTask(task);
    setIsFormOpen(true);
  };

  const closeForm = () => {
    setIsFormOpen(false);
    setSelectedTask(null);
  };

  const handleSubmit = async (values: { name: string; description: string }) => {
    setLoading(true);
    setError("");

    try {
      if (formMode === "Create") {
        const created = await createTask(values);
        dispatch({ type: "ADD", payload: created });
      } else if (selectedTask) {
        const updated = await updateTask({ ...selectedTask, ...values });
        if (updated) dispatch({ type: "UPDATE", payload: updated });
      }
      closeForm();
    } catch {
      setError("Unable to save the task changes.");
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    setLoading(true);
    setError("");
    try {
      const success = await deleteTask(id);
      if (success) {
        dispatch({ type: "DELETE", payload: id });
      } else {
        setError("Task not found on server.");
      }
    } catch {
      setError("Unable to delete the task.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-8 max-w-6xl mx-auto">
      {/* Page Header Banner */}
      <div className="relative overflow-hidden rounded-[32px] border border-slate-200/80 bg-white p-6 shadow-sm sm:p-8">
        <div className="absolute top-0 right-0 -mt-6 -mr-6 h-36 w-36 rounded-full bg-indigo-50/50 blur-2xl" />
        <div className="relative flex flex-col gap-5 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <span className="inline-flex items-center gap-1 rounded-full bg-indigo-50 px-2.5 py-0.5 text-xs font-semibold uppercase tracking-wider text-indigo-700">
              Workspace
            </span>
            <h1 className="mt-2 text-3xl font-extrabold tracking-tight text-slate-900">Task Control Center</h1>
            <p className="mt-1 text-sm text-slate-500 max-w-xl">
              Track, organize, and manage your tasks. Features include reactive list updates, live client validation, and persistent storage.
            </p>
          </div>
          <button
            type="button"
            onClick={openCreateForm}
            className="inline-flex items-center justify-center gap-2 rounded-2xl bg-indigo-600 px-5 py-3 text-sm font-semibold text-white shadow-md shadow-indigo-100 transition hover:bg-indigo-700 hover:shadow-indigo-200"
          >
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="2.5" stroke="currentColor" className="w-4 h-4">
              <path strokeLinecap="round" strokeLinejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
            </svg>
            Add Task
          </button>
        </div>
      </div>

      {/* Error Alert Box */}
      {error ? (
        <div className="flex items-center gap-3 rounded-2xl border border-red-100 bg-red-50/80 p-4 text-sm text-red-700 shadow-sm">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="2" stroke="currentColor" className="w-5 h-5 flex-shrink-0 text-red-600">
            <path strokeLinecap="round" strokeLinejoin="round" d="M12 9v3.75m9-.75a9 9 0 11-18 0 9 9 0 0118 0zm-9 3.75h.008v.008H12v-.008z" />
          </svg>
          <span className="font-medium">{error}</span>
        </div>
      ) : null}

      {/* Task Form Modal overlay */}
      {isFormOpen && (
        <TaskForm
          initialValues={selectedTask ? { name: selectedTask.name, description: selectedTask.description ?? "" } : emptyTask}
          submitLabel={formMode === "Create" ? "Create Task" : "Save Changes"}
          onSubmit={handleSubmit}
          onCancel={closeForm}
        />
      )}

      {/* Main Grid / Lists Section */}
      {loading && !isFormOpen ? (
        // Skeleton loader screen
        <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
          {[1, 2, 3].map((n) => (
            <div key={n} className="animate-pulse rounded-[28px] border border-slate-100 bg-white p-6 shadow-sm space-y-4">
              <div className="flex justify-between items-start">
                <div className="h-6 w-2/3 rounded-lg bg-slate-100" />
                <div className="h-5 w-12 rounded-full bg-slate-100" />
              </div>
              <div className="space-y-2">
                <div className="h-4 w-full rounded-md bg-slate-100" />
                <div className="h-4 w-5/6 rounded-md bg-slate-100" />
              </div>
              <div className="pt-2 flex gap-3">
                <div className="h-9 w-16 rounded-xl bg-slate-100" />
                <div className="h-9 w-16 rounded-xl bg-slate-100" />
                <div className="h-9 w-16 rounded-xl bg-slate-100" />
              </div>
            </div>
          ))}
        </div>
      ) : tasks.length === 0 ? (
        // Empty state page
        <div className="rounded-[32px] border border-dashed border-slate-300 bg-white py-16 px-6 text-center shadow-sm max-w-xl mx-auto flex flex-col items-center">
          <div className="flex h-16 w-16 items-center justify-center rounded-2xl bg-indigo-50 text-indigo-600 mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="2" stroke="currentColor" className="w-8 h-8">
              <path strokeLinecap="round" strokeLinejoin="round" d="M15.666 3.888A2.25 2.25 0 0013.5 2.25h-3c-1.03 0-1.9.693-2.166 1.638m7.332 0c.055.194.084.4.084.612v0a.75.75 0 01-.75.75H9a.75.75 0 01-.75-.75v0c0-.212.03-.418.084-.612m7.332 0c.346.102.637.321.806.628.217.396.223.87.048 1.29l-1.39 3.34a2.25 2.25 0 01-2.08 1.386H8.625a2.25 2.25 0 01-2.08-1.386l-1.39-3.34a1.72 1.72 0 01.048-1.29c.169-.307.46-.526.806-.628m8.19 0h-8.19" />
            </svg>
          </div>
          <h3 className="text-lg font-bold text-slate-800">No tasks found</h3>
          <p className="mt-2 text-sm text-slate-500 max-w-xs">
            Start organizing your day by creating your very first dashboard task item.
          </p>
          <button
            type="button"
            onClick={openCreateForm}
            className="mt-5 inline-flex items-center gap-1.5 rounded-xl bg-indigo-600 px-4 py-2.5 text-xs font-semibold text-white shadow transition hover:bg-indigo-700"
          >
            Create First Task
          </button>
        </div>
      ) : (
        // Task Grid list
        <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
          {tasks.map((task) => (
            <article 
              key={task.id} 
              className="group relative flex flex-col justify-between rounded-[28px] border border-slate-200/80 bg-white p-6 shadow-sm transition duration-300 hover:-translate-y-1 hover:shadow-md hover:border-slate-300"
            >
              <div>
                <div className="flex items-start justify-between gap-4">
                  <h2 className="text-lg font-bold text-slate-800 group-hover:text-indigo-600 transition duration-200 line-clamp-1">
                    {task.name}
                  </h2>
                  <span className="flex-shrink-0 inline-flex items-center rounded-full bg-indigo-50 px-2 py-0.5 text-[10px] font-semibold uppercase tracking-wider text-indigo-700 border border-indigo-100/40">
                    ID: {task.id}
                  </span>
                </div>
                <p className="mt-3 text-sm leading-relaxed text-slate-500 line-clamp-3">
                  {task.description || "No description provided."}
                </p>
              </div>

              <div className="mt-6 flex items-center gap-2.5 pt-4 border-t border-slate-100">
                <Link
                  to={`/tasks/${task.id}`}
                  className="inline-flex flex-1 items-center justify-center rounded-xl border border-indigo-100 bg-indigo-50/50 py-2.5 text-xs font-bold text-indigo-700 transition hover:bg-indigo-100/70"
                >
                  Details
                </Link>
                <button
                  type="button"
                  onClick={() => openEditForm(task)}
                  className="inline-flex rounded-xl border border-slate-200 bg-slate-50 p-2.5 text-slate-500 transition hover:bg-slate-100 hover:text-slate-800 hover:border-slate-300"
                  title="Edit task"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="2" stroke="currentColor" className="w-4 h-4">
                    <path strokeLinecap="round" strokeLinejoin="round" d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L6.832 19.82a4.5 4.5 0 01-1.897 1.13l-2.685.8.8-2.685a4.5 4.5 0 011.13-1.897L16.863 4.487zm0 0L19.5 7.125" />
                  </svg>
                </button>
                <button
                  type="button"
                  onClick={() => handleDelete(task.id)}
                  className="inline-flex rounded-xl border border-red-100 bg-red-50/50 p-2.5 text-red-600 transition hover:bg-red-100 hover:text-red-700 hover:border-red-200"
                  title="Delete task"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="2" stroke="currentColor" className="w-4 h-4">
                    <path strokeLinecap="round" strokeLinejoin="round" d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 01-2.244 2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 00-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 00-7.5 0" />
                  </svg>
                </button>
              </div>
            </article>
          ))}
        </div>
      )}
    </div>
  );
}

