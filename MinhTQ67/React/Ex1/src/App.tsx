import { useState } from 'react';
import Layout from './components/Layout';
import DashboardPage from './pages/DashboardPage';
import TaskPage from './pages/TaskPage';
import StudentPage from './pages/StudentPage';
import TeacherPage from './pages/TeacherPage';
import ProductPage from './pages/ProductPage';
import UserPage from './pages/UserPage';
import './index.css';

function App() {
  const [activeTab, setActiveTab] = useState('dashboard');

  const renderContent = () => {
    switch (activeTab) {
      case 'dashboard': return <DashboardPage />;
      case 'tasks': return <TaskPage />;
      case 'students': return <StudentPage />;
      case 'teachers': return <TeacherPage />;
      case 'products': return <ProductPage />;
      case 'users': return <UserPage />;
      default: return <DashboardPage />;
    }
  };

  return (
    <Layout activeTab={activeTab} setActiveTab={setActiveTab}>
      {renderContent()}
    </Layout>
  );
}

export default App;
