import { JsonRepository } from './jsonRepository';
import { Task } from '@/types';

export const tasksRepo = new JsonRepository<Task>('tasks.json');
