"use client";

import { Formik, Form, Field, ErrorMessage } from "formik";
import Link from "next/link";
import { createTask } from "../../actions";

interface FormValues {
  name: string;
  description: string;
}

export default function NewTaskPage() {
  const initialValues: FormValues = {
    name: "",
    description: "",
  };

  const validate = (values: FormValues) => {
    const errors: Partial<FormValues> = {};
    if (!values.name) {
      errors.name = "Task name is required";
    } else if (values.name.length > 40) {
      errors.name = "Task name must be 40 characters or less";
    }

    if (values.description && values.description.length > 200) {
      errors.description = "Description must be 200 characters or less";
    }

    return errors;
  };

  return (
    <div className="max-w-xl mx-auto space-y-6 py-4">
      <div>
        <h1 className="text-2xl font-bold tracking-tight text-white sm:text-3xl">Create New Task</h1>
        <p className="mt-1 text-sm text-zinc-400">
          Enter details below to register a new task.
        </p>
      </div>

      <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-6 shadow-md">
        <Formik
          initialValues={initialValues}
          validate={validate}
          onSubmit={async (values, { setSubmitting, setErrors }) => {
            try {
              const formData = new FormData();
              formData.append("name", values.name);
              formData.append("description", values.description);
              await createTask(formData);
            } catch (err: any) {
              setErrors({ name: err.message || "Failed to create task" });
            } finally {
              setSubmitting(false);
            }
          }}
        >
          {({ isSubmitting, errors, touched }) => (
            <Form className="space-y-5">
              {/* Task Name */}
              <div>
                <label htmlFor="name" className="block text-sm font-medium text-zinc-300">
                  Task Name <span className="text-red-500">*</span>
                </label>
                <div className="mt-1.5">
                  <Field
                    type="text"
                    name="name"
                    id="name"
                    placeholder="Enter task name"
                    className={`block w-full rounded-lg border bg-zinc-950 px-4 py-2.5 text-sm text-white placeholder-zinc-500 focus:outline-none focus:ring-2 focus:ring-teal-500/20 ${
                      errors.name && touched.name
                        ? "border-red-500/80 focus:border-red-500 focus:ring-red-500/10"
                        : "border-zinc-800 focus:border-teal-500/80"
                    }`}
                  />
                </div>
                <ErrorMessage
                  name="name"
                  component="div"
                  className="mt-1.5 text-xs font-medium text-red-400"
                />
              </div>

              {/* Task Description */}
              <div>
                <label htmlFor="description" className="block text-sm font-medium text-zinc-300">
                  Description <span className="text-zinc-500 text-xs">(Optional)</span>
                </label>
                <div className="mt-1.5">
                  <Field
                    as="textarea"
                    name="description"
                    id="description"
                    rows={4}
                    placeholder="Describe your task details..."
                    className={`block w-full rounded-lg border bg-zinc-950 px-4 py-2.5 text-sm text-white placeholder-zinc-500 focus:outline-none focus:ring-2 focus:ring-teal-500/20 ${
                      errors.description && touched.description
                        ? "border-red-500/80 focus:border-red-500 focus:ring-red-500/10"
                        : "border-zinc-800 focus:border-teal-500/80"
                    }`}
                  />
                </div>
                <ErrorMessage
                  name="description"
                  component="div"
                  className="mt-1.5 text-xs font-medium text-red-400"
                />
              </div>

              {/* Submit / Cancel Buttons */}
              <div className="flex items-center justify-end gap-3 pt-2">
                <Link
                  href="/tasks"
                  className="inline-flex items-center justify-center rounded-lg border border-zinc-800 bg-zinc-950 hover:bg-zinc-800 hover:text-white px-4 py-2.5 text-sm font-semibold text-zinc-400 transition-colors"
                >
                  Cancel
                </Link>
                <button
                  type="submit"
                  disabled={isSubmitting}
                  className="inline-flex items-center justify-center rounded-lg bg-teal-500 hover:bg-teal-400 px-5 py-2.5 text-sm font-semibold text-zinc-950 shadow-sm transition-all focus-visible:outline focus-visible:outline-2 focus-visible:outline-teal-500 disabled:opacity-50"
                >
                  {isSubmitting ? (
                    <span className="inline-block animate-spin mr-2 h-4 w-4 border-2 border-zinc-950 border-t-transparent rounded-full" />
                  ) : null}
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
