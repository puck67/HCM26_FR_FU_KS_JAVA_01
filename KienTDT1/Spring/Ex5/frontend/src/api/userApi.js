import axiosInstance from "./axiosInstance";

export const getUsers = () => {
  return axiosInstance.get("/users");
};
