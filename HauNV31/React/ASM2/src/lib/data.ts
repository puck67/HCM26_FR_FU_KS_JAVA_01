export interface Task {
  id: string;
  name: string;
  description?: string;
  completed: boolean;
}

declare global {
  var __tasks: Task[] | undefined;
}

if (!globalThis.__tasks) {
  globalThis.__tasks = [
    {
      id: '1',
      name: 'Learn Next.js App Router',
      description: 'Study how file-based routing and server components work.',
      completed: true,
    },
    {
      id: '2',
      name: 'Build Assignment',
      description: 'Implement a task manager using Next.js and Formik.',
      completed: false,
    }
  ];
}

export const getTasks = async (): Promise<Task[]> => {
  return [...globalThis.__tasks!];
};

export const getTask = async (id: string): Promise<Task | undefined> => {
  const task = globalThis.__tasks!.find(t => t.id === id);
  return task ? { ...task } : undefined;
};

export const addTask = async (task: Omit<Task, 'id' | 'completed'>): Promise<Task> => {
  const newTask: Task = {
    ...task,
    id: Date.now().toString(),
    completed: false,
  };
  globalThis.__tasks!.push(newTask);
  return newTask;
};

export const updateTaskData = async (id: string, updates: Partial<Task>): Promise<Task | undefined> => {
  const index = globalThis.__tasks!.findIndex(t => t.id === id);
  if (index !== -1) {
    globalThis.__tasks![index] = { ...globalThis.__tasks![index], ...updates };
    return globalThis.__tasks![index];
  }
  return undefined;
};

export const removeTask = async (id: string): Promise<boolean> => {
  const initialLength = globalThis.__tasks!.length;
  globalThis.__tasks! = globalThis.__tasks!.filter(t => t.id !== id);
  return globalThis.__tasks!.length < initialLength;
};
