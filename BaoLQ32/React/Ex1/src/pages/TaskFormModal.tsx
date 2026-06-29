import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";
import { useTasks } from "../TaskContext";
import type { Task } from "../taskReducer";

// Validation schema using Yup as requested by pdf validation rules:
// - name: required, max 40 chars
// - description: optional, max 200 chars
const TaskSchema = Yup.object().shape({
  name: Yup.string()
    .required("Tên công việc là bắt buộc")
    .max(40, "Tên công việc không quá 40 ký tự"),
  description: Yup.string()
    .max(200, "Mô tả công việc không quá 200 ký tự"),
  status: Yup.string().required("Trạng thái là bắt buộc"),
  priority: Yup.string().required("Độ ưu tiên là bắt buộc")
});

export default function TaskFormModal({
  isOpen,
  onClose,
  initialData,
  mode
}: {
  isOpen: boolean;
  onClose: () => void;
  initialData?: Task;
  mode: "add" | "edit";
}) {
  const { tasks, dispatch } = useTasks();

  const getNextId = () => {
    const maxId = tasks.reduce((max, t) => {
      const num = parseInt(t.id, 10);
      return isNaN(num) ? max : Math.max(max, num);
    }, 0);
    return String(maxId + 1);
  };

  if (!isOpen) return null;

  const defaultValues = initialData || {
    id: "",
    name: "",
    description: "",
    status: "Đang chờ" as const,
    priority: "Trung bình" as const
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>{mode === "add" ? "Tạo công việc mới" : "Chỉnh sửa công việc"}</h2>
          <button className="modal-close" onClick={onClose}>&times;</button>
        </div>

        <Formik
          initialValues={defaultValues}
          validationSchema={TaskSchema}
          onSubmit={(values, { setSubmitting }) => {
            if (mode === "add") {
              dispatch({
                type: "ADD_TASK",
                payload: {
                  ...values,
                  id: getNextId()
                }
              });
            } else {
              dispatch({
                type: "UPDATE_TASK",
                payload: values
              });
            }
            setSubmitting(false);
            onClose();
          }}
        >
          {({ isSubmitting, errors, touched }) => (
            <Form className="modal-form">
              <div className="form-group">
                <label>Tên công việc *</label>
                <Field
                  name="name"
                  type="text"
                  placeholder="Ví dụ: Thiết kế cơ sở dữ liệu"
                  className={errors.name && touched.name ? "error-field" : ""}
                />
                <ErrorMessage name="name" component="div" className="form-error-msg" />
              </div>

              <div className="form-group">
                <label>Mô tả chi tiết</label>
                <Field
                  name="description"
                  as="textarea"
                  rows={3}
                  placeholder="Nhập ghi chú hoặc mô tả ngắn..."
                  className={errors.description && touched.description ? "error-field" : ""}
                />
                <ErrorMessage name="description" component="div" className="form-error-msg" />
              </div>

              <div className="form-group">
                <label>Mức độ ưu tiên *</label>
                <Field name="priority" as="select">
                  <option value="Thấp">Thấp</option>
                  <option value="Trung bình">Trung bình</option>
                  <option value="Cao">Cao</option>
                </Field>
              </div>

              <div className="form-group">
                <label>Trạng thái công việc *</label>
                <Field name="status" as="select">
                  <option value="Đang chờ">Đang chờ</option>
                  <option value="Đang thực hiện">Đang thực hiện</option>
                  <option value="Hoàn thành">Hoàn thành</option>
                </Field>
              </div>

              <div className="modal-actions">
                <button type="button" className="btn-cancel" onClick={onClose} disabled={isSubmitting}>
                  Hủy bỏ
                </button>
                <button type="submit" className="btn-submit" disabled={isSubmitting}>
                  {isSubmitting ? "Đang lưu..." : "Lưu lại"}
                </button>
              </div>
            </Form>
          )}
        </Formik>
      </div>
    </div>
  );
}
