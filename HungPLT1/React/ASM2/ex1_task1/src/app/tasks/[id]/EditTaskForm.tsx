"use client";

import { Formik, Form } from "formik";
import * as yup from "yup";
import { updateTask } from "@/app/actions";
import { FormField } from "@/components/FormField";
import Link from "next/link";
import { ArrowLeft, Loader2 } from "lucide-react";
import { useTransition } from "react";
import { Task } from "@/types";

const validationSchema = yup.object().shape({
  name: yup
    .string()
    .required("Name is required")
    .max(40, "Name must be at most 40 characters"),
  description: yup
    .string()
    .max(200, "Description must be at most 200 characters")
    .optional()
    .default(""),
});

interface EditTaskFormProps {
  task: Task;
}

export function EditTaskForm({ task }: EditTaskFormProps) {
  const [isPending, startTransition] = useTransition();

  return (
    <div className="flex-1 bg-slate-950 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md mx-auto space-y-6">
        <div className="flex items-center gap-2">
          <Link
            href="/tasks"
            className="inline-flex items-center justify-center p-2 rounded-xl border border-slate-800 bg-slate-800/20 hover:bg-slate-800 text-slate-400 hover:text-slate-200 transition duration-150"
          >
            <ArrowLeft size={16} />
          </Link>
          <span className="text-sm font-semibold text-slate-400">Back to Tasks</span>
        </div>

        <div className="bg-slate-900 border border-slate-800 rounded-2xl p-6 shadow-xl space-y-6">
          <div>
            <h1 className="text-2xl font-bold text-slate-100">Edit Task</h1>
            <p className="text-sm text-slate-500 font-medium">Update the name and description of your task.</p>
          </div>

          <Formik
            initialValues={{
              name: task.name,
              description: task.description || "",
            }}
            validationSchema={validationSchema}
            onSubmit={(values) => {
              startTransition(async () => {
                try {
                  await updateTask(task.id, values);
                } catch (error) {
                  console.error(error);
                }
              });
            }}
          >
            {({ isValid, dirty }) => (
              <Form className="space-y-4">
                <FormField<"name">
                  label="Task Name"
                  name="name"
                  placeholder="Enter task name..."
                />

                <FormField<"description">
                  label="Description"
                  name="description"
                  placeholder="Enter task description (optional)..."
                  isTextArea={true}
                />

                <button
                  type="submit"
                  disabled={!isValid || !dirty || isPending}
                  className="w-full inline-flex items-center justify-center gap-2 px-4 py-2.5 rounded-xl bg-violet-600 hover:bg-violet-700 text-sm font-semibold text-white shadow-lg shadow-violet-600/25 disabled:opacity-50 transition duration-150"
                >
                  {isPending ? (
                    <>
                      <Loader2 size={18} className="animate-spin" />
                      Saving Changes...
                    </>
                  ) : (
                    "Save Changes"
                  )}
                </button>
              </Form>
            )}
          </Formik>
        </div>
      </div>
    </div>
  );
}
