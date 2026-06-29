import { Task } from '@/types/task';

const taskStore = new Map<string, Task>();

// Seed on first load — runs once per server process lifetime
if (taskStore.size === 0) {
  const seeds: Task[] = [
    {
      id: 'seed-1',
      name: 'Setup Next.js project',
      description: 'Initialize the project with App Router and TypeScript',
      completed: true,
      createdAt: new Date('2024-01-01T09:00:00Z').toISOString(),
    },
    {
      id: 'seed-2',
      name: 'Design database schema',
      description: 'Plan the in-memory store structure and seed data',
      completed: true,
      createdAt: new Date('2024-01-02T10:00:00Z').toISOString(),
    },
    {
      id: 'seed-3',
      name: 'Build task list page',
      description: 'Create the /tasks route with Server Component rendering',
      completed: false,
      createdAt: new Date('2024-01-03T11:00:00Z').toISOString(),
    },
    {
      id: 'seed-4',
      name: 'Implement Server Actions',
      description: 'Write createTask, updateTask, deleteTask, toggleTaskCompleted',
      completed: false,
      createdAt: new Date('2024-01-04T12:00:00Z').toISOString(),
    },
    {
      id: 'seed-5',
      name: 'Write Formik form with Yup validation',
      description: 'Add name and description fields with proper error handling',
      completed: false,
      createdAt: new Date('2024-01-05T13:00:00Z').toISOString(),
    },
  ];
  seeds.forEach((t) => taskStore.set(t.id, t));
}

export { taskStore as store };
