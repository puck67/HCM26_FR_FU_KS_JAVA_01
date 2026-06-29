import React, { createContext, useContext, useReducer, useCallback } from 'react';
import type { Task, TaskStatus } from '../types';
import { api } from '../utils/api';

export interface TaskState {
  tasks: Task[];
  loading: boolean;
  error: string | null;
}

export type TaskAction =
  | { type: 'FETCH_INIT' }
  | { type: 'FETCH_SUCCESS'; payload: Task[] }
  | { type: 'FETCH_FAILURE'; payload: string }
  | { type: 'ADD_TASK'; payload: Task }
  | { type: 'UPDATE_TASK'; payload: Task }
  | { type: 'DELETE_TASK'; payload: string };

export function taskReducer(state: TaskState, action: TaskAction): TaskState {
  switch (action.type) {
    case 'FETCH_INIT':
      return { ...state, loading: true, error: null };
    case 'FETCH_SUCCESS':
      return { ...state, loading: false, tasks: action.payload, error: null };
    case 'FETCH_FAILURE':
      return { ...state, loading: false, error: action.payload };
    case 'ADD_TASK':
      return { ...state, tasks: [...state.tasks, action.payload] };
    case 'UPDATE_TASK':
      return {
        ...state,
        tasks: state.tasks.map((task) =>
          task.id === action.payload.id ? action.payload : task
        ),
      };
    case 'DELETE_TASK':
      return {
        ...state,
        tasks: state.tasks.filter((task) => task.id !== action.payload),
      };
    default:
      return state;
  }
}

interface TaskContextType extends TaskState {
  fetchTasks: () => Promise<void>;
  addTask: (name: string, description: string, status: TaskStatus) => Promise<void>;
  updateTaskStatus: (id: string, status: TaskStatus) => Promise<void>;
  editTask: (id: string, name: string, description: string, status: TaskStatus) => Promise<void>;
  deleteTask: (id: string) => Promise<void>;
}

const TaskContext = createContext<TaskContextType | undefined>(undefined);

export const TaskProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [state, dispatch] = useReducer(taskReducer, {
    tasks: [],
    loading: false,
    error: null,
  });

  const fetchTasks = useCallback(async () => {
    dispatch({ type: 'FETCH_INIT' });
    try {
      const data = await api.getTasks();
      dispatch({ type: 'FETCH_SUCCESS', payload: data });
    } catch (err: any) {
      dispatch({ type: 'FETCH_FAILURE', payload: err.message || 'An error occurred' });
    }
  }, []);

  const addTask = useCallback(async (name: string, description: string, status: TaskStatus) => {
    try {
      const newTask = await api.createTask({ name, description, status });
      dispatch({ type: 'ADD_TASK', payload: newTask });
    } catch (err: any) {
      throw new Error(err.message || 'Failed to add task');
    }
  }, []);

  const updateTaskStatus = useCallback(async (id: string, status: TaskStatus) => {
    try {
      const updated = await api.updateTask(id, { status });
      dispatch({ type: 'UPDATE_TASK', payload: updated });
    } catch (err: any) {
      throw new Error(err.message || 'Failed to update task status');
    }
  }, []);

  const editTask = useCallback(async (id: string, name: string, description: string, status: TaskStatus) => {
    try {
      const updated = await api.updateTask(id, { name, description, status });
      dispatch({ type: 'UPDATE_TASK', payload: updated });
    } catch (err: any) {
      throw new Error(err.message || 'Failed to edit task');
    }
  }, []);

  const deleteTask = useCallback(async (id: string) => {
    try {
      await api.deleteTask(id);
      dispatch({ type: 'DELETE_TASK', payload: id });
    } catch (err: any) {
      throw new Error(err.message || 'Failed to delete task');
    }
  }, []);

  return (
    <TaskContext.Provider
      value={{
        ...state,
        fetchTasks,
        addTask,
        updateTaskStatus,
        editTask,
        deleteTask,
      }}
    >
      {children}
    </TaskContext.Provider>
  );
};

export const useTasks = () => {
  const context = useContext(TaskContext);
  if (!context) {
    throw new Error('useTasks must be used within a TaskProvider');
  }
  return context;
};
