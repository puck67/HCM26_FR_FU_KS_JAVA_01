import React, { createContext, useContext, useEffect } from 'react';
import { useTasks } from '../hooks/useTasks';
import { useTaskReducer } from '../hooks/useTaskReducer';
import type { Task, TaskAction } from '../types/task';

interface TaskContextType {
  tasks: Task[];
  dispatch: React.Dispatch<TaskAction>;
  isLoading: boolean;
  isError: boolean;
}

const TaskContext = createContext<TaskContextType | null>(null);

export const TaskProvider = ({ children }: { children: React.ReactNode }) => {
  const { data: serverTasks, isLoading, isError } = useTasks();
  const { tasks, dispatch } = useTaskReducer();

  useEffect(() => {
    if (serverTasks) {
      dispatch({ type: 'SET_TASKS', payload: serverTasks });
    }
  }, [serverTasks, dispatch]);

  return (
    <TaskContext.Provider value={{ tasks, dispatch, isLoading, isError }}>
      {children}
    </TaskContext.Provider>
  );
};

export const useTaskContext = () => {
  const context = useContext(TaskContext);
  if (!context) {
    throw new Error('useTaskContext must be used within a TaskProvider');
  }
  return context;
};
