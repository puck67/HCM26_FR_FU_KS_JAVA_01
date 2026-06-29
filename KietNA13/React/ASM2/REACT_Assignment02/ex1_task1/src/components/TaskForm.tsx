'use client';

import { Formik, Form, Field, ErrorMessage } from 'formik';
import Link from 'next/link';
import { Task, TaskFormValues } from '@/types/task';
import { TASK_NAME_MAX, TASK_DESCRIPTION_MAX } from '@/constants/validation';
import { TASK_SCHEMA } from '@/constants/taskSchema';

interface TaskFormProps {
  task?: Task;
  onSubmit: (values: TaskFormValues) => Promise<void>;
  cancelHref?: string;
}

const initialValues = (task?: Task): TaskFormValues => ({
  name: task?.name ?? '',
  description: task?.description ?? '',
});

export default function TaskForm({ task, onSubmit, cancelHref }: TaskFormProps) {
  const isEditing = Boolean(task);

  return (
    <div className="max-w-xl mx-auto">
      <div className="bg-slate-800 border border-slate-700 rounded-2xl p-6 sm:p-8 shadow-xl">
        <h1 className="text-2xl font-bold text-white mb-6">
          {isEditing ? 'Edit task' : 'Create new task'}
        </h1>

        <Formik
          initialValues={initialValues(task)}
          validationSchema={TASK_SCHEMA}
          onSubmit={async (values, { setSubmitting }) => {
            try {
              await onSubmit(values);
            } finally {
              setSubmitting(false);
            }
          }}
        >
          {({ isSubmitting, values }) => (
            <Form className="flex flex-col gap-5">
              {/* Name field */}
              <div className="flex flex-col gap-1.5">
                <label htmlFor="name" className="text-sm font-medium text-slate-300">
                  Task name <span className="text-red-400">*</span>
                </label>
                <Field
                  id="name"
                  name="name"
                  type="text"
                  placeholder="Enter task name…"
                  className="w-full px-4 py-2.5 bg-slate-900 border border-slate-600 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500/20 rounded-lg text-slate-100 placeholder-slate-500 text-sm outline-none transition-all"
                />
                <div className="flex justify-between items-center">
                  <ErrorMessage
                    name="name"
                    component="span"
                    className="text-xs text-red-400"
                  />
                  <span className="text-xs text-slate-500 ml-auto">
                    {values.name.length}/{TASK_NAME_MAX}
                  </span>
                </div>
              </div>

              {/* Description field */}
              <div className="flex flex-col gap-1.5">
                <label htmlFor="description" className="text-sm font-medium text-slate-300">
                  Description{' '}
                  <span className="text-slate-500 font-normal">(optional)</span>
                </label>
                <Field
                  id="description"
                  name="description"
                  as="textarea"
                  rows={4}
                  placeholder="Describe the task…"
                  className="w-full px-4 py-2.5 bg-slate-900 border border-slate-600 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500/20 rounded-lg text-slate-100 placeholder-slate-500 text-sm outline-none transition-all resize-none"
                />
                <div className="flex justify-between items-center">
                  <ErrorMessage
                    name="description"
                    component="span"
                    className="text-xs text-red-400"
                  />
                  <span className="text-xs text-slate-500 ml-auto">
                    {(values.description ?? '').length}/{TASK_DESCRIPTION_MAX}
                  </span>
                </div>
              </div>

              {/* Actions */}
              <div className="flex gap-3 pt-2">
                <button
                  type="submit"
                  disabled={isSubmitting}
                  className="flex-1 flex items-center justify-center gap-2 px-6 py-2.5 bg-indigo-600 hover:bg-indigo-500 disabled:bg-indigo-800 disabled:cursor-not-allowed text-white font-semibold rounded-lg transition-all text-sm"
                >
                  {isSubmitting && (
                    <span className="inline-block w-4 h-4 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                  )}
                  {isSubmitting ? 'Saving…' : isEditing ? 'Update task' : 'Create task'}
                </button>

                {cancelHref && (
                  <Link
                    href={cancelHref}
                    className="px-5 py-2.5 bg-slate-700 hover:bg-slate-600 text-slate-300 font-semibold rounded-lg transition-all text-sm text-center"
                  >
                    Cancel
                  </Link>
                )}
              </div>
            </Form>
          )}
        </Formik>
      </div>
    </div>
  );
}
