import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { TaskProvider } from './context/TaskProvider';
import { Layout } from './components/layout/Layout';
import { Home } from './pages/Home';
import { TaskList } from './pages/TaskList';
import { TaskDetail } from './pages/TaskDetail';

export const App: React.FC = () => {
  return (
    <TaskProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Layout />}>
            <Route index element={<Home />} />
            <Route path="tasks" element={<TaskList />} />
            <Route path="tasks/:id" element={<TaskDetail />} />
            <Route path="*" element={<Navigate to="/" replace />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </TaskProvider>
  );
};

export default App;
