import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useLmsStore } from '../store/useLmsStore';
import { courseService } from '../services/courseService';
import type { Course } from '../types';

export default function Courses() {
  const navigate = useNavigate();
  const { courses, isCoursesLoading } = useLmsStore();
  
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState<'ALL' | 'ACTIVE' | 'INACTIVE' | 'DRAFT'>('ALL');
  
  // Pagination states
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage, setItemsPerPage] = useState(5);

  useEffect(() => {
    courseService.getAllCourses();
  }, []);

  // Reset page when search or status filters change
  useEffect(() => {
    setCurrentPage(1);
  }, [searchQuery, statusFilter]);


  const handleDelete = async (course: Course) => {
    const confirm = window.confirm(`Bạn có chắc chắn muốn xóa khóa học "${course.title}" (${course.code})?`);
    if (confirm) {
      try {
        await courseService.deleteCourse(course.id);
      } catch (err) {
        alert('Có lỗi xảy ra khi xóa khóa học.');
      }
    }
  };

  const filteredCourses = courses.filter((c) => {
    const matchesSearch = 
      c.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
      c.code.toLowerCase().includes(searchQuery.toLowerCase()) ||
      c.instructorName.toLowerCase().includes(searchQuery.toLowerCase());
    
    const matchesStatus = statusFilter === 'ALL' || c.status === statusFilter;

    return matchesSearch && matchesStatus;
  });

  const totalPages = Math.ceil(filteredCourses.length / itemsPerPage);
  const startIndex = (currentPage - 1) * itemsPerPage;
  const paginatedCourses = filteredCourses.slice(startIndex, startIndex + itemsPerPage);

  const getPageNumbers = () => {
    const pages: (number | string)[] = [];
    const maxVisiblePages = 5;
    if (totalPages <= maxVisiblePages) {
      for (let i = 1; i <= totalPages; i++) {
        pages.push(i);
      }
    } else {
      pages.push(1);
      
      let start = Math.max(2, currentPage - 1);
      let end = Math.min(totalPages - 1, currentPage + 1);
      
      if (currentPage <= 3) {
        end = 4;
      } else if (currentPage >= totalPages - 2) {
        start = totalPages - 3;
      }
      
      if (start > 2) {
        pages.push('...');
      }
      
      for (let i = start; i <= end; i++) {
        pages.push(i);
      }
      
      if (end < totalPages - 1) {
        pages.push('...');
      }
      
      pages.push(totalPages);
    }
    return pages;
  };

  // Adjust page number if it goes out of bounds (e.g. after deletion or filtering)
  useEffect(() => {
    if (currentPage > totalPages && totalPages > 0) {
      setCurrentPage(totalPages);
    }
  }, [filteredCourses.length, itemsPerPage, totalPages, currentPage]);

  return (
    <div className="animate-fade-in flex flex-col gap-6">
      {/* Page Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-outline-variant pb-4">
        <div className="flex flex-col">
          <h2 className="text-2xl font-bold text-on-surface">Quản lý Khóa học</h2>
          <p className="text-sm text-on-surface-variant italic">Danh sách các khóa học hiện có trong hệ thống LMS</p>
        </div>
        <Link 
          to="/courses/add" 
          className="px-4 py-2 bg-primary hover:bg-primary/90 text-white font-medium rounded-md shadow-sm transition-all text-sm flex items-center gap-1.5 w-fit no-underline"
        >
          <span className="material-icons text-lg">add</span>
          Thêm Khóa học
        </Link>
      </div>

      {/* Filter and Search Bar */}
      <div className="flex flex-col sm:flex-row gap-4">
        <div className="relative flex-1">
          <span className="material-icons absolute left-3 top-1/2 -translate-y-1/2 text-on-surface-variant/70">search</span>
          <input 
            type="text" 
            placeholder="Tìm theo mã, tên khóa học hoặc giảng viên..." 
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
          <option value="ALL">Tất cả trạng thái</option>
          <option value="ACTIVE">Hoạt động</option>
          <option value="INACTIVE">Tạm dừng</option>
          <option value="DRAFT">Bản nháp</option>
        </select>
      </div>

      {/* Courses Table Container */}
      <div className="bg-white border border-outline-variant rounded-lg shadow-sm overflow-hidden">
        {isCoursesLoading && filteredCourses.length === 0 ? (
          <div className="py-12 text-center text-on-surface-variant text-sm">
            Đang tải dữ liệu khóa học...
          </div>
        ) : filteredCourses.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-16 text-center">
            <span className="material-icons text-5xl text-on-surface-variant/30 mb-3">library_books</span>
            <h3 className="text-base font-semibold text-on-surface">Không tìm thấy khóa học nào</h3>
            <p className="text-sm text-on-surface-variant mt-1">Thử thay đổi bộ lọc hoặc từ khóa tìm kiếm</p>
          </div>
        ) : (
          <div className="overflow-auto max-h-[420px] relative">
            <table className="w-full border-collapse text-left">
              <thead>
                <tr className="bg-surface border-b border-outline-variant">
                  <th className="px-6 py-3.5 text-sm font-semibold text-on-surface-variant sticky top-0 z-10 bg-surface">Mã</th>
                  <th className="px-6 py-3.5 text-sm font-semibold text-on-surface-variant sticky top-0 z-10 bg-surface">Tên khóa học</th>
                  <th className="px-6 py-3.5 text-sm font-semibold text-on-surface-variant sticky top-0 z-10 bg-surface">Giảng viên</th>
                  <th className="px-6 py-3.5 text-sm font-semibold text-on-surface-variant sticky top-0 z-10 bg-surface">Thời lượng</th>
                  <th className="px-6 py-3.5 text-sm font-semibold text-on-surface-variant sticky top-0 z-10 bg-surface">Trạng thái</th>
                  <th className="px-6 py-3.5 text-sm font-semibold text-on-surface-variant w-28 text-center sticky top-0 z-10 bg-surface">Hành động</th>
                </tr>
              </thead>
              <tbody>
                {paginatedCourses.map((c) => {
                  const isActive = c.status === 'ACTIVE';
                  const isDraft = c.status === 'DRAFT';
                  
                  const textClass = `text-sm font-semibold whitespace-nowrap ${
                    isActive 
                      ? 'text-emerald-600' 
                      : isDraft 
                        ? 'text-on-surface-variant/70' 
                        : 'text-red-600'
                  }`;
                  const statusLabel = isActive ? 'Hoạt động' : isDraft ? 'Bản nháp' : 'Tạm dừng';

                  return (
                    <tr key={c.id} className="hover:bg-primary/5 transition-colors border-b border-outline-variant last:border-0">
                      <td className="px-6 py-4 text-sm font-semibold text-primary">{c.code}</td>
                      <td className="px-6 py-4 text-sm">
                        <span className="font-semibold text-on-surface block">{c.title}</span>
                        {c.subtitle && <p className="text-xs text-on-surface-variant mt-0.5">{c.subtitle}</p>}
                      </td>
                      <td className="px-6 py-4 text-sm text-on-surface">{c.instructorName}</td>
                      <td className="px-6 py-4 text-sm text-on-surface">{c.duration} giờ</td>
                      <td className="px-6 py-4 text-sm">
                        <span className={textClass}>{statusLabel}</span>
                      </td>
                      <td className="px-6 py-4 text-sm text-center">
                        <div className="flex items-center justify-center gap-1.5">
                          <button 
                            onClick={() => navigate(`/courses/edit/${c.id}`)}
                            className="p-1.5 text-primary hover:bg-primary-container/30 rounded transition-colors flex items-center justify-center"
                            title="Sửa"
                            aria-label="Sửa khóa học"
                          >
                            <span className="material-icons text-lg">edit</span>
                          </button>
                          <button 
                            onClick={() => handleDelete(c)}
                            className="p-1.5 text-red-600 hover:bg-red-50 rounded transition-colors flex items-center justify-center"
                            title="Xóa"
                            aria-label="Xóa khóa học"
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
        {filteredCourses.length > 0 && (
          <div className="flex flex-col sm:flex-row items-center justify-between gap-4 px-6 py-4 bg-surface border-t border-outline-variant">
            <div className="flex flex-col sm:flex-row sm:items-center gap-4 text-sm text-on-surface-variant">
              <div>
                Hiển thị <span className="font-semibold">{filteredCourses.length === 0 ? 0 : startIndex + 1}</span> đến{' '}
                <span className="font-semibold">{Math.min(startIndex + itemsPerPage, filteredCourses.length)}</span> trong số{' '}
                <span className="font-semibold">{filteredCourses.length}</span> khóa học
              </div>
              <div className="flex items-center gap-1.5">
                <span>Số dòng mỗi trang:</span>
                <select
                  value={itemsPerPage}
                  onChange={(e) => {
                    setItemsPerPage(Number(e.target.value));
                    setCurrentPage(1);
                  }}
                  className="px-2 py-1 border border-outline-variant rounded bg-white text-xs outline-none focus:border-primary focus:ring-1 focus:ring-primary/20 text-on-surface cursor-pointer"
                >
                  <option value={5}>5</option>
                  <option value={10}>10</option>
                  <option value={20}>20</option>
                  <option value={50}>50</option>
                </select>
              </div>
            </div>
            
            {totalPages > 1 && (
              <div className="flex items-center gap-2">
                <button
                  onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
                  disabled={currentPage === 1}
                  className="p-1.5 rounded hover:bg-surface-container-highest disabled:opacity-40 disabled:hover:bg-transparent transition-colors flex items-center justify-center text-on-surface border border-outline-variant disabled:border-transparent bg-white cursor-pointer"
                  title="Trang trước"
                >
                  <span className="material-icons text-lg">keyboard_arrow_left</span>
                </button>
                
                {getPageNumbers().map((page, index) => {
                  if (page === '...') {
                    return (
                      <span key={`ellipsis-${index}`} className="px-2 text-on-surface-variant select-none">
                        ...
                      </span>
                    );
                  }
                  
                  return (
                    <button
                      key={`page-${page}`}
                      onClick={() => setCurrentPage(page as number)}
                      className={`px-3 py-1 text-sm font-semibold rounded border transition-colors cursor-pointer ${
                        currentPage === page
                          ? 'bg-primary text-white border-primary'
                          : 'hover:bg-surface-container-highest text-on-surface border-outline-variant bg-white'
                      }`}
                    >
                      {page}
                    </button>
                  );
                })}

                <button
                  onClick={() => setCurrentPage((prev) => Math.min(prev + 1, totalPages))}
                  disabled={currentPage === totalPages}
                  className="p-1.5 rounded hover:bg-surface-container-highest disabled:opacity-40 disabled:hover:bg-transparent transition-colors flex items-center justify-center text-on-surface border border-outline-variant disabled:border-transparent bg-white cursor-pointer"
                  title="Trang sau"
                >
                  <span className="material-icons text-lg">keyboard_arrow_right</span>
                </button>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
