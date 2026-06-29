export const API_BASE_URL = 'https://jsonplaceholder.typicode.com';
export const TODOS_ENDPOINT = `${API_BASE_URL}/todos`;

export const DEFAULT_TASK_LIMIT = 20;

export const STALE_TIME_MS = 1000 * 60 * 5;

export const QUERY_KEYS = {
  TASKS: ['tasks'] as const,
  TASK: (id: number) => ['tasks', id] as const,
} as const;
