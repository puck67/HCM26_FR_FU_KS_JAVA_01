import { useState } from 'react';
import { CrudTable } from './components/CrudTable';
import { CrudActionBar } from './components/CrudActionBar';
import './App.css';

type Task = {
  id: number;
  title: string;
  status: string;
};

function App() {
  const [tasks, setTasks] = useState<Task[]>([
    { id: 1, title: 'Learn React', status: 'Pending' },
    { id: 2, title: 'Build CRUD', status: 'In Progress' },
  ]);

  return (
    <div style={{ padding: '20px' }}>
      <h1>Task Management</h1>
      
      <div style={{ marginBottom: '20px' }}>
        <CrudActionBar
          onAdd={() => {
            const title = window.prompt("Enter new task title:");
            if (title) setTasks([...tasks, { id: Date.now(), title, status: 'Pending' }]);
          }}
          onImport={() => alert("Import feature coming soon!")}
          onExport={() => alert("Export feature coming soon!")}
        />
      </div>

      <CrudTable
        data={tasks}
        onEdit={(task) => {
          const newTitle = window.prompt("Edit task title:", task.title);
          if (newTitle) setTasks(tasks.map(t => t.id === task.id ? { ...t, title: newTitle } : t));
        }}
        onDelete={(task) => {
          if (window.confirm(`Are you sure you want to delete "${task.title}"?`)) {
            setTasks(tasks.filter(t => t.id !== task.id));
          }
        }}
        extraActions={(task) => [
          {
            title: "Complete",
            action: () => {
              setTasks(tasks.map(t => t.id === task.id ? { ...t, status: 'Done' } : t));
            },
            style: "text-green-500"
          }
        ]}
      />
    </div>
  );
}

export default App;
