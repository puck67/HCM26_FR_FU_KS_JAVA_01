export interface Task {
  id: string;
  name: string;
  description: string;
  completed: boolean;
  createdAt: string;
}

const seed: Task[] = [
  {
    id: "1",
    name: "Set up Next.js project",
    description: "Initialize the project with App Router, TypeScript, and Tailwind CSS configuration",
    completed: true,
    createdAt: "2025-01-15T08:00:00Z",
  },
  {
    id: "2",
    name: "Implement server actions",
    description: "Create server actions for CRUD operations with revalidation",
    completed: false,
    createdAt: "2025-01-16T09:30:00Z",
  },
  {
    id: "3",
    name: "Add Formik validation",
    description: "Integrate Formik with Yup schema for form handling",
    completed: false,
    createdAt: "2025-01-17T10:00:00Z",
  },
  {
    id: "4",
    name: "Design responsive UI",
    description: "Apply Tailwind CSS for a clean, responsive layout across all pages",
    completed: false,
    createdAt: "2025-01-18T14:00:00Z",
  },
  {
    id: "5",
    name: "Write documentation",
    description: "",
    completed: false,
    createdAt: "2025-01-19T16:00:00Z",
  },
];

// Persist the store on globalThis so it survives module re-evaluation
// during Next.js dev hot-reloads and across server-action / render boundaries.
const globalStore = globalThis as unknown as { __tasks?: Task[] };
const tasks: Task[] = (globalStore.__tasks ??= seed.map((t) => ({ ...t })));

export function getTasks(): Task[] {
  return [...tasks];
}

export function getTask(id: string): Task | undefined {
  return tasks.find((t) => t.id === id);
}

export function addTask(data: Pick<Task, "name" | "description">): Task {
  const task: Task = {
    id: Date.now().toString(36),
    name: data.name,
    description: data.description,
    completed: false,
    createdAt: new Date().toISOString(),
  };
  tasks.unshift(task);
  return task;
}

export function editTask(id: string, data: Pick<Task, "name" | "description">): Task | null {
  const idx = tasks.findIndex((t) => t.id === id);
  if (idx === -1) return null;
  tasks[idx] = { ...tasks[idx], ...data };
  return tasks[idx];
}

export function removeTask(id: string): boolean {
  const idx = tasks.findIndex((t) => t.id === id);
  if (idx === -1) return false;
  tasks.splice(idx, 1);
  return true;
}

export function toggleCompleted(id: string): Task | null {
  const idx = tasks.findIndex((t) => t.id === id);
  if (idx === -1) return null;
  tasks[idx] = { ...tasks[idx], completed: !tasks[idx].completed };
  return tasks[idx];
}
