# React Task Dashboard (TaskFlow)

REACT_Assignment01 - Op1. Vite + React + TypeScript task dashboard with full CRUD, built with React Router, Formik + Yup validation, and Tailwind CSS.

## Tech stack

- **Vite** + **React 19** + **TypeScript**
- **React Router DOM v7** - routing (Home, Tasks, Task Detail, Create/Edit)
- **Formik + Yup** - form handling and validation
- **Tailwind CSS v4** - styling, responsive

## Getting started

```bash
npm install
npm run dev        # start dev server
npm run build      # type-check + production build
npm run preview    # preview production build
```

## Requirements coverage

| Requirement | Where |
|---|---|
| `useState` for UI controls | `pages/Tasks.tsx` (filter, deletingId), `Layout.tsx` (mobile menu) |
| `useReducer` for task data (add/update/delete) | `taskReducer.ts` + `pages/Tasks.tsx` |
| `useEffect` to load data | `pages/Tasks.tsx`, `pages/TaskDetail.tsx`, `pages/TaskForm.tsx` |
| REST API (mock) | `api.ts` - async service with simulated latency, persisted to localStorage |
| Loading + error states | `pages/Tasks.tsx`, `pages/TaskDetail.tsx` (skeletons + error UI) |
| Task List page | `pages/Tasks.tsx` |
| Task Detail page | `pages/TaskDetail.tsx` |
| Formik form (name required ≤40, description optional ≤200) | `pages/TaskForm.tsx` |
| Tailwind responsive UI | all pages |

## Pages / routes

- `/` - Home (landing)
- `/tasks` - Task list with status filter
- `/tasks/:id` - Task detail
- `/tasks/new` - Create task
- `/tasks/:id/edit` - Edit task

## Mock API

`api.ts` simulates a REST backend: async methods with network delay, data seeded and persisted in `localStorage`. To point at a real REST endpoint, swap the method bodies for `fetch` calls - the component interface stays the same.

## Reducer test

```bash
npx tsx src/taskReducer.test.ts
```
