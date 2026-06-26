import { useEffect, useState } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import { useFormik } from "formik";
import * as Yup from "yup";
import { api } from "../api";
import type { Task } from "../taskReducer";

const validationSchema = Yup.object({
  name: Yup.string().required("Task name is required").max(40, "Max 40 characters"),
  description: Yup.string().max(200, "Max 200 characters"),
});

export default function TaskForm() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const isEdit = Boolean(id);
  const [loading, setLoading] = useState(isEdit);
  const [task, setTask] = useState<Task | null>(null);

  useEffect(() => {
    if (!id) return;
    api.getTask(id).then((data) => {
      if (data) setTask(data);
      setLoading(false);
    });
  }, [id]);

  const formik = useFormik({
    initialValues: {
      name: task?.name ?? "",
      description: task?.description ?? "",
    },
    enableReinitialize: true,
    validationSchema,
    onSubmit: async (values) => {
      try {
        if (isEdit && id) {
          await api.updateTask(id, values);
          navigate(`/tasks/${id}`);
        } else {
          const created = await api.createTask(values);
          navigate(`/tasks/${created.id}`);
        }
      } catch {
        alert("Something went wrong");
      }
    },
  });

  if (loading) {
    return (
      <div className="max-w-xl mx-auto animate-pulse space-y-4">
        <div className="h-4 w-24 bg-slate-200 rounded" />
        <div className="h-8 w-48 bg-slate-200 rounded-lg" />
        <div className="h-64 bg-white rounded-(--radius-card) border border-slate-200/60" />
      </div>
    );
  }

  return (
    <div className="max-w-xl mx-auto">
      {/* Breadcrumb */}
      <nav className="flex items-center gap-2 text-sm text-slate-400 mb-6">
        <Link to="/tasks" className="hover:text-accent transition-colors">Tasks</Link>
        <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round">
          <path d="M9 18l6-6-6-6" />
        </svg>
        <span className="text-slate-600">{isEdit ? "Edit" : "New Task"}</span>
      </nav>

      <div className="bg-white rounded-(--radius-card) border border-slate-200/60 shadow-sm overflow-hidden">
        <div className="p-6 sm:p-8">
          <h1 className="text-xl font-bold text-slate-900 tracking-tight mb-6">
            {isEdit ? "Edit Task" : "Create New Task"}
          </h1>

          <form onSubmit={formik.handleSubmit} className="space-y-5">
            {/* Name field */}
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
                ) : (
                  <span />
                )}
                <span className="text-xs text-slate-300">
                  {formik.values.name.length}/40
                </span>
              </div>
            </div>

            {/* Description field */}
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
                ) : (
                  <span />
                )}
                <span className="text-xs text-slate-300">
                  {formik.values.description.length}/200
                </span>
              </div>
            </div>

            {/* Actions */}
            <div className="flex items-center gap-3 pt-2">
              <button
                type="submit"
                disabled={formik.isSubmitting}
                className="px-5 py-2.5 rounded-(--radius-btn) bg-accent text-white text-sm font-medium hover:bg-accent-dark transition-colors active:scale-[0.98] disabled:opacity-60 disabled:cursor-not-allowed"
              >
                {formik.isSubmitting
                  ? "Saving..."
                  : isEdit
                  ? "Update Task"
                  : "Create Task"}
              </button>
              <Link
                to={isEdit ? `/tasks/${id}` : "/tasks"}
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
