import React from "react";
import type { Course, Student } from "../types";

interface LmsModalFormProps {
  entityType: "course" | "lesson" | "student" | "enrollment";
  formData: any;
  setFormData: (data: any) => void;
  courses: Course[];
  students: Student[];
  onSubmit: (e: React.FormEvent) => void;
}

export const LmsModalForm: React.FC<LmsModalFormProps> = ({
  entityType,
  formData,
  setFormData,
  courses,
  students,
  onSubmit,
}) => {
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  return (
    <form id="lms-entity-form" onSubmit={onSubmit} style={{ contentVisibility: "auto" }}>
      {entityType === "course" && (
        <div className="form-grid">
          <div className="form-group-custom form-group-full">
            <label className="form-label-custom" htmlFor="course-title">
              <span className="material-symbols-outlined">title</span>
              Tên khóa học
            </label>
            <div className="input-with-icon">
              <span className="material-symbols-outlined input-prefix-icon">school</span>
              <input
                id="course-title"
                name="title"
                className="form-control-custom"
                value={formData.title || ""}
                onChange={handleChange}
                placeholder="Ví dụ: React & TypeScript Masterclass"
                required
              />
            </div>
          </div>

          <div className="form-group-custom">
            <label className="form-label-custom" htmlFor="course-instructor">
              <span className="material-symbols-outlined">person</span>
              Giảng viên
            </label>
            <div className="input-with-icon">
              <span className="material-symbols-outlined input-prefix-icon">co_present</span>
              <input
                id="course-instructor"
                name="instructor"
                className="form-control-custom"
                value={formData.instructor || ""}
                onChange={handleChange}
                placeholder="Tên giảng viên"
                required
              />
            </div>
          </div>

          <div className="form-group-custom">
            <label className="form-label-custom" htmlFor="course-category">
              <span className="material-symbols-outlined">category</span>
              Danh mục
            </label>
            <div className="input-with-icon">
              <span className="material-symbols-outlined input-prefix-icon">folder_open</span>
              <select
                id="course-category"
                name="category"
                className="form-control-custom"
                value={formData.category || "Frontend Development"}
                onChange={handleChange}
              >
                <option value="Frontend Development">Frontend Development</option>
                <option value="Backend Development">Backend Development</option>
                <option value="Data Science">Data Science</option>
                <option value="DevOps">DevOps</option>
              </select>
            </div>
          </div>

          <div className="form-group-custom">
            <label className="form-label-custom" htmlFor="course-duration">
              <span className="material-symbols-outlined">schedule</span>
              Thời lượng
            </label>
            <div className="input-with-icon">
              <span className="material-symbols-outlined input-prefix-icon">timer</span>
              <input
                id="course-duration"
                name="duration"
                className="form-control-custom"
                value={formData.duration || ""}
                onChange={handleChange}
                placeholder="Ví dụ: 40 Hours"
              />
            </div>
          </div>

          <div className="form-group-custom">
            <label className="form-label-custom" htmlFor="course-level">
              <span className="material-symbols-outlined">trending_up</span>
              Cấp độ
            </label>
            <div className="input-with-icon">
              <span className="material-symbols-outlined input-prefix-icon">grade</span>
              <select
                id="course-level"
                name="level"
                className="form-control-custom"
                value={formData.level || "Beginner"}
                onChange={handleChange}
              >
                <option value="Beginner">Beginner</option>
                <option value="Intermediate">Intermediate</option>
                <option value="Advanced">Advanced</option>
              </select>
            </div>
          </div>

          <div className="form-group-custom form-group-full">
            <label className="form-label-custom" htmlFor="course-status">
              <span className="material-symbols-outlined">published_with_changes</span>
              Trạng thái xuất bản
            </label>
            <div className="input-with-icon">
              <span className="material-symbols-outlined input-prefix-icon">settings</span>
              <select
                id="course-status"
                name="status"
                className="form-control-custom"
                value={formData.status || "Draft"}
                onChange={handleChange}
              >
                <option value="Draft">Bản nháp (Draft)</option>
                <option value="Published">Đã đăng (Published)</option>
              </select>
            </div>
          </div>
        </div>
      )}

      {entityType === "lesson" && (
        <div className="form-grid">
          <div className="form-group-custom form-group-full">
            <label className="form-label-custom" htmlFor="lesson-course">
              <span className="material-symbols-outlined">school</span>
              Khóa học thuộc về
            </label>
            <div className="input-with-icon">
              <span className="material-symbols-outlined input-prefix-icon">book</span>
              <select
                id="lesson-course"
                name="courseTitle"
                className="form-control-custom"
                value={formData.courseTitle || ""}
                onChange={handleChange}
                required
              >
                <option value="">Chọn khóa học</option>
                {courses.map((c) => (
                  <option key={c.id} value={c.title}>
                    {c.title}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className="form-group-custom form-group-full">
            <label className="form-label-custom" htmlFor="lesson-title">
              <span className="material-symbols-outlined">title</span>
              Tên bài học
            </label>
            <div className="input-with-icon">
              <span className="material-symbols-outlined input-prefix-icon">menu_book</span>
              <input
                id="lesson-title"
                name="title"
                className="form-control-custom"
                value={formData.title || ""}
                onChange={handleChange}
                placeholder="Ví dụ: Introduction to Props"
                required
              />
            </div>
          </div>

          <div className="form-group-custom">
            <label className="form-label-custom" htmlFor="lesson-duration">
              <span className="material-symbols-outlined">schedule</span>
              Thời lượng bài học
            </label>
            <div className="input-with-icon">
              <span className="material-symbols-outlined input-prefix-icon">timer</span>
              <input
                id="lesson-duration"
                name="duration"
                className="form-control-custom"
                value={formData.duration || ""}
                onChange={handleChange}
                placeholder="Ví dụ: 15 mins"
              />
            </div>
          </div>

          <div className="form-group-custom">
            <label className="form-label-custom" htmlFor="lesson-format">
              <span className="material-symbols-outlined">video_library</span>
              Định dạng
            </label>
            <div className="input-with-icon">
              <span className="material-symbols-outlined input-prefix-icon">movie_creation</span>
              <select
                id="lesson-format"
                name="format"
                className="form-control-custom"
                value={formData.format || "Video"}
                onChange={handleChange}
              >
                <option value="Video">Video</option>
                <option value="Article">Article</option>
                <option value="Quiz">Quiz</option>
              </select>
            </div>
          </div>
        </div>
      )}

      {entityType === "student" && (
        <div className="form-grid">
          <div className="form-group-custom form-group-full">
            <label className="form-label-custom" htmlFor="student-name">
              <span className="material-symbols-outlined">person</span>
              Họ và tên học viên
            </label>
            <div className="input-with-icon">
              <span className="material-symbols-outlined input-prefix-icon">badge</span>
              <input
                id="student-name"
                name="name"
                className="form-control-custom"
                value={formData.name || ""}
                onChange={handleChange}
                placeholder="Ví dụ: Nguyễn Văn A"
                required
              />
            </div>
          </div>

          <div className="form-group-custom form-group-full">
            <label className="form-label-custom" htmlFor="student-email">
              <span className="material-symbols-outlined">mail</span>
              Địa chỉ Email
            </label>
            <div className="input-with-icon">
              <span className="material-symbols-outlined input-prefix-icon">alternate_email</span>
              <input
                id="student-email"
                name="email"
                type="email"
                className="form-control-custom"
                value={formData.email || ""}
                onChange={handleChange}
                placeholder="email@example.com"
                required
              />
            </div>
          </div>

          <div className="form-group-custom form-group-full">
            <label className="form-label-custom" htmlFor="student-status">
              <span className="material-symbols-outlined">check_circle</span>
              Trạng thái hoạt động
            </label>
            <div className="input-with-icon">
              <span className="material-symbols-outlined input-prefix-icon">settings</span>
              <select
                id="student-status"
                name="status"
                className="form-control-custom"
                value={formData.status || "Active"}
                onChange={handleChange}
              >
                <option value="Active">Hoạt động (Active)</option>
                <option value="Inactive">Khóa hoạt động (Inactive)</option>
              </select>
            </div>
          </div>
        </div>
      )}

      {entityType === "enrollment" && (
        <div className="form-grid">
          <div className="form-group-custom form-group-full">
            <label className="form-label-custom" htmlFor="enrollment-student">
              <span className="material-symbols-outlined">person</span>
              Chọn học viên
            </label>
            <div className="input-with-icon">
              <span className="material-symbols-outlined input-prefix-icon">badge</span>
              <select
                id="enrollment-student"
                name="studentName"
                className="form-control-custom"
                value={formData.studentName || ""}
                onChange={handleChange}
                required
              >
                <option value="">Chọn học viên</option>
                {students.map((s) => (
                  <option key={s.id} value={s.name}>
                    {s.name}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className="form-group-custom form-group-full">
            <label className="form-label-custom" htmlFor="enrollment-course">
              <span className="material-symbols-outlined">school</span>
              Chọn khóa học đăng ký
            </label>
            <div className="input-with-icon">
              <span className="material-symbols-outlined input-prefix-icon">book</span>
              <select
                id="enrollment-course"
                name="courseTitle"
                className="form-control-custom"
                value={formData.courseTitle || ""}
                onChange={handleChange}
                required
              >
                <option value="">Chọn khóa học</option>
                {courses.map((c) => (
                  <option key={c.id} value={c.title}>
                    {c.title}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className="form-group-custom form-group-full">
            <label className="form-label-custom" htmlFor="enrollment-payment">
              <span className="material-symbols-outlined">payments</span>
              Trạng thái thanh toán
            </label>
            <div className="input-with-icon">
              <span className="material-symbols-outlined input-prefix-icon">credit_card</span>
              <select
                id="enrollment-payment"
                name="paymentStatus"
                className="form-control-custom"
                value={formData.paymentStatus || "Pending"}
                onChange={handleChange}
              >
                <option value="Paid">Đã thanh toán (Paid)</option>
                <option value="Pending">Chờ thanh toán (Pending)</option>
                <option value="Refunded">Đã hoàn tiền (Refunded)</option>
              </select>
            </div>
          </div>
        </div>
      )}
    </form>
  );
};
