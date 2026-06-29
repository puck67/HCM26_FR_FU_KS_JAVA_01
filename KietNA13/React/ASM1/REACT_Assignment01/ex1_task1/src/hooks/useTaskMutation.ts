import { useMutation, useQueryClient } from '@tanstack/react-query';
import axios from 'axios';
import toast from 'react-hot-toast';
import { QUERY_KEYS, TODOS_ENDPOINT } from '../constants/api';
import { useTaskContext } from '../context/TaskContext';
import type { Task, TaskFormValues } from '../types/task';

interface MutationContext {
  previousTasks: Task[] | undefined;
}

const createTaskApi = async (values: TaskFormValues): Promise<Task> => {
  const { data } = await axios.post<{ id: number }>(TODOS_ENDPOINT, {
    title: values.name,
    completed: false,
    userId: 1,
  });
  return {
    id: data.id,
    name: values.name,
    description: values.description || undefined,
    completed: false,
    userId: 1,
  };
};

const updateTaskApi = async (task: Task): Promise<Task> => {
  await axios.put(`${TODOS_ENDPOINT}/${task.id}`, {
    title: task.name,
    completed: task.completed,
    userId: task.userId,
  });
  return task;
};

const deleteTaskApi = async (id: number): Promise<number> => {
  await axios.delete(`${TODOS_ENDPOINT}/${id}`);
  return id;
};

export const useTaskMutation = () => {
  const { dispatch } = useTaskContext();
  const queryClient = useQueryClient();

  const createMutation = useMutation<Task, Error, TaskFormValues>({
    mutationFn: createTaskApi,
    onSuccess: (newTask) => {
      dispatch({ type: 'ADD_TASK', payload: newTask });
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.TASKS });
      toast.success('Task created successfully!');
    },
    onError: () => {
      toast.error('Failed to create task. Please try again.');
    },
  });

  const updateMutation = useMutation<Task, Error, Task, MutationContext>({
    mutationFn: updateTaskApi,
    onMutate: async (updatedTask) => {
      await queryClient.cancelQueries({ queryKey: QUERY_KEYS.TASKS });
      const previousTasks = queryClient.getQueryData<Task[]>(QUERY_KEYS.TASKS);
      dispatch({ type: 'UPDATE_TASK', payload: updatedTask });
      return { previousTasks };
    },
    onError: (_err, _updatedTask, context) => {
      if (context?.previousTasks) {
        dispatch({ type: 'SET_TASKS', payload: context.previousTasks });
      }
      toast.error('Failed to update task. Please try again.');
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.TASKS });
      toast.success('Task updated successfully!');
    },
  });

  const deleteMutation = useMutation<number, Error, number, MutationContext>({
    mutationFn: deleteTaskApi,
    onMutate: async (id) => {
      await queryClient.cancelQueries({ queryKey: QUERY_KEYS.TASKS });
      const previousTasks = queryClient.getQueryData<Task[]>(QUERY_KEYS.TASKS);
      dispatch({ type: 'DELETE_TASK', payload: id });
      return { previousTasks };
    },
    onError: (_err, _id, context) => {
      if (context?.previousTasks) {
        dispatch({ type: 'SET_TASKS', payload: context.previousTasks });
      }
      toast.error('Failed to delete task. Please try again.');
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.TASKS });
      toast.success('Task deleted successfully!');
    },
  });

  return { createMutation, updateMutation, deleteMutation };
};
