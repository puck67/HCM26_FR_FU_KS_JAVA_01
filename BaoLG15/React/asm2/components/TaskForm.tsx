"use client";

import { useFormik } from "formik";
import * as Yup from "yup";
import { useRouter } from "next/navigation";

interface TaskFormProps {
  mode: "create" | "edit";
  initialValues?: {
    name: string;
    description: string;
  };
  onSubmit: (formData: FormData) => Promise<void>;
}

const validationSchema = Yup.object({
  name: Yup.string()
    .required("Task name is required")
    .max(40, "Name must be at most 40 characters"),
  description: Yup.string()
    .max(200, "Description must be at most 200 characters"),
});

export default function TaskForm({ mode, initialValues, onSubmit }: TaskFormProps) {
  const router = useRouter();

  const formik = useFormik({
    initialValues: {
      name: initialValues?.name ?? "",
      description: initialValues?.description ?? "",
    },
    validationSchema,
    onSubmit: async (values, { setSubmitting, setStatus }) => {
      try {
        const formData = new FormData();
        formData.append("name", values.name);
        formData.append("description", values.description);
        await onSubmit(formData);
      } catch {
        setStatus("An error occurred. Please try again.");
        setSubmitting(false);
      }
    },
  });

  return (
    <form onSubmit={formik.handleSubmit} noValidate className="space-y-6">
      {/* Name Field */}
      <div className="space-y-2">
        <label htmlFor="task-name" className="block text-sm font-medium text-gray-300">
          Task Name <span className="text-rose-400">*</span>
        </label>
        <div className="relative">
          <input
            id="task-name"
            name="name"
            type="text"
            placeholder="Enter task name..."
            value={formik.values.name}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            className={`w-full px-4 py-3 bg-gray-900/60 border rounded-xl text-white placeholder-gray-600 focus:outline-none focus:ring-2 transition-all duration-200 ${
              formik.touched.name && formik.errors.name
                ? "border-rose-500/60 focus:ring-rose-500/30"
                : "border-gray-700/60 focus:ring-violet-500/30 focus:border-violet-500/60"
            }`}
          />
          {/* Character count */}
          <span
            className={`absolute right-3 top-1/2 -translate-y-1/2 text-xs ${
              formik.values.name.length > 35
                ? formik.values.name.length >= 40
                  ? "text-rose-400"
                  : "text-amber-400"
                : "text-gray-600"
            }`}
          >
            {formik.values.name.length}/40
          </span>
        </div>
        {formik.touched.name && formik.errors.name && (
          <p className="text-xs text-rose-400 flex items-center gap-1">
            <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            {formik.errors.name}
          </p>
        )}
      </div>

      {/* Description Field */}
      <div className="space-y-2">
        <label htmlFor="task-description" className="block text-sm font-medium text-gray-300">
          Description{" "}
          <span className="text-gray-600 font-normal text-xs">(optional)</span>
        </label>
        <div className="relative">
          <textarea
            id="task-description"
            name="description"
            rows={4}
            placeholder="Describe the task in detail..."
            value={formik.values.description}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            className={`w-full px-4 py-3 bg-gray-900/60 border rounded-xl text-white placeholder-gray-600 focus:outline-none focus:ring-2 transition-all duration-200 resize-none ${
              formik.touched.description && formik.errors.description
                ? "border-rose-500/60 focus:ring-rose-500/30"
                : "border-gray-700/60 focus:ring-violet-500/30 focus:border-violet-500/60"
            }`}
          />
          <span
            className={`absolute right-3 bottom-3 text-xs ${
              formik.values.description.length > 180
                ? formik.values.description.length >= 200
                  ? "text-rose-400"
                  : "text-amber-400"
                : "text-gray-600"
            }`}
          >
            {formik.values.description.length}/200
          </span>
        </div>
        {formik.touched.description && formik.errors.description && (
          <p className="text-xs text-rose-400 flex items-center gap-1">
            <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            {formik.errors.description}
          </p>
        )}
      </div>

      {/* Status error */}
      {formik.status && (
        <div className="p-3 rounded-xl bg-rose-900/20 border border-rose-800/40 text-rose-400 text-sm">
          {formik.status}
        </div>
      )}

      {/* Actions */}
      <div className="flex items-center gap-3 pt-2">
        <button
          id="task-form-submit"
          type="submit"
          disabled={formik.isSubmitting || !formik.isValid}
          className="flex-1 py-3 bg-gradient-to-r from-violet-600 to-indigo-600 hover:from-violet-500 hover:to-indigo-500 disabled:from-gray-700 disabled:to-gray-700 disabled:text-gray-500 text-white font-semibold rounded-xl transition-all duration-200 shadow-lg shadow-violet-500/20 disabled:shadow-none flex items-center justify-center gap-2"
        >
          {formik.isSubmitting ? (
            <>
              <svg className="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
                <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
              </svg>
              Saving...
            </>
          ) : mode === "create" ? (
            "Create Task"
          ) : (
            "Save Changes"
          )}
        </button>

        <button
          id="task-form-cancel"
          type="button"
          onClick={() => router.push("/tasks")}
          className="px-6 py-3 bg-gray-800/60 hover:bg-gray-700/60 text-gray-300 font-semibold rounded-xl border border-gray-700/60 hover:border-gray-600 transition-all duration-200"
        >
          Cancel
        </button>
      </div>
    </form>
  );
}
