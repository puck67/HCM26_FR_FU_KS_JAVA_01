'use client';

import React, { useState } from 'react';
import Link from 'next/link';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import { Task } from '../types';
import { updateTask } from '../app/actions';
import { AlertCircle, CheckCircle2, RefreshCw } from 'lucide-react';

interface TaskEditFormProps {
  task: Task;
}

interface FormValues {
  name: string;
  description: string;
}

export default function TaskEditForm({ task }: TaskEditFormProps) {
  const [serverError, setServerError] = useState<string | null>(null);

  const initialValues: FormValues = {
    name: task.name,
    description: task.description || ''
  };

  const validate = (values: FormValues) => {
    const errors: { name?: string; description?: string } = {};

    if (!values.name.trim()) {
      errors.name = 'Task name is required';
    } else if (values.name.length > 40) {
      errors.name = 'Task name must be 40 characters or less';
    }

    if (values.description.length > 200) {
      errors.description = 'Description must be 200 characters or less';
    }

    return errors;
  };

  const handleSubmit = async (
    values: FormValues,
    { setSubmitting }: { setSubmitting: (isSubmitting: boolean) => void }
  ) => {
    setServerError(null);
    try {
      await updateTask(task.id, values);
    } catch (err) {
      setServerError(err instanceof Error ? err.message : 'An unexpected error occurred.');
      setSubmitting(false);
    }
  };

  return (
    <div className="glass-panel rounded-3xl p-6 md:p-8 space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 border-b border-white/5 pb-4">
        <div>
          <h1 className="text-2xl font-bold text-white">Edit Task Details</h1>
          <p className="text-xs text-gray-400 mt-1">
            Modify task attributes and hit save to persist changes.
          </p>
        </div>
        <div className="flex items-center gap-2">
          <span className="text-[10px] uppercase font-bold text-gray-500">Status:</span>
          <span className={`text-[10px] px-2.5 py-0.5 rounded-full font-medium ${
            task.completed 
              ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20' 
              : 'bg-yellow-500/10 text-yellow-400 border border-yellow-500/20'
          }`}>
            {task.completed ? 'Completed' : 'Pending'}
          </span>
        </div>
      </div>

      {serverError && (
        <div className="flex items-center gap-2.5 p-4 rounded-xl bg-rose-500/10 border border-rose-500/20 text-rose-400 text-xs">
          <AlertCircle className="w-4 h-4 shrink-0" />
          <span>{serverError}</span>
        </div>
      )}

      <Formik
        initialValues={initialValues}
        validate={validate}
        onSubmit={handleSubmit}
      >
        {({ values, errors, touched, isSubmitting }) => (
          <Form className="space-y-6">
            {/* Task Name Field */}
            <div className="space-y-2">
              <div className="flex justify-between items-center">
                <label htmlFor="name" className="text-xs font-semibold text-gray-300">
                  Task Name <span className="text-rose-500">*</span>
                </label>
                <span className={`text-[10px] ${values.name.length > 40 ? 'text-rose-400 font-bold' : 'text-gray-500'}`}>
                  {values.name.length}/40
                </span>
              </div>
              <Field
                type="text"
                name="name"
                id="name"
                placeholder="e.g., Finalize presentation slides"
                className={`w-full px-4 py-3 rounded-xl bg-slate-950/40 border text-sm text-white placeholder-gray-600 focus:outline-none focus:ring-2 transition-all ${
                  errors.name && touched.name
                    ? 'border-rose-500/50 focus:ring-rose-500/20'
                    : 'border-white/5 focus:border-indigo-500/50 focus:ring-indigo-500/20'
                }`}
              />
              <ErrorMessage 
                name="name" 
                component="div" 
                className="text-xs text-rose-400 flex items-center gap-1.5 pt-0.5"
              />
            </div>

            {/* Description Field */}
            <div className="space-y-2">
              <div className="flex justify-between items-center">
                <label htmlFor="description" className="text-xs font-semibold text-gray-300">
                  Description <span className="text-gray-500">(Optional)</span>
                </label>
                <span className={`text-[10px] ${values.description.length > 200 ? 'text-rose-400 font-bold' : 'text-gray-500'}`}>
                  {values.description.length}/200
                </span>
              </div>
              <Field
                as="textarea"
                name="description"
                id="description"
                rows={4}
                placeholder="Describe your task in more detail..."
                className={`w-full px-4 py-3 rounded-xl bg-slate-950/40 border text-sm text-white placeholder-gray-600 focus:outline-none focus:ring-2 transition-all ${
                  errors.description && touched.description
                    ? 'border-rose-500/50 focus:ring-rose-500/20'
                    : 'border-white/5 focus:border-indigo-500/50 focus:ring-indigo-500/20'
                }`}
              />
              <ErrorMessage 
                name="description" 
                component="div" 
                className="text-xs text-rose-400 flex items-center gap-1.5 pt-0.5"
              />
            </div>

            {/* Form Actions */}
            <div className="flex items-center gap-3 pt-2">
              <button
                type="submit"
                disabled={isSubmitting}
                className="flex-1 flex items-center justify-center gap-2 px-5 py-3 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-semibold shadow-lg hover:shadow-indigo-500/20 transition-all disabled:opacity-50 cursor-pointer"
              >
                {isSubmitting ? (
                  <>
                    <RefreshCw className="w-4 h-4 animate-spin" /> Saving Changes...
                  </>
                ) : (
                  <>
                    <CheckCircle2 className="w-4 h-4" /> Save Changes
                  </>
                )}
              </button>
              <Link
                href="/tasks"
                className="px-5 py-3 rounded-xl bg-white/5 hover:bg-white/10 text-gray-300 hover:text-white border border-white/5 hover:border-white/10 font-semibold text-sm transition-all text-center"
              >
                Cancel
              </Link>
            </div>
          </Form>
        )}
      </Formik>
    </div>
  );
}
