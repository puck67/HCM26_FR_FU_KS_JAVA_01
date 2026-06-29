"use client";

import { Formik, Form, Field } from "formik";
import { updateTask } from "@/lib/actions";
import { Task } from "@/lib/tasksStore";
import Link from "next/link";
import { ArrowLeft, Loader2, Save } from "lucide-react";
import { useState } from "react";

interface EditTaskFormProps {
  task: Task;
}

interface FormValues {
  name: string;
  description: string;
}

export default function EditTaskForm({ task }: EditTaskFormProps) {
  const [errorMsg, setErrorMsg] = useState<string | null>(null);

  const initialValues: FormValues = {
    name: task.name,
    description: task.description || "",
  };

  const validate = (values: FormValues) => {
    const errors: { name?: string; description?: string } = {};

    if (!values.name.trim()) {
      errors.name = "Tên công việc là bắt buộc.";
    } else if (values.name.trim().length > 40) {
      errors.name = "Tên công việc không được vượt quá 40 ký tự.";
    }

    if (values.description && values.description.length > 200) {
      errors.description = "Mô tả không được vượt quá 200 ký tự.";
    }

    return errors;
  };

  const handleSubmit = async (
    values: FormValues,
    { setSubmitting }: { setSubmitting: (isSubmitting: boolean) => void }
  ) => {
    setErrorMsg(null);
    try {
      await updateTask(task.id, values);
    } catch (err: any) {
      // In Next.js, redirect() throws a special redirect error that should bubble up.
      const isRedirect =
        err.message === "NEXT_REDIRECT" ||
        (err.digest && err.digest.includes("NEXT_REDIRECT"));

      if (isRedirect) {
        throw err;
      } else {
        setErrorMsg(err.message || "Có lỗi xảy ra khi cập nhật công việc.");
        setSubmitting(false);
      }
    }
  };

  return (
    <div className="max-w-2xl mx-auto">
      {/* Back button */}
      <div className="mb-6">
        <Link
          href="/tasks"
          className="inline-flex items-center gap-2 text-sm font-medium text-zinc-400 hover:text-white transition-colors group"
        >
          <ArrowLeft className="w-4 h-4 group-hover:-translate-x-1 transition-transform" />
          Quay lại danh sách
        </Link>
      </div>

      {/* Form Container */}
      <div className="rounded-2xl border border-zinc-900 bg-zinc-900/20 p-6 sm:p-8 backdrop-blur-sm">
        <h1 className="text-2xl font-bold tracking-tight text-white mb-2">
          Chỉnh sửa công việc
        </h1>
        <p className="text-sm text-zinc-400 mb-6">
          Cập nhật thông tin công việc của bạn bên dưới.
        </p>

        {errorMsg && (
          <div className="mb-6 rounded-xl border border-red-500/20 bg-red-950/15 p-4 text-sm text-red-400">
            {errorMsg}
          </div>
        )}

        <Formik
          initialValues={initialValues}
          validate={validate}
          onSubmit={handleSubmit}
        >
          {({ errors, touched, isSubmitting }) => (
            <Form className="space-y-6">
              {/* Name Field */}
              <div>
                <label
                  htmlFor="name"
                  className="block text-sm font-medium text-zinc-350 mb-2"
                >
                  Tên công việc <span className="text-red-500">*</span>
                </label>
                <Field
                  type="text"
                  name="name"
                  id="name"
                  className={`w-full rounded-xl border bg-zinc-950/50 px-4 py-3 text-sm text-white placeholder-zinc-550 outline-none transition-all focus:ring-2 focus:ring-indigo-500/25 ${
                    errors.name && touched.name
                      ? "border-red-500/50 focus:border-red-500"
                      : "border-zinc-800 focus:border-indigo-500"
                  }`}
                  placeholder="Ví dụ: Lập trình giao diện ASM2"
                />
                {errors.name && touched.name && (
                  <p className="mt-2 text-xs font-medium text-red-400">
                    {errors.name}
                  </p>
                )}
                <p className="mt-1.5 text-xs text-zinc-500 text-right">
                  Tối đa 40 ký tự
                </p>
              </div>

              {/* Description Field */}
              <div>
                <label
                  htmlFor="description"
                  className="block text-sm font-medium text-zinc-350 mb-2"
                >
                  Mô tả công việc
                </label>
                <Field
                  as="textarea"
                  name="description"
                  id="description"
                  rows={4}
                  className={`w-full rounded-xl border bg-zinc-950/50 px-4 py-3 text-sm text-white placeholder-zinc-550 outline-none transition-all focus:ring-2 focus:ring-indigo-500/25 ${
                    errors.description && touched.description
                      ? "border-red-500/50 focus:border-red-500"
                      : "border-zinc-800 focus:border-indigo-500"
                  }`}
                  placeholder="Mô tả ngắn gọn về các bước cần làm..."
                />
                {errors.description && touched.description && (
                  <p className="mt-2 text-xs font-medium text-red-400">
                    {errors.description}
                  </p>
                )}
                <p className="mt-1.5 text-xs text-zinc-500 text-right">
                  Tối đa 200 ký tự
                </p>
              </div>

              {/* Action Buttons */}
              <div className="flex items-center justify-end gap-4 pt-4 border-t border-zinc-900">
                <Link
                  href="/tasks"
                  className="inline-flex items-center justify-center rounded-xl border border-zinc-800 bg-zinc-900/50 hover:bg-zinc-900 px-4 py-2.5 text-sm font-semibold text-zinc-400 hover:text-white transition-colors"
                >
                  Hủy
                </Link>
                <button
                  type="submit"
                  disabled={isSubmitting}
                  className="inline-flex items-center justify-center gap-2 rounded-xl bg-indigo-600 px-5 py-2.5 text-sm font-semibold text-white shadow-md shadow-indigo-600/10 hover:bg-indigo-500 hover:scale-[1.02] active:scale-[0.98] disabled:opacity-50 disabled:pointer-events-none transition-all duration-200 cursor-pointer"
                >
                  {isSubmitting ? (
                    <>
                      <Loader2 className="w-4 h-4 animate-spin" />
                      Đang lưu...
                    </>
                  ) : (
                    <>
                      <Save className="w-4 h-4" />
                      Cập nhật
                    </>
                  )}
                </button>
              </div>
            </Form>
          )}
        </Formik>
      </div>
    </div>
  );
}
