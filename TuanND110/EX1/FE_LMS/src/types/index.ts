/**
 * LMS Type Definitions
 */

export interface Course {
  id: string;
  code: string;
  title: string;
  subtitle?: string;
  description?: string;
  duration: number;
  instructorName: string;
  category: string;
  status: 'ACTIVE' | 'INACTIVE' | 'DRAFT';
  createdAt: string;
  updatedAt: string;
}

export interface CourseInput {
  code: string;
  title: string;
  subtitle?: string;
  description?: string;
  duration: number;
  instructorName: string;
  category: string;
  status: 'ACTIVE' | 'INACTIVE' | 'DRAFT';
}

export interface Student {
  id: string;
  studentCode: string;
  fullName: string;
  email: string;
  phone: string;
  avatarUrl?: string;
  enrollmentDate: string;
  status: 'ACTIVE' | 'SUSPENDED' | 'GRADUATED';
  coursesEnrolledCount: number;
}

export interface StudentInput {
  studentCode: string;
  fullName: string;
  email: string;
  phone: string;
  avatarUrl?: string;
  status: 'ACTIVE' | 'SUSPENDED' | 'GRADUATED';
}

// Backend Model Interfaces for Course & Lesson (API integration)
export interface BackendCourseId {
  courseCode: string;
  startDate: string;
}

export interface BackendLesson {
  id?: number;
  lessonName: string;
  duration: number; // minutes
  contentType: 'VIDEO' | 'THEORY' | 'PRACTICE';
  status: 'DRAFT' | 'ACTIVE' | 'LOCKED';
}

export interface BackendCourse {
  id: BackendCourseId;
  courseName: string;
  category: string;
  instructor: string;
  lessons?: BackendLesson[];
}
