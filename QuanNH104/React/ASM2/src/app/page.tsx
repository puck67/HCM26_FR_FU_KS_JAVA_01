import Link from "next/link";
import { getTasks } from "@/lib/tasksStore";
import { CheckCircle2, Clock, ListTodo, PlusCircle, ArrowRight } from "lucide-react";

export const revalidate = 0; // Disable static rendering cache to get fresh stats

export default function Home() {
  const tasks = getTasks();
  const total = tasks.length;
  const completed = tasks.filter((t) => t.completed).length;
  const pending = total - completed;

  return (
    <div className="relative isolate overflow-hidden">
      {/* Background glow effects */}
      <div className="absolute inset-x-0 -top-40 -z-10 transform-gpu overflow-hidden blur-3xl sm:-top-80">
        <div
          className="relative left-[calc(50%-11rem)] aspect-1155/678 w-[36rem] -translate-x-1/2 rotate-[30deg] bg-gradient-to-tr from-indigo-500 to-violet-500 opacity-20 sm:left-[calc(50%-30rem)] sm:w-[72.1875rem]"
          style={{
            clipPath:
              "polygon(74.1% 44.1%, 100% 61.6%, 97.5% 26.9%, 85.5% 0.1%, 80.7% 2%, 72.5% 32.5%, 60.2% 62.4%, 52.4% 68.1%, 47.5% 58.3%, 45.2% 34.5%, 27.5% 76.7%, 0.1% 64.9%, 17.9% 100%, 27.6% 76.8%, 76.1% 97.7%, 74.1% 44.1%)",
          }}
        />
      </div>

      <div className="text-center max-w-3xl mx-auto py-8">
        <h1 className="text-4xl font-extrabold tracking-tight text-white sm:text-6xl bg-gradient-to-b from-white to-zinc-400 bg-clip-text text-transparent">
          Quản lý công việc hiệu quả với TaskFlow
        </h1>
        <p className="mt-6 text-lg leading-8 text-zinc-400">
          Ứng dụng Next.js hiện đại giúp bạn lên kế hoạch, tổ chức và hoàn thành các mục tiêu công việc hàng ngày một cách trực quan và mượt mà nhất.
        </p>

        {/* Call to Actions */}
        <div className="mt-10 flex items-center justify-center gap-x-6">
          <Link
            href="/tasks"
            className="group inline-flex items-center gap-2 rounded-xl bg-indigo-600 px-6 py-3 text-base font-semibold text-white shadow-md shadow-indigo-600/10 hover:bg-indigo-500 hover:shadow-indigo-500/20 hover:scale-[1.02] active:scale-[0.98] transition-all duration-200"
          >
            Xem danh sách
            <ArrowRight className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
          </Link>
          <Link
            href="/tasks/new"
            className="inline-flex items-center gap-2 rounded-xl border border-zinc-800 bg-zinc-900/50 hover:bg-zinc-900 px-6 py-3 text-base font-semibold text-zinc-350 hover:text-white transition-all duration-200"
          >
            <PlusCircle className="w-5 h-5" />
            Tạo việc mới
          </Link>
        </div>
      </div>

      {/* Stats Section */}
      <div className="mx-auto mt-16 sm:mt-20 max-w-5xl">
        <h2 className="text-center text-sm font-semibold tracking-wider text-indigo-400 uppercase">
          Trạng thái hiện tại
        </h2>
        <div className="mt-6 grid grid-cols-1 gap-5 sm:grid-cols-3">
          {/* Card 1: Total */}
          <div className="overflow-hidden rounded-2xl border border-zinc-900 bg-zinc-900/40 p-6 backdrop-blur-sm">
            <div className="flex items-center gap-4">
              <div className="rounded-xl bg-indigo-500/10 p-3 text-indigo-400">
                <ListTodo className="h-6 w-6" />
              </div>
              <div>
                <p className="text-sm font-medium text-zinc-400">Tổng số công việc</p>
                <p className="text-3xl font-semibold tracking-tight text-white mt-1">
                  {total}
                </p>
              </div>
            </div>
          </div>

          {/* Card 2: Completed */}
          <div className="overflow-hidden rounded-2xl border border-zinc-900 bg-zinc-900/40 p-6 backdrop-blur-sm">
            <div className="flex items-center gap-4">
              <div className="rounded-xl bg-emerald-500/10 p-3 text-emerald-400">
                <CheckCircle2 className="h-6 w-6" />
              </div>
              <div>
                <p className="text-sm font-medium text-zinc-400">Đã hoàn thành</p>
                <p className="text-3xl font-semibold tracking-tight text-white mt-1">
                  {completed}
                </p>
              </div>
            </div>
          </div>

          {/* Card 3: Pending */}
          <div className="overflow-hidden rounded-2xl border border-zinc-900 bg-zinc-900/40 p-6 backdrop-blur-sm">
            <div className="flex items-center gap-4">
              <div className="rounded-xl bg-amber-500/10 p-3 text-amber-400">
                <Clock className="h-6 w-6" />
              </div>
              <div>
                <p className="text-sm font-medium text-zinc-400">Đang chờ xử lý</p>
                <p className="text-3xl font-semibold tracking-tight text-white mt-1">
                  {pending}
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
