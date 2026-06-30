import { useState } from "react";
import { CrudActionBar, CrudTable } from "../components";
import type { ColumnDef } from "../components";

// ─── Model ───────────────────────────────────────────────────────────────────

type TaskStatus = "todo" | "in-progress" | "done";

interface Task {
  id: number;
  title: string;
  assignee: string;
  priority: "low" | "medium" | "high";
  status: TaskStatus;
  dueDate: string;
}

// ─── Seed Data ────────────────────────────────────────────────────────────────

const seed: Task[] = [
  { id: 1, title: "Design system setup", assignee: "Alice", priority: "high", status: "done", dueDate: "2026-06-10" },
  { id: 2, title: "API integration", assignee: "Bob", priority: "high", status: "in-progress", dueDate: "2026-06-20" },
  { id: 3, title: "Write unit tests", assignee: "Charlie", priority: "medium", status: "todo", dueDate: "2026-06-25" },
  { id: 4, title: "Fix login bug", assignee: "Alice", priority: "high", status: "in-progress", dueDate: "2026-06-18" },
  { id: 5, title: "Update documentation", assignee: "Diana", priority: "low", status: "todo", dueDate: "2026-06-30" },
];

// ─── Column Definitions ──────────────────────────────────────────────────────

const priorityColors: Record<string, string> = {
  high: "badge--danger",
  medium: "badge--warning",
  low: "badge--info",
};

const statusColors: Record<TaskStatus, string> = {
  "todo": "badge--info",
  "in-progress": "badge--warning",
  "done": "badge--success",
};

const columns: ColumnDef<Task>[] = [
  { key: "id", label: "ID", width: "60px" },
  { key: "title", label: "Task Title" },
  { key: "assignee", label: "Assignee" },
  {
    key: "priority",
    label: "Priority",
    render: (val) => (
      <span className={`badge ${priorityColors[String(val)]}`}>
        {String(val).charAt(0).toUpperCase() + String(val).slice(1)}
      </span>
    ),
  },
  {
    key: "status",
    label: "Status",
    render: (val) => (
      <span className={`badge ${statusColors[val as TaskStatus]}`}>
        {String(val).replace("-", " ")}
      </span>
    ),
  },
  { key: "dueDate", label: "Due Date" },
];

// ─── Page ─────────────────────────────────────────────────────────────────────

export default function TaskPage() {
  const [tasks, setTasks] = useState<Task[]>(seed);

  const handleEdit = (task: Task) => {
    alert(`Edit task: ${task.title}`);
  };

  const handleDelete = (task: Task) => {
    if (confirm(`Delete "${task.title}"?`)) {
      setTasks((prev) => prev.filter((t) => t.id !== task.id));
    }
  };

  const handleMarkDone = (task: Task) => {
    setTasks((prev) =>
      prev.map((t) => (t.id === task.id ? { ...t, status: "done" } : t))
    );
  };

  return (
    <div className="page">
      <CrudActionBar
        title="Task Management"
        onAdd={() => alert("Open Add Task form")}
        onImport={() => alert("Import tasks from CSV")}
        onExport={() => alert("Export tasks to CSV")}
      />

      <CrudTable<Task>
        data={tasks}
        columns={columns}
        onEdit={handleEdit}
        onDelete={handleDelete}
        showIndex
        keyExtractor={(t) => t.id}
        pagination={{ pageSize: 4 }}
        searchable={{
          placeholder: "Search tasks by title or assignee...",
          keys: ["title", "assignee"],
        }}
        extraActions={(task) =>
          task.status !== "done"
            ? [
                {
                  title: "Done",
                  action: () => handleMarkDone(task),
                  variant: "success",
                  size: "sm",
                  icon: (
                    <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5">
                      <polyline points="20 6 9 17 4 12" />
                    </svg>
                  ),
                },
              ]
            : []
        }
      />
    </div>
  );
}
