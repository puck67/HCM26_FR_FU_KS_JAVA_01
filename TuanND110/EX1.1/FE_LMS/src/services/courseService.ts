import type { Course, CourseInput } from '../types';
import { useLmsStore } from '../store/useLmsStore';

interface BackendCourseId {
  courseCode: string;
  startDate: string;
}

interface BackendLesson {
  id?: number;
  lessonName: string;
  duration: number; // minutes
  contentType: 'VIDEO' | 'THEORY' | 'PRACTICE';
  status: 'DRAFT' | 'ACTIVE' | 'LOCKED';
}

interface BackendCourse {
  id: BackendCourseId;
  courseName: string;
  category: string;
  instructor: string;
  lessons?: BackendLesson[];
}

function mapBackendToFrontend(bc: BackendCourse): Course {
  const code = bc.id.courseCode;
  const startDate = bc.id.startDate;
  const idStr = `${code}_${startDate}`;
  
  // Calculate duration from lessons (in hours)
  const totalMinutes = bc.lessons?.reduce((acc: number, lesson: BackendLesson) => acc + (lesson.duration || 0), 0) || 0;
  const durationInHours = totalMinutes > 0 ? Math.round((totalMinutes / 60) * 10) / 10 : 40;
  
  // Map status: Check if first lesson is DRAFT or LOCKED, or check if any lesson status matches.
  let status: 'ACTIVE' | 'INACTIVE' | 'DRAFT' = 'ACTIVE';
  if (bc.lessons && bc.lessons.length > 0) {
    const mainStatus = bc.lessons[0].status;
    if (mainStatus === 'DRAFT') status = 'DRAFT';
    else if (mainStatus === 'LOCKED') status = 'INACTIVE';
    else status = 'ACTIVE';
  }

  // Description mapping
  const description = bc.lessons && bc.lessons.length > 0 
    ? bc.lessons[0].lessonName 
    : '';

  return {
    id: idStr,
    code,
    title: bc.courseName,
    subtitle: bc.category,
    description,
    duration: durationInHours,
    instructorName: bc.instructor,
    category: bc.category || 'Development',
    status,
    createdAt: startDate,
    updatedAt: startDate,
  };
}

