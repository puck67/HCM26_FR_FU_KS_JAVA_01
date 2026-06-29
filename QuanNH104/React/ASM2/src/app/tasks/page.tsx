import Link from "next/link";
import { getTasks } from "@/lib/tasksStore";
import { deleteTask, toggleTaskCompleted } from "@/lib/actions";
import { CheckCircle2, Edit3, Trash2, Plus, Calendar, AlertCircle } from "lucide-react";

export const dynamic = "force-dynamic";

export default async function TasksPage() {
  const tasks = getTasks();

  return (
    <div className="max-w-4xl mx-auto">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 border-b border-zinc-900 pb-6 mb-8">
        <div>
          <h1 className="text-3xl font-bold tracking-tight text-white">
            Danh sách công việc
          </h1>
          <p className="mt-1 text-sm text-zinc-400">
            Quản lý, sắp xếp và theo dõi tiến trình thực hiện của các đầu việc.
          </p>
        </div>
        <Link
          href="/tasks/new"
          className="inline-flex items-center justify-center gap-2 rounded-xl bg-indigo-600 px-4 py-2.5 text-sm font-semibold text-white shadow-md shadow-indigo-600/10 hover:bg-indigo-500 active:scale-95 transition-all duration-200"
        >
          <Plus className="w-4 h-4" />
          Tạo công việc mới
        </Link>
      </div>

      {/* Task List */}
      {tasks.length === 0 ? (
        <div className="flex flex-col items-center justify-center rounded-2xl border border-dashed border-zinc-800 bg-zinc-900/10 py-16 px-4 text-center">
          <div className="rounded-xl bg-zinc-900 p-4 text-zinc-400 mb-4">
            <AlertCircle className="h-8 w-8 text-zinc-500" />
          </div>
          <h3 className="text-lg font-semibold text-white">Chưa có công việc nào</h3>
          <p className="mt-1 text-sm text-zinc-500 max-w-sm">
            Hộp cát công việc hiện đang trống. Hãy bắt đầu bằng cách tạo công việc đầu tiên của bạn!
          </p>
          <Link
            href="/tasks/new"
            className="mt-6 inline-flex items-center gap-2 rounded-xl bg-indigo-600/10 border border-indigo-500/20 px-4 py-2 text-sm font-semibold text-indigo-400 hover:bg-indigo-600 hover:text-white transition-all duration-200"
          >
            Tạo công việc đầu tiên
          </Link>
        </div>
      ) : (
        <div className="space-y-4">
          {tasks.map((task) => (
            <div
              key={task.id}
              className={`group relative overflow-hidden rounded-2xl border transition-all duration-300 ${
                task.completed
                  ? "border-emerald-500/20 bg-emerald-950/5 hover:border-emerald-500/30"
                  : "border-zinc-900 bg-zinc-900/25 hover:border-zinc-800 hover:bg-zinc-900/40"
              }`}
            >
              <div className="p-5 sm:p-6 flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                {/* Checkbox and Info */}
                <div className="flex items-start gap-4 flex-1">
                  <form
                    action={toggleTaskCompleted.bind(null, task.id)}
                    className="mt-1"
                  >
                    <button
                      type="submit"
                      className={`flex h-6 w-6 items-center justify-center rounded-full border transition-all duration-200 ${
                        task.completed
                          ? "border-emerald-500 bg-emerald-500/15 text-emerald-400 hover:bg-emerald-550"
                          : "border-zinc-700 hover:border-indigo-500 hover:bg-indigo-500/10 text-transparent hover:text-indigo-400"
                      }`}
                      title={task.completed ? "Đánh dấu chưa hoàn thành" : "Đánh dấu hoàn thành"}
                    >
                      <CheckCircle2 className="h-4.5 w-4.5 fill-current" />
                    </button>
                  </form>

                  <div className="space-y-1">
                    <h3
                      className={`text-lg font-semibold leading-6 transition-all duration-200 ${
                        task.completed
                          ? "text-zinc-550 line-through decoration-zinc-700"
                          : "text-white"
                      }`}
                    >
                      {task.name}
                    </h3>
                    {task.description && (
                      <p
                        className={`text-sm leading-relaxed ${
                          task.completed ? "text-zinc-600 line-through" : "text-zinc-400"
                        }`}
                      >
                        {task.description}
                      </p>
                    )}
                    <div className="flex items-center gap-2 text-xs text-zinc-500 pt-1">
                      <Calendar className="w-3.5 h-3.5" />
                      <span>
                        Tạo lúc: {new Date(task.createdAt).toLocaleString("vi-VN")}
                      </span>
                    </div>
                  </div>
                </div>

                {/* Actions */}
                <div className="flex items-center sm:justify-end gap-2.5 pl-10 sm:pl-0">
                  {/* Mark Completed Button */}
                  <form action={toggleTaskCompleted.bind(null, task.id)}>
                    <button
                      type="submit"
                      className={`inline-flex items-center gap-1.5 rounded-lg px-3 py-1.5 text-xs font-semibold border transition-all duration-200 cursor-pointer ${
                        task.completed
                          ? "border-emerald-500/20 bg-emerald-500/10 text-emerald-400 hover:bg-emerald-500/20"
                          : "border-zinc-800 bg-zinc-900 text-zinc-400 hover:border-zinc-700 hover:text-white"
                      }`}
                    >
                      {task.completed ? "Đã xong" : "Chưa xong"}
                    </button>
                  </form>

                  {/* Edit Link */}
                  <Link
                    href={`/tasks/${task.id}`}
                    className="inline-flex items-center justify-center rounded-lg border border-zinc-800 bg-zinc-900 p-2 text-zinc-400 hover:border-zinc-700 hover:text-white transition-all duration-200"
                    title="Chỉnh sửa công việc"
                  >
                    <Edit3 className="w-4 h-4" />
                  </Link>

                  {/* Delete Form */}
                  <form action={deleteTask.bind(null, task.id)}>
                    <button
                      type="submit"
                      className="inline-flex items-center justify-center rounded-lg border border-red-950/40 bg-red-950/10 p-2 text-red-400 hover:border-red-500/30 hover:bg-red-500/15 hover:text-red-300 transition-all duration-200 cursor-pointer"
                      title="Xóa công việc"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </form>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
