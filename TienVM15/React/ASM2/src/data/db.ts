import fs from "fs";
import path from "path";

export interface Task {
  id: string;
  name: string;
  description?: string;
  completed: boolean;
  createdAt: string;
}

const dbPath = path.join(process.cwd(), "src/data/tasks.json");

export function getTasks(): Task[] {
  try {
    if (!fs.existsSync(dbPath)) {
      // Ensure directory exists
      fs.mkdirSync(path.dirname(dbPath), { recursive: true });
      fs.writeFileSync(dbPath, JSON.stringify([]));
      return [];
    }
    const data = fs.readFileSync(dbPath, "utf-8");
    return JSON.parse(data) as Task[];
  } catch (error) {
    console.error("Error reading database:", error);
    return [];
  }
}

export function saveTasks(tasks: Task[]): void {
  try {
    fs.mkdirSync(path.dirname(dbPath), { recursive: true });
    fs.writeFileSync(dbPath, JSON.stringify(tasks, null, 2), "utf-8");
  } catch (error) {
    console.error("Error writing to database:", error);
  }
}
