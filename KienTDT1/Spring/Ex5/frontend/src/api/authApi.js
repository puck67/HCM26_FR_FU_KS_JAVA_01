import axiosInstance from "./axiosInstance";

export const loginApi = ({ username, password }) => {
  return axiosInstance.post("/auth/login", { username, password });
};

export const refreshTokenApi = (refreshToken) => {
  return axiosInstance.post("/auth/refresh", { refreshToken });
};
