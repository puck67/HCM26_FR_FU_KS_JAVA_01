# TaskSphere — Next.js App Router Task Dashboard (TS + Server Actions)

This project is a React-based Task Dashboard application built with **Next.js 15+ (App Router)**, **TypeScript**, **Tailwind CSS v4**, and **Formik**. It implements the requirements of the FSOFT `REACT_Assignment02_Op1` exam.

---

## 🚀 Key Features

1. **Next.js App Router & Server Components**:
   - Uses file-based App Router structure.
   - Server-side rendering (SSR) for loading initial layouts and task lists, optimizing performance.
   - Implements hybrid client-server rendering model: loading task metadata on the server and handing forms to the client.
2. **Next.js Server Actions**:
   - Secure server-side CRUD executions marked with `"use server"`.
   - Core server actions implemented:
     - `createTask(formData)`: Adds a new task.
     - `updateTask(id, formData)`: Modifies task properties.
     - `deleteTask(id)`: Removes a task.
     - `toggleTaskCompleted(id)`: Inverts the task's completion status.
   - Uses `revalidatePath` to trigger page updates automatically across routes upon data changes.
3. **Data Caching & Revalidation**:
   - Dynamic pages use `export const dynamic = 'force-dynamic'` and `revalidatePath` to ensure tasks are always up-to-date.
4. **Form Management & Validations**:
   - Form operations (Task Create and Edit) are implemented using **Formik**.
   - Input validations:
     - **Task Name**: Required, maximum of 40 characters.
     - **Description**: Optional, maximum of 200 characters.
   - Validation warning messages are rendered dynamically in real-time, accompanied by live character counters.
5. **Glassmorphic Theme & Responsive Layout**:
   - Styled with Tailwind CSS v4 and Google Fonts (`Plus Jakarta Sans`).
   - Vibrant cosmic backdrop gradients, card glassmorphic panels, transition hover animations, and Lucide React icons.
   - Completed tasks are visually distinguished with a dimmed opacity, text line-through, and an emerald checkbox indicator.

---

## 🛠️ Project Structure

The project has been packaged according to the exam requirements:
```
REACT_Assignment02/
└── ex1_task1/
    ├── src/
    │   ├── app/               # Next.js App Router Pages
    │   │   ├── tasks/
    │   │   │   ├── [id]/
    │   │   │   │   └── page.tsx      # Task Detail Page (Server Component)
    │   │   │   ├── new/
    │   │   │   │   └── page.tsx      # Task Create Page (Client Component)
    │   │   │   └── page.tsx          # Task Dashboard List Page (Server Component)
    │   │   ├── actions.ts     # Next.js Server Actions
    │   │   ├── globals.css    # Custom scrollbars, glass styles, and tailwind
    │   │   ├── layout.tsx     # RootLayout with Navbar and Footer
    │   │   └── page.tsx       # Landing Home Page (Server Component)
    │   ├── components/        # Reusable UI widgets
    │   │   ├── Navbar.tsx           # Global glassmorphic menu
    │   │   ├── TaskCard.tsx         # Task item visualizer using Server Actions
    │   │   └── TaskEditForm.tsx     # Formik edit validation form
    │   ├── lib/
    │   │   └── tasks.ts       # Server file read/write database layer
    │   ├── types/
    │   │   └── index.ts       # Typescript interfaces
    │   └── ...
    ├── package.json
    ├── tsconfig.json
    └── ...
```

---

## ⚙️ Running Locally

Follow these steps to run the application on your computer:

### 1. Install Dependencies
Change directory to the project folder and install dependencies:
```bash
cd REACT_Assignment02/ex1_task1
npm install
```

### 2. Start Development Server
Run the local development server:
```bash
npm run dev
```
Open your browser and navigate to the local address (typically `http://localhost:3000`).

### 3. Build for Production
To build the optimized static asset bundle:
```bash
npm run build
```

### 4. Run Production Build
Start the production server:
```bash
npm run start
```

---

## 📦 Submission Package

The complete project source code (excluding `node_modules` and `.next` build caches) has been zipped and prepared at:
`E:\React\HaoNC6_REACT_Assignment02.zip`
You can submit this file directly for evaluation.
