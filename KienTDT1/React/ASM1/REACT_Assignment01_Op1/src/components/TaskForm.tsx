import { ErrorMessage, Field, Form, Formik } from "formik";
import * as Yup from "yup";

interface TaskFormValues {
  name: string;
  description: string;
}

interface TaskFormProps {
  initialValues: TaskFormValues;
  submitLabel: string;
  onSubmit: (values: TaskFormValues) => void;
  onCancel: () => void;
}

const TaskSchema = Yup.object().shape({
  name: Yup.string()
    .trim()
    .required("Task name is required")
    .max(40, "Name must be 40 characters or less"),
  description: Yup.string().trim().max(200, "Description must be 200 characters or less"),
});

export default function TaskForm({ initialValues, submitLabel, onSubmit, onCancel }: TaskFormProps) {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/40 backdrop-blur-sm animate-fade-in">
      <div 
        className="relative w-full max-w-md overflow-hidden rounded-3xl border border-slate-200 bg-white p-6 shadow-xl transition-all sm:p-8 animate-scale-up"
        onClick={(e) => e.stopPropagation()}
      >
        <button
          type="button"
          onClick={onCancel}
          className="absolute top-4 right-4 rounded-xl border border-slate-100 p-2 text-slate-400 transition hover:bg-slate-50 hover:text-slate-600"
          aria-label="Close dialog"
        >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="2.5" stroke="currentColor" className="w-5 h-5">
            <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>

        <h2 className="text-xl font-bold text-slate-900">{submitLabel}</h2>
        <p className="mt-1 text-sm text-slate-500">Provide the task details below.</p>

        <Formik 
          initialValues={initialValues} 
          validationSchema={TaskSchema} 
          onSubmit={onSubmit}
        >
          {({ values }) => (
            <Form className="mt-6 space-y-5">
              <div>
                <div className="flex justify-between items-center mb-2">
                  <label className="text-sm font-semibold text-slate-700" htmlFor="name">
                    Task Name <span className="text-red-500">*</span>
                  </label>
                  <span className="text-[11px] font-medium text-slate-400">
                    {values.name.length}/40
                  </span>
                </div>
                <Field
                  id="name"
                  name="name"
                  placeholder="e.g. Design app flow dashboard"
                  className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-slate-900 outline-none transition focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-100 placeholder:text-slate-400"
                />
                <ErrorMessage name="name" component="p" className="mt-1.5 text-xs font-semibold text-red-500" />
              </div>

              <div>
                <div className="flex justify-between items-center mb-2">
                  <label className="text-sm font-semibold text-slate-700" htmlFor="description">
                    Description <span className="text-slate-400 font-normal">(optional)</span>
                  </label>
                  <span className="text-[11px] font-medium text-slate-400">
                    {values.description.length}/200
                  </span>
                </div>
                <Field
                  as="textarea"
                  id="description"
                  name="description"
                  rows={4}
                  placeholder="Provide a detailed description of the task..."
                  className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-slate-900 outline-none transition focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-100 placeholder:text-slate-400 resize-none"
                />
                <ErrorMessage name="description" component="p" className="mt-1.5 text-xs font-semibold text-red-500" />
              </div>

              <div className="mt-8 flex flex-col gap-3 sm:flex-row sm:justify-end">
                <button
                  type="button"
                  onClick={onCancel}
                  className="w-full rounded-xl border border-slate-200 bg-white px-5 py-3 text-sm font-semibold text-slate-700 transition hover:bg-slate-50 sm:w-auto"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="w-full rounded-xl bg-indigo-600 px-5 py-3 text-sm font-semibold text-white shadow-md shadow-indigo-200 transition hover:bg-indigo-700 hover:shadow-indigo-300 sm:w-auto"
                >
                  {submitLabel}
                </button>
              </div>
            </Form>
          )}
        </Formik>
      </div>
    </div>
  );
}

