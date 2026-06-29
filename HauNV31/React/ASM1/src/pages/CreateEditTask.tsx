import { useEffect, useState } from 'react';
import { useNavigate, useParams, Link } from 'react-router-dom';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import { fetchTaskById, createTask, updateTask } from '../api/taskApi';

import { ArrowLeft, Save, Loader2 } from 'lucide-react';

const TaskSchema = Yup.object().shape({
  name: Yup.string()
    .required('Task name is required')
    .max(40, 'Task name must be at most 40 characters'),
  description: Yup.string()
    .max(200, 'Description must be at most 200 characters'),
});

export const CreateEditTask = () => {
  const { id } = useParams<{ id: string }>();
  const isEditMode = Boolean(id);
  const navigate = useNavigate();
  
  const [initialValues, setInitialValues] = useState({ name: '', description: '' });
  const [loading, setLoading] = useState(isEditMode);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (isEditMode && id) {
      const loadTask = async () => {
        try {
          const data = await fetchTaskById(id);
          if (data) {
            setInitialValues({
              name: data.name,
              description: data.description || '',
            });
          } else {
            setError('Task not found');
          }
        } catch (err) {
          setError('Failed to load task for editing.');
        } finally {
          setLoading(false);
        }
      };
      loadTask();
    }
  }, [id, isEditMode]);

  if (loading) {
    return (
      <div className="flex flex-col justify-center items-center h-64 gap-3 text-gray-500">
        <Loader2 className="w-10 h-10 animate-spin text-primary" />
        <p className="font-medium animate-pulse">Preparing form...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="max-w-3xl mx-auto px-4 mt-10 text-center text-red-600 bg-red-50 p-6 rounded-xl border border-red-100">
        {error}
      </div>
    );
  }

  return (
    <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      <Link to="/tasks" className="inline-flex items-center gap-2 text-sm text-gray-500 hover:text-gray-900 transition-colors mb-6">
        <ArrowLeft className="w-4 h-4" /> Back to tasks
      </Link>

      <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
        <div className="px-6 py-8 sm:p-10">
          <h1 className="text-3xl font-extrabold text-gray-900 mb-2">
            {isEditMode ? 'Edit Task' : 'Create New Task'}
          </h1>
          <p className="text-gray-500 mb-8">
            {isEditMode ? 'Update the details of your task.' : 'Add a new task to your list.'}
          </p>

          <Formik
            initialValues={initialValues}
            validationSchema={TaskSchema}
            enableReinitialize
            onSubmit={async (values, { setSubmitting }) => {
              try {
                if (isEditMode && id) {
                  await updateTask(id, values);
                } else {
                  await createTask(values);
                }
                navigate('/tasks');
              } catch (err) {
                alert('Failed to save task.');
              } finally {
                setSubmitting(false);
              }
            }}
          >
            {({ isSubmitting, errors, touched }) => (
              <Form className="space-y-6">
                <div>
                  <label htmlFor="name" className="block text-sm font-bold text-gray-700 mb-1">
                    Task Name <span className="text-red-500">*</span>
                  </label>
                  <Field
                    type="text"
                    name="name"
                    id="name"
                    placeholder="E.g., Complete the React assignment"
                    className={`block w-full px-4 py-3 rounded-xl border ${
                      errors.name && touched.name ? 'border-red-300 focus:ring-red-500 focus:border-red-500' : 'border-gray-300 focus:ring-primary focus:border-primary'
                    } shadow-sm focus:outline-none focus:ring-2 sm:text-sm transition-shadow`}
                  />
                  <ErrorMessage name="name" component="p" className="mt-1 text-sm text-red-600 font-medium" />
                </div>

                <div>
                  <label htmlFor="description" className="block text-sm font-bold text-gray-700 mb-1">
                    Description
                  </label>
                  <Field
                    as="textarea"
                    name="description"
                    id="description"
                    rows={4}
                    placeholder="Add more details about this task..."
                    className={`block w-full px-4 py-3 rounded-xl border ${
                      errors.description && touched.description ? 'border-red-300 focus:ring-red-500 focus:border-red-500' : 'border-gray-300 focus:ring-primary focus:border-primary'
                    } shadow-sm focus:outline-none focus:ring-2 sm:text-sm transition-shadow resize-none`}
                  />
                  <ErrorMessage name="description" component="p" className="mt-1 text-sm text-red-600 font-medium" />
                </div>

                <div className="pt-4 flex items-center justify-end gap-3">
                  <Link
                    to="/tasks"
                    className="px-6 py-2.5 bg-white border border-gray-300 rounded-xl text-gray-700 font-medium hover:bg-gray-50 transition-colors shadow-sm"
                  >
                    Cancel
                  </Link>
                  <button
                    type="submit"
                    disabled={isSubmitting}
                    className="flex items-center gap-2 px-6 py-2.5 bg-primary text-white rounded-xl font-medium hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary disabled:opacity-70 disabled:cursor-not-allowed transition-all shadow-sm shadow-blue-200"
                  >
                    {isSubmitting ? (
                      <Loader2 className="w-5 h-5 animate-spin" />
                    ) : (
                      <Save className="w-5 h-5" />
                    )}
                    {isEditMode ? 'Update Task' : 'Save Task'}
                  </button>
                </div>
              </Form>
            )}
          </Formik>
        </div>
      </div>
    </div>
  );
};
