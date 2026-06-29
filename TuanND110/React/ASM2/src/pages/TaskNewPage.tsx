import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Formik, Form } from 'formik';
import * as Yup from 'yup';
import { ArrowLeft } from 'lucide-react';
import { useTasks } from '../context/TaskContext.tsx';
import { Button } from '../components/generic/Button.tsx';
import { Card } from '../components/generic/Card.tsx';
import { FormField } from '../components/generic/FormField.tsx';
import { Toast } from '../components/generic/Toast.tsx';

const TaskSchema = Yup.object({
  name: Yup.string()
    .required('Task name is required')
    .max(40, 'Name must be 40 characters or less'),
  description: Yup.string()
    .max(200, 'Description must be 200 characters or less'),
});

interface ToastState {
  message: string;
  variant: 'success' | 'error' | 'info';
}

export const TaskNewPage: React.FC = () => {
  const navigate = useNavigate();
  const { createTask } = useTasks();
  const [toast, setToast] = useState<ToastState | null>(null);

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
          <h1 className="text-xl font-extrabold text-slate-900 tracking-tight">Create New Task</h1>
          <p className="text-xs text-slate-500 mt-0.5">Add a new task to your workspace</p>
        </div>
      </div>

      <div className="max-w-2xl">
        <Card className="space-y-5">
          <h3 className="text-xs font-bold text-slate-500 uppercase tracking-wider">Task Form</h3>

          <Formik
            initialValues={{ name: '', description: '' }}
            validationSchema={TaskSchema}
            onSubmit={async (values, { setSubmitting, resetForm }) => {
              try {
                await createTask(values.name, values.description);
                resetForm();
                setToast({ message: 'Task created successfully!', variant: 'success' });
                setTimeout(() => navigate('/tasks'), 1500);
              } catch {
                setToast({ message: 'Failed to create task.', variant: 'error' });
              } finally {
                setSubmitting(false);
              }
            }}
          >
            {({ isSubmitting }) => (
              <Form className="space-y-4">
                <FormField
                  label="Task Name"
                  name="name"
                  placeholder="e.g. Implement login page"
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
                <div className="flex justify-end gap-2.5 pt-3 border-t border-slate-100">
                  <Button
                    variant="secondary"
                    type="button"
                    onClick={() => navigate('/tasks')}
                    disabled={isSubmitting}
                  >
                    Cancel
                  </Button>
                  <Button variant="primary" type="submit" isLoading={isSubmitting}>
                    Create Task
                  </Button>
                </div>
              </Form>
            )}
          </Formik>
        </Card>
      </div>

      {toast && (
        <Toast
          message={toast.message}
          variant={toast.variant}
          onDismiss={() => setToast(null)}
        />
      )}
    </div>
  );
};
