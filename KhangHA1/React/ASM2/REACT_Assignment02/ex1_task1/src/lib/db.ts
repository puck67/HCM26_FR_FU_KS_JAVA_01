import { v4 as uuidv4 } from 'uuid';

export interface Task {
  id: string;
  name: string;
  description?: string;
  completed: boolean;
}

// In-memory data store using global so it persists across Next.js HMR
const globalForDb = global as unknown as { tasks: Task[] };

if (!globalForDb.tasks) {
  globalForDb.tasks = [
    { id: uuidv4(), name: 'Learn Next.js', description: 'Study App Router', completed: true },
    { id: uuidv4(), name: 'Build Task App', description: 'Apply Server Actions', completed: false },
  ];
}

export const db = {
  getTasks: async (): Promise<Task[]> => {
    // Simulate delay
    await new Promise(resolve => setTimeout(resolve, 300));
    return globalForDb.tasks;
  },
  getTaskById: async (id: string): Promise<Task | null> => {
    await new Promise(resolve => setTimeout(resolve, 300));
    return globalForDb.tasks.find(t => t.id === id) || null;
  },
  createTask: async (task: Omit<Task, 'id' | 'completed'>): Promise<Task> => {
    await new Promise(resolve => setTimeout(resolve, 300));
    const newTask = { ...task, id: uuidv4(), completed: false };
    globalForDb.tasks.push(newTask);
    return newTask;
  },
  updateTask: async (id: string, updates: Partial<Task>): Promise<Task> => {
    await new Promise(resolve => setTimeout(resolve, 300));
    const index = globalForDb.tasks.findIndex(t => t.id === id);
    if (index === -1) throw new Error('Task not found');
    globalForDb.tasks[index] = { ...globalForDb.tasks[index], ...updates };
    return globalForDb.tasks[index];
  },
  deleteTask: async (id: string): Promise<void> => {
    await new Promise(resolve => setTimeout(resolve, 300));
    globalForDb.tasks = globalForDb.tasks.filter(t => t.id !== id);
  }
};
