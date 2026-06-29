import { useState } from "react";
import { CheckCircle, Clock, AlertTriangle } from "lucide-react";
import { CrudTable } from "../components/CrudTable";
import { CrudActionBar } from "../components/CrudActionBar";
import { ConfirmModal } from "../components/ConfirmModal";
import { ImportModal } from "../components/ImportModal";
import { CrudFormModal, type FieldConfig } from "../components/CrudFormModal";
import { ViewModal } from "../components/ViewModal";
import { StatCard } from "../components/StatCard";

type Task = { id: number; title: string; priority: string; status: string };

export default function TaskPage() {
  const [tasks, setTasks] = useState<Task[]>([
    { id: 1, title: "Design System Architecture", priority: "High", status: "Done" },
    { id: 2, title: "Setup PostgreSQL Database", priority: "Medium", status: "In Progress" },
    { id: 3, title: "Deploy to Vercel", priority: "High", status: "Done" },
    { id: 4, title: "Write Documentation", priority: "Low", status: "Todo" },
    { id: 5, title: "Create API Endpoints", priority: "High", status: "Todo" },
    { id: 6, title: "Implement Authentication", priority: "High", status: "In Progress" },
    { id: 7, title: "Write Unit Tests", priority: "Medium", status: "Todo" },
    { id: 8, title: "Configure CI/CD Pipeline", priority: "Low", status: "Todo" },
  ]);

  const [isAddOpen, setIsAddOpen] = useState(false);
  const [isImportOpen, setIsImportOpen] = useState(false);
  const [viewingTask, setViewingTask] = useState<Task | null>(null);
  const [editingTask, setEditingTask] = useState<Task | null>(null);
  const [deletingTask, setDeletingTask] = useState<Task | null>(null);

  const totalTasks = tasks.length;
  const completedTasks = tasks.filter(t => t.status === "Done").length;
  const highPriority = tasks.filter(t => t.priority === "High").length;

  const fields: FieldConfig[] = [
    { name: "title", label: "Task Title", type: "text" },
    { name: "priority", label: "Priority", type: "select", options: ["Low", "Medium", "High"] },
    { name: "status", label: "Status", type: "select", options: ["Todo", "In Progress", "Done"] }
  ];

  const handleExport = () => {
    const csvContent = "data:text/csv;charset=utf-8," 
      + ["id", "title", "priority", "status"].join(",") + "\\n"
      + tasks.map(t => `${t.id},"${t.title}",${t.priority},${t.status}`).join("\\n");
    const link = document.createElement("a");
    link.setAttribute("href", encodeURI(csvContent));
    link.setAttribute("download", "tasks_export.csv");
    link.click();
  };

  const handleSaveAdd = (data: any) => {
    const newTask: Task = { id: tasks.length ? Math.max(...tasks.map(t => t.id)) + 1 : 1, ...data };
    setTasks([...tasks, newTask]);
    setIsAddOpen(false);
  };

  const handleSaveEdit = (data: any) => {
    setTasks(tasks.map(t => t.id === data.id ? data : t));
    setEditingTask(null);
  };

  const handleDelete = () => {
    if (!deletingTask) return;
    setTasks(tasks.filter(t => t.id !== deletingTask.id));
    setDeletingTask(null);
  };

  return (
    <div className="w-full max-w-[1600px] mx-auto animate-fadeIn flex flex-col gap-5">
      <CrudActionBar
        onAdd={() => setIsAddOpen(true)}
        onImport={() => setIsImportOpen(true)}
        onExport={handleExport}
      />
      
      <div className="bg-white border border-slate-200 rounded-2xl shadow-sm overflow-hidden">
        <CrudTable
          data={tasks}
          onView={(task) => setViewingTask(task as Task)}
          onEdit={(task) => setEditingTask(task as Task)}
          onDelete={(task) => setDeletingTask(task as Task)}
          extraActions={(task) => [
            {
              title: <CheckCircle size={16} />,
              action: () => setTasks(tasks.map(t => t.id === (task as Task).id ? { ...t, status: "Done" } : t)),
              style: "w-8 h-8 bg-emerald-50 text-emerald-600 hover:bg-emerald-100 rounded-lg transition-colors"
            }
          ]}
        />
      </div>

      <ViewModal isOpen={!!viewingTask} onClose={() => setViewingTask(null)} title="Task Details" data={viewingTask} />
      <CrudFormModal isOpen={isAddOpen} onClose={() => setIsAddOpen(false)} onSave={handleSaveAdd} title="Add New Task" fields={fields} />
      <CrudFormModal isOpen={!!editingTask} onClose={() => setEditingTask(null)} onSave={handleSaveEdit} title="Edit Task" fields={fields} initialData={editingTask} />
      <ImportModal isOpen={isImportOpen} onClose={() => setIsImportOpen(false)} onImport={() => alert("File imported!")} />
      <ConfirmModal isOpen={!!deletingTask} onClose={() => setDeletingTask(null)} onConfirm={handleDelete} title="Delete Task" message={`Are you sure you want to delete "${deletingTask?.title}"?`} />
    </div>
  );
}
