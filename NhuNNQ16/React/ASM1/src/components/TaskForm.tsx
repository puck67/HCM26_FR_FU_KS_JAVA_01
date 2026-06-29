import { useFormik } from 'formik';
import * as Yup from 'yup';

interface TaskFormProps {
  initialValues?: { name: string; description: string };
  onSubmit: (name: string, description: string) => void;
  onCancel: () => void;
  submitLabel?: string;
}

// Schema validation sử dụng Yup theo đúng yêu cầu đề bài:
// - name: bắt buộc, tối đa 40 ký tự
// - description: không bắt buộc, tối đa 200 ký tự
const validationSchema = Yup.object({
  name: Yup.string()
    .required('Tên công việc là bắt buộc')
    .max(40, 'Tên công việc không được vượt quá 40 ký tự'),
  description: Yup.string()
    .max(200, 'Mô tả không được vượt quá 200 ký tự'),
});

export default function TaskForm({ initialValues, onSubmit, onCancel, submitLabel = 'Tạo Task' }: TaskFormProps) {
  const formik = useFormik({
    initialValues: initialValues ?? { name: '', description: '' },
    validationSchema,
    enableReinitialize: true,
    onSubmit: (values, { resetForm }) => {
      onSubmit(values.name, values.description);
      resetForm();
    },
  });

  const nameLength = formik.values.name.length;
  const descLength = formik.values.description.length;

  return (
    <form onSubmit={formik.handleSubmit} className="space-y-6">
      {/* Trường Tên Task */}
      <div>
        <div className="flex justify-between items-center mb-2">
          <label htmlFor="name" className="block text-sm font-semibold text-slate-300">
            Tên Task <span className="text-rose-500">*</span>
          </label>
          <span className={`text-xs font-mono ${nameLength > 40 ? 'text-rose-500' : 'text-slate-500'}`}>
            {nameLength}/40
          </span>
        </div>
        <div className="relative">
          <input
            id="name"
            type="text"
            name="name"
            placeholder="Ví dụ: Thiết kế giao diện Dashboard"
            value={formik.values.name}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            className={`w-full bg-slate-800/80 border rounded-xl px-4 py-3 text-slate-100 placeholder-slate-500 transition duration-200 outline-none focus:ring-2 ${
              formik.touched.name && formik.errors.name
                ? 'border-rose-500/80 focus:ring-rose-500/30'
                : 'border-slate-700 focus:border-indigo-500 focus:ring-indigo-500/20'
            }`}
          />
        </div>
        {formik.touched.name && formik.errors.name ? (
          <div className="flex items-center gap-1.5 mt-1.5 text-rose-400 text-xs font-medium animate-fadeIn">
            <svg className="w-3.5 h-3.5 flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
            </svg>
            <span>{formik.errors.name}</span>
          </div>
        ) : null}
      </div>

      {/* Trường Mô tả */}
      <div>
        <div className="flex justify-between items-center mb-2">
          <label htmlFor="description" className="block text-sm font-semibold text-slate-300">
            Mô tả chi tiết
          </label>
          <span className={`text-xs font-mono ${descLength > 200 ? 'text-rose-500' : 'text-slate-500'}`}>
            {descLength}/200
          </span>
        </div>
        <textarea
          id="description"
          name="description"
          placeholder="Nhập nội dung mô tả công việc (không bắt buộc)..."
          rows={4}
          value={formik.values.description}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          className={`w-full bg-slate-800/80 border rounded-xl px-4 py-3 text-slate-100 placeholder-slate-500 transition duration-200 outline-none focus:ring-2 resize-none ${
            formik.touched.description && formik.errors.description
              ? 'border-rose-500/80 focus:ring-rose-500/30'
              : 'border-slate-700 focus:border-indigo-500 focus:ring-indigo-500/20'
          }`}
        />
        {formik.touched.description && formik.errors.description ? (
          <div className="flex items-center gap-1.5 mt-1.5 text-rose-400 text-xs font-medium animate-fadeIn">
            <svg className="w-3.5 h-3.5 flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
            </svg>
            <span>{formik.errors.description}</span>
          </div>
        ) : null}
      </div>

      {/* Cụm nút hành động */}
      <div className="flex justify-end gap-3 pt-2">
        <button
          type="button"
          onClick={onCancel}
          className="px-5 py-2.5 rounded-xl border border-slate-700 text-slate-300 font-medium hover:bg-slate-800 hover:text-white transition duration-200 text-sm focus:outline-none"
        >
          Hủy bỏ
        </button>
        <button
          type="submit"
          className="px-5 py-2.5 rounded-xl bg-indigo-600 text-white font-medium hover:bg-indigo-500 shadow-lg shadow-indigo-600/20 active:scale-[0.98] transition duration-200 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500/40"
        >
          {submitLabel}
        </button>
      </div>
    </form>
  );
}
