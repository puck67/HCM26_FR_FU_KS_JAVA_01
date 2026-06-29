import { useReducer, useEffect, useState } from 'react';
import { taskReducer, initialTaskState } from '../reducers/taskReducer';
import { fetchTasks, createTask, updateTask, deleteTask } from '../services/taskService';
import { Task, TaskFormValues } from '../types/task';

export function useTasks() {
  const [state, dispatch] = useReducer(taskReducer, initialTaskState);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadTasks = async () => {
      setLoading(true);
      setError(null);
      try {
        const tasks = await fetchTasks(20);
        dispatch({ type: 'SET_TASKS', payload: tasks });
      } catch (err) {
        setError(err instanceof Error ? err.message : 'An error occurred');
      } finally {
        setLoading(false);
      }
    };

    loadTasks();
  }, []);

  const addTask = async (values: TaskFormValues): Promise<Task> => {
    const newTask = await createTask(values.name, values.description);
    dispatch({ type: 'ADD_TASK', payload: newTask });
    return newTask;
  };

  const editTask = async (task: Task, values: TaskFormValues): Promise<void> => {
    const updated: Task = { ...task, name: values.name, description: values.description };
    await updateTask(updated);
    dispatch({ type: 'UPDATE_TASK', payload: updated });
  };

  const removeTask = async (id: number): Promise<void> => {
    await deleteTask(id);
    dispatch({ type: 'DELETE_TASK', payload: id });
  };

  return {
    tasks: state.tasks,
    loading,
    error,
    addTask,
    editTask,
    removeTask,
  };
}
