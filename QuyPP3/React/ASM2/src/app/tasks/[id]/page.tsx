import { dataStore } from "@/lib/dataStore";
import { updateTask } from "@/app/actions/taskActions";
import TaskFormClient from "@/components/TaskFormClient";
import Link from "next/link";

interface Props {
  params: Promise<{ id: string }>;
}

export default async function TaskDetailPage({ params }: Props) {
  const { id } = await params;
  const task = await dataStore.getById(id);

  if (!task) {
    return (
      <div className="text-center py-12">
        <h3 className="text-lg font-bold text-slate-800">Record Missing</h3>
        <p className="text-slate-500 mt-1 mb-4">The task entry you requested could not be located.</p>
        <Link href="/tasks" className="text-blue-600 hover:underline text-sm font-semibold">&larr; Return to Workspace</Link>
      </div>
    );
  }

  // Wraps server mutation payload arguments securely
  const boundUpdateAction = updateTask.bind(null, id);

  return (
    <TaskFormClient 
      title={`Update Assignment #${task.id}`}
      initialValues={{ name: task.name, description: task.description }}
      onSubmitAction={boundUpdateAction}
    />
  );
}
