import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Formik, Form } from 'formik';
import * as Yup from 'yup';
import { ArrowLeft, Calendar, CheckCircle, Clock, AlertCircle } from 'lucide-react';
import { useTasks } from '../context/TaskContext.tsx';
import { taskApi } from '../services/api.ts';
import type { Task } from '../types/task.ts';
import { Card } from '../components/generic/Card.tsx';
import { Button } from '../components/generic/Button.tsx';
import { FormField } from '../components/generic/FormField.tsx';
import { Badge, statusToBadgeProps } from '../components/generic/Badge.tsx';
import { Spinner } from '../components/generic/Spinner.tsx';
import { Alert } from '../components/generic/Alert.tsx';
import { Toast } from '../components/generic/Toast.tsx';

const EditTaskSchema = Yup.object({
  name: Yup.string()
    .required('Task name is required')
    .max(40, 'Name must be 40 characters or less'),
  description: Yup.string()
    .max(200, 'Description must be 200 characters or less'),
  status: Yup.string()
    .oneOf(['todo', 'in_progress', 'completed'])
    .required(),
});

const STATUS_ICON: Record<Task['status'], React.ReactNode> = {
  todo:        <AlertCircle  size={15} className="text-amber-600" />,
  in_progress: <Clock        size={15} className="text-blue-600"  />,
  completed:   <CheckCircle  size={15} className="text-emerald-600" />,
};

export const TaskDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { updateTask } = useTasks();

  const [task, setTask]       = useState<Task | null>(null);
  const [loading, setLoading] = useState(true);
  const [loadErr, setLoadErr] = useState<string | null>(null);
  const [toast, setToast]     = useState<{ message: string; variant: 'success' | 'error' } | null>(null);

  useEffect(() => {
    if (!id) return;
    setLoading(true);
    taskApi.getTask(id)
      .then(setTask)
      .catch(() => setLoadErr('Task not found or failed to load.'))
      .finally(() => setLoading(false));
  }, [id]);

  if (loading) {
    return (
      <div className="flex justify-center items-center py-20">
        <Spinner size="lg" label="Loading task…" />
      </div>
    );
  }

  if (loadErr || !task) {
    return (
      <div className="max-w-md mx-auto space-y-4 mt-8">
        <Alert variant="error" title="Not Found">{loadErr ?? 'Task does not exist.'}</Alert>
        <Button variant="outline" leadingIcon={<ArrowLeft size={15} />} onClick={() => navigate('/tasks')}>
          Back to Tasks
        </Button>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center gap-3">
        <button
          onClick={() => navigate('/tasks')}
          className="p-2 rounded-lg border border-slate-200 hover:bg-slate-50 text-slate-500 hover:text-slate-700 transition-colors"
          aria-label="Back to tasks"
        >
          <ArrowLeft size={16} />
        </button>
        <div>
          <h1 className="text-xl font-extrabold text-slate-900 tracking-tight">Task Detail</h1>
          <p className="text-xs text-slate-400 mt-0.5">ID: {task.id}</p>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="md:col-span-1 space-y-4">
          <Card className="space-y-4">
            <h3 className="text-xs font-bold text-slate-500 uppercase tracking-wider">Task Info</h3>

            <div className="space-y-3">
              <div className="flex flex-col gap-1">
                <span className="text-[10px] font-semibold uppercase tracking-wider text-slate-400">Status</span>
                <div className="flex items-center gap-2">
                  {STATUS_ICON[task.status]}
                  <Badge {...statusToBadgeProps(task.status)} />
                </div>
              </div>

              <div className="flex flex-col gap-1">
                <span className="text-[10px] font-semibold uppercase tracking-wider text-slate-400">Created</span>
                <div className="flex items-center gap-1.5 text-xs text-slate-600">
                  <Calendar size={13} className="text-slate-400" />
                  {new Date(task.createdAt).toLocaleDateString('en-US', {
                    year: 'numeric', month: 'long', day: 'numeric',
                  })}
                </div>
              </div>

              {task.description && (
                <div className="flex flex-col gap-1">
                  <span className="text-[10px] font-semibold uppercase tracking-wider text-slate-400">Description</span>
                  <p className="text-xs text-slate-600 leading-relaxed">{task.description}</p>
                </div>
              )}
            </div>
          </Card>
        </div>

        <div className="md:col-span-2">
          <Card className="space-y-5">
            <h3 className="text-xs font-bold text-slate-500 uppercase tracking-wider">Edit Task</h3>

            <Formik
              initialValues={{
                name: task.name,
                description: task.description,
                status: task.status,
              }}
              validationSchema={EditTaskSchema}
              enableReinitialize
              onSubmit={async (values, { setSubmitting }) => {
                try {
                  const updated = await updateTask(task.id, values);
                  setTask(updated);
                  setToast({ message: 'Task updated successfully.', variant: 'success' });
                } catch {
                  setToast({ message: 'Failed to update task.', variant: 'error' });
                } finally {
                  setSubmitting(false);
                }
              }}
            >
              {({ isSubmitting, dirty, values, handleChange, handleBlur }) => (
                <Form className="space-y-4">
                  <FormField
                    label="Task Name"
                    name="name"
                    placeholder="Enter task name"
                    maxLength={40}
                    required
                  />

                  <FormField
                    label="Description"
                    name="description"
                    as="textarea"
                    placeholder="Optional description…"
                    maxLength={200}
                    rows={4}
                  />

                  <div className="flex flex-col gap-1.5">
                    <label htmlFor="status" className="text-xs font-semibold uppercase tracking-wider text-slate-700">
                      Status <span className="text-rose-500">*</span>
                    </label>
                    <select
                      id="status"
                      name="status"
                      value={values.status}
                      onChange={handleChange}
                      onBlur={handleBlur}
                      className="w-full px-3 py-2 rounded-md border border-slate-300 bg-white text-slate-900 text-sm focus:outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-500 transition-all"
                    >
                      <option value="todo">To Do</option>
                      <option value="in_progress">In Progress</option>
                      <option value="completed">Completed</option>
                    </select>
                  </div>

                  <div className="flex justify-end gap-2.5 pt-3 border-t border-slate-100">
                    <Button variant="secondary" type="button" onClick={() => navigate('/tasks')}>
                      Cancel
                    </Button>
                    <Button
                      variant="primary"
                      type="submit"
                      disabled={!dirty}
                      isLoading={isSubmitting}
                    >
                      Save Changes
                    </Button>
                  </div>
                </Form>
              )}
            </Formik>
          </Card>
        </div>
      </div>

      {toast && (
        <Toast message={toast.message} variant={toast.variant} onDismiss={() => setToast(null)} />
      )}
    </div>
  );
};
