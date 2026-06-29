import fs from "fs";
import path from "path";

export interface Task {
  id: string;
  name: string;
  description: string;
  completed: boolean;
  createdAt: string;
}

const filePath = path.join(process.cwd(), "src/data/tasks.json");

const ensureDirectoryExists = () => {
  const dir = path.dirname(filePath);
  if (!fs.existsSync(dir)) {
    fs.mkdirSync(dir, { recursive: true });
  }
};

export const getTasks = (): Task[] => {
  ensureDirectoryExists();
  if (!fs.existsSync(filePath)) {
    fs.writeFileSync(filePath, JSON.stringify([], null, 2), "utf-8");
    return [];
  }
  try {
    const data = fs.readFileSync(filePath, "utf-8");
    return JSON.parse(data) as Task[];
  } catch (error) {
    console.error("Error reading tasks:", error);
    return [];
  }
};

export const saveTasks = (tasks: Task[]): void => {
  ensureDirectoryExists();
  fs.writeFileSync(filePath, JSON.stringify(tasks, null, 2), "utf-8");
};
