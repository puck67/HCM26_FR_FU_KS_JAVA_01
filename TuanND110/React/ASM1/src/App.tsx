import React from 'react';
import { TaskProvider } from './context/TaskContext.tsx';
import { AppRouter } from './routes/AppRouter.tsx';

const App: React.FC = () => {
  return (
    <TaskProvider>
      <AppRouter />
    </TaskProvider>
  );
};

export default App;
