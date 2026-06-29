import type { Task } from '../types';

const STORAGE_KEY = 'tasks_data';

const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

const getInitialTasks = (): Task[] => {
  const data = localStorage.getItem(STORAGE_KEY);
  if (data) {
    return JSON.parse(data);
  }
  const defaultTasks: Task[] = [
    { id: '1', name: 'Learn React', description: 'Understand React fundamentals and Hooks.', createdAt: Date.now() },
    { id: '2', name: 'Master Tailwind CSS', description: 'Build responsive layouts using Tailwind.', createdAt: Date.now() - 100000 },
  ];
  localStorage.setItem(STORAGE_KEY, JSON.stringify(defaultTasks));
  return defaultTasks;
};

export const fetchTasks = async (): Promise<Task[]> => {
  await delay(800); // Simulate network delay
  return getInitialTasks();
};

export const fetchTaskById = async (id: string): Promise<Task | null> => {
  await delay(500);
  const tasks = getInitialTasks();
  return tasks.find(t => t.id === id) || null;
};

export const createTask = async (taskData: Omit<Task, 'id' | 'createdAt'>): Promise<Task> => {
  await delay(500);
  const tasks = getInitialTasks();
  const newTask: Task = {
    ...taskData,
    id: Math.random().toString(36).substr(2, 9),
    createdAt: Date.now(),
  };
  tasks.push(newTask);
  localStorage.setItem(STORAGE_KEY, JSON.stringify(tasks));
  return newTask;
};

export const updateTask = async (id: string, taskData: Partial<Task>): Promise<Task> => {
  await delay(500);
  const tasks = getInitialTasks();
  const index = tasks.findIndex(t => t.id === id);
  if (index === -1) throw new Error('Task not found');
  
  tasks[index] = { ...tasks[index], ...taskData };
  localStorage.setItem(STORAGE_KEY, JSON.stringify(tasks));
  return tasks[index];
};

export const deleteTask = async (id: string): Promise<void> => {
  await delay(500);
  let tasks = getInitialTasks();
  tasks = tasks.filter(t => t.id !== id);
  localStorage.setItem(STORAGE_KEY, JSON.stringify(tasks));
};
