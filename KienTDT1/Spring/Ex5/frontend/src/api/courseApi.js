import axiosInstance from "./axiosInstance";

export const getCourses = () => {
  return axiosInstance.get("/courses");
};

export const getCourseById = (id) => {
  return axiosInstance.get(`/courses/${id}`);
};

export const createCourse = (courseData) => {
  return axiosInstance.post("/courses", courseData);
};

export const updateCourse = (id, courseData) => {
  return axiosInstance.put(`/courses/${id}`, courseData);
};

export const deleteCourse = (id) => {
  return axiosInstance.delete(`/courses/${id}`);
};

export const searchCourses = (keyword) => {
  return axiosInstance.get(`/courses/search?keyword=${keyword}`);
};
