import { useNavigate, Link } from 'react-router-dom';
import { useTasks } from '../hooks/useTasks';
import TaskForm from '../components/TaskForm';
import { TaskFormValues } from '../types/task';

export default function CreateTaskPage() {
  const navigate = useNavigate();
  const { addTask } = useTasks();

  const handleSubmit = async (values: TaskFormValues) => {
    const newTask = await addTask(values);
    navigate(`/tasks/${newTask.id}`);
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-lg mx-auto px-4 py-8">
        {/* Breadcrumb */}
        <nav className="flex items-center gap-2 text-sm text-gray-500 mb-6">
          <Link to="/tasks" className="hover:text-indigo-600 transition-colors">Tasks</Link>
          <span>/</span>
          <span className="text-gray-800 font-medium">New Task</span>
        </nav>

        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
          <div className="mb-6">
            <h1 className="text-xl font-bold text-gray-900">Create New Task</h1>
            <p className="text-gray-500 text-sm mt-1">Fill in the details below to add a new task.</p>
          </div>

          <TaskForm onSubmit={handleSubmit} submitLabel="Create Task" />
        </div>
      </div>
    </div>
  );
}
