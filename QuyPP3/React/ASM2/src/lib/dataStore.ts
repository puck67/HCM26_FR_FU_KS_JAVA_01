export interface Task {
  id: string;
  name: string;
  description: string;
  completed: boolean;
}

// Global scope ensures memory persistence across runtime re-executions
declare global {
  var __tasksDb: Task[] | undefined;
}

if (!globalThis.__tasksDb) {
  globalThis.__tasksDb = [
    {
      id: "1",
      name: "Configure Server Actions",
      description: "Understand mutable data mutations without API routes.",
      completed: true,
    },
    {
      id: "2",
      name: "Optimize Cache Hydration",
      description: "Leverage revalidatePath directly inside mutations.",
      completed: false,
    },
  ];
}

export const dataStore = {
  getAll: async (): Promise<Task[]> => [...(globalThis.__tasksDb || [])],
  getById: async (id: string): Promise<Task | undefined> => 
    globalThis.__tasksDb?.find((t) => t.id === id),
  add: async (task: Omit<Task, "id" | "completed">): Promise<Task> => {
    const newTask: Task = { ...task, id: Math.random().toString(36).substring(2, 9), completed: false };
    globalThis.__tasksDb?.push(newTask);
    return newTask;
  },
  update: async (id: string, updates: Partial<Omit<Task, "id">>): Promise<Task | null> => {
    const index = globalThis.__tasksDb?.findIndex((t) => t.id === id) ?? -1;
    if (index === -1 || !globalThis.__tasksDb) return null;
    globalThis.__tasksDb[index] = { ...globalThis.__tasksDb[index], ...updates };
    return globalThis.__tasksDb[index];
  },
  delete: async (id: string): Promise<boolean> => {
    const originalLength = globalThis.__tasksDb?.length || 0;
    globalThis.__tasksDb = globalThis.__tasksDb?.filter((t) => t.id !== id) || [];
    return (globalThis.__tasksDb?.length || 0) < originalLength;
  }
};
