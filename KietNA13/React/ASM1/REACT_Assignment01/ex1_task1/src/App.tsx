import { lazy, Suspense } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Toaster } from 'react-hot-toast';
import { Navbar } from './components/Navbar';
import { PageSkeleton } from './components/PageSkeleton';
import { ErrorBoundary } from './components/ErrorBoundary';
import { TaskProvider } from './context/TaskContext';
import { ROUTES } from './constants/routes';
import { STALE_TIME_MS } from './constants/api';

const HomePage = lazy(() => import('./pages/HomePage'));
const TasksPage = lazy(() => import('./pages/TasksPage'));
const TaskDetailPage = lazy(() => import('./pages/TaskDetailPage'));
const TaskFormPage = lazy(() => import('./pages/TaskFormPage'));
const NotFoundPage = lazy(() => import('./pages/NotFoundPage'));

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: STALE_TIME_MS,
      retry: 1,
    },
  },
});

const App = () => (
  <QueryClientProvider client={queryClient}>
    <TaskProvider>
      <BrowserRouter>
        <div className="min-h-screen bg-gray-50 font-sans">
          <Navbar />
          <Suspense fallback={<PageSkeleton />}>
            <Routes>
              <Route
                path={ROUTES.HOME}
                element={
                  <ErrorBoundary>
                    <HomePage />
                  </ErrorBoundary>
                }
              />
              <Route
                path={ROUTES.TASKS}
                element={
                  <ErrorBoundary>
                    <TasksPage />
                  </ErrorBoundary>
                }
              />
              <Route
                path={ROUTES.TASK_NEW}
                element={
                  <ErrorBoundary>
                    <TaskFormPage />
                  </ErrorBoundary>
                }
              />
              <Route
                path="/tasks/:id"
                element={
                  <ErrorBoundary>
                    <TaskDetailPage />
                  </ErrorBoundary>
                }
              />
              <Route
                path="/tasks/:id/edit"
                element={
                  <ErrorBoundary>
                    <TaskFormPage />
                  </ErrorBoundary>
                }
              />
              <Route
                path="*"
                element={
                  <ErrorBoundary>
                    <NotFoundPage />
                  </ErrorBoundary>
                }
              />
            </Routes>
          </Suspense>
          <Toaster
            position="bottom-right"
            toastOptions={{
              duration: 3000,
              style: {
                borderRadius: '10px',
                background: '#1f2937',
                color: '#f9fafb',
                fontSize: '14px',
              },
              success: {
                iconTheme: { primary: '#10b981', secondary: '#f9fafb' },
              },
              error: {
                iconTheme: { primary: '#ef4444', secondary: '#f9fafb' },
              },
            }}
          />
        </div>
      </BrowserRouter>
    </TaskProvider>
  </QueryClientProvider>
);

export default App;
