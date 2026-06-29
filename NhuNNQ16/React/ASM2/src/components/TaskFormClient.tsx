'use client';

import { useFormik } from 'formik';
import * as Yup from 'yup';
import Link from 'next/link';

interface TaskFormClientProps {
  initialValues?: { name: string; description: string };
  action: (formData: FormData) => Promise<void>;
  submitLabel?: string;
}

const validationSchema = Yup.object({
  name: Yup.string()
    .required('Task title is required')
    .max(40, 'Title must be 40 characters or less'),
  description: Yup.string().max(200, 'Description must be 200 characters or less'),
});

export default function TaskFormClient({
  initialValues,
  action,
  submitLabel = 'Save Task',
}: TaskFormClientProps) {
  const formik = useFormik({
    initialValues: initialValues ?? { name: '', description: '' },
    validationSchema,
    onSubmit: async (values) => {
      const fd = new FormData();
      fd.append('name', values.name);
      fd.append('description', values.description);
      await action(fd);
    },
  });

  const nameLength = formik.values.name.length;
  const descLength = formik.values.description.length;

  return (
    <form onSubmit={formik.handleSubmit} className="space-y-6">
      {/* Task Name */}
      <div className="space-y-2">
        <div className="flex justify-between items-baseline">
          <label htmlFor="name" className="text-sm font-bold text-slate-700">
            Task Name <span className="text-rose-500">*</span>
          </label>
          <span className={`text-[10px] font-mono ${nameLength > 35 ? 'text-rose-600 font-bold' : nameLength > 25 ? 'text-amber-600' : 'text-slate-400'}`}>
            {nameLength}/40
          </span>
        </div>
        <div className="relative">
          <input
            id="name"
            type="text"
            name="name"
            placeholder="e.g. Design Dashboard Prototypes"
            value={formik.values.name}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            disabled={formik.isSubmitting}
            className={`w-full bg-white border rounded-xl px-4 py-3 text-sm text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 transition-all ${
              formik.touched.name && formik.errors.name
                ? 'border-rose-300 focus:ring-rose-500/20'
                : 'border-slate-200 focus:border-indigo-600 focus:ring-indigo-600/10'
            }`}
          />
        </div>
        {formik.touched.name && formik.errors.name && (
          <p className="text-rose-600 text-xs flex items-center gap-1 mt-1 font-semibold">
            <span className="text-sm">⚠️</span> {formik.errors.name}
          </p>
        )}
      </div>

      {/* Description */}
      <div className="space-y-2">
        <div className="flex justify-between items-baseline">
          <label htmlFor="description" className="text-sm font-bold text-slate-700">
            Description <span className="text-slate-400 text-xs font-normal">(Optional)</span>
          </label>
          <span className={`text-[10px] font-mono ${descLength > 180 ? 'text-rose-600 font-bold' : descLength > 150 ? 'text-amber-600' : 'text-slate-400'}`}>
            {descLength}/200
          </span>
        </div>
        <textarea
          id="description"
          name="description"
          placeholder="Describe the objective and criteria of the task..."
          rows={4}
          value={formik.values.description}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          disabled={formik.isSubmitting}
          className={`w-full bg-white border rounded-xl px-4 py-3 text-sm text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 transition-all resize-none ${
            formik.touched.description && formik.errors.description
              ? 'border-rose-300 focus:ring-rose-500/20'
              : 'border-slate-200 focus:border-indigo-600 focus:ring-indigo-600/10'
          }`}
        />
        {formik.touched.description && formik.errors.description && (
          <p className="text-rose-600 text-xs flex items-center gap-1 mt-1 font-semibold">
            <span className="text-sm">⚠️</span> {formik.errors.description}
          </p>
        )}
      </div>

      {/* Action Buttons */}
      <div className="flex gap-3 pt-4 border-t border-slate-200">
        <button
          type="submit"
          disabled={formik.isSubmitting || !formik.isValid}
          className="px-6 py-3 rounded-xl bg-indigo-600 hover:bg-indigo-700 text-white font-bold text-sm transition-all shadow-md active:scale-95 disabled:opacity-50 disabled:pointer-events-none flex items-center gap-2"
        >
          {formik.isSubmitting ? (
            <>
              <svg className="animate-spin -ml-1 mr-1 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              Saving...
            </>
          ) : (
            submitLabel
          )}
        </button>
        
        <Link
          href="/tasks"
          className="px-6 py-3 rounded-xl bg-slate-100 hover:bg-slate-200 border border-slate-200 text-slate-700 font-bold text-sm transition-all active:scale-95"
        >
          Cancel
        </Link>
      </div>
    </form>
  );
}
