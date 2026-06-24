'use client';

import { Formik, Form, Field, ErrorMessage } from 'formik';
import { Button } from './common/Button';

export interface TaskFormValues {
  name: string;
  description: string;
}

interface TaskFormProps {
  initialValues: TaskFormValues;
  onSubmit: (values: TaskFormValues) => Promise<void> | void;
  onCancel: () => void;
}

export function TaskForm({ initialValues, onSubmit, onCancel }: TaskFormProps) {
  return (
    <div className="bg-white p-6 rounded-lg shadow-md border max-w-2xl mx-auto">
      <h3 className="text-lg font-bold mb-4">{initialValues.name ? 'Edit Task' : 'Create Task'}</h3>
      <Formik
        initialValues={initialValues}
        validate={values => {
          const errors: Partial<TaskFormValues> = {};
          if (!values.name) {
            errors.name = 'Name is required';
          } else if (values.name.length > 40) {
            errors.name = 'Must be 40 characters or less';
          }

          if (values.description && values.description.length > 200) {
            errors.description = 'Must be 200 characters or less';
          }
          return errors;
        }}
        onSubmit={async (values, { setSubmitting }) => {
          await onSubmit(values);
          setSubmitting(false);
        }}
      >
        {({ isSubmitting }) => (
          <Form className="flex flex-col gap-4">
            <div>
              <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-1">Task Name *</label>
              <Field
                type="text"
                name="name"
                className="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Enter task name"
              />
              <ErrorMessage name="name" component="div" className="text-red-500 text-sm mt-1" />
            </div>

            <div>
              <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-1">Description</label>
              <Field
                as="textarea"
                name="description"
                className="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500 h-24"
                placeholder="Enter description (optional)"
              />
              <ErrorMessage name="description" component="div" className="text-red-500 text-sm mt-1" />
            </div>

            <div className="flex gap-2 justify-end mt-4">
              <Button title="Cancel" action={onCancel} style="bg-gray-200 text-gray-800 hover:bg-gray-300" type="button" />
              <button
                type="submit"
                disabled={isSubmitting}
                className={`px-4 py-2 rounded font-medium transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2 bg-blue-600 text-white hover:bg-blue-700 ${isSubmitting ? 'opacity-50 cursor-not-allowed' : ''}`}
              >
                {isSubmitting ? 'Saving...' : 'Save'}
              </button>
            </div>
          </Form>
        )}
      </Formik>
    </div>
  );
}
