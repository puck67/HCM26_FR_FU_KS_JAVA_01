import fs from 'fs/promises';
import path from 'path';

const filePath = path.join(process.cwd(), 'src/data/tasks.json');

export async function getTasks() {
  try {
    const data = await fs.readFile(filePath, 'utf8');
    return JSON.parse(data);
  } catch (error) {
    // If file doesn't exist or is empty, return empty array
    return [];
  }
}

export async function saveTasks(tasks) {
  // Ensure the directory exists
  const dirPath = path.dirname(filePath);
  try {
    await fs.mkdir(dirPath, { recursive: true });
  } catch (err) {
    // Directory might already exist
  }
  await fs.writeFile(filePath, JSON.stringify(tasks, null, 2), 'utf8');
}
