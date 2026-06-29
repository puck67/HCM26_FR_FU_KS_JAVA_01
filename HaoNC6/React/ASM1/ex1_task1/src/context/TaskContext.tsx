import React, { createContext, useContext, useReducer, useEffect, useCallback } from 'react';
import type { Task, TaskState, TaskAction } from '../types';
import { fetchTasksAPI, saveTasksToStorage } from '../mockDb';

interface TaskContextProps {
  state: TaskState;
  addTask: (name: string, description: string) => void;
  updateTask: (task: Task) => void;
  deleteTask: (id: string) => void;
  reloadTasks: () => Promise<void>;
}

const TaskContext = createContext<TaskContextProps | undefined>(undefined);

const initialState: TaskState = {
  tasks: [],
  loading: true,
  error: null,
};

function taskReducer(state: TaskState, action: TaskAction): TaskState {
  switch (action.type) {
    case 'FETCH_START':
      return { ...state, loading: true, error: null };
    case 'FETCH_SUCCESS':
      return { ...state, loading: false, tasks: action.payload, error: null };
    case 'FETCH_ERROR':
      return { ...state, loading: false, error: action.payload };
    case 'ADD_TASK': {
      const updatedTasks = [action.payload, ...state.tasks];
      saveTasksToStorage(updatedTasks);
      return { ...state, tasks: updatedTasks };
    }
    case 'UPDATE_TASK': {
      const updatedTasks = state.tasks.map((task) =>
        task.id === action.payload.id ? action.payload : task
      );
      saveTasksToStorage(updatedTasks);
      return { ...state, tasks: updatedTasks };
    }
    case 'DELETE_TASK': {
      const updatedTasks = state.tasks.filter((task) => task.id !== action.payload);
      saveTasksToStorage(updatedTasks);
      return { ...state, tasks: updatedTasks };
    }
    default:
      return state;
  }
}

export const TaskProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [state, dispatch] = useReducer(taskReducer, initialState);

  const loadTasks = useCallback(async () => {
    dispatch({ type: 'FETCH_START' });
    try {
      const data = await fetchTasksAPI();
      dispatch({ type: 'FETCH_SUCCESS', payload: data });
    } catch (err: any) {
      dispatch({ type: 'FETCH_ERROR', payload: err.message || 'An error occurred while loading tasks.' });
    }
  }, []);

  useEffect(() => {
    loadTasks();
  }, [loadTasks]);

  const addTask = useCallback((name: string, description: string) => {
    const newTask: Task = {
      id: crypto.randomUUID ? crypto.randomUUID() : Math.random().toString(36).substring(2, 9),
      name,
      description,
      status: 'todo',
      createdAt: new Date().toISOString(),
    };
    dispatch({ type: 'ADD_TASK', payload: newTask });
  }, []);

  const updateTask = useCallback((task: Task) => {
    dispatch({ type: 'UPDATE_TASK', payload: task });
  }, []);

  const deleteTask = useCallback((id: string) => {
    dispatch({ type: 'DELETE_TASK', payload: id });
  }, []);

  const reloadTasks = useCallback(async () => {
    await loadTasks();
  }, [loadTasks]);

  return (
    <TaskContext.Provider value={{ state, addTask, updateTask, deleteTask, reloadTasks }}>
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
