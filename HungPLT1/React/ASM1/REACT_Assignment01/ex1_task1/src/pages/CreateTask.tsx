import React from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useTasks } from '../context/TaskContext';
import { TaskForm } from '../components/TaskForm';
import { ArrowLeft } from 'lucide-react';
import type { TaskStatus } from '../types';

export const CreateTask: React.FC = () => {
  const navigate = useNavigate();
  const { addTask } = useTasks();

  const handleSubmit = async (values: { name: string; description: string; status: TaskStatus }) => {
    try {
      await addTask(values.name, values.description, values.status);
      navigate('/tasks');
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      <div className="flex items-center">
        <Link
          to="/tasks"
          className="inline-flex items-center gap-2 text-xs text-slate-400 hover:text-slate-200 font-semibold transition-colors"
        >
          <ArrowLeft className="w-4 h-4" /> Back to tasks
        </Link>
      </div>

      <div className="bg-slate-900 border border-slate-800 rounded-2xl p-6 md:p-8 shadow-xl">
        <div className="mb-6">
          <h1 className="text-2xl font-bold text-slate-100 mb-1">Create New Task</h1>
          <p className="text-slate-450 text-xs">Fill out the form below to create a new task tracking entry.</p>
        </div>

        <TaskForm
          onSubmit={handleSubmit}
          onCancel={() => navigate('/tasks')}
          submitButtonText="Create Task"
        />
      </div>
    </div>
  );
};
