import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AppLayout } from '../components/layout/AppLayout';
import { HomePage } from '../pages/HomePage';
import { TaskListPage } from '../pages/TaskListPage';
import { TaskNewPage } from '../pages/TaskNewPage';
import { TaskDetailPage } from '../pages/TaskDetailPage';

/**
 * AppRouter — defines all application routes.
 *
 * Routes:
 *  /           → HomePage (Home dashboard)
 *  /tasks      → TaskListPage (list all tasks)
 *  /tasks/new  → TaskNewPage (create task form)
 *  /tasks/:id  → TaskDetailPage (view/edit a specific task)
 *  *           → redirects to /
 */
export const AppRouter: React.FC = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<AppLayout />}>
          <Route index element={<HomePage />} />
          <Route path="tasks" element={<TaskListPage />} />
          <Route path="tasks/new" element={<TaskNewPage />} />
          <Route path="tasks/:id" element={<TaskDetailPage />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

// Router configured.
