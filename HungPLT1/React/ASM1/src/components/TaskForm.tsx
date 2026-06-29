import React from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import type { TaskStatus } from '../types';

interface TaskFormValues {
  name: string;
  description: string;
  status: TaskStatus;
}

interface TaskFormProps {
  initialValues?: TaskFormValues;
  onSubmit: (values: TaskFormValues) => Promise<void>;
  onCancel: () => void;
  submitButtonText: string;
}

const validationSchema = Yup.object().shape({
  name: Yup.string()
    .required('Task name is required')
    .max(40, 'Task name cannot exceed 40 characters'),
  description: Yup.string()
    .max(200, 'Description cannot exceed 200 characters'),
  status: Yup.string()
    .oneOf(['pending', 'in_progress', 'completed'] as const)
    .required('Status is required'),
});

const defaultInitialValues: TaskFormValues = {
  name: '',
  description: '',
  status: 'pending',
};

export const TaskForm: React.FC<TaskFormProps> = ({
  initialValues = defaultInitialValues,
  onSubmit,
  onCancel,
  submitButtonText,
}) => {
  return (
    <Formik
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={async (values, { setSubmitting, resetForm }) => {
        try {
          await onSubmit(values);
          resetForm();
        } catch (err) {
          console.error(err);
        } finally {
          setSubmitting(false);
        }
      }}
    >
      {({ isSubmitting, values, errors, touched }) => (
        <Form className="space-y-6">
          <div>
            <div className="flex justify-between items-center mb-2">
              <label htmlFor="name" className="block text-sm font-semibold text-slate-300">
                Task Name <span className="text-rose-500">*</span>
              </label>
              <span className={`text-xs ${values.name.length > 40 ? 'text-rose-500 font-semibold' : 'text-slate-500'}`}>
                {values.name.length}/40
              </span>
            </div>
            <Field
              type="text"
              id="name"
              name="name"
              placeholder="e.g. Design homepage layout"
              className={`w-full bg-slate-950/80 border text-slate-100 rounded-xl px-4 py-3 text-sm placeholder-slate-600 outline-none transition-all duration-200 ${
                touched.name && errors.name
                  ? 'border-rose-500 focus:border-rose-500 focus:ring-1 focus:ring-rose-500/20'
                  : 'border-slate-800 focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500/20'
              }`}
            />
            <div className="mt-1.5 h-5">
              <ErrorMessage name="name" component="div" className="text-rose-400 text-xs font-medium" />
            </div>
          </div>

          <div>
            <div className="flex justify-between items-center mb-2">
              <label htmlFor="description" className="block text-sm font-semibold text-slate-300">
                Description
              </label>
              <span className={`text-xs ${values.description.length > 200 ? 'text-rose-500 font-semibold' : 'text-slate-500'}`}>
                {values.description.length}/200
              </span>
            </div>
            <Field
              as="textarea"
              id="description"
              name="description"
              rows={4}
              placeholder="Describe the task objective, criteria for success..."
              className={`w-full bg-slate-950/80 border text-slate-100 rounded-xl px-4 py-3 text-sm placeholder-slate-600 outline-none transition-all duration-200 resize-none ${
                touched.description && errors.description
                  ? 'border-rose-500 focus:border-rose-500 focus:ring-1 focus:ring-rose-500/20'
                  : 'border-slate-800 focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500/20'
              }`}
            />
            <div className="mt-1.5 h-5">
              <ErrorMessage name="description" component="div" className="text-rose-400 text-xs font-medium" />
            </div>
          </div>

          <div>
            <label htmlFor="status" className="block text-sm font-semibold text-slate-300 mb-2">
              Status
            </label>
            <Field
              as="select"
              id="status"
              name="status"
              className="w-full bg-slate-950/80 border border-slate-800 text-slate-100 rounded-xl px-4 py-3 text-sm outline-none focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500/20 transition-all duration-200"
            >
              <option value="pending">Pending</option>
              <option value="in_progress">In Progress</option>
              <option value="completed">Completed</option>
            </Field>
            <div className="mt-1.5 h-5">
              <ErrorMessage name="status" component="div" className="text-rose-400 text-xs font-medium" />
            </div>
          </div>

          <div className="flex gap-4 pt-4 border-t border-slate-800/60">
            <button
              type="button"
              onClick={onCancel}
              className="flex-1 px-4 py-3 bg-slate-800 hover:bg-slate-700 active:bg-slate-750 text-slate-300 text-sm font-semibold rounded-xl transition-all duration-200 cursor-pointer"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={isSubmitting}
              className="flex-1 px-4 py-3 bg-indigo-600 hover:bg-indigo-500 active:bg-indigo-700 text-white text-sm font-semibold rounded-xl shadow-lg shadow-indigo-950/30 hover:shadow-indigo-500/10 transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed cursor-pointer"
            >
              {isSubmitting ? 'Saving...' : submitButtonText}
            </button>
          </div>
        </Form>
      )}
    </Formik>
  );
};
