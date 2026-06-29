import type { Task } from '../types/task';

const STORAGE_KEY = 'asm2_tasks';

const SEED: Task[] = [
  {
    id: '1',
    name: 'Set up project structure',
    description: 'Initialize Vite + React + Tailwind and configure routing.',
    status: 'completed',
    createdAt: new Date(Date.now() - 86400000 * 2).toISOString(),
  },
  {
    id: '2',
    name: 'Build generic components',
    description: 'Create Button, FormField, Card, ListView, Toast, ConfirmDialog.',
    status: 'in_progress',
    createdAt: new Date(Date.now() - 86400000).toISOString(),
  },
  {
    id: '3',
    name: 'Implement task CRUD operations',
    description: 'Create, read, update, delete and toggle tasks via service layer.',
    status: 'todo',
    createdAt: new Date().toISOString(),
  },
];

const delay = (ms: number) => new Promise((r) => setTimeout(r, ms));

function load(): Task[] {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    return raw ? (JSON.parse(raw) as Task[]) : SEED;
  } catch {
    return SEED;
  }
}

function persist(tasks: Task[]): void {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(tasks));
}

export const taskApi = {
  async getTasks(): Promise<Task[]> {
    await delay(400);
    return load();
  },

  /** Fetch single task by id */
  async getTask(id: string): Promise<Task> {
    await delay(300);
    const task = load().find((t) => t.id === id);
    if (!task) throw new Error(`Task "${id}" not found.`);
    return task;
  },

  async createTask(name: string, description: string): Promise<Task> {
    await delay(500);
    const tasks = load();
    const next: Task = {
      id: Date.now().toString(),
      name: name.trim(),
      description: description.trim(),
      status: 'todo',
      createdAt: new Date().toISOString(),
    };
    tasks.unshift(next);
    persist(tasks);
    return next;
  },

  async updateTask(
    id: string,
    data: Partial<Pick<Task, 'name' | 'description' | 'status'>>
  ): Promise<Task> {
    await delay(400);
    const tasks = load();
    const idx = tasks.findIndex((t) => t.id === id);
    if (idx === -1) throw new Error(`Task "${id}" not found.`);
    tasks[idx] = { ...tasks[idx], ...data };
    persist(tasks);
    return tasks[idx];
  },

  async deleteTask(id: string): Promise<void> {
    await delay(400);
    persist(load().filter((t) => t.id !== id));
  },

  async toggleTaskCompleted(id: string): Promise<Task> {
    await delay(300);
    const tasks = load();
    const idx = tasks.findIndex((t) => t.id === id);
    if (idx === -1) throw new Error(`Task "${id}" not found.`);
    tasks[idx].status = tasks[idx].status === 'completed' ? 'todo' : 'completed';
    persist(tasks);
    return tasks[idx];
  },
};
