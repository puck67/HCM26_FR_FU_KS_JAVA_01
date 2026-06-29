import type { Task } from '../types/task';

const STORAGE_KEY = 'react_task_dashboard_tasks';

const defaultTasks: Task[] = [
  {
    id: 'task-1',
    name: 'Complete React Assignment',
    description: 'Develop dynamic user interfaces using React components, hooks, Formik, and Tailwind CSS.',
    status: 'In Progress',
    createdAt: '2026-06-29'
  },
  {
    id: 'task-2',
    name: 'Setup Vite and TypeScript',
    description: 'Initialize a light and fast React environment using Vite and configure TypeScript.',
    status: 'Completed',
    createdAt: '2026-06-28'
  },
  {
    id: 'task-3',
    name: 'Design Dashboard UI',
    description: 'Apply clean and responsive Tailwind CSS styling for task cards and form modals.',
    status: 'Pending',
    createdAt: '2026-06-29'
  }
];

const delay = (ms: number) => new Promise((resolve) => setTimeout(resolve, ms));

export const getStoredTasks = (): Task[] => {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(defaultTasks));
      return defaultTasks;
    }
    const parsed = JSON.parse(raw);
    return Array.isArray(parsed) ? parsed : defaultTasks;
  } catch {
    return defaultTasks;
  }
};

export const saveStoredTasks = (tasks: Task[]): void => {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(tasks));
  } catch (e) {
    console.error('Failed to save to localStorage', e);
  }
};

export const fetchTasksApi = async (): Promise<Task[]> => {
  await delay(500);
  try {
    const response = await fetch('/api/tasks.json');
    if (response.ok) {
      const remoteTasks = await response.json();
      const localTasks = getStoredTasks();
      if (!localStorage.getItem(STORAGE_KEY)) {
        saveStoredTasks(remoteTasks);
        return remoteTasks;
      }
      return localTasks;
    }
  } catch {
  }
  return getStoredTasks();
};

export const fetchTaskByIdApi = async (id: string): Promise<Task | null> => {
  await delay(300);
  if (!id || typeof id !== 'string') return null;
  const tasks = getStoredTasks();
  const found = tasks.find((t) => t.id === id);
  return found || null;
};
