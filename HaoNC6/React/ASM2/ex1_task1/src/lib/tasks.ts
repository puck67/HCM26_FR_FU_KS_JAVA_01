import fs from 'fs';
import path from 'path';
import { Task } from '../types';

const FILE_PATH = path.join(process.cwd(), 'tasks.json');

const defaultTasks: Task[] = [
  {
    id: '1',
    name: 'Set up Next.js Project structure',
    description: 'Configure file-based App routing and directory layouts.',
    completed: true,
    createdAt: new Date(Date.now() - 3600000 * 5).toISOString()
  },
  {
    id: '2',
    name: 'Implement Next.js Server Actions',
    description: 'Write createTask, updateTask, deleteTask and toggleTaskCompleted server actions.',
    completed: false,
    createdAt: new Date(Date.now() - 3600000 * 2).toISOString()
  },
  {
    id: '3',
    name: 'Integrate Formik validation form',
    description: 'Add validation rules for task fields (name: required, max 40; description: max 200).',
    completed: false,
    createdAt: new Date().toISOString()
  }
];

export function getTasks(): Task[] {
  try {
    if (!fs.existsSync(FILE_PATH)) {
      fs.writeFileSync(FILE_PATH, JSON.stringify(defaultTasks, null, 2), 'utf-8');
      return defaultTasks;
    }
    const data = fs.readFileSync(FILE_PATH, 'utf-8');
    return JSON.parse(data) as Task[];
  } catch (error) {
    console.error('Error reading tasks file:', error);
    return [];
  }
}

export function saveTasks(tasks: Task[]): void {
  try {
    fs.writeFileSync(FILE_PATH, JSON.stringify(tasks, null, 2), 'utf-8');
  } catch (error) {
    console.error('Error writing tasks file:', error);
  }
}
