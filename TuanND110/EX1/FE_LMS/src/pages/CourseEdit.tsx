import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useLmsStore } from '../store/useLmsStore';
import { courseService } from '../services/courseService';

export default function CourseEdit() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const course = useLmsStore((state) => 
    state.courses.find((c) => c.id === id)
  );

  const [title, setTitle] = useState('');
  const [subtitle, setSubtitle] = useState('');
  const [instructor, setInstructor] = useState('');
  const [duration, setDuration] = useState('');
  const [status, setStatus] = useState<'ACTIVE' | 'INACTIVE' | 'DRAFT'>('DRAFT');
  const [description, setDescription] = useState('');
  
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [submitting, setSubmitting] = useState(false);

  const validateField = (name: string, value: string) => {
    let errorMsg = '';
    if (name === 'title') {
      if (!value.trim()) {
        errorMsg = 'Tên khóa học là bắt buộc.';
      }
    } else if (name === 'instructor') {
      if (!value.trim()) {
        errorMsg = 'Tên giảng viên là bắt buộc.';
      }
    } else if (name === 'duration') {
      if (!value.trim()) {
        errorMsg = 'Thời lượng là bắt buộc.';
      } else {
        const hrs = Number(value);
        if (isNaN(hrs) || hrs <= 0) {
          errorMsg = 'Thời lượng phải là số nguyên dương lớn hơn 0.';
        }
      }
    }

    setErrors((prev) => {
      const next = { ...prev };
      if (errorMsg) {
        next[name] = errorMsg;
      } else {
        delete next[name];
      }
      return next;
    });
  };

  useEffect(() => {
    courseService.getAllCourses(); // Đảm bảo dữ liệu đã được nạp
  }, []);

  useEffect(() => {
    if (course) {
      setTitle(course.title);
      setSubtitle(course.subtitle || '');
      setInstructor(course.instructorName);
      setDuration(String(course.duration));
      setStatus(course.status);
      setDescription(course.description || '');
    }
  }, [course]);

  if (!course) {
    return (
      <div className="py-12 text-center text-red-600 font-semibold text-sm">
        Không tìm thấy khóa học tương ứng.
      </div>
    );
  }

  const validate = (): boolean => {
    const tempErrors: Record<string, string> = {};
    
    if (!title.trim()) {
      tempErrors.title = 'Tên khóa học là bắt buộc.';
    }
    
    if (!instructor.trim()) {
      tempErrors.instructor = 'Tên giảng viên là bắt buộc.';
    }
    
    if (!duration.trim()) {
      tempErrors.duration = 'Thời lượng là bắt buộc.';
    } else {
      const hrs = Number(duration);
      if (isNaN(hrs) || hrs <= 0) {
        tempErrors.duration = 'Thời lượng phải là số nguyên dương lớn hơn 0.';
      }
    }

    setErrors(tempErrors);
    return Object.keys(tempErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validate()) return;

    setSubmitting(true);
    try {
      await courseService.updateCourse(course.id, {
        code: course.code, // Không sửa mã
        title: title.trim(),
        subtitle: subtitle.trim() || undefined,
        instructorName: instructor.trim(),
        duration: Number(duration),
        status,
        description: description.trim() || undefined,
        category: course.category,
      });
      navigate('/courses');
    } catch (err) {
      alert('Lỗi khi cập nhật thông tin khóa học.');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="animate-fade-in flex flex-col gap-6">
      {/* Page Header */}
      <div className="flex flex-col border-b border-outline-variant pb-4">
        <h2 className="text-2xl font-bold text-on-surface">Cập Nhật Khóa Học</h2>
        <p className="text-sm text-on-surface-variant italic mt-0.5">Chỉnh sửa các thiết lập và thông tin của khóa học: <strong className="text-primary">{course.code}</strong></p>
      </div>

      {/* Form Card */}
      <div className="bg-white border border-outline-variant rounded-lg shadow-sm p-6 max-w-3xl">
        <form onSubmit={handleSubmit} className="flex flex-col gap-5">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
            {/* Code Field (Read-only) */}
            <div className="flex flex-col gap-1.5">
              <label className="text-xs font-semibold text-on-surface-variant">Mã khóa học (Không thể sửa)</label>
              <input 
                type="text" 
                value={course.code}
                disabled
                className="px-3.5 py-2 border border-outline-variant rounded-md text-sm outline-none bg-surface text-on-surface-variant/70 cursor-not-allowed opacity-75"
              />
            </div>

            {/* Duration Field */}
            <div className="flex flex-col gap-1.5">
              <label htmlFor="course-duration" className="text-xs font-semibold text-on-surface-variant">Thời lượng (Giờ) *</label>
              <input 
                id="course-duration"
                type="number" 
                placeholder="Ví dụ: 40" 
                value={duration}
                onChange={(e) => {
                  setDuration(e.target.value);
                  validateField('duration', e.target.value);
                }}
                onBlur={() => validateField('duration', duration)}
                className={`px-3.5 py-2 border rounded-md text-sm outline-none transition-all bg-white ${
                  errors.duration 
                    ? 'border-red-500 focus:ring-2 focus:ring-red-200 focus:border-red-500' 
                    : 'border-outline-variant focus:border-primary focus:ring-2 focus:ring-primary/20'
                }`}
              />
              {errors.duration && <span className="text-xs text-red-600 font-medium mt-0.5">{errors.duration}</span>}
            </div>
          </div>

          {/* Title Field */}
          <div className="flex flex-col gap-1.5">
            <label htmlFor="course-title" className="text-xs font-semibold text-on-surface-variant">Tên khóa học (Tiếng Việt) *</label>
            <input 
              id="course-title"
              type="text" 
              placeholder="Ví dụ: Lập trình Java Cơ bản" 
              value={title}
              onChange={(e) => {
                setTitle(e.target.value);
                validateField('title', e.target.value);
              }}
              onBlur={() => validateField('title', title)}
              className={`w-full px-3.5 py-2 border rounded-md text-sm outline-none transition-all bg-white ${
                errors.title 
                  ? 'border-red-500 focus:ring-2 focus:ring-red-200 focus:border-red-500' 
                  : 'border-outline-variant focus:border-primary focus:ring-2 focus:ring-primary/20'
              }`}
            />
            {errors.title && <span className="text-xs text-red-600 font-medium mt-0.5">{errors.title}</span>}
          </div>

          {/* Subtitle Field */}
          <div className="flex flex-col gap-1.5">
            <label htmlFor="course-subtitle" className="text-xs font-semibold text-on-surface-variant">Tên tiếng Anh (Tùy chọn)</label>
            <input 
              id="course-subtitle"
              type="text" 
              placeholder="Ví dụ: Java Programming Basics" 
              value={subtitle}
              onChange={(e) => setSubtitle(e.target.value)}
              className="w-full px-3.5 py-2 border border-outline-variant rounded-md text-sm outline-none focus:border-primary focus:ring-2 focus:ring-primary/20 transition-all bg-white text-on-surface"
            />
          </div>

          {/* Instructor Field */}
          <div className="flex flex-col gap-1.5">
            <label htmlFor="course-instructor" className="text-xs font-semibold text-on-surface-variant">Giảng viên phụ trách *</label>
            <input 
              id="course-instructor"
              type="text" 
              placeholder="Ví dụ: Nguyễn Văn A" 
              value={instructor}
              onChange={(e) => {
                setInstructor(e.target.value);
                validateField('instructor', e.target.value);
              }}
              onBlur={() => validateField('instructor', instructor)}
              className={`w-full px-3.5 py-2 border rounded-md text-sm outline-none transition-all bg-white ${
                errors.instructor 
                  ? 'border-red-500 focus:ring-2 focus:ring-red-200 focus:border-red-500' 
                  : 'border-outline-variant focus:border-primary focus:ring-2 focus:ring-primary/20'
              }`}
            />
            {errors.instructor && <span className="text-xs text-red-600 font-medium mt-0.5">{errors.instructor}</span>}
          </div>

          {/* Status Selection (Radio Group) */}
          <div className="flex flex-col gap-1.5">
            <label className="text-xs font-semibold text-on-surface-variant">Trạng thái khóa học</label>
            <div className="flex gap-3">
              {(['DRAFT', 'ACTIVE', 'INACTIVE'] as const).map((s) => {
                const active = status === s;
                const label = s === 'DRAFT' ? 'Bản nháp' : s === 'ACTIVE' ? 'Hoạt động' : 'Tạm dừng';
                return (
                  <div 
                    key={s}
                    className={`flex-1 py-2 px-3 border rounded-md text-sm font-medium text-center cursor-pointer select-none transition-all ${
                      active 
                        ? 'border-primary bg-primary-container text-on-primary-container font-semibold shadow-sm' 
                        : 'border-outline-variant text-on-surface hover:bg-surface'
                    }`}
                    onClick={() => setStatus(s)}
                  >
                    {label}
                  </div>
                );
              })}
            </div>
          </div>

          {/* Description Field */}
          <div className="flex flex-col gap-1.5">
            <label htmlFor="course-description" className="text-xs font-semibold text-on-surface-variant">Mô tả khóa học</label>
            <textarea 
              id="course-description"
              placeholder="Nhập nội dung mô tả chi tiết khóa học..." 
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              rows={4}
              className="w-full px-3.5 py-2 border border-outline-variant rounded-md text-sm outline-none focus:border-primary focus:ring-2 focus:ring-primary/20 transition-all bg-white text-on-surface"
            />
          </div>

          {/* Form Actions */}
          <div className="flex justify-end gap-3 border-t border-outline-variant pt-5 mt-2">
            <button 
              type="button" 
              onClick={() => navigate('/courses')}
              className="px-4 py-2 border border-outline-variant text-on-surface hover:bg-surface font-medium rounded-md text-sm transition-all cursor-pointer"
            >
              Hủy
            </button>
            <button 
              type="submit" 
              disabled={submitting}
              className="px-5 py-2 bg-primary hover:bg-primary/90 text-white font-medium rounded-md text-sm transition-all cursor-pointer shadow-sm disabled:opacity-50"
            >
              Lưu Thay Đổi
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
