"use client";

import { useFormik } from "formik";
import * as Yup from "yup";
import { useTransition } from "react";
import Link from "next/link";
import type { Task } from "@/lib/data";
import { updateTask, deleteTask, toggleTaskCompleted } from "@/lib/actions";
import { useRouter } from "next/navigation";

const validationSchema = Yup.object({
  name: Yup.string().required("Task name is required").max(40, "Max 40 characters"),
  description: Yup.string().max(200, "Max 200 characters"),
});

export function TaskEditForm({ task }: { task: Task }) {
  const [isPending, startTransition] = useTransition();
  const router = useRouter();

  const formik = useFormik({
    initialValues: {
      name: task.name,
      description: task.description,
    },
    validationSchema,
    onSubmit: (values) => {
      startTransition(() => {
        updateTask(task.id, { name: values.name, description: values.description || "" });
      });
    },
  });

  const handleDelete = () => {
    if (!confirm("Delete this task?")) return;
    startTransition(async () => {
      await deleteTask(task.id);
      router.push("/tasks");
    });
  };

  const handleToggle = () => {
    startTransition(async () => {
      await toggleTaskCompleted(task.id);
      router.refresh();
    });
  };

  return (
    <div className="bg-white rounded-(--radius-card) border border-slate-200/60 shadow-sm overflow-hidden">
      <div className="p-6 sm:p-8">
        <h2 className="text-lg font-bold text-slate-900 tracking-tight mb-5">Edit Task</h2>

        <form onSubmit={formik.handleSubmit} className="space-y-5">
          {/* Name */}
          <div className="space-y-1.5">
            <label htmlFor="name" className="block text-sm font-medium text-slate-700">
              Task Name <span className="text-danger">*</span>
            </label>
            <input
              id="name"
              type="text"
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
          <div className="flex flex-wrap items-center gap-2 pt-2">
            <button
              type="submit"
              disabled={isPending}
              className="px-5 py-2.5 rounded-(--radius-btn) bg-accent text-white text-sm font-medium hover:bg-accent-dark transition-colors active:scale-[0.98] disabled:opacity-60 disabled:cursor-not-allowed"
            >
              {isPending ? "Saving..." : "Update Task"}
            </button>
            <button
              type="button"
              onClick={handleToggle}
              disabled={isPending}
              className="px-4 py-2.5 rounded-(--radius-btn) bg-slate-100 text-slate-700 text-sm font-medium hover:bg-slate-200 transition-colors disabled:opacity-60"
            >
              {task.completed ? "Mark Incomplete" : "Mark Completed"}
            </button>
            <button
              type="button"
              onClick={handleDelete}
              disabled={isPending}
              className="px-4 py-2.5 rounded-(--radius-btn) bg-white border border-slate-200 text-sm font-medium text-danger hover:bg-danger-light transition-colors disabled:opacity-60"
            >
              Delete
            </button>
            <Link
              href="/tasks"
              className="px-4 py-2.5 rounded-(--radius-btn) bg-white border border-slate-200 text-sm font-medium text-slate-600 hover:bg-slate-50 transition-colors ml-auto"
            >
              Back
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
}
