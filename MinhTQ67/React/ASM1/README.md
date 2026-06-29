# React Task Dashboard

**Assignment:** REACT_Assignment01_Op1  
**Duration:** 180 minutes  
**Tech Stack:** React 18 + TypeScript + Vite + Tailwind CSS + Formik + React Router v6

---

## Project Structure

```
ex1_task1/
├── src/
│   ├── components/       # Reusable UI components
│   │   ├── Navbar.tsx
│   │   ├── TaskCard.tsx
│   │   ├── TaskForm.tsx  (Formik form with Yup validation)
│   │   ├── LoadingSpinner.tsx
│   │   └── ErrorDisplay.tsx
│   ├── hooks/
│   │   └── useTasks.ts   (custom hook wrapping useReducer + useEffect)
│   ├── pages/
│   │   ├── HomePage.tsx
│   │   ├── TasksPage.tsx
│   │   ├── TaskDetailPage.tsx
│   │   ├── CreateTaskPage.tsx
│   │   └── EditTaskPage.tsx
│   ├── reducers/
│   │   └── taskReducer.ts  (useReducer: SET, ADD, UPDATE, DELETE)
│   ├── services/
│   │   └── taskService.ts  (REST API calls to JSONPlaceholder)
│   ├── types/
│   │   └── task.ts
│   ├── App.tsx
│   ├── main.tsx
│   └── index.css
├── index.html
├── package.json
├── tailwind.config.js
├── tsconfig.json
└── vite.config.ts
```

## Getting Started

```bash
# Install dependencies
npm install

# Start development server
npm run dev

# Build for production
npm run build
```

## Features

- ✅ React + TypeScript project via Vite
- ✅ React Router DOM v6 (Home, Tasks, Task Detail, Create, Edit)
- ✅ `useEffect` to fetch tasks from JSONPlaceholder REST API
- ✅ `useState` for UI controls (search, filter, loading states)
- ✅ `useReducer` for task list state management (add, update, delete)
- ✅ Formik + Yup for form handling and validation
  - `name`: required, max 40 characters
  - `description`: optional, max 200 characters
- ✅ Loading and error states on all data-fetching pages
- ✅ Tailwind CSS responsive UI

## API

Uses [JSONPlaceholder](https://jsonplaceholder.typicode.com/todos) as a mock REST API.
