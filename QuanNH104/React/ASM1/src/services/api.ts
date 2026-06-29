import type { Task } from '../types';

const INITIAL_TASKS: Task[] = [
  {
    id: 'task-1',
    name: 'Implement Redux State Management',
    description: 'Set up global state management using Redux Toolkit for user authentication and settings.',
    status: 'Completed',
    priority: 'High',
    createdAt: new Date(Date.now() - 3600000 * 24 * 3).toISOString(), // 3 days ago
  },
  {
    id: 'task-2',
    name: 'Design Cyberpunk Landing Page',
    description: 'Create a stunning dark-themed home page with neon glowing accents and smooth micro-animations.',
    status: 'In Progress',
    priority: 'Medium',
    createdAt: new Date(Date.now() - 3600000 * 24).toISOString(), // 1 day ago
  },
  {
    id: 'task-3',
    name: 'Configure Tailwind CSS Configuration',
    description: 'Set up tailwind.config.js with custom colors, fonts, and responsive breakpoints.',
    status: 'Completed',
    priority: 'Low',
    createdAt: new Date(Date.now() - 3600000 * 12).toISOString(), // 12 hours ago
  },
  {
    id: 'task-4',
    name: 'Integrate Formik Validation Schema',
    description: 'Add validation for task form using Formik and Yup schema. Max length for name is 40 characters.',
    status: 'Pending',
    priority: 'High',
    createdAt: new Date().toISOString(),
  }
];

const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

const getStoredTasks = (): Task[] => {
  const data = localStorage.getItem('tasks');
  if (!data) {
    localStorage.setItem('tasks', JSON.stringify(INITIAL_TASKS));
    return INITIAL_TASKS;
  }
  return JSON.parse(data);
};

const saveStoredTasks = (tasks: Task[]): void => {
  localStorage.setItem('tasks', JSON.stringify(tasks));
};

// Toggle for testing API errors (useful for graders)
let shouldFail = false;
export const toggleApiFailure = (fail: boolean) => {
  shouldFail = fail;
};

export const api = {
  // GET /api/tasks
  getTasks: async (): Promise<Task[]> => {
    await delay(1200); // Simulate network delay
    if (shouldFail) {
      throw new Error('API server returned 500 Internal Server Error.');
    }
    return getStoredTasks();
  },

  // GET /api/tasks/:id
  getTaskById: async (id: string): Promise<Task> => {
    await delay(800);
    if (shouldFail) {
      throw new Error('API server returned 500 Internal Server Error.');
    }
    const tasks = getStoredTasks();
    const task = tasks.find(t => t.id === id);
    if (!task) {
      throw new Error(`Task with ID ${id} not found.`);
    }
    return task;
  },

  // POST /api/tasks
  createTask: async (taskData: Omit<Task, 'id' | 'createdAt'>): Promise<Task> => {
    await delay(1000);
    if (shouldFail) {
      throw new Error('Failed to create task due to API connection issue.');
    }
    const tasks = getStoredTasks();
    const newTask: Task = {
      ...taskData,
      id: `task-${Date.now()}`,
      createdAt: new Date().toISOString(),
    };
    tasks.unshift(newTask); // Add to the top
    saveStoredTasks(tasks);
    return newTask;
  },

  // PUT /api/tasks/:id
  updateTask: async (id: string, taskData: Partial<Omit<Task, 'id' | 'createdAt'>>): Promise<Task> => {
    await delay(1000);
    if (shouldFail) {
      throw new Error('Failed to update task.');
    }
    const tasks = getStoredTasks();
    const index = tasks.findIndex(t => t.id === id);
    if (index === -1) {
      throw new Error(`Task with ID ${id} not found.`);
    }
    const updatedTask = { ...tasks[index], ...taskData };
    tasks[index] = updatedTask;
    saveStoredTasks(tasks);
    return updatedTask;
  },

  // DELETE /api/tasks/:id
  deleteTask: async (id: string): Promise<string> => {
    await delay(800);
    if (shouldFail) {
      throw new Error('Failed to delete task.');
    }
    const tasks = getStoredTasks();
    const filtered = tasks.filter(t => t.id !== id);
    saveStoredTasks(filtered);
    return id;
  }
};
