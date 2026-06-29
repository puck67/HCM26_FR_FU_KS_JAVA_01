import { getTodoById } from "@/lib/data";
import { notFound } from "next/navigation";
import EditTodoForm from "@/components/EditTodoForm";
import Link from "next/link";
import type { Metadata } from "next";

interface TodoDetailPageProps {
    params: Promise<{ id: string }>;
}

// Arrow function syntax for generateMetadata
export const generateMetadata = async ({ params }: TodoDetailPageProps): Promise<Metadata> => {
    const { id } = await params;
    const todo = getTodoById(id);
    return {
        title: todo ? `Edit: ${todo.name} | ASM2 TodoFlow` : "Todo Not Found",
        description: todo?.description ?? "Edit your todo details",
    };
};

export default async function TodoDetailPage({ params }: TodoDetailPageProps) {
    const { id } = await params;
    const todo = getTodoById(id);

    if (!todo) {
        notFound();
    }

    return (
        <div className="max-w-2xl mx-auto space-y-8">
            <div className="flex items-center gap-2 text-gray-500 text-sm">
                <Link
                    href="/todos"
                    className="hover:text-gray-300 transition-colors"
                >
                    Todos
                </Link>
                <svg
                    className="w-3 h-3"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                >
                    <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M9 5l7 7-7 7"
                    />
                </svg>
                <span className="text-gray-400 truncate max-w-[200px]">
                    {todo.name}
                </span>
                <svg
                    className="w-3 h-3"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                >
                    <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M9 5l7 7-7 7"
                    />
                </svg>
                <span className="text-gray-400">Edit</span>
            </div>

            <div>
                <h1 className="text-3xl font-bold text-white">Edit Todo</h1>
                <p className="text-gray-400 mt-1 text-sm">
                    Update the details for this todo.
                </p>
            </div>

            <div className="glass-card rounded-2xl p-5 flex items-start gap-4">
                <div
                    className={`w-10 h-10 rounded-xl flex items-center justify-center flex-shrink-0 ${
                        todo.completed
                            ? "bg-emerald-900/40 border border-emerald-800/40"
                            : "bg-amber-900/30 border border-amber-800/30"
                    }`}
                >
                    {todo.completed ? (
                        <svg
                            className="w-5 h-5 text-emerald-400"
                            fill="none"
                            stroke="currentColor"
                            viewBox="0 0 24 24"
                        >
                            <path
                                strokeLinecap="round"
                                strokeLinejoin="round"
                                strokeWidth={2}
                                d="M5 13l4 4L19 7"
                            />
                        </svg>
                    ) : (
                        <svg
                            className="w-5 h-5 text-amber-400"
                            fill="none"
                            stroke="currentColor"
                            viewBox="0 0 24 24"
                        >
                            <path
                                strokeLinecap="round"
                                strokeLinejoin="round"
                                strokeWidth={2}
                                d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"
                            />
                        </svg>
                    )}
                </div>
                <div className="flex-1 min-w-0">
                    <div className="flex items-center gap-2 flex-wrap">
                        <h2
                            className={`font-semibold ${
                                todo.completed ? "line-through text-gray-500" : "text-white"
                            }`}
                        >
                            {todo.name}
                        </h2>
                        <span
                            className={`px-2 py-0.5 rounded-full text-xs font-medium ${
                                todo.completed
                                    ? "bg-emerald-900/40 text-emerald-400 border border-emerald-800/50"
                                    : "bg-amber-900/40 text-amber-400 border border-amber-800/50"
                            }`}
                        >
                            {todo.completed ? "Completed" : "Pending"}
                        </span>
                    </div>
                    <p className="text-xs text-gray-600 mt-1">
                        Created{" "}
                        {new Date(todo.createdAt).toLocaleDateString("en-US", {
                            month: "long",
                            day: "numeric",
                            year: "numeric",
                        })}
                        {todo.updatedAt !== todo.createdAt && (
                            <>
                                {" · "}Updated{" "}
                                {new Date(todo.updatedAt).toLocaleDateString("en-US", {
                                    month: "long",
                                    day: "numeric",
                                    year: "numeric",
                                })}
                            </>
                        )}
                    </p>
                </div>
            </div>

            <div className="glass-card rounded-3xl p-8">
                <div className="flex items-center gap-3 mb-6 pb-6 border-b border-gray-800/60">
                    <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-indigo-500 to-violet-600 flex items-center justify-center shadow-lg shadow-indigo-500/25">
                        <svg
                            className="w-5 h-5 text-white"
                            fill="none"
                            stroke="currentColor"
                            viewBox="0 0 24 24"
                        >
                            <path
                                strokeLinecap="round"
                                strokeLinejoin="round"
                                strokeWidth={2}
                                d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"
                            />
                        </svg>
                    </div>
                    <div>
                        <h2 className="font-semibold text-white">Update Details</h2>
                        <p className="text-xs text-gray-500">Modify name and description</p>
                    </div>
                </div>

                <EditTodoForm todo={todo} />
            </div>
        </div>
    );
}
