import fs from 'fs';
import path from 'path';

export interface Task {
  id: string;
  name: string;
  description?: string;
  completed: boolean;
  createdAt: string;
}

const dbFilePath = path.join(process.cwd(), 'tasks.json');

// Helper to initialize the DB with seed data if it doesn't exist
function initDb() {
  if (!fs.existsSync(dbFilePath)) {
    const seedTasks: Task[] = [
      {
        id: 'task-1',
        name: 'Implement Server Actions',
        description: 'Create server actions for task management including createTask, updateTask, and deleteTask.',
        completed: true,
        createdAt: new Date(Date.now() - 3600000 * 2).toISOString(),
      },
      {
        id: 'task-2',
        name: 'Integrate Formik & Yup',
        description: 'Set up form validation for task creation and editing pages with strict length constraints.',
        completed: false,
        createdAt: new Date(Date.now() - 3600000).toISOString(),
      },
      {
        id: 'task-3',
        name: 'Design Premium Tailwind UI',
        description: 'Apply high-quality styling with glassmorphism effects, harmonized colors, and responsive layouts.',
        completed: false,
        createdAt: new Date().toISOString(),
      }
    ];
    fs.writeFileSync(dbFilePath, JSON.stringify(seedTasks, null, 2), 'utf-8');
  }
}

export async function getTasks(): Promise<Task[]> {
  initDb();
  try {
    const data = fs.readFileSync(dbFilePath, 'utf-8');
    return JSON.parse(data) as Task[];
  } catch (error) {
    console.error('Error reading tasks database:', error);
    return [];
  }
}

export async function getTaskById(id: string): Promise<Task | null> {
  const tasks = await getTasks();
  return tasks.find(t => t.id === id) || null;
}

export async function saveTasks(tasks: Task[]): Promise<void> {
  try {
    fs.writeFileSync(dbFilePath, JSON.stringify(tasks, null, 2), 'utf-8');
  } catch (error) {
    console.error('Error writing to tasks database:', error);
  }
}
