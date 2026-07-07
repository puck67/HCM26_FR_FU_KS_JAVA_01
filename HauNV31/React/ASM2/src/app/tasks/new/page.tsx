'use client';

import { useFormik } from 'formik';
import { createTask } from '@/app/actions';
import Link from 'next/link';

export default function NewTaskPage() {
  const formik = useFormik({
    initialValues: {
      name: '',
      description: '',
    },
    validate: (values) => {
      const errors: { name?: string; description?: string } = {};
      if (!values.name) {
        errors.name = 'Required';
      } else if (values.name.length > 40) {
        errors.name = 'Must be 40 characters or less';
      }

      if (values.description && values.description.length > 200) {
        errors.description = 'Must be 200 characters or less';
      }
      return errors;
    },
    onSubmit: async (values, { setSubmitting }) => {
      const formData = new FormData();
      formData.append('name', values.name);
      formData.append('description', values.description);
      try {
        await createTask(formData);
      } catch (error) {
        console.error('Failed to create task:', error);
      }
      setSubmitting(false);
    },
  });

  return (
    <main className="min-h-screen p-10 bg-gray-100 flex items-center justify-center">
      <div className="bg-white p-8 rounded-xl shadow-lg max-w-md w-full">
        <h1 className="text-2xl font-bold text-gray-800 mb-6">Create New Task</h1>
        <form onSubmit={formik.handleSubmit} className="space-y-4">
          <div>
            <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-1">
              Task Name *
            </label>
            <input
              id="name"
              name="name"
              type="text"
              className={`w-full p-3 border rounded focus:outline-none focus:ring-2 focus:ring-indigo-500 ${formik.touched.name && formik.errors.name ? 'border-red-500' : 'border-gray-300'}`}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.name}
              placeholder="E.g., Complete assignment"
            />
            {formik.touched.name && formik.errors.name ? (
              <div className="text-red-500 text-sm mt-1">{formik.errors.name}</div>
            ) : null}
          </div>

          <div>
            <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-1">
              Description
            </label>
            <textarea
              id="description"
              name="description"
              rows={4}
              className={`w-full p-3 border rounded focus:outline-none focus:ring-2 focus:ring-indigo-500 ${formik.touched.description && formik.errors.description ? 'border-red-500' : 'border-gray-300'}`}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.description}
              placeholder="Task details..."
            />
            {formik.touched.description && formik.errors.description ? (
              <div className="text-red-500 text-sm mt-1">{formik.errors.description}</div>
            ) : null}
          </div>

          <div className="flex gap-4 pt-4">
            <button
              type="submit"
              disabled={formik.isSubmitting}
              className="flex-1 bg-indigo-600 text-white py-3 rounded hover:bg-indigo-700 transition disabled:opacity-50"
            >
              {formik.isSubmitting ? 'Saving...' : 'Save Task'}
            </button>
            <Link 
              href="/tasks"
              className="flex-1 bg-gray-200 text-gray-800 py-3 rounded hover:bg-gray-300 transition text-center"
            >
              Cancel
            </Link>
          </div>
        </form>
      </div>
    </main>
  );
}
