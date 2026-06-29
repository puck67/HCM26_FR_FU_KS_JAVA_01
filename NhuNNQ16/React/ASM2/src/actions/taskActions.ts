'use server';

// Server Actions: chỉ chạy phía server, được gọi từ client component
// Sau mỗi mutation, dùng revalidatePath để Next.js re-fetch dữ liệu mới

import { revalidatePath } from 'next/cache';
import { redirect } from 'next/navigation';
import { addTask, editTask, removeTask, getTaskById, getTasks } from '@/lib/store';

// -------------------------------------------------------
// Tạo task mới
// Được gọi từ form ở /tasks/new (client component)
// -------------------------------------------------------
export async function createTask(formData: FormData) {
  const name = (formData.get('name') as string).trim();
  const description = (formData.get('description') as string | null) ?? '';
  const newId = Date.now().toString();

  console.log(`[Server Action] createTask: name="${name}", id="${newId}"`);

  // Tạo task với id dạng timestamp
  addTask({
    id: newId,
    name,
    description: description.trim(),
    completed: false,
    createdAt: new Date().toISOString(),
  });

  console.log('[Server Action] Current tasks count:', getTasks().length);

  // Làm mới cache cho trang /tasks rồi redirect về đó
  revalidatePath('/tasks');
  redirect('/tasks');
}

// -------------------------------------------------------
// Cập nhật thông tin task
// -------------------------------------------------------
export async function updateTask(id: string, formData: FormData) {
  const name = (formData.get('name') as string).trim();
  const description = (formData.get('description') as string | null) ?? '';

  console.log(`[Server Action] updateTask: id="${id}", name="${name}"`);

  editTask(id, { name, description: description.trim() });

  revalidatePath('/tasks');
  revalidatePath(`/tasks/${id}`);
  redirect('/tasks');
}

// -------------------------------------------------------
// Xóa task
// -------------------------------------------------------
export async function deleteTask(id: string) {
  console.log(`[Server Action] deleteTask: id="${id}"`);
  removeTask(id);
  revalidatePath('/tasks');
}

// -------------------------------------------------------
// Đánh dấu hoàn thành / chưa hoàn thành
// -------------------------------------------------------
export async function toggleTaskCompleted(id: string) {
  console.log(`[Server Action] toggleTaskCompleted: id="${id}"`);
  const task = getTaskById(id);
  if (!task) {
    console.log(`[Server Action] toggleTaskCompleted error: Task with id="${id}" NOT found!`);
    return;
  }

  console.log(`[Server Action] toggling task id="${id}" completed status from ${task.completed} to ${!task.completed}`);
  editTask(id, { completed: !task.completed });
  console.log(`[Server Action] Task id="${id}" completed is now:`, getTaskById(id)?.completed);

  revalidatePath('/tasks');
}

