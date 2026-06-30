import React, { useState } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import type { Task } from '../types/task';
import { X, Loader2 } from 'lucide-react';

interface TaskFormModalProps {
  task?: Task | null;
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (values: { name: string; description: string; status: 'Pending' | 'Completed' }) => Promise<void>;
}

const TaskSchema = Yup.object().shape({
  name: Yup.string()
    .max(40, 'Task name must be at most 40 characters')
    .required('Task name is required'),
  description: Yup.string()
    .max(200, 'Description must be at most 200 characters'),
  status: Yup.string().oneOf(['Pending', 'Completed']).required(),
});

export const TaskFormModal: React.FC<TaskFormModalProps> = ({
  task,
  isOpen,
  onClose,
  onSubmit,
}) => {
  const [submitting, setSubmitting] = useState(false);

  if (!isOpen) return null;

  const initialValues = {
    name: task?.name || '',
    description: task?.description || '',
    status: task?.status || 'Pending',
  };

  const handleFormSubmit = async (
    values: { name: string; description: string; status: 'Pending' | 'Completed' },
    { resetForm }: { resetForm: () => void }
  ) => {
    setSubmitting(true);
    try {
      await onSubmit(values);
      resetForm();
      onClose();
    } catch (error) {
      console.error(error);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-950/80 backdrop-blur-sm animate-fade-in">
      <div className="relative w-full max-w-lg bg-slate-900 border border-slate-800 rounded-2xl shadow-2xl overflow-hidden animate-slide-up">
        {/* Header */}
        <div className="flex items-center justify-between px-6 py-4 border-b border-slate-850">
          <h2 className="text-xl font-bold text-white">
            {task ? 'Edit Task' : 'Create New Task'}
          </h2>
          <button
            onClick={onClose}
            className="p-1 rounded-lg text-slate-400 hover:text-white hover:bg-slate-800 transition-colors"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Formik Form Container */}
        <Formik
          initialValues={initialValues}
          validationSchema={TaskSchema}
          onSubmit={handleFormSubmit}
          enableReinitialize
        >
          {({ isSubmitting, errors, touched }) => (
            <Form className="p-6 space-y-5">
              {/* Name Field */}
              <div className="space-y-1.5">
                <label htmlFor="name" className="text-xs font-semibold text-slate-350 tracking-wide uppercase">
                  Task Name <span className="text-rose-500">*</span>
                </label>
                <Field
                  id="name"
                  name="name"
                  type="text"
                  placeholder="e.g. Implement layout design"
                  className={`w-full bg-slate-950 border rounded-xl px-4 py-3 text-sm text-slate-100 placeholder-slate-500 focus:outline-none focus:ring-2 transition-all ${
                    errors.name && touched.name
                      ? 'border-rose-500/50 focus:ring-rose-500/25'
                      : 'border-slate-800 focus:border-violet-500/50 focus:ring-violet-500/25'
                  }`}
                />
                <ErrorMessage
                  name="name"
                  component="div"
                  className="text-xs text-rose-400 font-medium"
                />
              </div>

              {/* Description Field */}
              <div className="space-y-1.5">
                <label htmlFor="description" className="text-xs font-semibold text-slate-350 tracking-wide uppercase">
                  Description
                </label>
                <Field
                  id="description"
                  name="description"
                  as="textarea"
                  rows={4}
                  placeholder="Describe your task requirements, notes..."
                  className={`w-full bg-slate-950 border rounded-xl px-4 py-3 text-sm text-slate-100 placeholder-slate-500 focus:outline-none focus:ring-2 transition-all ${
                    errors.description && touched.description
                      ? 'border-rose-500/50 focus:ring-rose-500/25'
                      : 'border-slate-800 focus:border-violet-500/50 focus:ring-violet-500/25'
                  }`}
                />
                <ErrorMessage
                  name="description"
                  component="div"
                  className="text-xs text-rose-400 font-medium"
                />
              </div>

              {/* Status Field */}
              <div className="space-y-1.5">
                <label htmlFor="status" className="text-xs font-semibold text-slate-355 tracking-wide uppercase">
                  Status
                </label>
                <Field
                  id="status"
                  name="status"
                  as="select"
                  className="w-full bg-slate-950 border border-slate-800 rounded-xl px-4 py-3 text-sm text-slate-100 focus:outline-none focus:ring-2 focus:border-violet-500/50 focus:ring-violet-500/25 transition-all"
                >
                  <option value="Pending">Pending</option>
                  <option value="Completed">Completed</option>
                </Field>
              </div>

              {/* Action Buttons */}
              <div className="flex items-center justify-end gap-3 pt-3 border-t border-slate-850">
                <button
                  type="button"
                  onClick={onClose}
                  className="px-4 py-2.5 rounded-xl border border-slate-800 text-slate-300 font-semibold text-sm hover:bg-slate-800 hover:text-white transition-colors"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={submitting || isSubmitting}
                  className="inline-flex items-center justify-center gap-2 bg-gradient-to-r from-violet-600 to-indigo-600 hover:from-violet-500 hover:to-indigo-500 text-white font-semibold text-sm px-5 py-2.5 rounded-xl shadow-lg shadow-violet-500/20 transition-all disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  {(submitting || isSubmitting) && <Loader2 className="w-4 h-4 animate-spin" />}
                  {task ? 'Save Changes' : 'Create Task'}
                </button>
              </div>
            </Form>
          )}
        </Formik>
      </div>
    </div>
  );
};
