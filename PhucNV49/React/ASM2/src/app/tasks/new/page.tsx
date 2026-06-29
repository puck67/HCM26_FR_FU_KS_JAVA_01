"use client";

import { useFormik } from "formik";
import * as Yup from "yup";
import Link from "next/link";
import { useTransition } from "react";
import { createTask } from "@/lib/actions";

const validationSchema = Yup.object({
  name: Yup.string().required("Task name is required").max(40, "Max 40 characters"),
  description: Yup.string().max(200, "Max 200 characters"),
});

export default function NewTaskPage() {
  const [isPending, startTransition] = useTransition();

  const formik = useFormik({
    initialValues: { name: "", description: "" },
    validationSchema,
    onSubmit: (values) => {
      startTransition(() => {
        createTask({ name: values.name, description: values.description || "" });
      });
    },
  });

  return (
    <div className="max-w-xl mx-auto">
      {/* Breadcrumb */}
      <nav className="flex items-center gap-2 text-sm text-slate-400 mb-6">
        <Link href="/tasks" className="hover:text-accent transition-colors">Tasks</Link>
        <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round">
          <path d="M9 18l6-6-6-6" />
        </svg>
        <span className="text-slate-600">New Task</span>
      </nav>

      <div className="bg-white rounded-(--radius-card) border border-slate-200/60 shadow-sm overflow-hidden">
        <div className="p-6 sm:p-8">
          <h1 className="text-xl font-bold text-slate-900 tracking-tight mb-6">
            Create New Task
          </h1>

          <form onSubmit={formik.handleSubmit} className="space-y-5">
            {/* Name */}
            <div className="space-y-1.5">
              <label htmlFor="name" className="block text-sm font-medium text-slate-700">
                Task Name <span className="text-danger">*</span>
              </label>
              <input
                id="name"
                type="text"
                placeholder="Enter task name"
                {...formik.getFieldProps("name")}
                className={`w-full px-4 py-2.5 rounded-(--radius-btn) border text-sm transition-colors outline-none ${
                  formik.touched.name && formik.errors.name
                    ? "border-danger bg-danger-light/50 focus:ring-2 focus:ring-danger/20"
                    : "border-slate-200 bg-white focus:border-accent focus:ring-2 focus:ring-accent/20"
                }`}
              />
              <div className="flex items-center justify-between">
                {formik.touched.name && formik.errors.name ? (
                  <p className="text-xs text-danger">{formik.errors.name}</p>
                ) : <span />}
                <span className="text-xs text-slate-300">{formik.values.name.length}/40</span>
              </div>
            </div>

            {/* Description */}
            <div className="space-y-1.5">
              <label htmlFor="description" className="block text-sm font-medium text-slate-700">
                Description <span className="text-slate-300 font-normal">(optional)</span>
              </label>
              <textarea
                id="description"
                rows={4}
                placeholder="Describe the task"
                {...formik.getFieldProps("description")}
                className={`w-full px-4 py-2.5 rounded-(--radius-btn) border text-sm transition-colors outline-none resize-none ${
                  formik.touched.description && formik.errors.description
                    ? "border-danger bg-danger-light/50 focus:ring-2 focus:ring-danger/20"
                    : "border-slate-200 bg-white focus:border-accent focus:ring-2 focus:ring-accent/20"
                }`}
              />
              <div className="flex items-center justify-between">
                {formik.touched.description && formik.errors.description ? (
                  <p className="text-xs text-danger">{formik.errors.description}</p>
                ) : <span />}
                <span className="text-xs text-slate-300">{(formik.values.description || "").length}/200</span>
              </div>
            </div>

            {/* Actions */}
            <div className="flex items-center gap-3 pt-2">
              <button
                type="submit"
                disabled={isPending}
                className="px-5 py-2.5 rounded-(--radius-btn) bg-accent text-white text-sm font-medium hover:bg-accent-dark transition-colors active:scale-[0.98] disabled:opacity-60 disabled:cursor-not-allowed"
              >
                {isPending ? "Creating..." : "Create Task"}
              </button>
              <Link
                href="/tasks"
                className="px-5 py-2.5 rounded-(--radius-btn) bg-slate-100 text-slate-600 text-sm font-medium hover:bg-slate-200 transition-colors"
              >
                Cancel
              </Link>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
