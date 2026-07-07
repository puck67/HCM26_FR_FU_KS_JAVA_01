"use client";

import { Formik, Form, Field, ErrorMessage } from "formik";
import Link from "next/link";

interface TaskFormProps {
  initialValues?: { name: string; description: string };
  onSubmitAction: (values: { name: string; description: string }) => Promise<void>;
  title: string;
}

export default function TaskFormClient({ initialValues = { name: "", description: "" }, onSubmitAction, title }: TaskFormProps) {
  const validate = (values: { name: string; description: string }) => {
    const errors: Record<string, string> = {};
    if (!values.name.trim()) {
      errors.name = "Task name is required";
    } else if (values.name.length > 40) {
      errors.name = "Must be 40 characters or less";
    }

    if (values.description && values.description.length > 200) {
      errors.description = "Description must be 200 characters or less";
    }
    return errors;
  };

  return (
    <div className="max-w-md mx-auto bg-white border border-slate-200 rounded-2xl shadow-sm p-6 mt-8">
      <h2 className="text-xl font-bold text-slate-900 mb-6">{title}</h2>
      
      <Formik
        initialValues={initialValues}
        validate={validate}
        onSubmit={async (values, { setSubmitting }) => {
          try {
            await onSubmitAction(values);
          } catch (err) {
            console.error("Mutation failed:", err);
          } finally {
            setSubmitting(false);
          }
        }}
      >
        {({ isSubmitting }) => (
          <Form className="space-y-4">
            <div>
              <label className="block text-sm font-semibold text-slate-700 mb-1">Task Title *</label>
              <Field
                name="name"
                type="text"
                className="w-full px-3 py-2 border border-slate-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm text-slate-900 bg-white"
              />
              <ErrorMessage name="name" component="div" className="text-rose-500 text-xs mt-1 font-medium" />
            </div>

            <div>
              <label className="block text-sm font-semibold text-slate-700 mb-1">Description</label>
              <Field
                name="description"
                as="textarea"
                rows={4}
                className="w-full px-3 py-2 border border-slate-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm text-slate-900 bg-white"
              />
              <ErrorMessage name="description" component="div" className="text-rose-500 text-xs mt-1 font-medium" />
            </div>

            <div className="flex justify-end gap-2 pt-2">
              <Link
                href="/tasks"
                className="px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 font-medium rounded-xl text-sm transition-colors"
              >
                Cancel
              </Link>
              <button
                type="submit"
                disabled={isSubmitting}
                className="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-xl text-sm shadow-sm transition-colors disabled:opacity-50"
              >
                {isSubmitting ? "Processing..." : "Save Record"}
              </button>
            </div>
          </Form>
        )}
      </Formik>
    </div>
  );
}
