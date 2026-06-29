export interface Task {
  id: string;
  name: string;
  description: string;
  completed: boolean;
  createdAt: string;
}

const STORAGE_KEY = 'asm1_tasks';

const defaultTasks: Task[] = [
  {
    id: '1',
    name: 'Research Target Audience',
    description: 'Conduct surveys and interview users to define user personas for the new landing page.',
    completed: true,
    createdAt: new Date(Date.now() - 86400000 * 3).toISOString(), // 3 days ago
  },
  {
    id: '2',
    name: 'Design High-Fidelity Mockups',
    description: 'Create responsive UI designs in Figma using our premium color palette and glassmorphism guidelines.',
    completed: false,
    createdAt: new Date(Date.now() - 86400000 * 2).toISOString(), // 2 days ago
  },
  {
    id: '3',
    name: 'Configure State Management',
    description: 'Implement React context and useReducer to handle tasks data globally in the application.',
    completed: false,
    createdAt: new Date(Date.now() - 86400000).toISOString(), // 1 day ago
  },
  {
    id: '4',
    name: 'Setup Form Validation',
    description: 'Build task forms using Formik and schema validation via Yup, handling limits and error displays.',
    completed: false,
    createdAt: new Date().toISOString(),
  }
];

const getStoredTasks = (): Task[] => {
  const data = localStorage.getItem(STORAGE_KEY);
  if (!data) {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(defaultTasks));
    return defaultTasks;
  }
  return JSON.parse(data);
};

const setStoredTasks = (tasks: Task[]): void => {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(tasks));
};

const delay = (ms: number) => new Promise((resolve) => setTimeout(resolve, ms));

// Mock API endpoints simulating a REST backend with 800ms latency
export const api = {
  getTasks: async (): Promise<Task[]> => {
    await delay(800);
    // Introduce a small random probability of failure to test error states (e.g., 5% chance)
    if (Math.random() < 0.02) {
      throw new Error('Failed to fetch tasks from server. Please try again.');
    }
    return getStoredTasks();
  },

  getTaskById: async (id: string): Promise<Task> => {
    await delay(600);
    const tasks = getStoredTasks();
    const task = tasks.find(t => t.id === id);
    if (!task) {
      throw new Error(`Task with ID ${id} not found.`);
    }
    return task;
  },

  createTask: async (taskData: Omit<Task, 'id' | 'createdAt' | 'completed'>): Promise<Task> => {
    await delay(800);
    const tasks = getStoredTasks();
    const newTask: Task = {
      id: Math.random().toString(36).substring(2, 9),
      name: taskData.name,
      description: taskData.description || '',
      completed: false,
      createdAt: new Date().toISOString()
    };
    tasks.push(newTask);
    setStoredTasks(tasks);
    return newTask;
  },

  updateTask: async (id: string, updatedFields: Partial<Omit<Task, 'id'>>): Promise<Task> => {
    await delay(800);
    const tasks = getStoredTasks();
    const index = tasks.findIndex(t => t.id === id);
    if (index === -1) {
      throw new Error(`Task with ID ${id} not found.`);
    }
    const updatedTask = { ...tasks[index], ...updatedFields };
    tasks[index] = updatedTask;
    setStoredTasks(tasks);
    return updatedTask;
  },

  deleteTask: async (id: string): Promise<string> => {
    await delay(800);
    const tasks = getStoredTasks();
    const filteredTasks = tasks.filter(t => t.id !== id);
    if (filteredTasks.length === tasks.length) {
      throw new Error(`Task with ID ${id} not found.`);
    }
    setStoredTasks(filteredTasks);
    return id;
  }
};
