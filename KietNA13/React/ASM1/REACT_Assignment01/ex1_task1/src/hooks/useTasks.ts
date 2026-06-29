import { useQuery } from '@tanstack/react-query';
import axios from 'axios';
import { QUERY_KEYS, STALE_TIME_MS, TODOS_ENDPOINT, DEFAULT_TASK_LIMIT } from '../constants/api';
import { mapTodosToTasks } from '../utils/taskMapper';
import type { JsonPlaceholderTodo, Task } from '../types/task';

const fetchTasks = async (): Promise<Task[]> => {
  const { data } = await axios.get<JsonPlaceholderTodo[]>(
    `${TODOS_ENDPOINT}?_limit=${DEFAULT_TASK_LIMIT}`,
  );
  return mapTodosToTasks(data);
};

export const useTasks = () => {
  return useQuery<Task[], Error>({
    queryKey: QUERY_KEYS.TASKS,
    queryFn: fetchTasks,
    staleTime: STALE_TIME_MS,
  });
};
