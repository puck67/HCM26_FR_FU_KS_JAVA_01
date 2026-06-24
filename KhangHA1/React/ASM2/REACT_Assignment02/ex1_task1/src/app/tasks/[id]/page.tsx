import { notFound } from 'next/navigation';
import { db } from '../../../lib/db';
import { EditTaskClientView } from './EditTaskClientView';

// Revalidate this page dynamically if needed
export const dynamic = 'force-dynamic';

export default async function EditTaskPage({ params }: { params: Promise<{ id: string }> }) {
  const resolvedParams = await params;
  const task = await db.getTaskById(resolvedParams.id);

  if (!task) {
    notFound();
  }

  return <EditTaskClientView task={task} />;
}
