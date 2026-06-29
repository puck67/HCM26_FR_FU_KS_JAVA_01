# Next.js Task Dashboard (TaskFlow)

REACT_Assignment02 - Op1. Next.js App Router task manager with Server Components, Server Actions, Formik validation, and Tailwind CSS.

## Tech stack

- **Next.js 15** (App Router) + **TypeScript**
- **Server Actions** - createTask, updateTask, deleteTask, toggleTaskCompleted
- **Formik + Yup** - form validation
- **Tailwind CSS v4** - responsive UI

## Getting started

```bash
npm install
npm run dev      # http://localhost:3000
npm run build    # production build
npm start        # run production build
```

## Requirements coverage

| Requirement | Where |
|---|---|
| App Router + file-based routing | `src/app/` |
| `/` Home | `src/app/page.tsx` |
| `/tasks` (Server Component, fetches data) | `src/app/tasks/page.tsx` |
| `/tasks/new` (Client Component + Formik) | `src/app/tasks/new/page.tsx` |
| `/tasks/[id]` (Server loads + Client form) | `src/app/tasks/[id]/page.tsx` + `edit-form.tsx` |
| Server data source (in-memory) | `src/lib/data.ts` |
| `createTask` | `src/lib/actions.ts` |
| `updateTask` | `src/lib/actions.ts` |
| `deleteTask` | `src/lib/actions.ts` |
| `toggleTaskCompleted` | `src/lib/actions.ts` |
| Caching via `revalidatePath` after mutations | `src/lib/actions.ts` |
| Formik validation (name required ≤40, desc ≤200) | `new/page.tsx`, `[id]/edit-form.tsx` |
| Submit calls server action + redirects to /tasks | `createTask`, `updateTask` |
| Edit / Delete / Mark Completed buttons | `src/app/tasks/task-item.tsx` |
| Completed tasks styled differently (color + strikethrough) | `task-item.tsx`, `[id]/page.tsx` |
| Responsive Tailwind layout | all pages |

## Architecture notes

**Server vs Client split:**
- `/tasks` is a Server Component - reads data on the server, no client JS for the list shell.
- `/tasks/[id]` is a Server Component that loads the task, then renders a Client Component (`edit-form.tsx`) for the interactive Formik form.
- `/tasks/new` is a Client Component (Formik needs client state) that calls the `createTask` server action.
- `task-item.tsx` is a Client Component so toggle/delete buttons can call server actions via `useTransition`.

**Caching / revalidation:**
Every mutation calls `revalidatePath("/tasks")` (and the detail path on update) so the Server Component re-fetches fresh data after the change. This is the App Router pattern that replaces `fetch(..., { cache: 'no-store' })` for in-memory data.

**Data source:**
`src/lib/data.ts` holds an in-memory array, seeded on server start. Mutations operate on it directly. To swap for a real REST API, replace the function bodies with `fetch` calls (using `{ cache: 'no-store' }` on reads) - the action and component interfaces stay the same.
