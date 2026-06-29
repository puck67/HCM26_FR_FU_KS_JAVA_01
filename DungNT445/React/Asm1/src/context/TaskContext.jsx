import React, { createContext, useContext, useReducer, useEffect } from 'react';
import { taskReducer, initialTaskState } from '../state/taskReducer';

const TaskContext = createContext(undefined);

export const useTasks = () => {
  const context = useContext(TaskContext);
  if (!context) {
    throw new Error('useTasks must be used within a TaskProvider');
  }
  return context;
};

export const TaskProvider = ({ children }) => {
  const [state, dispatch] = useReducer(taskReducer, initialTaskState);

  // Load initial tasks from API or LocalStorage
  const fetchTasks = async () => {
    try {
      const cached = localStorage.getItem('react_assignment_tasks');
      if (cached) {
        const parsedTasks = JSON.parse(cached);
        dispatch({ type: 'SET_TASKS', payload: parsedTasks });
        return;
      }

      // Fetch from public REST API
      const response = await fetch('https://jsonplaceholder.typicode.com/todos?_limit=8');
      if (!response.ok) {
        throw new Error('Failed to fetch tasks from REST API');
      }
      
      const data = await response.json();
      const mappedTasks = data.map((item) => ({
        id: String(item.id),
        name: item.title.charAt(0).toUpperCase() + item.title.slice(1).substring(0, 39),
        description: `This is a task loaded from JSONPlaceholder API. It simulates the details of item #${item.id} with some auto-generated description text.`,
        completed: item.completed,
        priority: ['low', 'medium', 'high'][item.id % 3],
        createdAt: new Date(Date.now() - item.id * 2 * 3600000).toISOString(),
      }));

      // Cache in local storage
      localStorage.setItem('react_assignment_tasks', JSON.stringify(mappedTasks));
      dispatch({ type: 'SET_TASKS', payload: mappedTasks });
    } catch (err) {
      dispatch({ type: 'SET_ERROR', payload: err.message || 'Failed to fetch tasks from the server.' });
    }
  };

  useEffect(() => {
    // Simulate slight network delay
    const timer = setTimeout(() => {
      fetchTasks();
    }, 1000);
    return () => clearTimeout(timer);
  }, []);

  // Save to localStorage when tasks state changes
  useEffect(() => {
    if (!state.loading && state.tasks.length > 0) {
      localStorage.setItem('react_assignment_tasks', JSON.stringify(state.tasks));
    }
  }, [state.tasks, state.loading]);

  const addTask = (name, description, priority) => {
    const newTask = {
      id: Math.random().toString(36).substring(2, 9),
      name,
      description,
      completed: false,
      priority,
      createdAt: new Date().toISOString(),
    };
    dispatch({ type: 'ADD_TASK', payload: newTask });
    
    // Save to local storage manually to cover empty list transition
    const currentTasks = JSON.parse(localStorage.getItem('react_assignment_tasks') || '[]');
    localStorage.setItem('react_assignment_tasks', JSON.stringify([newTask, ...currentTasks]));
  };

  const updateTask = (updatedTask) => {
    dispatch({ type: 'UPDATE_TASK', payload: updatedTask });
  };

  const deleteTask = (id) => {
    dispatch({ type: 'DELETE_TASK', payload: id });
    // Update local storage manually for deletion
    const currentTasks = JSON.parse(localStorage.getItem('react_assignment_tasks') || '[]');
    const filtered = currentTasks.filter((t) => t.id !== id);
    localStorage.setItem('react_assignment_tasks', JSON.stringify(filtered));
  };

  const toggleTaskStatus = (id) => {
    dispatch({ type: 'TOGGLE_TASK_STATUS', payload: id });
  };

  const refetchTasks = async () => {
    localStorage.removeItem('react_assignment_tasks');
    await fetchTasks();
  };

  return (
    <TaskContext.Provider
      value={{
        tasks: state.tasks,
        loading: state.loading,
        error: state.error,
        addTask,
        updateTask,
        deleteTask,
        toggleTaskStatus,
        refetchTasks,
      }}
    >
      {children}
    </TaskContext.Provider>
  );
};
