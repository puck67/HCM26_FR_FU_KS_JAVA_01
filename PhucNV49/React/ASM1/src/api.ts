import type { Task } from "./taskReducer";

// Simulated network delay
const delay = (ms: number) => new Promise((res) => setTimeout(res, ms));

const STORAGE_KEY = "taskflow_tasks";

function getStoredTasks(): Task[] {
  const raw = localStorage.getItem(STORAGE_KEY);
  if (raw) return JSON.parse(raw);
  // Seed data
  const seed: Task[] = [
    {
      id: "1",
      name: "Set up project structure",
      description: "Initialize Vite + React + TypeScript project with Tailwind CSS and React Router",
      status: "done",
      createdAt: "2025-01-15T08:00:00Z",
    },
    {
      id: "2",
      name: "Implement task list page",
      description: "Create a responsive task list with loading and error states",
      status: "in-progress",
      createdAt: "2025-01-16T09:30:00Z",
    },
    {
      id: "3",
      name: "Add form validation",
      description: "Use Formik with Yup for form handling and validation",
      status: "todo",
      createdAt: "2025-01-17T10:00:00Z",
    },
    {
      id: "4",
      name: "Write unit tests",
      description: "Add tests for reducer and key components",
      status: "todo",
      createdAt: "2025-01-18T14:00:00Z",
    },
    {
      id: "5",
      name: "Deploy to production",
      description: "",
      status: "todo",
      createdAt: "2025-01-19T16:00:00Z",
    },
  ];
  localStorage.setItem(STORAGE_KEY, JSON.stringify(seed));
  return seed;
}

function save(tasks: Task[]) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(tasks));
}

export const api = {
  async getTasks(): Promise<Task[]> {
    await delay(600);
    return getStoredTasks();
  },

  async getTask(id: string): Promise<Task | undefined> {
    await delay(400);
    return getStoredTasks().find((t) => t.id === id);
  },

  async createTask(data: Pick<Task, "name" | "description">): Promise<Task> {
    await delay(500);
    const tasks = getStoredTasks();
    const task: Task = {
      id: Date.now().toString(36),
      name: data.name,
      description: data.description,
      status: "todo",
      createdAt: new Date().toISOString(),
    };
    save([task, ...tasks]);
    return task;
  },

  async updateTask(id: string, data: Pick<Task, "name" | "description">): Promise<Task> {
    await delay(500);
    const tasks = getStoredTasks();
    const idx = tasks.findIndex((t) => t.id === id);
    if (idx === -1) throw new Error("Task not found");
    tasks[idx] = { ...tasks[idx], ...data };
    save(tasks);
    return tasks[idx];
  },

  async deleteTask(id: string): Promise<void> {
    await delay(400);
    const tasks = getStoredTasks().filter((t) => t.id !== id);
    save(tasks);
  },
};
