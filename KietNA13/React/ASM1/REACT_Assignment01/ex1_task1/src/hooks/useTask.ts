import { useQuery } from '@tanstack/react-query';
import axios from 'axios';
import { QUERY_KEYS, STALE_TIME_MS, TODOS_ENDPOINT } from '../constants/api';
import { mapTodoToTask } from '../utils/taskMapper';
import type { JsonPlaceholderTodo, Task } from '../types/task';

const fetchTask = async (id: number): Promise<Task> => {
  const { data } = await axios.get<JsonPlaceholderTodo>(`${TODOS_ENDPOINT}/${id}`);
  return mapTodoToTask(data);
};

export const useTask = (id: number) => {
  return useQuery<Task, Error>({
    queryKey: QUERY_KEYS.TASK(id),
    queryFn: () => fetchTask(id),
    staleTime: STALE_TIME_MS,
    enabled: id > 0,
  });
};