export const courseService = {
  async getAllCourses(): Promise<Course[]> {
    const store = useLmsStore.getState();
    store.setCoursesLoading(true);
    try {
      const res = await fetch('/api/courses');
      if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
      const json = await res.json();
      const data: BackendCourse[] = json.data || [];
      const mapped = data.map(mapBackendToFrontend);
      store.setCourses(mapped);
      return mapped;
    } catch (error) {
      console.error('Error fetching courses from API:', error);
      store.setCourses([]);
      return [];
    } finally {
      store.setCoursesLoading(false);
    }
  },

  async createCourse(input: CourseInput): Promise<Course> {
    const store = useLmsStore.getState();
    try {
      const startDate = new Date().toISOString().split('T')[0];
      const backendCourse: BackendCourse = {
        id: {
          courseCode: input.code,
          startDate
        },
        courseName: input.title,
        category: input.category || 'Development',
        instructor: input.instructorName,
      };

      // 1. Create course
      const res = await fetch('/api/courses', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(backendCourse)
      });
      if (!res.ok) {
        const errorText = await res.text();
        throw new Error(errorText || 'Failed to create course');
      }
      const json = await res.json();
      const createdCourse: BackendCourse = json.data;
      const successMessage = json.message || 'Thêm khóa học thành công!';

      // 2. Create the associated default lesson to store duration, status, and description
      const backendLesson: BackendLesson = {
        lessonName: input.description || 'Giới thiệu môn học',
        duration: (input.duration || 40) * 60, // hours to minutes
        contentType: 'THEORY',
        status: input.status === 'ACTIVE' ? 'ACTIVE' : input.status === 'DRAFT' ? 'DRAFT' : 'LOCKED',
      };

      const lessonRes = await fetch(`/api/lessons`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          ...backendLesson,
          course: {
            id: {
              courseCode: input.code,
              startDate
            }
          }
        })
      });
      
      if (!lessonRes.ok) {
        console.error('Failed to create default lesson for course');
      } else {
        const lessonJson = await lessonRes.json();
        const createdLesson: BackendLesson = lessonJson.data;
        createdCourse.lessons = [createdLesson];
      }

      const mapped = mapBackendToFrontend(createdCourse);
      store.addCourse(mapped);
      store.showToast(successMessage, 'success');
      return mapped;
    } catch (error) {
      console.error('Error creating course:', error);
      store.showToast('Lỗi khi thêm khóa học!', 'error');
      throw error;
    }
  },

  async updateCourse(id: string, input: CourseInput): Promise<Course> {
    const store = useLmsStore.getState();
    try {
      const [courseCode, startDate] = id.split('_');
      const backendCourse: BackendCourse = {
        id: {
          courseCode,
          startDate
        },
        courseName: input.title,
        category: input.category || 'Development',
        instructor: input.instructorName,
      };

      // 1. Update the main Course
      const res = await fetch(`/api/courses/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(backendCourse)
      });
      if (!res.ok) {
        const errorText = await res.text();
        throw new Error(errorText || 'Failed to update course');
      }
      const json = await res.json();
      const updatedCourse: BackendCourse = json.data;
      const successMessage = json.message || 'Cập nhật khóa học thành công!';

      // 2. Manage lessons
      const lessonsRes = await fetch(`/api/lessons/course/${id}`);
      let lessonsList: BackendLesson[] = [];
      if (lessonsRes.ok) {
        const lessonsJson = await lessonsRes.json();
        lessonsList = lessonsJson.data || [];
      }

      const targetStatus: 'ACTIVE' | 'DRAFT' | 'LOCKED' = 
        input.status === 'ACTIVE' ? 'ACTIVE' : input.status === 'DRAFT' ? 'DRAFT' : 'LOCKED';

      if (lessonsList.length > 0) {
        // Update first lesson
        const firstLesson = lessonsList[0];
        const updatedLessonPatch: Partial<BackendLesson> = {
          lessonName: input.description || 'Giới thiệu môn học',
          duration: (input.duration || 40) * 60,
          status: targetStatus,
        };
        const lessonPatchRes = await fetch(`/api/lessons/${firstLesson.id}`, {
          method: 'PATCH',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(updatedLessonPatch)
        });
        if (lessonPatchRes.ok) {
          const lessonJson = await lessonPatchRes.json();
          const updatedLesson = lessonJson.data;
          updatedCourse.lessons = [updatedLesson, ...lessonsList.slice(1)];
        } else {
          updatedCourse.lessons = lessonsList;
        }
      } else {
        // Create a new default lesson
        const backendLesson: BackendLesson = {
          lessonName: input.description || 'Giới thiệu môn học',
          duration: (input.duration || 40) * 60,
          contentType: 'THEORY',
          status: targetStatus,
        };
        const lessonCreateRes = await fetch(`/api/lessons`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            ...backendLesson,
            course: {
              id: { courseCode, startDate }
            }
          })
        });
        if (lessonCreateRes.ok) {
          const lessonJson = await lessonCreateRes.json();
          const createdLesson = lessonJson.data;
          updatedCourse.lessons = [createdLesson];
        }
      }

      const mapped = mapBackendToFrontend(updatedCourse);
      store.updateCourse(id, mapped);
      store.showToast(successMessage, 'success');
      return mapped;
    } catch (error) {
      console.error('Error updating course:', error);
      store.showToast('Lỗi khi cập nhật khóa học!', 'error');
      throw error;
    }
  },

  async deleteCourse(id: string): Promise<void> {
    const store = useLmsStore.getState();
    try {
      const res = await fetch(`/api/courses/${id}`, {
        method: 'DELETE'
      });
      if (!res.ok) {
        const errorText = await res.text();
        throw new Error(errorText || 'Failed to delete course');
      }
      const json = await res.json();
      store.deleteCourse(id);
      store.showToast(json.message || 'Xóa khóa học thành công!', 'success');
    } catch (error) {
      console.error('Error deleting course:', error);
      store.showToast('Lỗi khi xóa khóa học!', 'error');
      throw error;
    }
  }
};
