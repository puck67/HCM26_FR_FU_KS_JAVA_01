import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { CrudTable } from '../components/common/CrudTable';
import { CrudActionBar } from '../components/common/CrudActionBar';
import { TaskForm, type TaskFormValues } from '../components/TaskForm';
import { useTaskReducer } from '../hooks/useTaskReducer';
import { mockApi, type Task } from '../api/mockApi';

export function Tasks() {
  const { state, dispatch } = useTaskReducer();
  const [showForm, setShowForm] = useState(false);
  const [editingTask, setEditingTask] = useState<Task | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    let mounted = true;
    const fetchTasks = async () => {
      dispatch({ type: 'FETCH_INIT' });
      try {
        const data = await mockApi.getTasks();
        if (mounted) dispatch({ type: 'FETCH_SUCCESS', payload: data });
      } catch (error: any) {
        if (mounted) dispatch({ type: 'FETCH_FAILURE', payload: error.message || 'Failed to fetch' });
      }
    };
    fetchTasks();
    return () => { mounted = false; };
  }, [dispatch]);

  const handleAddClick = () => {
    setEditingTask(null);
    setShowForm(true);
  };

  const handleEditClick = (task: Task) => {
    setEditingTask(task);
    setShowForm(true);
  };

  const handleDeleteClick = async (task: Task) => {
    if (window.confirm(`Are you sure you want to delete "${task.name}"?`)) {
      try {
        await mockApi.deleteTask(task.id);
        dispatch({ type: 'DELETE', payload: task.id });
      } catch (e) {
        alert('Failed to delete task');
      }
    }
  };

  const handleFormSubmit = async (values: TaskFormValues) => {
    setIsSubmitting(true);
    try {
      if (editingTask) {
        const updated = await mockApi.updateTask(editingTask.id, { ...values });
        dispatch({ type: 'UPDATE', payload: updated });
      } else {
        const created = await mockApi.createTask({ ...values, status: 'Pending' });
        dispatch({ type: 'CREATE', payload: created });
      }
      setShowForm(false);
    } catch (e) {
      alert('Failed to save task');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleRowClick = (task: Task) => {
    navigate(`/tasks/${task.id}`);
  };

  return (
    <div>
      <h1 className="text-3xl font-bold text-gray-800 mb-6">Tasks</h1>

      {showForm ? (
        <div className="mb-8">
          <TaskForm 
            initialValues={editingTask ? { name: editingTask.name, description: editingTask.description || '' } : { name: '', description: '' }}
            onSubmit={handleFormSubmit}
            onCancel={() => setShowForm(false)}
            isSubmittingTask={isSubmitting}
          />
        </div>
      ) : (
        <CrudActionBar onAdd={handleAddClick} />
      )}

      {state.loading && <div className="text-center py-4 text-gray-600">Loading tasks...</div>}
      {state.error && <div className="text-center py-4 text-red-600">Error: {state.error}</div>}
      
      {!state.loading && !state.error && (
        <CrudTable
          data={state.tasks}
          onEdit={handleEditClick}
          onDelete={handleDeleteClick}
          onRowClick={handleRowClick}
        />
      )}
    </div>
  );
}
