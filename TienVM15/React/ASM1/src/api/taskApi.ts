import type { Task } from '../types/task';

const MOCK_TASKS: Task[] = [
  {
    id: '1',
    name: 'Implement authentication flow',
    description: 'Set up login, logout, and route guarding using JWT tokens.',
    status: 'Completed',
    createdAt: new Date(Date.now() - 86400000 * 3).toISOString(),
  },
  {
    id: '2',
    name: 'Create Task Dashboard layout',
    description: 'Design a responsive, gorgeous navigation system and sidebar for the app.',
    status: 'Completed',
    createdAt: new Date(Date.now() - 86400000 * 2).toISOString(),
  },
  {
    id: '3',
    name: 'Integrate Formik validation',
    description: 'Connect Formik and Yup schemas to handle error and success states in tasks page.',
    status: 'Pending',
    createdAt: new Date(Date.now() - 86400000).toISOString(),
  },
  {
    id: '4',
    name: 'Performance testing & optimization',
    description: 'Run lighthouse audit and optimize package chunks and lazy load components.',
    status: 'Pending',
    createdAt: new Date().toISOString(),
  },
];

// Initialize localStorage if not present
if (!localStorage.getItem('react_tasks')) {
  localStorage.setItem('react_tasks', JSON.stringify(MOCK_TASKS));
}

export const taskApi = {
  getTasks: async (): Promise<Task[]> => {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        try {
          const tasks = localStorage.getItem('react_tasks');
          resolve(tasks ? JSON.parse(tasks) : []);
        } catch (e) {
          reject(new Error('Failed to fetch tasks from local storage'));
        }
      }, 800);
    });
  },
  saveTasks: (tasks: Task[]) => {
    localStorage.setItem('react_tasks', JSON.stringify(tasks));
  },
};
