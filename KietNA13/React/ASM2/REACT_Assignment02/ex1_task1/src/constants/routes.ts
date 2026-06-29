export const ROUTES = {
  HOME: '/',
  TASKS: '/tasks',
  NEW_TASK: '/tasks/new',
  TASK_DETAIL: (id: string) => `/tasks/${id}`,
} as const;
