import { useEffect, useState } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { fetchTaskById, deleteTask } from '../api/taskApi';
import type { Task } from '../types';
import { ArrowLeft, Edit, Trash2, Calendar, Clock, Loader2, AlertTriangle } from 'lucide-react';

export const TaskDetail = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [task, setTask] = useState<Task | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadTask = async () => {
      if (!id) return;
      try {
        const data = await fetchTaskById(id);
        if (data) {
          setTask(data);
        } else {
          setError('Task not found');
        }
      } catch (err) {
        setError('Failed to load task details.');
      } finally {
        setLoading(false);
      }
    };
    loadTask();
  }, [id]);

  const handleDelete = async () => {
    if (task && window.confirm('Are you sure you want to delete this task?')) {
      try {
        await deleteTask(task.id);
        navigate('/tasks');
      } catch (err) {
        alert('Failed to delete task.');
      }
    }
  };

  if (loading) {
    return (
      <div className="flex flex-col justify-center items-center h-64 gap-3 text-gray-500">
        <Loader2 className="w-10 h-10 animate-spin text-primary" />
        <p className="font-medium animate-pulse">Loading task details...</p>
      </div>
    );
  }

  if (error || !task) {
    return (
      <div className="max-w-3xl mx-auto px-4 mt-10 text-center">
        <div className="bg-red-50 text-red-600 p-8 rounded-2xl flex flex-col items-center gap-4 shadow-sm border border-red-100">
          <AlertTriangle className="w-12 h-12" />
          <h3 className="text-2xl font-bold">{error || 'Task not found'}</h3>
          <Link to="/tasks" className="mt-4 px-6 py-2 bg-red-100 hover:bg-red-200 text-red-700 font-medium rounded-lg transition-colors">
            Back to Tasks
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      <Link to="/tasks" className="inline-flex items-center gap-2 text-sm text-gray-500 hover:text-gray-900 transition-colors mb-8">
        <ArrowLeft className="w-4 h-4" /> Back to tasks
      </Link>
      
      <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
        <div className="p-8 sm:p-10">
          <div className="flex flex-col sm:flex-row justify-between items-start gap-6 mb-8">
            <h1 className="text-3xl sm:text-4xl font-extrabold text-gray-900 leading-tight">
              {task.name}
            </h1>
            <div className="flex items-center gap-3 shrink-0">
              <Link
                to={`/tasks/edit/${task.id}`}
                className="flex items-center gap-2 px-4 py-2 bg-blue-50 text-blue-700 hover:bg-blue-100 font-medium rounded-lg transition-colors"
              >
                <Edit className="w-4 h-4" /> Edit
              </Link>
              <button
                onClick={handleDelete}
                className="flex items-center gap-2 px-4 py-2 bg-red-50 text-red-700 hover:bg-red-100 font-medium rounded-lg transition-colors"
              >
                <Trash2 className="w-4 h-4" /> Delete
              </button>
            </div>
          </div>

          <div className="prose max-w-none mb-10">
            <h3 className="text-sm font-bold text-gray-400 uppercase tracking-wider mb-3">Description</h3>
            {task.description ? (
              <div className="text-gray-700 text-lg whitespace-pre-wrap leading-relaxed bg-gray-50 p-6 rounded-xl border border-gray-100">
                {task.description}
              </div>
            ) : (
              <p className="text-gray-400 italic bg-gray-50 p-6 rounded-xl border border-gray-100">No description provided for this task.</p>
            )}
          </div>

          <div className="border-t border-gray-100 pt-8 flex flex-col sm:flex-row gap-6 text-sm text-gray-500">
            <div className="flex items-center gap-2">
              <Calendar className="w-5 h-5 text-gray-400" />
              <span>Created on <strong className="text-gray-700">{new Date(task.createdAt).toLocaleDateString()}</strong></span>
            </div>
            <div className="flex items-center gap-2">
              <Clock className="w-5 h-5 text-gray-400" />
              <span>Time: <strong className="text-gray-700">{new Date(task.createdAt).toLocaleTimeString()}</strong></span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
