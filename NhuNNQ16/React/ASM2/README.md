# React Task Manager — Assignment 02

Project thực hành **Next.js App Router** + TypeScript + Tailwind CSS + Formik.

## Cách chạy

```bash
cd ex1_task1
npm install
npm run dev
```

Mở trình duyệt tại `http://localhost:3000`

## Cấu trúc thư mục

```
src/
├── types/
│   └── task.ts                  # Interface Task
├── lib/
│   └── store.ts                 # In-memory data store (thay DB)
├── actions/
│   └── taskActions.ts           # Server Actions (createTask, updateTask, deleteTask, toggleTaskCompleted)
├── components/
│   ├── Navbar.tsx               # Server Component - điều hướng
│   ├── TaskFormClient.tsx       # Client Component - form Formik + Yup
│   └── TaskActions.tsx          # Client Component - nút Edit/Delete/Toggle
└── app/                         # File-based routing (App Router)
    ├── layout.tsx               # Root layout
    ├── page.tsx                 # / (Home) - Server Component
    └── tasks/
        ├── page.tsx             # /tasks - Server Component
        ├── new/
        │   └── page.tsx         # /tasks/new - Server + Client Component
        └── [id]/
            └── page.tsx         # /tasks/[id] - Mix Server + Client Component
```

## Kiến trúc Next.js

### Server Components (mặc định)
- `app/page.tsx` — lấy stats từ store
- `app/tasks/page.tsx` — fetch danh sách tasks
- `app/tasks/[id]/page.tsx` — load task theo id

### Client Components (`'use client'`)
- `TaskFormClient.tsx` — Formik cần useState
- `TaskActions.tsx` — nút cần onClick handler

### Server Actions (`'use server'`)
- `createTask(formData)` — thêm task + redirect /tasks
- `updateTask(id, formData)` — sửa task + redirect /tasks
- `deleteTask(id)` — xóa task + revalidatePath
- `toggleTaskCompleted(id)` — toggle + revalidatePath

## Validation (Formik + Yup)
- `name`: bắt buộc, tối đa 40 ký tự
- `description`: tùy chọn, tối đa 200 ký tự
