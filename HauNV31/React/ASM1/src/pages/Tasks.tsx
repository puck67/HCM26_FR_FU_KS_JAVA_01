import { useEffect, useReducer } from 'react';
import { Link } from 'react-router-dom';
import { fetchTasks, deleteTask } from '../api/taskApi';
import { initialTaskState, taskReducer } from '../store/taskReducer';
import { Trash2, Edit, Loader2, Calendar, AlertCircle } from 'lucide-react';

export const Tasks = () => {
  const [state, dispatch] = useReducer(taskReducer, initialTaskState);

  const loadTasks = async () => {
    dispatch({ type: 'FETCH_INIT' });
    try {
      const data = await fetchTasks();
      dispatch({ type: 'FETCH_SUCCESS', payload: data });
    } catch (error) {
      dispatch({ type: 'FETCH_FAILURE', payload: 'Failed to load tasks.' });
    }
  };

  useEffect(() => {
    loadTasks();
  }, []);

  const handleDelete = async (e: React.MouseEvent, id: string) => {
    e.preventDefault();
    if (window.confirm('Are you sure you want to delete this task?')) {
      try {
        await deleteTask(id);
        dispatch({ type: 'DELETE_TASK', payload: id });
      } catch (error) {
        alert('Failed to delete task.');
      }
    }
  };

  if (state.loading) {
    return (
      <div className="flex flex-col justify-center items-center h-64 gap-3 text-gray-500">
        <Loader2 className="w-10 h-10 animate-spin text-primary" />
        <p className="font-medium animate-pulse">Loading your tasks...</p>
      </div>
    );
  }

  if (state.error) {
    return (
      <div className="bg-red-50 text-red-600 p-6 rounded-xl flex items-center gap-3 shadow-sm border border-red-100 max-w-3xl mx-auto mt-10">
        <AlertCircle className="w-6 h-6 shrink-0" />
        <div>
          <h3 className="font-bold">Error</h3>
          <p>{state.error}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      <div className="flex justify-between items-end mb-8 border-b border-gray-100 pb-4">
        <div>
          <h1 className="text-3xl font-extrabold text-gray-900 tracking-tight">Your Tasks</h1>
          <p className="text-gray-500 mt-1">Manage and track your ongoing work</p>
        </div>
      </div>

      {state.tasks.length === 0 ? (
        <div className="text-center py-20 bg-white rounded-2xl border border-dashed border-gray-300 shadow-sm">
          <div className="w-16 h-16 bg-gray-50 text-gray-400 rounded-full flex items-center justify-center mx-auto mb-4">
            <Calendar className="w-8 h-8" />
          </div>
          <h3 className="text-xl font-bold text-gray-900">No tasks found</h3>
          <p className="text-gray-500 mt-2">Get started by creating a new task.</p>
          <Link
            to="/tasks/create"
            className="inline-block mt-6 px-6 py-2 bg-primary text-white font-medium rounded-lg hover:bg-blue-600 transition-colors"
          >
            Create Task
          </Link>
        </div>
      ) : (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {state.tasks.map((task) => (
            <Link
              key={task.id}
              to={`/tasks/${task.id}`}
              className="bg-white p-5 rounded-xl border border-gray-100 shadow-sm hover:shadow-md hover:border-blue-200 transition-all group flex flex-col h-full"
            >
              <div className="flex-1">
                <h3 className="text-lg font-bold text-gray-900 mb-2 line-clamp-1 group-hover:text-primary transition-colors">
                  {task.name}
                </h3>
                {task.description && (
                  <p className="text-gray-500 text-sm line-clamp-2">
                    {task.description}
                  </p>
                )}
              </div>
              <div className="mt-4 pt-4 border-t border-gray-50 flex items-center justify-between">
                <span className="text-xs text-gray-400 font-medium">
                  {new Date(task.createdAt).toLocaleDateString()}
                </span>
                <div className="flex gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                  <Link
                    to={`/tasks/edit/${task.id}`}
                    onClick={(e) => e.stopPropagation()}
                    className="p-1.5 text-gray-400 hover:text-blue-600 hover:bg-blue-50 rounded-md transition-colors"
                    title="Edit Task"
                  >
                    <Edit className="w-4 h-4" />
                  </Link>
                  <button
                    onClick={(e) => handleDelete(e, task.id)}
                    className="p-1.5 text-gray-400 hover:text-red-600 hover:bg-red-50 rounded-md transition-colors"
                    title="Delete Task"
                  >
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>
              </div>
            </Link>
          ))}
        </div>
      )}
    </div>
  );
};
