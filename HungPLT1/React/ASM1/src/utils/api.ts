import type { Task } from '../types';
import { storage } from './storage';

const STORAGE_KEY = 'react_task_dashboard_tasks';

const INITIAL_TASKS: Task[] = [
  {
    id: '1',
    name: 'Setup React Project with Vite',
    description: 'Create a new Vite React TypeScript application and configure essential dependencies like React Router, Formik, and Tailwind CSS.',
    status: 'completed',
    createdAt: new Date(Date.now() - 3600000 * 24 * 3).toISOString(),
  },
  {
    id: '2',
    name: 'Implement Redux/Reducer State Management',
    description: 'Create the task reducer using useReducer to manage complex state transitions for adding, updating, and deleting tasks.',
    status: 'in_progress',
    createdAt: new Date(Date.now() - 3600000 * 24 * 2).toISOString(),
  },
  {
    id: '3',
    name: 'Design Premium Dashboard Layout',
    description: 'Use Tailwind CSS to build a highly responsive and modern dark-themed layout with sidebar, header, and statistical summary widgets.',
    status: 'pending',
    createdAt: new Date(Date.now() - 3600000 * 24).toISOString(),
  },
  {
    id: '4',
    name: 'Write Vitest Unit and Component Tests',
    description: 'Setup Vitest and write unit tests for the reducer and form validation schemas to ensure robust test coverage.',
    status: 'pending',
    createdAt: new Date().toISOString(),
  }
];

const getStoredTasks = (): Task[] => {
  return storage.get<Task[]>(STORAGE_KEY, INITIAL_TASKS);
};

const saveStoredTasks = (tasks: Task[]): void => {
  storage.set<Task[]>(STORAGE_KEY, tasks);
};

export const api = {
  getTasks: async (): Promise<Task[]> => {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        try {
          if (Math.random() < 0.02) {
            throw new Error('Failed to fetch tasks from the server. Please try again.');
          }
          resolve(getStoredTasks());
        } catch (error) {
          reject(error);
        }
      }, 600);
    });
  },

  getTaskById: async (id: string): Promise<Task> => {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        try {
          const tasks = getStoredTasks();
          const task = tasks.find(t => t.id === id);
          if (!task) {
            throw new Error('Task not found.');
          }
          resolve(task);
        } catch (error) {
          reject(error);
        }
      }, 300);
    });
  },

  createTask: async (taskData: Omit<Task, 'id' | 'createdAt'>): Promise<Task> => {
    return new Promise((resolve) => {
      setTimeout(() => {
        const tasks = getStoredTasks();
        const newTask: Task = {
          ...taskData,
          id: Math.random().toString(36).substr(2, 9),
          createdAt: new Date().toISOString(),
        };
        tasks.push(newTask);
        saveStoredTasks(tasks);
        resolve(newTask);
      }, 400);
    });
  },

  updateTask: async (id: string, taskData: Partial<Task>): Promise<Task> => {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        const tasks = getStoredTasks();
        const taskIndex = tasks.findIndex(t => t.id === id);
        if (taskIndex === -1) {
          reject(new Error('Task not found.'));
          return;
        }
        const updatedTask = { ...tasks[taskIndex], ...taskData };
        tasks[taskIndex] = updatedTask;
        saveStoredTasks(tasks);
        resolve(updatedTask);
      }, 400);
    });
  },

  deleteTask: async (id: string): Promise<string> => {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        const tasks = getStoredTasks();
        const taskIndex = tasks.findIndex(t => t.id === id);
        if (taskIndex === -1) {
          reject(new Error('Task not found.'));
          return;
        }
        const updatedTasks = tasks.filter(t => t.id !== id);
        saveStoredTasks(updatedTasks);
        resolve(id);
      }, 400);
    });
  }
};
