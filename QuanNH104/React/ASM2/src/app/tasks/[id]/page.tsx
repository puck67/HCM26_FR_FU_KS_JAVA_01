import { getTasks } from "@/lib/tasksStore";
import EditTaskForm from "./EditTaskForm";
import Link from "next/link";
import { AlertTriangle, ArrowLeft } from "lucide-react";

export const dynamic = "force-dynamic";

interface TaskDetailPageProps {
  params: Promise<{ id: string }>;
}

export default async function TaskDetailPage({ params }: TaskDetailPageProps) {
  const resolvedParams = await params;
  const { id } = resolvedParams;

  const tasks = getTasks();
  const task = tasks.find((t) => t.id === id);

  if (!task) {
    return (
      <div className="max-w-md mx-auto text-center py-16 px-4">
        <div className="rounded-full bg-red-950/20 border border-red-500/20 p-4 w-16 h-16 flex items-center justify-center mx-auto text-red-400 mb-6">
          <AlertTriangle className="w-8 h-8" />
        </div>
        <h2 className="text-2xl font-bold text-white mb-2">Không tìm thấy công việc</h2>
        <p className="text-zinc-400 mb-8">
          Công việc bạn đang tìm kiếm không tồn tại hoặc đã bị xóa.
        </p>
        <Link
          href="/tasks"
          className="inline-flex items-center gap-2 rounded-xl bg-indigo-600 px-5 py-2.5 text-sm font-semibold text-white shadow-md shadow-indigo-600/10 hover:bg-indigo-500 transition-all duration-200"
        >
          <ArrowLeft className="w-4 h-4" />
          Quay lại danh sách
        </Link>
      </div>
    );
  }

  return <EditTaskForm task={task} />;
}
