import { useEffect, useState } from 'react';
import { useLmsStore } from '../store/useLmsStore';
import { studentService } from '../services/studentService';
import type { Student } from '../types';

export default function Students() {
  const { students, isStudentsLoading } = useLmsStore();

  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState<'ALL' | 'ACTIVE' | 'SUSPENDED' | 'GRADUATED'>('ALL');
  
  // Pagination states
  const [currentPage, setCurrentPage] = useState(1);
  const ITEMS_PER_PAGE = 5;

  // Reset page when search or status filters change
  useEffect(() => {
    setCurrentPage(1);
  }, [searchQuery, statusFilter]);
  
  // States for inline form / side panel drawer
  const [showDrawer, setShowDrawer] = useState(false);
  const [editingStudent, setEditingStudent] = useState<Student | null>(null);

  // Form states
  const [studentCode, setStudentCode] = useState('');
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [status, setStatus] = useState<Student['status']>('ACTIVE');
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    studentService.getAllStudents();
  }, []);

  // Sync form inputs when editing changes
  useEffect(() => {
    if (editingStudent) {
      setStudentCode(editingStudent.studentCode);
      setFullName(editingStudent.fullName);
      setEmail(editingStudent.email);
      setPhone(editingStudent.phone);
      setStatus(editingStudent.status);
      setErrors({});
    } else {
      setStudentCode('');
      setFullName('');
      setEmail('');
      setPhone('');
      setStatus('ACTIVE');
      setErrors({});
    }
  }, [editingStudent]);

  const handleOpenAdd = () => {
    setEditingStudent(null);
    setShowDrawer(true);
  };

  const handleOpenEdit = (student: Student) => {
    setEditingStudent(student);
    setShowDrawer(true);
  };

  const handleDelete = async (student: Student) => {
    const confirm = window.confirm(`Bạn có chắc muốn xóa học viên "${student.fullName}" (${student.studentCode})?`);
    if (confirm) {
      try {
        await studentService.deleteStudent(student.id);
        useLmsStore.getState().showToast('Xóa học viên thành công!', 'success');
      } catch (err) {
        useLmsStore.getState().showToast('Có lỗi xảy ra khi xóa học viên.', 'error');
      }
    }
  };

  const validateField = (name: string, value: string) => {
    let errorMsg = '';
    if (name === 'studentCode') {
      if (!value.trim()) {
        errorMsg = 'Mã học viên là bắt buộc.';
      }
    } else if (name === 'fullName') {
      if (!value.trim()) {
        errorMsg = 'Họ tên là bắt buộc.';
      }
    } else if (name === 'email') {
      if (!value.trim()) {
        errorMsg = 'Email là bắt buộc.';
      } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value.trim())) {
        errorMsg = 'Email không hợp lệ.';
      }
    } else if (name === 'phone') {
      if (!value.trim()) {
        errorMsg = 'Số điện thoại là bắt buộc.';
      } else if (!/^(0|84)(3|5|7|8|9)[0-9]{8}$/.test(value.trim())) {
        errorMsg = 'Số điện thoại gồm 10 chữ số.';
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

  const validate = (): boolean => {
    const tempErrors: Record<string, string> = {};
    if (!studentCode.trim()) tempErrors.studentCode = 'Mã học viên là bắt buộc.';
    if (!fullName.trim()) tempErrors.fullName = 'Họ tên là bắt buộc.';
    
    if (!email.trim()) {
      tempErrors.email = 'Email là bắt buộc.';
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.trim())) {
      tempErrors.email = 'Email không hợp lệ.';
    }

    if (!phone.trim()) {
      tempErrors.phone = 'Số điện thoại là bắt buộc.';
    } else if (!/^(0|84)(3|5|7|8|9)[0-9]{8}$/.test(phone.trim())) {
      tempErrors.phone = 'Số điện thoại gồm 10 chữ số.';
    }

    setErrors(tempErrors);
    return Object.keys(tempErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validate()) return;

    setSubmitting(true);
    try {
      if (editingStudent) {
        // Update
        await studentService.updateStudent(editingStudent.id, {
          studentCode,
          fullName: fullName.trim(),
          email: email.trim(),
          phone: phone.trim(),
          status,
        });
        useLmsStore.getState().showToast('Cập nhật học viên thành công!', 'success');
      } else {
        // Create
        await studentService.createStudent({
          studentCode: studentCode.trim().toUpperCase(),
          fullName: fullName.trim(),
          email: email.trim(),
          phone: phone.trim(),
          status,
        });
        useLmsStore.getState().showToast('Thêm học viên thành công!', 'success');
      }
      setShowDrawer(false);
      setEditingStudent(null);
    } catch (err) {
      useLmsStore.getState().showToast('Có lỗi xảy ra khi lưu thông tin.', 'error');
    } finally {
      setSubmitting(false);
    }
  };

  const filteredStudents = students.filter((s) => {
    const matchesSearch = 
      s.fullName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      s.studentCode.toLowerCase().includes(searchQuery.toLowerCase()) ||
      s.email.toLowerCase().includes(searchQuery.toLowerCase()) ||
      s.phone.includes(searchQuery);
    
    const matchesStatus = statusFilter === 'ALL' || s.status === statusFilter;

    return matchesSearch && matchesStatus;
  });

  const totalPages = Math.ceil(filteredStudents.length / ITEMS_PER_PAGE);
  const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
  const paginatedStudents = filteredStudents.slice(startIndex, startIndex + ITEMS_PER_PAGE);
  // Adjust page number if it goes out of bounds (e.g. after deletion or filtering)
  useEffect(() => {
    if (currentPage > totalPages && totalPages > 0) {
      setCurrentPage(totalPages);
    }
  }, [filteredStudents.length, ITEMS_PER_PAGE, totalPages, currentPage]);
  return (
    <div className="animate-fade-in flex flex-col lg:flex-row gap-8 relative items-start h-full">
      {/* Left side: List Table */}
      <div className="flex-1 min-w-0 w-full flex flex-col gap-6">
        {/* Page Header */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-outline-variant pb-4">
          <div className="flex flex-col">
            <h2 className="text-2xl font-bold text-on-surface">Quản lý Học viên</h2>
            <p className="text-sm text-on-surface-variant italic mt-0.5">Tra cứu thông tin, lịch sử nhập học và quản lý học viên lms</p>
          </div>
          <button 
            onClick={handleOpenAdd}
            className="px-4 py-2 bg-primary hover:bg-primary/90 text-white font-medium rounded-md shadow-sm transition-all text-sm flex items-center gap-1.5 w-fit"
          >
            <span className="material-icons text-lg">person_add</span>
            Thêm Học viên
          </button>
        </div>

        {/* Filter and Search Bar */}
        <div className="flex flex-col sm:flex-row gap-4">
          <div className="relative flex-1">
            <span className="material-icons absolute left-3 top-1/2 -translate-y-1/2 text-on-surface-variant/70">search</span>
            <input 
              type="text" 
              placeholder="Tìm theo mã học viên, họ tên, email, sđt..." 
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border border-outline-variant rounded-md text-sm outline-none focus:border-primary focus:ring-2 focus:ring-primary/20 transition-all bg-white"
            />
          </div>

          <select 
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value as any)}
            className="px-3 py-2 border border-outline-variant rounded-md text-sm outline-none focus:border-primary focus:ring-2 focus:ring-primary/20 transition-all bg-white text-on-surface"
          >
            <option value="ALL">Tất cả học viên</option>
            <option value="ACTIVE">Đang học</option>
            <option value="SUSPENDED">Đình chỉ</option>
            <option value="GRADUATED">Tốt nghiệp</option>
          </select>
        </div>

        {/* Students Table Container */}
        <div className="bg-white border border-outline-variant rounded-lg shadow-sm overflow-hidden">
          {isStudentsLoading && filteredStudents.length === 0 ? (
            <div className="py-12 text-center text-on-surface-variant text-sm">
              Đang tải dữ liệu học viên...
            </div>
          ) : filteredStudents.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-16 text-center">
              <span className="material-icons text-5xl text-on-surface-variant/30 mb-3">people</span>
              <h3 className="text-base font-semibold text-on-surface">Không tìm thấy học viên nào</h3>
              <p className="text-sm text-on-surface-variant mt-1">Thử nhập từ khóa khác hoặc thay đổi bộ lọc</p>
            </div>
          ) : (
            <div className="overflow-auto max-h-[420px] relative">
              <table className="w-full border-collapse text-left">
                <thead>
                  <tr className="bg-surface border-b border-outline-variant">
                    <th className="px-6 py-3.5 text-sm font-semibold text-on-surface-variant sticky top-0 z-10 bg-surface">Mã HV</th>
                    <th className="px-6 py-3.5 text-sm font-semibold text-on-surface-variant sticky top-0 z-10 bg-surface">Họ Tên</th>
                    <th className="px-6 py-3.5 text-sm font-semibold text-on-surface-variant sticky top-0 z-10 bg-surface">Email / Số điện thoại</th>
                    <th className="px-6 py-3.5 text-sm font-semibold text-on-surface-variant sticky top-0 z-10 bg-surface">Ngày Nhập học</th>
                    <th className="px-6 py-3.5 text-sm font-semibold text-on-surface-variant sticky top-0 z-10 bg-surface">Trạng thái</th>
                    <th className="px-6 py-3.5 text-sm font-semibold text-on-surface-variant w-28 text-center sticky top-0 z-10 bg-surface">Hành động</th>
                  </tr>
                </thead>
                <tbody>
                  {paginatedStudents.map((s) => {
                    const isActive = s.status === 'ACTIVE';
                    const isGraduated = s.status === 'GRADUATED';
                    
                    const textClass = `text-sm font-semibold whitespace-nowrap ${
                      isActive 
                        ? 'text-emerald-600' 
                        : isGraduated 
                          ? 'text-blue-600' 
                          : 'text-red-600'
                    }`;
                    const statusLabel = isActive ? 'Đang học' : isGraduated ? 'Tốt nghiệp' : 'Đình chỉ';

                    return (
                      <tr key={s.id} className="hover:bg-primary/5 transition-colors border-b border-outline-variant last:border-0">
                        <td className="px-6 py-4 text-sm font-semibold text-primary">{s.studentCode}</td>
                        <td className="px-6 py-4 text-sm font-semibold text-on-surface">{s.fullName}</td>
                        <td className="px-6 py-4 text-sm text-on-surface">
                          <div className="flex flex-col">
                            <span>{s.email}</span>
                            <span className="text-xs text-on-surface-variant mt-0.5">{s.phone}</span>
                          </div>
                        </td>
                        <td className="px-6 py-4 text-sm text-on-surface">{new Date(s.enrollmentDate).toLocaleDateString('vi-VN')}</td>
                        <td className="px-6 py-4 text-sm">
                          <span className={textClass}>{statusLabel}</span>
                        </td>
                        <td className="px-6 py-4 text-sm text-center">
                          <div className="flex items-center justify-center gap-1.5">
                            <button 
                              onClick={() => handleOpenEdit(s)}
                              className="p-1.5 text-primary hover:bg-primary-container/30 rounded transition-colors flex items-center justify-center"
                              title="Sửa"
                              aria-label="Sửa học viên"
                            >
                              <span className="material-icons text-lg">edit</span>
                            </button>
                            <button 
                              onClick={() => handleDelete(s)}
                              className="p-1.5 text-red-600 hover:bg-red-50 rounded transition-colors flex items-center justify-center"
                              title="Xóa"
                              aria-label="Xóa học viên"
                            >
                              <span className="material-icons text-lg">delete</span>
                            </button>
                          </div>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          )}

          {/* Pagination Controls */}
          {totalPages > 1 && (
            <div className="flex flex-col sm:flex-row items-center justify-between gap-4 px-6 py-4 bg-surface border-t border-outline-variant">
              <div className="text-sm text-on-surface-variant">
                Hiển thị <span className="font-semibold">{startIndex + 1}</span> đến{' '}
                <span className="font-semibold">{Math.min(startIndex + ITEMS_PER_PAGE, filteredStudents.length)}</span> trong số{' '}
                <span className="font-semibold">{filteredStudents.length}</span> học viên
              </div>
              <div className="flex items-center gap-2">
                <button
                  onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
                  disabled={currentPage === 1}
                  className="p-1.5 rounded hover:bg-surface-container-highest disabled:opacity-40 disabled:hover:bg-transparent transition-colors flex items-center justify-center text-on-surface border border-outline-variant disabled:border-transparent bg-white"
                  title="Trang trước"
                >
                  <span className="material-icons text-lg">keyboard_arrow_left</span>
                </button>
                
                {Array.from({ length: totalPages }, (_, i) => i + 1).map((page) => (
                  <button
                    key={page}
                    onClick={() => setCurrentPage(page)}
                    className={`px-3 py-1 text-sm font-semibold rounded border transition-colors ${
                      currentPage === page
                        ? 'bg-primary text-white border-primary'
                        : 'hover:bg-surface-container-highest text-on-surface border-outline-variant bg-white'
                    }`}
                  >
                    {page}
                  </button>
                ))}

                <button
                  onClick={() => setCurrentPage((prev) => Math.min(prev + 1, totalPages))}
                  disabled={currentPage === totalPages}
                  className="p-1.5 rounded hover:bg-surface-container-highest disabled:opacity-40 disabled:hover:bg-transparent transition-colors flex items-center justify-center text-on-surface border border-outline-variant disabled:border-transparent bg-white"
                  title="Trang sau"
                >
                  <span className="material-icons text-lg">keyboard_arrow_right</span>
                </button>
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Right side: Side Panel Drawer Form */}
      {showDrawer && (
        <div className="w-full lg:w-[380px] bg-white border border-outline-variant rounded-lg shadow-lg p-6 flex flex-col gap-6 shrink-0 lg:sticky lg:top-[80px] animate-fade-in">
          <div className="flex justify-between items-center pb-3 border-b border-outline-variant">
            <h3 className="text-lg font-bold text-on-surface">{editingStudent ? 'Sửa thông tin học viên' : 'Thêm học viên mới'}</h3>
            <button 
              onClick={() => setShowDrawer(false)}
              className="p-1 hover:bg-surface-container-highest text-on-surface-variant rounded transition-colors flex items-center justify-center"
            >
              <span className="material-icons">close</span>
            </button>
          </div>

          <form onSubmit={handleSubmit} className="flex flex-col gap-4">
            {/* Student Code Field */}
            <div className="flex flex-col gap-1.5">
              <label htmlFor="student-code" className="text-xs font-semibold text-on-surface-variant">Mã học viên *</label>
              <input 
                id="student-code"
                type="text" 
                placeholder="Ví dụ: HE150123" 
                value={studentCode}
                onChange={(e) => {
                  const val = e.target.value.toUpperCase();
                  setStudentCode(val);
                  validateField('studentCode', val);
                }}
                onBlur={() => validateField('studentCode', studentCode)}
                disabled={!!editingStudent}
                className={`px-3.5 py-2 border rounded-md text-sm outline-none transition-all ${
                  editingStudent 
                    ? 'bg-surface text-on-surface-variant/70 border-outline-variant cursor-not-allowed opacity-75' 
                    : errors.studentCode 
                      ? 'border-red-500 focus:ring-2 focus:ring-red-200 focus:border-red-500 bg-white' 
                      : 'border-outline-variant focus:border-primary focus:ring-2 focus:ring-primary/20 bg-white'
                }`}
              />
              {errors.studentCode && <span className="text-xs text-red-600 font-medium mt-0.5">{errors.studentCode}</span>}
            </div>

            {/* Full Name Field */}
            <div className="flex flex-col gap-1.5">
              <label htmlFor="student-name" className="text-xs font-semibold text-on-surface-variant">Họ tên *</label>
              <input 
                id="student-name"
                type="text" 
                placeholder="Nhập họ và tên học viên" 
                value={fullName}
                onChange={(e) => {
                  setFullName(e.target.value);
                  validateField('fullName', e.target.value);
                }}
                onBlur={() => validateField('fullName', fullName)}
                className={`px-3.5 py-2 border rounded-md text-sm outline-none transition-all bg-white ${
                  errors.fullName 
                    ? 'border-red-500 focus:ring-2 focus:ring-red-200 focus:border-red-500' 
                    : 'border-outline-variant focus:border-primary focus:ring-2 focus:ring-primary/20'
                }`}
              />
              {errors.fullName && <span className="text-xs text-red-600 font-medium mt-0.5">{errors.fullName}</span>}
            </div>

            {/* Email Field */}
            <div className="flex flex-col gap-1.5">
              <label htmlFor="student-email" className="text-xs font-semibold text-on-surface-variant">Địa chỉ Email *</label>
              <input 
                id="student-email"
                type="email" 
                placeholder="Ví dụ: hoangtv@fpt.edu.vn" 
                value={email}
                onChange={(e) => {
                  setEmail(e.target.value);
                  validateField('email', e.target.value);
                }}
                onBlur={() => validateField('email', email)}
                className={`px-3.5 py-2 border rounded-md text-sm outline-none transition-all bg-white ${
                  errors.email 
                    ? 'border-red-500 focus:ring-2 focus:ring-red-200 focus:border-red-500' 
                    : 'border-outline-variant focus:border-primary focus:ring-2 focus:ring-primary/20'
                }`}
              />
              {errors.email && <span className="text-xs text-red-600 font-medium mt-0.5">{errors.email}</span>}
            </div>

            {/* Phone Field */}
            <div className="flex flex-col gap-1.5">
              <label htmlFor="student-phone" className="text-xs font-semibold text-on-surface-variant">Số điện thoại *</label>
              <input 
                id="student-phone"
                type="text" 
                placeholder="Ví dụ: 0912345678" 
                value={phone}
                onChange={(e) => {
                  setPhone(e.target.value);
                  validateField('phone', e.target.value);
                }}
                onBlur={() => validateField('phone', phone)}
                className={`px-3.5 py-2 border rounded-md text-sm outline-none transition-all bg-white ${
                  errors.phone 
                    ? 'border-red-500 focus:ring-2 focus:ring-red-200 focus:border-red-500' 
                    : 'border-outline-variant focus:border-primary focus:ring-2 focus:ring-primary/20'
                }`}
              />
              {errors.phone && <span className="text-xs text-red-600 font-medium mt-0.5">{errors.phone}</span>}
            </div>

            {/* Status Field */}
            <div className="flex flex-col gap-1.5">
              <label htmlFor="student-status" className="text-xs font-semibold text-on-surface-variant">Trạng thái học tập</label>
              <select 
                id="student-status"
                value={status}
                onChange={(e) => setStatus(e.target.value as any)}
                className="px-3.5 py-2 border border-outline-variant rounded-md text-sm outline-none focus:border-primary focus:ring-2 focus:ring-primary/20 transition-all bg-white text-on-surface"
              >
                <option value="ACTIVE">Đang học</option>
                <option value="SUSPENDED">Đình chỉ</option>
                <option value="GRADUATED">Tốt nghiệp</option>
              </select>
            </div>

            {/* Actions */}
            <div className="flex gap-3 mt-4 pt-3 border-t border-outline-variant">
              <button 
                type="button" 
                onClick={() => setShowDrawer(false)}
                className="flex-1 py-2 px-4 border border-outline-variant text-on-surface hover:bg-surface font-medium rounded-md text-sm transition-colors text-center"
              >
                Hủy
              </button>
              <button 
                type="submit" 
                disabled={submitting}
                className="flex-1 py-2 px-4 bg-primary hover:bg-primary/90 text-white font-medium rounded-md text-sm transition-colors shadow-sm text-center disabled:opacity-50"
              >
                Lưu
              </button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
}
