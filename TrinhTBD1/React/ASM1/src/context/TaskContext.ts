import { createContext } from 'react';
import type { Task, TaskStatus } from '../types/task';

export interface TaskContextType {
  tasks: Task[];
  loading: boolean;
  error: string | null;
  searchTerm: string;
  setSearchTerm: (term: string) => void;
  statusFilter: string;
  setStatusFilter: (status: string) => void;
  isModalOpen: boolean;
  setIsModalOpen: (open: boolean) => void;
  editingTask: Task | null;
  setEditingTask: (task: Task | null) => void;
  deletingTaskId: string | null;
  setDeletingTaskId: (id: string | null) => void;
  toastMessage: string | null;
  showToast: (msg: string) => void;
  clearToast: () => void;
  addTask: (name: string, description: string, status?: TaskStatus) => void;
  updateTask: (id: string, name: string, description: string, status?: TaskStatus) => void;
  deleteTask: (id: string) => void;
  reloadTasks: () => void;
}

export const TaskContext = createContext<TaskContextType | undefined>(undefined);
