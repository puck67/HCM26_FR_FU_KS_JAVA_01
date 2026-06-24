import { v4 as uuidv4 } from 'uuid';

export interface Task {
  id: string;
  name: string;
  description?: string;
  status: 'Pending' | 'Completed';
}

let mockTasks: Task[] = [
  { id: uuidv4(), name: 'Learn React', description: 'Study Vite, Hooks, and Router', status: 'Completed' },
  { id: uuidv4(), name: 'Complete ASM1', description: 'Build a Task Dashboard', status: 'Pending' },
];

const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

export const mockApi = {
  getTasks: async (): Promise<Task[]> => {
    await delay(800);
    return [...mockTasks];
  },
  getTaskById: async (id: string): Promise<Task> => {
    await delay(500);
    const task = mockTasks.find(t => t.id === id);
    if (!task) throw new Error('Task not found');
    return { ...task };
  },
  createTask: async (task: Omit<Task, 'id'>): Promise<Task> => {
    await delay(600);
    const newTask = { ...task, id: uuidv4() };
    mockTasks = [...mockTasks, newTask];
    return newTask;
  },
  updateTask: async (id: string, updates: Partial<Task>): Promise<Task> => {
    await delay(600);
    const index = mockTasks.findIndex(t => t.id === id);
    if (index === -1) throw new Error('Task not found');
    mockTasks[index] = { ...mockTasks[index], ...updates };
    return mockTasks[index];
  },
  deleteTask: async (id: string): Promise<void> => {
    await delay(600);
    mockTasks = mockTasks.filter(t => t.id !== id);
  }
};
