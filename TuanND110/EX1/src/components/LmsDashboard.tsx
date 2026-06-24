import React from "react";
import type { Course, Enrollment } from "../types";

interface LmsDashboardProps {
  coursesCount: number;
  lessonsCount: number;
  activeStudentsCount: number;
  totalRevenue: number;
  recentEnrollments: Enrollment[];
  recentCourses: Course[];
}

export const LmsDashboard: React.FC<LmsDashboardProps> = ({
  coursesCount,
  lessonsCount,
  activeStudentsCount,
  totalRevenue,
  recentEnrollments,
  recentCourses,
}) => {
  return (
    <>
      <div className="dashboard-grid">
        {/* Total Revenue Card */}
        <div className="summary-card">
          <div className="summary-icon" style={{ backgroundColor: 'var(--color-primary-container)', color: 'var(--color-primary)' }}>
            <span className="material-symbols-outlined fill-icon">payments</span>
          </div>
          <div className="summary-info">
            <span className="summary-label">Tổng doanh thu</span>
            <span className="summary-value" style={{ color: 'var(--color-primary)' }}>
              ${totalRevenue.toLocaleString()}
            </span>
            <span className="text-xs text-success font-medium" style={{ marginTop: '4px', display: 'flex', alignItems: 'center', gap: '2px' }}>
              <span className="material-symbols-outlined text-xs">trending_up</span> +14.2% so với tháng trước
            </span>
          </div>
        </div>

        {/* Active Students Card */}
        <div className="summary-card">
          <div className="summary-icon" style={{ backgroundColor: 'var(--color-success-bg)', color: 'var(--color-success)' }}>
            <span className="material-symbols-outlined fill-icon">group</span>
          </div>
          <div className="summary-info">
            <span className="summary-label">Học viên hoạt động</span>
            <span className="summary-value" style={{ color: 'var(--color-success)' }}>
              {activeStudentsCount.toLocaleString()}
            </span>
            <span className="text-xs text-success font-medium" style={{ marginTop: '4px', display: 'flex', alignItems: 'center', gap: '2px' }}>
              <span className="material-symbols-outlined text-xs">trending_up</span> +5.8% tháng này
            </span>
          </div>
        </div>

        {/* Courses Card */}
        <div className="summary-card">
          <div className="summary-icon" style={{ backgroundColor: 'var(--color-warning-bg)', color: 'var(--color-warning)' }}>
            <span className="material-symbols-outlined fill-icon">school</span>
          </div>
          <div className="summary-info">
            <span className="summary-label">Tổng khóa học</span>
            <span className="summary-value" style={{ color: 'var(--color-warning)' }}>
              {coursesCount.toLocaleString()}
            </span>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '4px', marginTop: '6px', width: '100px' }}>
              <div style={{ height: '4px', backgroundColor: 'var(--color-outline-variant)', borderRadius: 'var(--radius-full)', overflow: 'hidden' }}>
                <div style={{ width: '75%', height: '100%', backgroundColor: 'var(--color-warning)', borderRadius: 'var(--radius-full)' }}></div>
              </div>
              <span className="text-xs text-outline">75% hoàn thành mục tiêu</span>
            </div>
          </div>
        </div>

        {/* Lessons Card */}
        <div className="summary-card">
          <div className="summary-icon" style={{ backgroundColor: 'var(--color-info-bg)', color: 'var(--color-info)' }}>
            <span className="material-symbols-outlined fill-icon">menu_book</span>
          </div>
          <div className="summary-info">
            <span className="summary-label">Tổng bài học</span>
            <span className="summary-value" style={{ color: 'var(--color-info)' }}>
              {lessonsCount.toLocaleString()}
            </span>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '4px', marginTop: '6px', width: '100px' }}>
              <div style={{ height: '4px', backgroundColor: 'var(--color-outline-variant)', borderRadius: 'var(--radius-full)', overflow: 'hidden' }}>
                <div style={{ width: '90%', height: '100%', backgroundColor: 'var(--color-info)', borderRadius: 'var(--radius-full)' }}></div>
              </div>
              <span className="text-xs text-outline">90% đã xuất bản</span>
            </div>
          </div>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(350px, 1fr))', gap: '24px' }}>
        {/* Recent Enrollments Widget */}
        <div className="recent-list-card">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
            <h3 className="font-bold text-sm" style={{ fontFamily: 'var(--font-title)', fontSize: '16px', color: 'var(--color-on-surface)' }}>Đăng ký gần đây</h3>
            <span className="badge primary">Mới nhất</span>
          </div>
          <div className="recent-list">
            {recentEnrollments.length > 0 ? (
              recentEnrollments.map((en) => (
                <div key={en.id} className="recent-item">
                  <div className="recent-details">
                    <h4>{en.studentName}</h4>
                    <p>{en.courseTitle} • <span className="text-xs">{en.enrollmentDate}</span></p>
                  </div>
                  <span className={`badge ${en.paymentStatus === 'Paid' ? 'success' : 'danger'}`}>
                    {en.paymentStatus === 'Paid' ? 'Đã thanh toán' : 'Chưa thanh toán'}
                  </span>
                </div>
              ))
            ) : (
              <div style={{ textAlign: 'center', padding: '24px 0', color: 'var(--color-outline)' }}>
                <span className="material-symbols-outlined" style={{ fontSize: '36px', marginBottom: '8px' }}>inbox</span>
                <p className="text-sm">Không có đăng ký mới</p>
              </div>
            )}
          </div>
        </div>

        {/* Latest Courses Widget */}
        <div className="recent-list-card">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
            <h3 className="font-bold text-sm" style={{ fontFamily: 'var(--font-title)', fontSize: '16px', color: 'var(--color-on-surface)' }}>Khóa học mới nhất</h3>
            <span className="badge secondary">Tất cả</span>
          </div>
          <div className="recent-list">
            {recentCourses.length > 0 ? (
              recentCourses.map((c) => (
                <div key={c.id} className="recent-item">
                  <div className="recent-details">
                    <h4>{c.title}</h4>
                    <p>{c.instructor} • <span className="text-xs">{c.level}</span></p>
                  </div>
                  <span className={`badge ${c.status === 'Published' ? 'success' : 'secondary'}`}>
                    {c.status === 'Published' ? 'Đã đăng' : 'Bản nháp'}
                  </span>
                </div>
              ))
            ) : (
              <div style={{ textAlign: 'center', padding: '24px 0', color: 'var(--color-outline)' }}>
                <span className="material-symbols-outlined" style={{ fontSize: '36px', marginBottom: '8px' }}>inbox</span>
                <p className="text-sm">Không có khóa học nào</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </>
  );
};
