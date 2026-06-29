# React Task Dashboard — Assignment 02

**Assignment:** REACT_Assignment02_Op1  
**Duration:** 180 minutes  
**Tech Stack:** Next.js 14 (App Router) + TypeScript + Tailwind CSS + Formik + Yup

---

## Project Structure

```
ex1_task1/
├── src/
│   ├── app/
│   │   ├── layout.tsx              # Root layout with Navbar
│   │   ├── page.tsx                # Home page (Server Component)
│   │   ├── not-found.tsx           # 404 page
│   │   ├── globals.css
│   │   └── tasks/
│   │       ├── page.tsx            # Task List (Server Component, force-dynamic)
│   │       ├── new/
│   │       │   └── page.tsx        # Create Task (Client Component + Formik)
│   │       └── [id]/
│   │           └── page.tsx        # Task Detail (Server + Client mix)
│   ├── components/
│   │   ├── Navbar.tsx              # Server Component
│   │   ├── TaskActions.tsx         # Client Component (delete, toggle, edit)
│   │   └── EditTaskForm.tsx        # Client Component (Formik edit form)
│   ├── lib/
│   │   ├── store.ts                # In-memory data store (global singleton)
│   │   └── actions.ts              # Server Actions (create, update, delete, toggle)
│   └── types/
│       └── task.ts
├── package.json
├── next.config.mjs
├── tailwind.config.ts
└── tsconfig.json
```

## Getting Started

```bash
# Install dependencies
npm install

# Run development server
npm run dev

# Build for production
npm run build && npm start
```

Open [http://localhost:3000](http://localhost:3000).

## Features

- ✅ Next.js 14 App Router with file-based routing
- ✅ Server Components for data fetching (Home, Task List, Task Detail)
- ✅ Client Components where needed (forms, interactive buttons)
- ✅ Server Actions: `createTask`, `updateTask`, `deleteTask`, `toggleTaskCompleted`
- ✅ `revalidatePath` after every mutation for fresh data
- ✅ `force-dynamic` on `/tasks` page (no-store equivalent)
- ✅ Formik + Yup validation on Create and Edit forms
  - `name`: required, max 40 characters
  - `description`: optional, max 200 characters
- ✅ Completed tasks visually distinct (line-through, green background, badge)
- ✅ Tailwind CSS responsive UI

## Data Source

In-memory global store (`src/lib/store.ts`). Data persists during the server runtime
and resets on restart. Seeded with 4 sample tasks on startup.
