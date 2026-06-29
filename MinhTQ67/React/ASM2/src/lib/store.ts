import { Task } from "@/types/task";

// In-memory store — persists during server runtime (dev hot-reload may reset it)
// In production Next.js this lives across requests on the same server instance.
declare global {
  // eslint-disable-next-line no-var
  var __taskStore: Task[] | undefined;
}

function initStore(): Task[] {
  return [
    {
      id: "1",
      name: "Set up Next.js project",
      description: "Initialize a new Next.js 14 project with TypeScript and Tailwind CSS.",
      completed: true,
      createdAt: new Date("2024-11-01").toISOString(),
    },
    {
      id: "2",
      name: "Implement Server Actions",
      description: "Create server actions for CRUD operations on tasks.",
      completed: false,
      createdAt: new Date("2024-11-02").toISOString(),
    },
    {
      id: "3",
      name: "Build Task List UI",
      description: "Design and implement a responsive task list page using Tailwind CSS.",
      completed: false,
      createdAt: new Date("2024-11-03").toISOString(),
    },
    {
      id: "4",
      name: "Add Formik Validation",
      description: "Integrate Formik and Yup for form handling and validation.",
      completed: false,
      createdAt: new Date("2024-11-04").toISOString(),
    },
  ];
}

// Use global to persist across Next.js hot reloads in development
if (!global.__taskStore) {
  global.__taskStore = initStore();
}

export function getTasks(): Task[] {
  return global.__taskStore!;
}

export function getTaskById(id: string): Task | undefined {
  return global.__taskStore!.find((t) => t.id === id);
}

export function addTask(task: Task): void {
  global.__taskStore!.push(task);
}

export function updateTaskInStore(id: string, updates: Partial<Task>): Task | null {
  const index = global.__taskStore!.findIndex((t) => t.id === id);
  if (index === -1) return null;
  global.__taskStore![index] = { ...global.__taskStore![index], ...updates };
  return global.__taskStore![index];
}

export function deleteTaskFromStore(id: string): boolean {
  const before = global.__taskStore!.length;
  global.__taskStore = global.__taskStore!.filter((t) => t.id !== id);
  return global.__taskStore.length < before;
}
