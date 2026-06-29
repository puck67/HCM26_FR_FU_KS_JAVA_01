# React Task Dashboard

Project thực hành React + TypeScript + Vite theo đề bài Assignment 01.

## Cách chạy dự án

```bash
# Bước 1: Di chuyển vào thư mục dự án
cd ex1_task1

# Bước 2: Cài dependencies
npm install

# Bước 3: Chạy dev server
npm run dev
```

Mở trình duyệt tại `http://localhost:5173`

## Cấu trúc thư mục

```
src/
├── types/
│   └── task.ts           # Định nghĩa kiểu Task
├── reducers/
│   └── taskReducer.ts    # useReducer: add, update, delete, toggle
├── hooks/
│   └── useTasks.ts       # Custom hook quản lý state + fetch API
├── components/
│   ├── Navbar.tsx         # Thanh điều hướng
│   └── TaskForm.tsx       # Form dùng Formik + Yup validation
├── pages/
│   ├── Home.tsx           # Trang chủ
│   ├── Tasks.tsx          # Danh sách tasks
│   └── TaskDetail.tsx     # Chi tiết + chỉnh sửa task
├── App.tsx                # Routing với React Router DOM
└── main.tsx               # Entry point
```

## Các công nghệ sử dụng

- **Vite** - Build tool nhanh
- **React 18** + **TypeScript** - UI framework
- **React Router DOM v6** - Client-side routing
- **Tailwind CSS** - Utility-first CSS
- **Formik** + **Yup** - Form handling & validation
- **JSONPlaceholder API** - Mock REST API

## Các tính năng

- ✅ Fetch danh sách tasks từ REST API (`useEffect`)
- ✅ Quản lý state bằng `useReducer` (thêm, sửa, xóa, toggle)
- ✅ `useState` điều khiển UI (show/hide form, edit mode)
- ✅ Form validation với Formik + Yup
- ✅ Responsive UI với Tailwind CSS
- ✅ Loading state và Error state
- ✅ 3 trang: Home, Tasks, Task Detail
