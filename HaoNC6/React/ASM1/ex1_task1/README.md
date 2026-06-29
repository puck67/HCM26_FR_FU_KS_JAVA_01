# TaskSphere — React Task Dashboard (Vite + TS + Hooks)

This project is a React-based Task Dashboard application built with **React 19**, **TypeScript**, **Vite 5**, and **Tailwind CSS v4**. It implements the requirements of the FSOFT `REACT_Assignment01_Op1` exam.

## 🚀 Key Features

1. **State Management**:
   - Built a robust centralized state management system using React's `useReducer` and `createContext` API in `TaskContext.tsx`.
   - Exposes global state (`tasks`, `loading`, `error`) and actions (`addTask`, `updateTask`, `deleteTask`, `reloadTasks`) cleanly.
2. **REST API Simulation**:
   - Created a mock REST API database wrapper `mockDb.ts` that persists data in `localStorage`.
   - Simulates async REST API calls with **1-second network latency** to demonstrate skeleton loading states.
   - Simulates a **5% random failure rate** to demonstrate error handling and retry UI mechanisms.
3. **Form Management & Validations**:
   - Implemented Create and Edit operations using **Formik**.
   - Form fields include **Task Name** (required, max 40 characters) and **Description** (optional, max 200 characters).
   - Validation warning messages are rendered dynamically in real-time, accompanied by live character counters.
4. **Client-Side Routing**:
   - Configured routing with **React Router v7** (`react-router-dom`).
   - Routes:
     - `/` (Home landing page, displaying task statistics, completion rates, and recent activities).
     - `/tasks` (Tasks dashboard featuring search filtering, status tab navigation, inline actions, and card grid).
     - `/tasks/:id` (Detailed task information, timestamp metadata, status details, inline editing, and deletion support).
5. **Glassmorphic Theme**:
   - Designed a responsive dark theme using Tailwind CSS v4 and Google Fonts (`Plus Jakarta Sans`).
   - Utilizes card backdrop filters, border gradients, transition micro-animations, and Lucide React icons.

---

## 🛠️ Project Structure

The project has been packaged according to the exam requirements:
```
REACT_Assignment01/
└── ex1_task1/
    ├── src/
    │   ├── assets/            # Static assets and icons
    │   ├── components/        # Reusable UI widgets
    │   │   ├── Alert.tsx            # Error warning component
    │   │   ├── Navbar.tsx           # Global glassmorphic menu
    │   │   ├── SkeletonLoader.tsx   # Pulse animations for load states
    │   │   ├── TaskCard.tsx         # Task item visualizer
    │   │   └── TaskForm.tsx         # Formik validation form
    │   ├── context/           # React context state wrappers
    │   │   └── TaskContext.tsx      # useReducer and state provider
    │   ├── pages/             # Route layout pages
    │   │   ├── Home.tsx             # Statistics overview
    │   │   ├── TaskDetail.tsx       # Detail inspect & inline edits
    │   │   └── Tasks.tsx            # Card grid, search & modals
    │   ├── mockDb.ts          # Simulates REST API + LocalStorage
    │   ├── types.ts           # Typescript interfaces
    │   ├── index.css          # Custom scrollbars, glass styles
    │   └── main.tsx           # Mount point
    ├── index.html
    ├── package.json
    ├── tsconfig.json
    └── vite.config.ts
```

---

## ⚙️ Running Locally

Follow these steps to run the application on your computer:

### 1. Install Dependencies
Change directory to the project folder and install dependencies:
```bash
cd REACT_Assignment01/ex1_task1
npm install
```

### 2. Start Development Server
Run the local development server:
```bash
npm run dev
```
Open your browser and navigate to the local address (typically `http://localhost:5173`).

### 3. Build for Production
To build the optimized static asset bundle:
```bash
npm run build
```

---

## 📦 Submission Package

As requested, the complete project source code (excluding `node_modules` and `dist` build cache) has been zipped and prepared at:
`E:\React\HaoNC6_REACT_Assignment01.zip`
You can submit this file directly for evaluation.
