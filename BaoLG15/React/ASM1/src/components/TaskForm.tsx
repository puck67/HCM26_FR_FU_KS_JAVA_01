import React, { useState } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import { X, Loader2, Save } from 'lucide-react';
import type { Task } from '../services/api';
import { useTasks } from '../context/TaskContext';

interface TaskFormProps {
  isOpen: boolean;
  onClose: () => void;
  taskToEdit?: Task | null;
}

export const TaskForm: React.FC<TaskFormProps> = ({ isOpen, onClose, taskToEdit }) => {
  const { addTask, updateTask } = useTasks();
  const [submitError, setSubmitError] = useState<string | null>(null);

  if (!isOpen) return null;

  const isEditMode = !!taskToEdit;

  const initialValues = {
    name: taskToEdit?.name || '',
    description: taskToEdit?.description || '',
  };

  const validationSchema = Yup.object().shape({
    name: Yup.string()
      .max(40, 'Name must be 40 characters or less')
      .required('Task name is required'),
    description: Yup.string()
      .max(200, 'Description must be 200 characters or less')
      .optional(),
  });

  const handleSubmit = async (
    values: typeof initialValues,
    { setSubmitting, resetForm }: { setSubmitting: (isSubmitting: boolean) => void; resetForm: () => void }
  ) => {
    setSubmitError(null);
    try {
      if (isEditMode && taskToEdit) {
        await updateTask(taskToEdit.id, values);
      } else {
        await addTask(values.name, values.description);
      }
      resetForm();
      onClose();
    } catch (err: any) {
      setSubmitError(err.message || 'An error occurred. Please try again.');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
      {/* Backdrop */}
      <div 
        className="fixed inset-0 bg-slate-950/70 backdrop-blur-sm transition-opacity"
        onClick={onClose}
      />

      {/* Modal Content */}
      <div className="glass w-full max-w-md rounded-2xl overflow-hidden shadow-2xl border border-white/10 relative z-10 animate-in fade-in zoom-in-95 duration-200">
        <div className="px-6 py-4 border-b border-white/10 flex items-center justify-between">
          <h3 className="text-lg font-bold text-white">
            {isEditMode ? 'Edit Task' : 'Create New Task'}
          </h3>
          <button
            onClick={onClose}
            className="text-slate-400 hover:text-white p-1 rounded-lg hover:bg-white/5 transition-colors"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <Formik
          initialValues={initialValues}
          validationSchema={validationSchema}
          onSubmit={handleSubmit}
        >
          {({ isSubmitting, errors, touched }) => (
            <Form className="p-6 space-y-5">
              {submitError && (
                <div className="p-3 bg-rose-500/10 border border-rose-500/20 text-rose-400 text-xs rounded-lg">
                  {submitError}
                </div>
              )}

              {/* Task Name */}
              <div className="space-y-1.5">
                <label htmlFor="name" className="block text-xs font-semibold text-slate-300 uppercase tracking-wider">
                  Task Name <span className="text-indigo-400">*</span>
                </label>
                <Field
                  type="text"
                  id="name"
                  name="name"
                  placeholder="e.g. Design homepage mockup"
                  className={`w-full px-4 py-2.5 bg-slate-900/60 border rounded-xl text-white text-sm placeholder-slate-500 focus:outline-none focus:ring-2 transition-all ${
                    errors.name && touched.name
                      ? 'border-rose-500/50 focus:ring-rose-500/20'
                      : 'border-white/10 focus:ring-indigo-500/20 focus:border-indigo-500/50'
                  }`}
                />
                <ErrorMessage
                  name="name"
                  component="p"
                  className="text-rose-400 text-xs mt-1"
                />
              </div>

              {/* Description */}
              <div className="space-y-1.5">
                <label htmlFor="description" className="block text-xs font-semibold text-slate-300 uppercase tracking-wider">
                  Description
                </label>
                <Field
                  as="textarea"
                  id="description"
                  name="description"
                  rows={4}
                  placeholder="Describe your task objectives..."
                  className={`w-full px-4 py-2.5 bg-slate-900/60 border rounded-xl text-white text-sm placeholder-slate-500 focus:outline-none focus:ring-2 transition-all resize-none ${
                    errors.description && touched.description
                      ? 'border-rose-500/50 focus:ring-rose-500/20'
                      : 'border-white/10 focus:ring-indigo-500/20 focus:border-indigo-500/50'
                  }`}
                />
                <ErrorMessage
                  name="description"
                  component="p"
                  className="text-rose-400 text-xs mt-1"
                />
              </div>

              {/* Actions */}
              <div className="flex justify-end gap-3 pt-3 border-t border-white/10">
                <button
                  type="button"
                  onClick={onClose}
                  className="px-4 py-2.5 text-slate-300 hover:text-white text-sm font-semibold hover:bg-white/5 rounded-xl transition-all"
                  disabled={isSubmitting}
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={isSubmitting}
                  className="inline-flex items-center gap-2 px-5 py-2.5 bg-indigo-600 hover:bg-indigo-500 active:bg-indigo-700 disabled:opacity-50 text-white text-sm font-semibold rounded-xl shadow-lg shadow-indigo-600/25 transition-all duration-200"
                >
                  {isSubmitting ? (
                    <>
                      <Loader2 className="w-4 h-4 animate-spin" />
                      <span>Saving...</span>
                    </>
                  ) : (
                    <>
                      <Save className="w-4 h-4" />
                      <span>{isEditMode ? 'Update Task' : 'Create Task'}</span>
                    </>
                  )}
                </button>
              </div>
            </Form>
          )}
        </Formik>
      </div>
    </div>
  );
};
