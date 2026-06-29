import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";
import { useTasks } from "../TaskContext";
import type { Task } from "../taskReducer";

// Schema xác thực theo yêu cầu PDF:
// - name: bắt buộc, tối đa 40 ký tự
// - description: tuỳ chọn, tối đa 200 ký tự
const SchemaHopLe = Yup.object().shape({
  name: Yup.string()
    .required("Tên công việc là bắt buộc.")
    .max(40, "Tên công việc không được vượt quá 40 ký tự."),
  description: Yup.string()
    .max(200, "Mô tả không được vượt quá 200 ký tự."),
  priority: Yup.string().required("Vui lòng chọn mức độ ưu tiên."),
  status: Yup.string().required("Vui lòng chọn trạng thái."),
});

interface Props {
  isOpen: boolean;
  onClose: () => void;
  mode: "add" | "edit";
  initialData?: Task;
}

export default function TaskFormModal({ isOpen, onClose, mode, initialData }: Props) {
  const { dispatch } = useTasks();

  if (!isOpen) return null;

  const giaTriMacDinh = initialData ?? {
    id: "",
    name: "",
    description: "",
    status: "Chờ xử lý" as const,
    priority: "Trung bình" as const,
  };

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm animate-fadeIn"
      onClick={onClose}
    >
      <div
        className="bg-[#141522] border border-white/10 rounded-2xl p-8 w-full max-w-lg shadow-2xl animate-scaleIn"
        onClick={(e) => e.stopPropagation()}
      >
        {/* Tiêu đề Modal */}
        <div className="flex items-center justify-between mb-6 pb-4 border-b border-white/10">
          <h2 className="text-white text-xl font-semibold">
            {mode === "add" ? "Thêm Công Việc Mới" : "Chỉnh Sửa Công Việc"}
          </h2>
          <button
            onClick={onClose}
            className="text-slate-400 hover:text-white text-2xl leading-none transition-colors bg-transparent border-none p-0 cursor-pointer"
          >
            ×
          </button>
        </div>

        <Formik
          initialValues={giaTriMacDinh}
          validationSchema={SchemaHopLe}
          enableReinitialize
          onSubmit={(values, { setSubmitting }) => {
            if (mode === "add") {
              dispatch({
                type: "ADD_TASK",
                payload: { ...values, id: `cv-${Date.now()}` },
              });
            } else {
              dispatch({ type: "UPDATE_TASK", payload: values });
            }
            setSubmitting(false);
            onClose();
          }}
        >
          {({ isSubmitting, errors, touched }) => (
            <Form className="flex flex-col gap-5">
              {/* Trường Tên */}
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-semibold text-slate-400">
                  Tên công việc <span className="text-red-400">*</span>
                </label>
                <Field
                  name="name"
                  type="text"
                  placeholder="Ví dụ: Triển khai hệ thống xác thực"
                  className={`bg-white/5 border rounded-lg px-4 py-2.5 text-white text-sm outline-none transition-all placeholder-slate-500 focus:ring-2 focus:ring-violet-500/40 ${
                    errors.name && touched.name
                      ? "border-red-500"
                      : "border-white/10 focus:border-violet-500"
                  }`}
                />
                <ErrorMessage
                  name="name"
                  component="div"
                  className="text-red-400 text-xs"
                />
              </div>

              {/* Trường Mô tả */}
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-semibold text-slate-400">Mô tả chi tiết</label>
                <Field
                  name="description"
                  as="textarea"
                  rows={3}
                  placeholder="Ghi chú hoặc mô tả ngắn về công việc..."
                  className={`bg-white/5 border rounded-lg px-4 py-2.5 text-white text-sm outline-none transition-all placeholder-slate-500 resize-none focus:ring-2 focus:ring-violet-500/40 ${
                    errors.description && touched.description
                      ? "border-red-500"
                      : "border-white/10 focus:border-violet-500"
                  }`}
                />
                <ErrorMessage
                  name="description"
                  component="div"
                  className="text-red-400 text-xs"
                />
              </div>

              {/* Ưu tiên + Trạng thái */}
              <div className="grid grid-cols-2 gap-4">
                <div className="flex flex-col gap-1.5">
                  <label className="text-sm font-semibold text-slate-400">Mức ưu tiên</label>
                  <Field
                    name="priority"
                    as="select"
                    className="bg-[#1a1c2e] border border-white/10 rounded-lg px-3 py-2.5 text-white text-sm outline-none focus:border-violet-500 focus:ring-2 focus:ring-violet-500/40 cursor-pointer"
                  >
                    <option value="Thấp">Thấp</option>
                    <option value="Trung bình">Trung bình</option>
                    <option value="Cao">Cao</option>
                  </Field>
                </div>

                <div className="flex flex-col gap-1.5">
                  <label className="text-sm font-semibold text-slate-400">Trạng thái</label>
                  <Field
                    name="status"
                    as="select"
                    className="bg-[#1a1c2e] border border-white/10 rounded-lg px-3 py-2.5 text-white text-sm outline-none focus:border-violet-500 focus:ring-2 focus:ring-violet-500/40 cursor-pointer"
                  >
                    <option value="Chờ xử lý">Chờ xử lý</option>
                    <option value="Đang thực hiện">Đang thực hiện</option>
                    <option value="Hoàn thành">Hoàn thành</option>
                  </Field>
                </div>
              </div>

              {/* Nút hành động */}
              <div className="flex justify-end gap-3 mt-2">
                <button
                  type="button"
                  onClick={onClose}
                  className="px-5 py-2.5 rounded-xl border border-white/10 text-slate-300 hover:bg-white/5 text-sm font-medium transition-colors"
                >
                  Hủy bỏ
                </button>
                <button
                  type="submit"
                  disabled={isSubmitting}
                  className="px-5 py-2.5 rounded-xl bg-violet-600 hover:bg-violet-700 text-white text-sm font-semibold transition-colors disabled:opacity-60"
                >
                  {isSubmitting ? "Đang lưu..." : mode === "add" ? "Tạo công việc" : "Lưu thay đổi"}
                </button>
              </div>
            </Form>
          )}
        </Formik>
      </div>
    </div>
  );
}
