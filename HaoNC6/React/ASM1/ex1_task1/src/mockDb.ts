import type { Task } from './types';

const STORAGE_KEY = 'tasks_data';

const initialTasks: Task[] = [
  {
    id: '1',
    name: 'Configure Vite & React Router',
    description: 'Set up the development environment, routing structure, and navigation links.',
    status: 'completed',
    createdAt: new Date(Date.now() - 3600000 * 24 * 3).toISOString(),
  },
  {
    id: '2',
    name: 'Implement Tailwind CSS v4',
    description: 'Integrate Tailwind v4 and design a premium Glassmorphic dark theme.',
    status: 'completed',
    createdAt: new Date(Date.now() - 3600000 * 24).toISOString(),
  },
  {
    id: '3',
    name: 'Set up state with useReducer',
    description: 'Manage tasks array using standard useReducer hook and provide Context for global state.',
    status: 'in_progress',
    createdAt: new Date().toISOString(),
  },
  {
    id: '4',
    name: 'Form handling using Formik',
    description: 'Implement creation and modification of tasks using Formik with strict character constraints.',
    status: 'todo',
    createdAt: new Date().toISOString(),
  },
];

// Initialize localStorage if not present
if (!localStorage.getItem(STORAGE_KEY)) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(initialTasks));
}

export const getTasksFromStorage = (): Task[] => {
  const data = localStorage.getItem(STORAGE_KEY);
  return data ? JSON.parse(data) : [];
};

export const saveTasksToStorage = (tasks: Task[]): void => {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(tasks));
};

// Simulate REST API GET Call
export const fetchTasksAPI = async (): Promise<Task[]> => {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      // 5% chance of simulating API failure for testing the error state UI
      if (Math.random() < 0.05) {
        reject(new Error("Unable to establish connection to the remote API. Please check your connection."));
      } else {
        resolve(getTasksFromStorage());
      }
    }, 1000); // 1-second delay for smooth transitions
  });
};
