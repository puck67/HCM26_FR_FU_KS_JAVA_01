import React, { createContext, useContext, useReducer, useState, useEffect } from 'react';
import type { Task, TaskAction } from '../types';
import { api, toggleApiFailure } from '../services/api';

const taskReducer = (state: Task[], action: TaskAction): Task[] => {
  switch (action.type) {
    case 'SET_TASKS':
      return action.payload;
    case 'ADD_TASK':
      return [action.payload, ...state];
    case 'UPDATE_TASK':
      return state.map(task => (task.id === action.payload.id ? action.payload : task));
    case 'DELETE_TASK':
      return state.filter(task => task.id !== action.payload);
    default:
      return state;
  }
};

interface TaskContextType {
  tasks: Task[];
  loading: boolean;
  error: string | null;
  fetchTasks: () => Promise<void>;
  addTask: (task: Omit<Task, 'id' | 'createdAt'>) => Promise<Task>;
  updateTask: (id: string, task: Partial<Omit<Task, 'id' | 'createdAt'>>) => Promise<Task>;
  deleteTask: (id: string) => Promise<void>;
  isApiFailing: boolean;
  toggleApiError: (fail: boolean) => void;
}

const TaskContext = createContext<TaskContextType | undefined>(undefined);

export const TaskProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [tasks, dispatch] = useReducer(taskReducer, []);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [isApiFailing, setIsApiFailing] = useState<boolean>(false);

  const fetchTasks = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await api.getTasks();
      dispatch({ type: 'SET_TASKS', payload: data });
    } catch (err: any) {
      setError(err.message || 'An error occurred while loading tasks.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  const addTask = async (taskData: Omit<Task, 'id' | 'createdAt'>) => {
    setError(null);
    try {
      const newTask = await api.createTask(taskData);
      dispatch({ type: 'ADD_TASK', payload: newTask });
      return newTask;
    } catch (err: any) {
      setError(err.message || 'Failed to add task.');
      throw err;
    }
  };

  const updateTask = async (id: string, taskData: Partial<Omit<Task, 'id' | 'createdAt'>>) => {
    setError(null);
    try {
      const updated = await api.updateTask(id, taskData);
      dispatch({ type: 'UPDATE_TASK', payload: updated });
      return updated;
    } catch (err: any) {
      setError(err.message || 'Failed to update task.');
      throw err;
    }
  };

  const deleteTask = async (id: string) => {
    setError(null);
    try {
      await api.deleteTask(id);
      dispatch({ type: 'DELETE_TASK', payload: id });
    } catch (err: any) {
      setError(err.message || 'Failed to delete task.');
      throw err;
    }
  };

  const toggleApiError = (fail: boolean) => {
    setIsApiFailing(fail);
    toggleApiFailure(fail);
  };

  return (
    <TaskContext.Provider
      value={{
        tasks,
        loading,
        error,
        fetchTasks,
        addTask,
        updateTask,
        deleteTask,
        isApiFailing,
        toggleApiError,
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
