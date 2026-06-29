export type TaskStatus = 'pending' | 'in_progress' | 'completed';

export interface Task {
  id: string;
  name: string;
  description: string;
  status: TaskStatus;
  createdAt: string;
}
