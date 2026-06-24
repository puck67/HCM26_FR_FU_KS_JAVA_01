import type { Student, StudentInput } from '../types';
import { useLmsStore } from '../store/useLmsStore';

const MOCK_STUDENTS: Student[] = [
  {
    id: '1',
    studentCode: 'HE150123',
    fullName: 'Trần Văn Hoàng',
    email: 'hoangtv.he150123@fpt.edu.vn',
    phone: '0912345678',
    avatarUrl: undefined,
    enrollmentDate: new Date(2025, 4, 15).toISOString(),
    status: 'ACTIVE',
    coursesEnrolledCount: 3,
  },
  {
    id: '2',
    studentCode: 'HE150234',
    fullName: 'Nguyễn Thị Linh',
    email: 'linhnt.he150234@fpt.edu.vn',
    phone: '0987654321',
    avatarUrl: undefined,
    enrollmentDate: new Date(2025, 5, 20).toISOString(),
    status: 'ACTIVE',
    coursesEnrolledCount: 2,
  },
  {
    id: '3',
    studentCode: 'HE160345',
    fullName: 'Lê Hữu Tuấn',
    email: 'tuanlh.he160345@fpt.edu.vn',
    phone: '0901234567',
    avatarUrl: undefined,
    enrollmentDate: new Date(2025, 6, 1).toISOString(),
    status: 'SUSPENDED',
    coursesEnrolledCount: 0,
  },
  {
    id: '4',
    studentCode: 'HE160456',
    fullName: 'Phạm Minh Đức',
    email: 'ducpm.he160456@fpt.edu.vn',
    phone: '0922334455',
    avatarUrl: undefined,
    enrollmentDate: new Date(2025, 6, 10).toISOString(),
    status: 'ACTIVE',
    coursesEnrolledCount: 4,
  },
  {
    id: '5',
    studentCode: 'HE160567',
    fullName: 'Đỗ Thùy Trang',
    email: 'trangdt.he160567@fpt.edu.vn',
    phone: '0933445566',
    avatarUrl: undefined,
    enrollmentDate: new Date(2025, 6, 15).toISOString(),
    status: 'GRADUATED',
    coursesEnrolledCount: 5,
  },
  {
    id: '6',
    studentCode: 'HE160678',
    fullName: 'Vũ Quốc Anh',
    email: 'anhvq.he160678@fpt.edu.vn',
    phone: '0944556677',
    avatarUrl: undefined,
    enrollmentDate: new Date(2025, 7, 2).toISOString(),
    status: 'ACTIVE',
    coursesEnrolledCount: 1,
  },
  {
    id: '7',
    studentCode: 'HE160789',
    fullName: 'Nguyễn Tiến Dũng',
    email: 'dungnt.he160789@fpt.edu.vn',
    phone: '0955667788',
    avatarUrl: undefined,
    enrollmentDate: new Date(2025, 7, 5).toISOString(),
    status: 'ACTIVE',
    coursesEnrolledCount: 2,
  }
];

export const studentService = {
  async getAllStudents(): Promise<Student[]> {
    const store = useLmsStore.getState();
    if (store.students.length > 0) {
      return store.students;
    }
    store.setStudentsLoading(true);
    try {
      // Gọi API thực tế: const students = await api.get<Student[]>('/students');
      await new Promise(resolve => setTimeout(resolve, 500));
      store.setStudents(MOCK_STUDENTS);
      return MOCK_STUDENTS;
    } catch (error) {
      console.warn('Lỗi kết nối API, sử dụng mock data:', error);
      store.setStudents(MOCK_STUDENTS);
      return MOCK_STUDENTS;
    } finally {
      store.setStudentsLoading(false);
    }
  },

  async createStudent(input: StudentInput): Promise<Student> {
    const store = useLmsStore.getState();
    try {
      // Gọi API thực tế: const newStudent = await api.post<Student>('/students', input);
      await new Promise(resolve => setTimeout(resolve, 600));
      const newStudent: Student = {
        ...input,
        id: Math.random().toString(36).substring(2, 9),
        enrollmentDate: new Date().toISOString(),
        coursesEnrolledCount: 0,
      };
      store.addStudent(newStudent);
      return newStudent;
    } catch (error) {
      throw error;
    }
  },

  async updateStudent(id: string, input: StudentInput): Promise<Student> {
    const store = useLmsStore.getState();
    try {
      // Gọi API thực tế: const updated = await api.put<Student>(`/students/${id}`, input);
      await new Promise(resolve => setTimeout(resolve, 500));
      const currentStudent = store.students.find(s => s.id === id);
      const updated: Student = {
        ...input,
        id,
        enrollmentDate: currentStudent?.enrollmentDate || new Date().toISOString(),
        coursesEnrolledCount: currentStudent?.coursesEnrolledCount || 0,
      };
      store.updateStudent(id, updated);
      return updated;
    } catch (error) {
      throw error;
    }
  },

  async deleteStudent(id: string): Promise<void> {
    const store = useLmsStore.getState();
    try {
      // Gọi API thực tế: await api.delete(`/students/${id}`);
      await new Promise(resolve => setTimeout(resolve, 400));
      store.deleteStudent(id);
    } catch (error) {
      throw error;
    }
  }
};
