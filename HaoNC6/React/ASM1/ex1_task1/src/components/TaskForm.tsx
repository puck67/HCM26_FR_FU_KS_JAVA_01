import React from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import { AlertCircle, Plus, Check } from 'lucide-react';
import type { Task } from '../types';

interface TaskFormValues {
  name: string;
  description: string;
  status?: Task['status'];
}

interface TaskFormProps {
  initialValues?: TaskFormValues;
  onSubmit: (values: TaskFormValues) => void;
  submitText: string;
  onCancel: () => void;
  isEdit?: boolean;
}

export const TaskForm: React.FC<TaskFormProps> = ({
  initialValues = { name: '', description: '', status: 'todo' },
  onSubmit,
  submitText,
  onCancel,
  isEdit = false,
}) => {
  const validate = (values: TaskFormValues) => {
    const errors: Record<string, string> = {};

    if (!values.name.trim()) {
      errors.name = 'Task name is required';
    } else if (values.name.length > 40) {
      errors.name = 'Task name cannot exceed 40 characters';
    }

    if (values.description && values.description.length > 200) {
      errors.description = 'Description cannot exceed 200 characters';
    }

    return errors;
  };

  return (
    <Formik
      initialValues={initialValues}
      validate={validate}
      onSubmit={(values, { resetForm }) => {
        onSubmit(values);
        if (!isEdit) resetForm();
      }}
    >
      {({ values, errors, touched, isSubmitting }) => (
        <Form className="flex flex-col gap-5 w-full">
          <div>
            <div className="flex justify-between items-center mb-1.5">
              <label htmlFor="name" className="text-sm font-semibold text-gray-300">
                Task Name <span className="text-rose-500">*</span>
              </label>
              <span className={`text-xs ${values.name.length > 40 ? 'text-rose-400 font-bold' : 'text-gray-500'}`}>
                {values.name.length} / 40
              </span>
            </div>
            <div className="relative">
              <Field
                id="name"
                name="name"
                type="text"
                placeholder="e.g. Design Landing Page Mockup"
                className={`glass-input w-full px-4 py-3 rounded-xl text-base ${
                  touched.name && errors.name
                    ? 'border-rose-500/50 focus:border-rose-500 focus:ring-rose-500/20'
                    : 'border-white/10'
                }`}
              />
            </div>
            <ErrorMessage name="name">
              {(msg) => (
                <div className="flex items-center gap-1 mt-1.5 text-xs text-rose-400 font-medium">
                  <AlertCircle size={12} />
                  <span>{msg}</span>
                </div>
              )}
            </ErrorMessage>
          </div>

          <div>
            <div className="flex justify-between items-center mb-1.5">
              <label htmlFor="description" className="text-sm font-semibold text-gray-300">
                Description <span className="text-gray-500">(Optional)</span>
              </label>
              <span className={`text-xs ${values.description.length > 200 ? 'text-rose-400 font-bold' : 'text-gray-500'}`}>
                {values.description.length} / 200
              </span>
            </div>
            <Field
              id="description"
              name="description"
              as="textarea"
              rows={4}
              placeholder="Provide a brief description of the goals, milestones, or instructions..."
              className={`glass-input w-full px-4 py-3 rounded-xl text-base resize-none ${
                touched.description && errors.description
                  ? 'border-rose-500/50 focus:border-rose-500 focus:ring-rose-500/20'
                  : 'border-white/10'
              }`}
            />
            <ErrorMessage name="description">
              {(msg) => (
                <div className="flex items-center gap-1 mt-1.5 text-xs text-rose-400 font-medium">
                  <AlertCircle size={12} />
                  <span>{msg}</span>
                </div>
              )}
            </ErrorMessage>
          </div>

          {isEdit && (
            <div>
              <label htmlFor="status" className="block text-sm font-semibold text-gray-300 mb-1.5">
                Status
              </label>
              <Field
                id="status"
                name="status"
                as="select"
                className="glass-input w-full px-4 py-3 rounded-xl text-base border-white/10 cursor-pointer"
              >
                <option value="todo" className="bg-slate-900 text-gray-300">To Do</option>
                <option value="in_progress" className="bg-slate-900 text-gray-300">In Progress</option>
                <option value="completed" className="bg-slate-900 text-gray-300">Completed</option>
              </Field>
            </div>
          )}

          <div className="flex items-center justify-end gap-3 mt-4 pt-4 border-t border-white/5">
            <button
              type="button"
              onClick={onCancel}
              className="px-4 py-2.5 rounded-xl text-sm font-medium text-gray-400 hover:text-white hover:bg-white/5 border border-transparent transition-all cursor-pointer"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={isSubmitting || !!(errors.name || errors.description)}
              className="glass-button px-5 py-2.5 rounded-xl text-sm font-medium text-violet-100 flex items-center gap-2 cursor-pointer disabled:opacity-50 disabled:pointer-events-none"
            >
              {isEdit ? <Check size={16} /> : <Plus size={16} />}
              <span>{submitText}</span>
            </button>
          </div>
        </Form>
      )}
    </Formik>
  );
};
