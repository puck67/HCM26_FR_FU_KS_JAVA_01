import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { mockApi, type Task } from '../api/mockApi';
import { Button } from '../components/common/Button';

export function TaskDetail() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [task, setTask] = useState<Task | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let mounted = true;
    const fetchTask = async () => {
      setLoading(true);
      try {
        if (!id) throw new Error('No task ID provided');
        const data = await mockApi.getTaskById(id);
        if (mounted) {
          setTask(data);
          setError(null);
        }
      } catch (err: any) {
        if (mounted) setError(err.message || 'Failed to fetch task details');
      } finally {
        if (mounted) setLoading(false);
      }
    };

    fetchTask();
    return () => { mounted = false; };
  }, [id]);

  if (loading) return <div className="text-center py-8 text-gray-600">Loading task details...</div>;
  if (error || !task) return <div className="text-center py-8 text-red-600">{error || 'Task not found'}</div>;

  return (
    <div className="bg-white shadow overflow-hidden sm:rounded-lg">
      <div className="px-4 py-5 sm:px-6 flex justify-between items-center">
        <div>
          <h3 className="text-lg leading-6 font-medium text-gray-900">Task Details</h3>
          <p className="mt-1 max-w-2xl text-sm text-gray-500">Information about task ID: {task.id}</p>
        </div>
        <Button 
          title="Back to Tasks" 
          action={() => navigate('/tasks')} 
          style="bg-gray-100 text-gray-800 hover:bg-gray-200" 
        />
      </div>
      <div className="border-t border-gray-200">
        <dl>
          <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <dt className="text-sm font-medium text-gray-500">Task Name</dt>
            <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">{task.name}</dd>
          </div>
          <div className="bg-white px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <dt className="text-sm font-medium text-gray-500">Status</dt>
            <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
              <span className={`px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${task.status === 'Completed' ? 'bg-green-100 text-green-800' : 'bg-yellow-100 text-yellow-800'}`}>
                {task.status}
              </span>
            </dd>
          </div>
          <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <dt className="text-sm font-medium text-gray-500">Description</dt>
            <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">{task.description || 'No description provided.'}</dd>
          </div>
        </dl>
      </div>
    </div>
  );
}
