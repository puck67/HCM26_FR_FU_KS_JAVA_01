import fs from 'fs/promises';
import path from 'path';

export class JsonRepository<T extends { id: string }> {
  private filePath: string;

  constructor(fileName: string) {
    this.filePath = path.join(process.cwd(), 'src/data', fileName);
  }

  private async ensureFile(): Promise<void> {
    try {
      await fs.mkdir(path.dirname(this.filePath), { recursive: true });
      await fs.access(this.filePath);
    } catch {
      await fs.writeFile(this.filePath, JSON.stringify([]));
    }
  }

  async getAll(): Promise<T[]> {
    await this.ensureFile();
    const data = await fs.readFile(this.filePath, 'utf-8');
    return JSON.parse(data) as T[];
  }

  async getById(id: string): Promise<T | null> {
    const items = await this.getAll();
    return items.find((item) => item.id === id) || null;
  }

  async create(item: Omit<T, 'id' | 'createdAt' | 'updatedAt'> & { id?: string; createdAt?: string; updatedAt?: string }): Promise<T> {
    const items = await this.getAll();
    const newItem = {
      ...item,
      id: item.id || Math.random().toString(36).substring(2, 9),
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    } as unknown as T;
    items.push(newItem);
    await fs.writeFile(this.filePath, JSON.stringify(items, null, 2));
    return newItem;
  }

  async update(id: string, itemUpdates: Partial<Omit<T, 'id' | 'createdAt' | 'updatedAt'>>): Promise<T> {
    const items = await this.getAll();
    const index = items.findIndex((item) => item.id === id);
    if (index === -1) {
      throw new Error('Item not found');
    }
    const updatedItem = {
      ...items[index],
      ...itemUpdates,
      updatedAt: new Date().toISOString(),
    } as T;
    items[index] = updatedItem;
    await fs.writeFile(this.filePath, JSON.stringify(items, null, 2));
    return updatedItem;
  }

  async delete(id: string): Promise<boolean> {
    const items = await this.getAll();
    const filtered = items.filter((item) => item.id !== id);
    if (filtered.length === items.length) {
      return false;
    }
    await fs.writeFile(this.filePath, JSON.stringify(filtered, null, 2));
    return true;
  }
}
