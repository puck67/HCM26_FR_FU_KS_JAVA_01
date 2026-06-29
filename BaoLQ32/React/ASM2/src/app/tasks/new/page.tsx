'use client';

import { useRouter } from 'next/navigation';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { ArrowLeft, Save, Loader2, AlertCircle } from 'lucide-react';
import Link from 'next/link';
import { createTask } from '@/app/actions';

// Validation Schema using Yup
const TaskValidationSchema = Yup.object().shape({
  name: Yup.string()
    .max(40, 'Name must be 40 characters or less')
    .required('Task name is required'),
  description: Yup.string()
    .max(200, 'Description must be 200 characters or less')
    .optional(),
});

export default function NewTaskPage() {
  const router = useRouter();

  const formik = useFormik({
    initialValues: {
      name: '',
      description: '',
    },
    validationSchema: TaskValidationSchema,
    onSubmit: async (values, { setSubmitting, setFieldError, setStatus }) => {
      setStatus(null);
      try {
        await createTask({
          name: values.name,
          description: values.description,
        });
        // Success: redirect to tasks page
        router.push('/tasks');
        router.refresh();
      } catch (err) {
        console.error('Failed to create task:', err);
        setStatus(err instanceof Error ? err.message : 'Something went wrong. Please try again.');
      } finally {
        setSubmitting(false);
      }
    },
  });

  return (
    <div className="max-w-xl mx-auto py-6">
      {/* Back button */}
      <Link
        href="/tasks"
        className="inline-flex items-center gap-1.5 text-sm text-slate-400 hover:text-slate-200 mb-6 transition-colors group"
      >
        <ArrowLeft className="h-4 w-4 group-hover:-translate-x-0.5 transition-transform" />
        <span>Back to workspace</span>
      </Link>

      {/* Main card */}
      <div className="bg-slate-900 border border-slate-800 rounded-3xl p-6 sm:p-8 shadow-xl">
        <div className="mb-6 space-y-1">
          <h1 className="text-2xl font-bold text-white">Create New Task</h1>
          <p className="text-sm text-slate-300">
            Define your task parameters. Validation is enforced.
          </p>
        </div>

        {formik.status && (
          <div className="mb-6 p-4 rounded-xl border border-red-500/20 bg-red-950/20 text-red-400 text-sm flex gap-2 items-start">
            <AlertCircle className="h-5 w-5 shrink-0 mt-0.5" />
            <span>{formik.status}</span>
          </div>
        )}

        <form onSubmit={formik.handleSubmit} className="space-y-6">
          {/* Task Name */}
          <div className="space-y-2">
            <label htmlFor="name" className="block text-sm font-semibold text-slate-200">
              Task Name <span className="text-rose-500 font-bold">*</span>
            </label>
            <input
              id="name"
              type="text"
              {...formik.getFieldProps('name')}
              className={`w-full bg-slate-950 border rounded-xl py-3 px-4 text-sm text-slate-100 focus:outline-none focus:ring-2 transition-all ${
                formik.touched.name && formik.errors.name
                  ? 'border-red-500/50 focus:ring-red-500/25 focus:border-red-500'
                  : 'border-slate-800 focus:ring-indigo-500/25 focus:border-indigo-500'
              }`}
              placeholder="e.g. Write unit tests"
            />
            {formik.touched.name && formik.errors.name ? (
              <p className="text-xs text-red-400 font-medium mt-1 flex items-center gap-1">
                <span>{formik.errors.name}</span>
              </p>
            ) : (
              <p className="text-xs text-slate-400 pl-1">
                Maximum 40 characters. Required.
              </p>
            )}
          </div>

          {/* Task Description */}
          <div className="space-y-2">
            <label htmlFor="description" className="block text-sm font-semibold text-slate-200">
              Description <span className="text-slate-400 font-normal">(Optional)</span>
            </label>
            <textarea
              id="description"
              rows={4}
              {...formik.getFieldProps('description')}
              className={`w-full bg-slate-950 border rounded-xl py-3 px-4 text-sm text-slate-100 focus:outline-none focus:ring-2 transition-all ${
                formik.touched.description && formik.errors.description
                  ? 'border-red-500/50 focus:ring-red-500/25 focus:border-red-500'
                  : 'border-slate-800 focus:ring-indigo-500/25 focus:border-indigo-500'
              }`}
              placeholder="Provide a detailed description of the work needed..."
            />
            {formik.touched.description && formik.errors.description ? (
              <p className="text-xs text-red-400 font-medium mt-1">
                {formik.errors.description}
              </p>
            ) : (
              <p className="text-xs text-slate-400 pl-1">
                Maximum 200 characters. Optional.
              </p>
            )}
          </div>

          {/* Form Actions */}
          <div className="flex items-center justify-end gap-4 pt-4 border-t border-slate-800">
            <Link
              href="/tasks"
              className="px-5 py-2.5 rounded-xl border border-slate-800 text-sm font-semibold text-slate-400 hover:text-slate-200 hover:bg-slate-800/50 transition-colors"
            >
              Cancel
            </Link>
            <button
              type="submit"
              disabled={formik.isSubmitting}
              className="flex items-center gap-2 rounded-xl bg-indigo-600 px-5 py-2.5 text-sm font-semibold text-white shadow-md hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600 transition-all hover:scale-102 disabled:opacity-50 disabled:pointer-events-none"
            >
              {formik.isSubmitting ? (
                <>
                  <Loader2 className="h-4 w-4 animate-spin" />
                  <span>Saving...</span>
                </>
              ) : (
                <>
                  <Save className="h-4 w-4" />
                  <span>Create Task</span>
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
