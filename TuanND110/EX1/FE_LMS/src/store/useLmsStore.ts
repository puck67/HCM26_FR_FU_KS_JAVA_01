import { create } from 'zustand';
import type { Course, Student } from '../types';

export interface ToastState {
  message: string;
  type: 'success' | 'error' | 'info';
}

interface LmsStore {
  // Toast state
  toast: ToastState | null;
  showToast: (message: string, type?: 'success' | 'error' | 'info') => void;
  hideToast: () => void;

  // Course state
  courses: Course[];
  isCoursesLoading: boolean;
  setCourses: (courses: Course[]) => void;
  setCoursesLoading: (loading: boolean) => void;
  addCourse: (course: Course) => void;
  updateCourse: (id: string, updated: Course) => void;
  deleteCourse: (id: string) => void;

  // Student state
  students: Student[];
  isStudentsLoading: boolean;
  setStudents: (students: Student[]) => void;
  setStudentsLoading: (loading: boolean) => void;
  addStudent: (student: Student) => void;
  updateStudent: (id: string, updated: Student) => void;
  deleteStudent: (id: string) => void;
}

export const useLmsStore = create<LmsStore>((set) => ({
  // Toast State
  toast: null,
  showToast: (message, type = 'success') => {
    set({ toast: { message, type } });
    setTimeout(() => {
      set((state) => {
        if (state.toast && state.toast.message === message) {
          return { toast: null };
        }
        return {};
      });
    }, 3000);
  },
  hideToast: () => set({ toast: null }),

  // Course State
  courses: [],
  isCoursesLoading: false,
  setCourses: (courses) => set({ courses }),
  setCoursesLoading: (isCoursesLoading) => set({ isCoursesLoading }),
  addCourse: (course) => set((state) => ({ courses: [course, ...state.courses] })),
  updateCourse: (id, updated) => set((state) => ({
    courses: state.courses.map((c) => (c.id === id ? updated : c)),
  })),
  deleteCourse: (id) => set((state) => ({
    courses: state.courses.filter((c) => c.id !== id),
  })),

  // Student State
  students: [],
  isStudentsLoading: false,
  setStudents: (students) => set({ students }),
  setStudentsLoading: (isStudentsLoading) => set({ isStudentsLoading }),
  addStudent: (student) => set((state) => ({ students: [student, ...state.students] })),
  updateStudent: (id, updated) => set((state) => ({
    students: state.students.map((s) => (s.id === id ? updated : s)),
  })),
  deleteStudent: (id) => set((state) => ({
    students: state.students.filter((s) => s.id !== id),
  })),
}));
