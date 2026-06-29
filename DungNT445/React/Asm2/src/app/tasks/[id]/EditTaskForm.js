"use client";

import { ErrorMessage, Field, Form, Formik } from "formik";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";
import * as Yup from "yup";
import { updateTask } from "../../actions";

const TaskSchema = Yup.object().shape({
  name: Yup.string()
    .required("Task name is required")
    .max(40, "Task name cannot exceed 40 characters"),
  description: Yup.string()
    .max(200, "Description cannot exceed 200 characters")
    .nullable(),
});

export default function EditTaskForm({ task }) {
  const router = useRouter();
  const [errorMsg, setErrorMsg] = useState(null);

  const handleSubmit = async (values, { setSubmitting }) => {
    setErrorMsg(null);
    try {
      const formData = new FormData();
      formData.append("name", values.name);
      formData.append("description", values.description || "");

      await updateTask(task.id, formData);
      router.push("/tasks");
    } catch (err) {
      setErrorMsg(err.message || "An unexpected error occurred. Please try again.");
      setSubmitting(false);
    }
  };

  return (
    <div className="rounded-3xl border border-zinc-200 bg-white p-8 shadow-sm dark:border-zinc-800 dark:bg-zinc-900">
      <div className="border-b border-zinc-100 pb-5 dark:border-zinc-800">
        <div className="flex items-center justify-between">
          <h2 className="text-2xl font-bold tracking-tight text-zinc-900 dark:text-white">
            Edit Task
          </h2>
          <span
            className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-semibold ${
              task.completed
                ? "bg-green-50 text-green-700 dark:bg-green-950/30 dark:text-green-400"
                : "bg-amber-50 text-amber-700 dark:bg-amber-950/30 dark:text-amber-400"
            }`}
          >
            {task.completed ? "Completed" : "Pending"}
          </span>
        </div>
        <p className="mt-2 text-sm text-zinc-500 dark:text-zinc-400">
          Modify the details of your task. Changes will be saved instantly.
        </p>
      </div>

      {errorMsg && (
        <div className="mt-6 rounded-xl bg-red-50 p-4 text-sm text-red-700 dark:bg-red-950/20 dark:text-red-400">
          <div className="flex gap-2">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              strokeWidth={2}
              stroke="currentColor"
              className="h-5 w-5 shrink-0"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="M12 9v3.75m9-.75a9 9 0 11-18 0 9 9 0 0118 0zm-9 3.75h.008v.008H12v-.008z"
              />
            </svg>
            <span>{errorMsg}</span>
          </div>
        </div>
      )}

      <Formik
        initialValues={{
          name: task.name || "",
          description: task.description || "",
        }}
        validationSchema={TaskSchema}
        onSubmit={handleSubmit}
      >
        {({ isSubmitting, values }) => (
          <Form className="mt-6 space-y-6">
            {/* Task Name */}
            <div>
              <label
                htmlFor="name"
                className="block text-sm font-semibold leading-6 text-zinc-900 dark:text-zinc-200"
              >
                Task Name <span className="text-red-500">*</span>
              </label>
              <div className="relative mt-2">
                <Field
                  type="text"
                  name="name"
                  id="name"
                  placeholder="Task Name"
                  className="block w-full rounded-xl border border-zinc-300 bg-white px-4 py-3 text-zinc-900 placeholder-zinc-400 shadow-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500 dark:border-zinc-700 dark:bg-zinc-950 dark:text-white dark:placeholder-zinc-600 dark:focus:border-indigo-500 dark:focus:ring-indigo-500 text-sm"
                />
                <div className="absolute right-3 bottom-3 text-xs text-zinc-400">
                  {(values.name || "").length}/40
                </div>
              </div>
              <ErrorMessage
                name="name"
                component="div"
                className="mt-2 text-xs font-medium text-red-600 dark:text-red-400"
              />
            </div>

            {/* Description */}
            <div>
              <label
                htmlFor="description"
                className="block text-sm font-semibold leading-6 text-zinc-900 dark:text-zinc-200"
              >
                Description
              </label>
              <div className="relative mt-2">
                <Field
                  as="textarea"
                  name="description"
                  id="description"
                  rows={4}
                  placeholder="Task description..."
                  className="block w-full rounded-xl border border-zinc-300 bg-white px-4 py-3 text-zinc-900 placeholder-zinc-400 shadow-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500 dark:border-zinc-700 dark:bg-zinc-950 dark:text-white dark:placeholder-zinc-600 dark:focus:border-indigo-500 dark:focus:ring-indigo-500 text-sm resize-none"
                />
                <div className="absolute right-3 bottom-3 text-xs text-zinc-400">
                  {(values.description || "").length}/200
                </div>
              </div>
              <ErrorMessage
                name="description"
                component="div"
                className="mt-2 text-xs font-medium text-red-600 dark:text-red-400"
              />
            </div>

            {/* Actions */}
            <div className="flex items-center justify-end gap-3 border-t border-zinc-100 pt-6 dark:border-zinc-800">
              <Link
                href="/tasks"
                className="rounded-xl border border-zinc-300 bg-white px-5 py-2.5 text-sm font-semibold text-zinc-700 shadow-sm hover:bg-zinc-50 dark:border-zinc-700 dark:bg-zinc-800 dark:text-zinc-300 dark:hover:bg-zinc-700 transition-colors"
              >
                Cancel
              </Link>
              <button
                type="submit"
                disabled={isSubmitting}
                className="inline-flex justify-center rounded-xl bg-indigo-600 px-5 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600 disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-200"
              >
                {isSubmitting ? (
                  <div className="flex items-center gap-2">
                    <svg
                      className="h-4 w-4 animate-spin text-white"
                      xmlns="http://www.w3.org/2000/svg"
                      fill="none"
                      viewBox="0 0 24 24"
                    >
                      <circle
                        className="opacity-25"
                        cx="12"
                        cy="12"
                        r="10"
                        stroke="currentColor"
                        strokeWidth="4"
                      />
                      <path
                        className="opacity-75"
                        fill="currentColor"
                        d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                      />
                    </svg>
                    Saving...
                  </div>
                ) : (
                  "Save Changes"
                )}
              </button>
            </div>
          </Form>
        )}
      </Formik>
    </div>
  );
}
