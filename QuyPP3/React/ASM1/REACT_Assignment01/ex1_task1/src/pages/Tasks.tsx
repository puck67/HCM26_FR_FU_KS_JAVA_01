import { useTasks, type Task } from "../context/TaskContext";
import { CrudTable, type ColumnConfig } from "../components/CrudTable";
import { CrudActionBar } from "../components/CrudActionBar";
import { TaskForm } from "../components/TaskForm";

export default function Tasks() {
  const { state, isFormOpen, setIsFormOpen, setSelectedTask, dispatch } = useTasks();

  const columns: ColumnConfig<Task>[] = [
    { key: "name", header: "Assignment Name" },
    { 
      key: "description", 
      header: "Description Snippet",
      render: (t) => <span className="text-slate-500 truncate block max-w-xs">{t.description || "—"}</span>
    },
  ];

  if (state.error) {
    return <div className="p-6 bg-rose-50 text-rose-700 font-semibold rounded-xl border border-rose-200">{state.error}</div>;
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 border-b border-slate-200 pb-5">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Task Records</h1>
          <p className="text-sm text-slate-500">Track, prioritize, and adjust systemic task states dynamically.</p>
        </div>
        <CrudActionBar onAdd={() => setIsFormOpen(true)} />
      </div>

      {state.loading ? (
        <div className="flex justify-center items-center py-12 text-slate-400 font-medium">
          Synchronizing runtime database variables...
        </div>
      ) : (
        <CrudTable
          data={state.tasks}
          columns={columns}
          onEdit={(task) => {
            setSelectedTask(task);
            setIsFormOpen(true);
          }}
          onDelete={(task) => dispatch({ type: "DELETE_TASK", payload: task.id })}
        />
      )}

      {isFormOpen && <TaskForm />}
    </div>
  );
}
