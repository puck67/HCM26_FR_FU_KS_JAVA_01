import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import { TaskFormValues } from '../types/task';

interface TaskFormProps {
  initialValues?: TaskFormValues;
  onSubmit: (values: TaskFormValues) => Promise<void>;
  submitLabel?: string;
  isLoading?: boolean;
}

const validationSchema = Yup.object({
  name: Yup.string()
    .required('Name is required')
    .max(40, 'Name must be at most 40 characters'),
  description: Yup.string()
    .max(200, 'Description must be at most 200 characters'),
});

const defaultValues: TaskFormValues = {
  name: '',
  description: '',
};

export default function TaskForm({
  initialValues = defaultValues,
  onSubmit,
  submitLabel = 'Save Task',
  isLoading = false,
}: TaskFormProps) {
  return (
    <Formik
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={async (values, { setSubmitting }) => {
        try {
          await onSubmit(values);
        } finally {
          setSubmitting(false);
        }
      }}
      enableReinitialize
    >
      {({ values, isSubmitting }) => (
        <Form className="space-y-5">
          {/* Name field */}
          <div>
            <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-1">
              Task Name <span className="text-red-500">*</span>
            </label>
            <Field
              id="name"
              name="name"
              type="text"
              placeholder="Enter task name..."
              className="w-full px-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition"
            />
            <div className="flex justify-between mt-1">
              <ErrorMessage
                name="name"
                component="p"
                className="text-red-500 text-xs"
              />
              <span className="text-xs text-gray-400 ml-auto">{values.name.length}/40</span>
            </div>
          </div>

          {/* Description field */}
          <div>
            <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-1">
              Description
              <span className="text-gray-400 font-normal ml-1">(optional)</span>
            </label>
            <Field
              id="description"
              name="description"
              as="textarea"
              rows={4}
              placeholder="Describe the task..."
              className="w-full px-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition resize-none"
            />
            <div className="flex justify-between mt-1">
              <ErrorMessage
                name="description"
                component="p"
                className="text-red-500 text-xs"
              />
              <span className="text-xs text-gray-400 ml-auto">{values.description.length}/200</span>
            </div>
          </div>

          {/* Submit */}
          <button
            type="submit"
            disabled={isSubmitting || isLoading}
            className="w-full py-2.5 px-6 bg-indigo-600 text-white rounded-lg text-sm font-semibold hover:bg-indigo-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
          >
            {isSubmitting || isLoading ? 'Saving...' : submitLabel}
          </button>
        </Form>
      )}
    </Formik>
  );
}
