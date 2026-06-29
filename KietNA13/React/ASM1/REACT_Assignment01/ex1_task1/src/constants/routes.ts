export const ROUTES = {
  HOME: '/',
  TASKS: '/tasks',
  TASK_NEW: '/tasks/new',
  TASK_DETAIL: (id: number | string) => `/tasks/${id}`,
  TASK_EDIT: (id: number | string) => `/tasks/${id}/edit`,
} as const;
