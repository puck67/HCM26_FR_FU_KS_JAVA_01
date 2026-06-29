import axios from "axios";
import type { Task } from "../types/task";

const LOCAL_STORAGE_KEY = "react_task_dashboard_tasks";

const delay = <T,>(value: T, time = 400): Promise<T> =>
  new Promise((resolve) => setTimeout(() => resolve(value), time));

export const getTasks = async (): Promise<Task[]> => {
  const cached = localStorage.getItem(LOCAL_STORAGE_KEY);
  if (cached) {
    return delay(JSON.parse(cached));
  }

  try {
    const response = await axios.get<Task[]>("/tasks.json");
    const data = response.data;
    localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(data));
    return delay(data);
  } catch (error) {
    console.error("Failed to fetch initial tasks from REST API:", error);
    return delay([]);
  }
};

export const getTaskById = async (id: number): Promise<Task | undefined> => {
  const tasks = await getTasks();
  return delay(tasks.find((task) => task.id === id));
};

export const createTask = async (task: Omit<Task, "id">): Promise<Task> => {
  const tasks = await getTasks();
  const nextId = tasks.length ? Math.max(...tasks.map((item) => item.id)) + 1 : 1;
  const newTask = { id: nextId, ...task };
  const updatedTasks = [...tasks, newTask];
  localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(updatedTasks));
  return delay(newTask);
};

export const updateTask = async (task: Task): Promise<Task | undefined> => {
  const tasks = await getTasks();
  const exists = tasks.some((item) => item.id === task.id);
  if (!exists) return delay(undefined);
  const updatedTasks = tasks.map((item) => (item.id === task.id ? task : item));
  localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(updatedTasks));
  return delay(task);
};

export const deleteTask = async (id: number): Promise<boolean> => {
  const tasks = await getTasks();
  const exists = tasks.some((item) => item.id === id);
  if (!exists) return delay(false);
  const updatedTasks = tasks.filter((item) => item.id !== id);
  localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(updatedTasks));
  return delay(true);
};

