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
    <form id="lms-entity-form" onSubmit={onSubmit}>
      {entityType === "course" && (
        <>
          <div className="form-group">
            <label htmlFor="course-title">Tên khóa học</label>
            <input
              id="course-title"
              name="title"
              className="form-control"
              value={formData.title || ""}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="course-instructor">Giảng viên</label>
            <input
              id="course-instructor"
              name="instructor"
              className="form-control"
              value={formData.instructor || ""}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="course-category">Danh mục</label>
            <select
              id="course-category"
              name="category"
              className="form-control"
              value={formData.category || "Frontend Development"}
              onChange={handleChange}
            >
              <option value="Frontend Development">Frontend Development</option>
              <option value="Backend Development">Backend Development</option>
              <option value="Data Science">Data Science</option>
              <option value="DevOps">DevOps</option>
            </select>
          </div>
          <div className="form-group">
            <label htmlFor="course-duration">Thời lượng</label>
            <input
              id="course-duration"
              name="duration"
              className="form-control"
              value={formData.duration || ""}
              onChange={handleChange}
            />
          </div>
          <div className="form-group">
            <label htmlFor="course-level">Cấp độ</label>
            <select
              id="course-level"
              name="level"
              className="form-control"
              value={formData.level || "Beginner"}
              onChange={handleChange}
            >
              <option value="Beginner">Beginner</option>
              <option value="Intermediate">Intermediate</option>
              <option value="Advanced">Advanced</option>
            </select>
          </div>
        </>
      )}

      {entityType === "lesson" && (
        <>
          <div className="form-group">
            <label htmlFor="lesson-course">Khóa học</label>
            <select
              id="lesson-course"
              name="courseTitle"
              className="form-control"
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
          <div className="form-group">
            <label htmlFor="lesson-title">Tên bài học</label>
            <input
              id="lesson-title"
              name="title"
              className="form-control"
              value={formData.title || ""}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="lesson-duration">Thời lượng</label>
            <input
              id="lesson-duration"
              name="duration"
              className="form-control"
              value={formData.duration || ""}
              onChange={handleChange}
            />
          </div>
          <div className="form-group">
            <label htmlFor="lesson-format">Định dạng</label>
            <select
              id="lesson-format"
              name="format"
              className="form-control"
              value={formData.format || "Video"}
              onChange={handleChange}
            >
              <option value="Video">Video</option>
              <option value="Article">Article</option>
              <option value="Quiz">Quiz</option>
            </select>
          </div>
        </>
      )}

      {entityType === "student" && (
        <>
          <div className="form-group">
            <label htmlFor="student-name">Tên học viên</label>
            <input
              id="student-name"
              name="name"
              className="form-control"
              value={formData.name || ""}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="student-email">Email</label>
            <input
              id="student-email"
              name="email"
              type="email"
              className="form-control"
              value={formData.email || ""}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="student-status">Trạng thái</label>
            <select
              id="student-status"
              name="status"
              className="form-control"
              value={formData.status || "Active"}
              onChange={handleChange}
            >
              <option value="Active">Active</option>
              <option value="Inactive">Inactive</option>
            </select>
          </div>
        </>
      )}

      {entityType === "enrollment" && (
        <>
          <div className="form-group">
            <label htmlFor="enrollment-student">Học viên</label>
            <select
              id="enrollment-student"
              name="studentName"
              className="form-control"
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
          <div className="form-group">
            <label htmlFor="enrollment-course">Khóa học</label>
            <select
              id="enrollment-course"
              name="courseTitle"
              className="form-control"
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
          <div className="form-group">
            <label htmlFor="enrollment-payment">Trạng thái thanh toán</label>
            <select
              id="enrollment-payment"
              name="paymentStatus"
              className="form-control"
              value={formData.paymentStatus || "Pending"}
              onChange={handleChange}
            >
              <option value="Paid">Paid</option>
              <option value="Pending">Pending</option>
              <option value="Refunded">Refunded</option>
            </select>
          </div>
        </>
      )}
    </form>
  );
};
