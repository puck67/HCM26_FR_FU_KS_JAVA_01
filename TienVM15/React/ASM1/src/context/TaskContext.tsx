import React, { createContext, useContext, useReducer, useEffect, useState } from 'react';
import type { Task, TaskAction } from '../types/task';
import { tasksReducer } from '../types/task';
import { taskApi } from '../api/taskApi';

interface TaskContextType {
  tasks: Task[];
  loading: boolean;
  error: string | null;
  dispatch: React.Dispatch<TaskAction>;
  refreshTasks: () => Promise<void>;
}

const TaskContext = createContext<TaskContextType | undefined>(undefined);

export const TaskProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [tasks, dispatch] = useReducer(tasksReducer, []);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchTasks = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await taskApi.getTasks();
      dispatch({ type: 'SET_TASKS', payload: data });
    } catch (err: any) {
      setError(err.message || 'Something went wrong while fetching tasks.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  // Sync to localStorage whenever tasks state changes (except initial empty state before load)
  useEffect(() => {
    if (!loading && error === null) {
      taskApi.saveTasks(tasks);
    }
  }, [tasks, loading, error]);

  return (
    <TaskContext.Provider value={{ tasks, loading, error, dispatch, refreshTasks: fetchTasks }}>
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
