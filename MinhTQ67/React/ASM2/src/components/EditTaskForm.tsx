"use client";

import { useFormik } from "formik";
import * as Yup from "yup";
import Link from "next/link";
import { updateTask } from "@/lib/actions";
import { Task } from "@/types/task";

interface Props {
  task: Task;
}

const validationSchema = Yup.object({
  name: Yup.string()
    .required("Task name is required")
    .max(40, "Name must be at most 40 characters"),
  description: Yup.string().max(200, "Description must be at most 200 characters"),
});

export default function EditTaskForm({ task }: Props) {
  const formik = useFormik({
    initialValues: {
      name: task.name,
      description: task.description,
    },
    validationSchema,
    onSubmit: async (values, { setSubmitting, setStatus }) => {
      try {
        const formData = new FormData();
        formData.set("name", values.name);
        formData.set("description", values.description);
        await updateTask(task.id, formData);
      } catch {
        setStatus("An error occurred. Please try again.");
        setSubmitting(false);
      }
    },
  });

  return (
    <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
      <div className="mb-6">
        <h1 className="text-xl font-bold text-gray-900">Edit Task</h1>
        <p className="text-gray-500 text-sm mt-1">Update the task details below.</p>
      </div>

      <form onSubmit={formik.handleSubmit} className="space-y-5">
        {formik.status && (
          <div className="text-sm text-red-600 bg-red-50 border border-red-100 rounded-lg px-4 py-3">
            {formik.status}
          </div>
        )}

        {/* Name */}
        <div>
          <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-1">
            Task Name <span className="text-red-500">*</span>
          </label>
          <input
            id="name"
            type="text"
            placeholder="Enter task name..."
            {...formik.getFieldProps("name")}
            className={`w-full px-4 py-2.5 border rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition ${
              formik.touched.name && formik.errors.name ? "border-red-300" : "border-gray-300"
            }`}
          />
          <div className="flex justify-between mt-1">
            {formik.touched.name && formik.errors.name ? (
              <p className="text-red-500 text-xs">{formik.errors.name}</p>
            ) : (
              <span />
            )}
            <span className="text-xs text-gray-400 ml-auto">
              {formik.values.name.length}/40
            </span>
          </div>
        </div>

        {/* Description */}
        <div>
          <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-1">
            Description{" "}
            <span className="text-gray-400 font-normal">(optional)</span>
          </label>
          <textarea
            id="description"
            rows={4}
            placeholder="Describe the task..."
            {...formik.getFieldProps("description")}
            className={`w-full px-4 py-2.5 border rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition resize-none ${
              formik.touched.description && formik.errors.description
                ? "border-red-300"
                : "border-gray-300"
            }`}
          />
          <div className="flex justify-between mt-1">
            {formik.touched.description && formik.errors.description ? (
              <p className="text-red-500 text-xs">{formik.errors.description}</p>
            ) : (
              <span />
            )}
            <span className="text-xs text-gray-400 ml-auto">
              {formik.values.description.length}/200
            </span>
          </div>
        </div>

        {/* Submit */}
        <button
          type="submit"
          disabled={formik.isSubmitting}
          className="w-full py-2.5 px-6 bg-indigo-600 text-white rounded-lg text-sm font-semibold hover:bg-indigo-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
        >
          {formik.isSubmitting ? "Saving..." : "Save Changes"}
        </button>

        <Link
          href="/tasks"
          className="block w-full text-center py-2.5 border border-gray-200 text-gray-600 rounded-lg text-sm font-medium hover:bg-gray-50 transition-colors"
        >
          Cancel
        </Link>
      </form>
    </div>
  );
}
