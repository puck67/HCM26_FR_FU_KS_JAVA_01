import { Task } from "@/types/task";

// In-memory data store (server-side singleton)
let tasks: Task[] = [
  {
    id: "1",
    name: "Design system architecture",
    description: "Plan the overall architecture for the new microservices platform.",
    completed: true,
    createdAt: new Date("2026-06-25T08:00:00").toISOString(),
    updatedAt: new Date("2026-06-26T10:00:00").toISOString(),
  },
  {
    id: "2",
    name: "Implement authentication module",
    description: "Build JWT-based authentication with refresh tokens and role-based access control.",
    completed: false,
    createdAt: new Date("2026-06-26T09:00:00").toISOString(),
    updatedAt: new Date("2026-06-26T09:00:00").toISOString(),
  },
  {
    id: "3",
    name: "Write unit tests",
    description: "Cover all service layer methods with Jest unit tests.",
    completed: false,
    createdAt: new Date("2026-06-27T10:00:00").toISOString(),
    updatedAt: new Date("2026-06-27T10:00:00").toISOString(),
  },
  {
    id: "4",
    name: "Setup CI/CD pipeline",
    description: "Configure GitHub Actions for automated testing and deployment.",
    completed: true,
    createdAt: new Date("2026-06-28T08:30:00").toISOString(),
    updatedAt: new Date("2026-06-28T11:00:00").toISOString(),
  },
];

export function getAllTasks(): Task[] {
  return [...tasks];
}

export function getTaskById(id: string): Task | undefined {
  return tasks.find((t) => t.id === id);
}

export function createTaskInStore(name: string, description: string): Task {
  const newTask: Task = {
    id: Date.now().toString(),
    name,
    description,
    completed: false,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString(),
  };
  tasks = [...tasks, newTask];
  return newTask;
}

export function updateTaskInStore(
  id: string,
  name: string,
  description: string
): Task | null {
  const idx = tasks.findIndex((t) => t.id === id);
  if (idx === -1) return null;
  tasks[idx] = { ...tasks[idx], name, description, updatedAt: new Date().toISOString() };
  return tasks[idx];
}

export function deleteTaskFromStore(id: string): boolean {
  const before = tasks.length;
  tasks = tasks.filter((t) => t.id !== id);
  return tasks.length < before;
}

export function toggleTaskCompletedInStore(id: string): Task | null {
  const idx = tasks.findIndex((t) => t.id === id);
  if (idx === -1) return null;
  tasks[idx] = {
    ...tasks[idx],
    completed: !tasks[idx].completed,
    updatedAt: new Date().toISOString(),
  };
  return tasks[idx];
}
