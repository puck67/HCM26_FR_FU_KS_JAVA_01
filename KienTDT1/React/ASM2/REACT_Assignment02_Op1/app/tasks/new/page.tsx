"use client";

import { useTransition, useState } from "react";
import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";
import { createTask } from "@/lib/actions";
import Link from "next/link";

const TaskSchema = Yup.object().shape({
  name: Yup.string()
    .max(40, "Name must be 40 characters or less")
    .required("Name is required"),
  description: Yup.string()
    .max(200, "Description must be 200 characters or less")
    .optional(),
});

export default function NewTaskPage() {
  const [isPending, startTransition] = useTransition();
  const [error, setError] = useState<string | null>(null);

  return (
    <div className="max-w-xl mx-auto space-y-6 animate-slide-up">
      <div className="flex items-center gap-3">
        <Link
          href="/tasks"
          className="p-2 rounded-lg bg-slate-900 text-slate-400 hover:text-white border border-slate-800/80 transition-colors"
        >
          <svg
            className="h-5 w-5"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
            strokeWidth="2"
          >
            <path strokeLinecap="round" strokeLinejoin="round" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
          </svg>
        </Link>
        <div>
          <h1 className="text-2xl font-extrabold text-white tracking-tight">Create New Task</h1>
          <p className="text-slate-400 text-xs mt-0.5">Define your objective and guidelines.</p>
        </div>
      </div>

      <div className="glass-panel rounded-2xl p-6 sm:p-8 border border-slate-800/80">
        <Formik
          initialValues={{ name: "", description: "" }}
          validationSchema={TaskSchema}
          onSubmit={async (values, { setSubmitting }) => {
            setError(null);
            startTransition(async () => {
              try {
                const formData = new FormData();
                formData.append("name", values.name);
                formData.append("description", values.description || "");
                await createTask(formData);
              } catch (e: any) {
                if (e.message && !e.message.includes("NEXT_REDIRECT")) {
                  setError(e.message);
                }
              } finally {
                setSubmitting(false);
              }
            });
          }}
        >
          {({ isSubmitting, errors, touched }) => (
            <Form className="space-y-6">
              {error && (
                <div className="p-3.5 rounded-lg bg-rose-500/10 border border-rose-500/20 text-xs font-semibold text-rose-400">
                  {error}
                </div>
              )}

              <div className="space-y-2">
                <label htmlFor="name" className="text-sm font-semibold text-slate-300">
                  Task Name <span className="text-indigo-400">*</span>
                </label>
                <Field
                  id="name"
                  name="name"
                  type="text"
                  placeholder="e.g. Design Landing Page"
                  className={`w-full px-4 py-3 rounded-xl bg-slate-900/60 border ${
                    errors.name && touched.name ? "border-rose-500/50" : "border-slate-800/80 focus:border-indigo-500/50"
                  } text-slate-200 placeholder-slate-600 focus:outline-none focus:ring-1 focus:ring-indigo-500/30 transition-all`}
                />
                <ErrorMessage
                  name="name"
                  component="div"
                  className="text-rose-400 text-xs font-medium"
                />
              </div>

              <div className="space-y-2">
                <label htmlFor="description" className="text-sm font-semibold text-slate-300">
                  Description <span className="text-slate-500">(Optional)</span>
                </label>
                <Field
                  id="description"
                  name="description"
                  as="textarea"
                  rows={4}
                  placeholder="Provide a detailed description of the task details..."
                  className={`w-full px-4 py-3 rounded-xl bg-slate-900/60 border ${
                    errors.description && touched.description ? "border-rose-500/50" : "border-slate-800/80 focus:border-indigo-500/50"
                  } text-slate-200 placeholder-slate-600 focus:outline-none focus:ring-1 focus:ring-indigo-500/30 transition-all resize-none`}
                />
                <ErrorMessage
                  name="description"
                  component="div"
                  className="text-rose-400 text-xs font-medium"
                />
              </div>

              <div className="flex items-center justify-end gap-3 pt-4 border-t border-slate-900">
                <Link
                  href="/tasks"
                  className="px-5 py-2.5 rounded-xl font-semibold text-sm bg-slate-900 text-slate-400 border border-slate-800/80 hover:text-white hover:bg-slate-800/50 transition-all duration-300"
                >
                  Cancel
                </Link>
                <button
                  type="submit"
                  disabled={isSubmitting || isPending}
                  className="px-5 py-2.5 rounded-xl font-semibold text-sm bg-gradient-to-r from-indigo-500 to-violet-600 text-white shadow-lg shadow-indigo-500/10 hover:shadow-indigo-500/25 hover:scale-[1.02] active:scale-[0.98] disabled:opacity-50 disabled:scale-100 transition-all duration-300 flex items-center gap-2"
                >
                  {(isSubmitting || isPending) && (
                    <svg className="animate-spin -ml-1 mr-1 h-4 w-4 text-white" fill="none" viewBox="0 0 24 24">
                      <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
                      <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z" />
                    </svg>
                  )}
                  Create Task
                </button>
              </div>
            </Form>
          )}
        </Formik>
      </div>
    </div>
  );
}
