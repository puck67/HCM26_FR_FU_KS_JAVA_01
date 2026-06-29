import { getAllTodos } from "@/lib/data";
import TodoCard from "@/components/TodoCard";
import Link from "next/link";
import type { Metadata } from "next";

export const metadata: Metadata = {
    title: "All Todos | ASM2 TodoFlow",
    description: "View and manage all your todos in one place.",
};

export default async function TodosPage() {
    const todos = getAllTodos();

    const completed = todos.filter((t) => t.completed).length;
    const pending = todos.length - completed;

    // Early return for empty state - a clean, human coding style
    if (todos.length === 0) {
        return (
            <div className="space-y-8">
                <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                    <div>
                        <h1 className="text-3xl font-bold text-white">All Todos</h1>
                        <p className="text-gray-400 mt-1 text-sm">
                            0 total · 0 completed · 0 pending
                        </p>
                    </div>
                    <Link
                        href="/todos/new"
                        id="todos-page-new"
                        className="inline-flex items-center gap-2 px-5 py-2.5 bg-gradient-to-r from-violet-600 to-indigo-600 hover:from-violet-500 hover:to-indigo-500 text-white font-semibold rounded-xl transition-all duration-200 shadow-lg shadow-violet-500/20 text-sm"
                    >
                        <svg
                            className="w-4 h-4"
                            fill="none"
                            stroke="currentColor"
                            viewBox="0 0 24 24"
                        >
                            <path
                                strokeLinecap="round"
                                strokeLinejoin="round"
                                strokeWidth={2.5}
                                d="M12 4v16m8-8H4"
                            />
                        </svg>
                        New Todo
                    </Link>
                </div>

                <div className="glass-card rounded-3xl p-16 text-center">
                    <div className="w-16 h-16 rounded-2xl bg-gray-800/60 flex items-center justify-center mx-auto mb-4">
                        <svg
                            className="w-8 h-8 text-gray-600"
                            fill="none"
                            stroke="currentColor"
                            viewBox="0 0 24 24"
                        >
                            <path
                                strokeLinecap="round"
                                strokeLinejoin="round"
                                strokeWidth={1.5}
                                d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"
                            />
                        </svg>
                    </div>
                    <h2 className="text-xl font-semibold text-gray-400 mb-2">
                        No todos yet
                    </h2>
                    <p className="text-gray-600 mb-6 text-sm">
                        Create your first todo to get started
                    </p>
                    <Link
                        href="/todos/new"
                        id="empty-state-new-todo"
                        className="inline-flex items-center gap-2 px-6 py-2.5 bg-gradient-to-r from-violet-600 to-indigo-600 text-white font-semibold rounded-xl transition-all duration-200 text-sm"
                    >
                        <svg
                            className="w-4 h-4"
                            fill="none"
                            stroke="currentColor"
                            viewBox="0 0 24 24"
                        >
                            <path
                                strokeLinecap="round"
                                strokeLinejoin="round"
                                strokeWidth={2.5}
                                d="M12 4v16m8-8H4"
                            />
                        </svg>
                        Create Todo
                    </Link>
                </div>
            </div>
        );
    }

    return (
        <div className="space-y-8">
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                <div>
                    <h1 className="text-3xl font-bold text-white">All Todos</h1>
                    <p className="text-gray-400 mt-1 text-sm">
                        {todos.length} total · {completed} completed · {pending} pending
                    </p>
                </div>
                <Link
                    href="/todos/new"
                    id="todos-page-new"
                    className="inline-flex items-center gap-2 px-5 py-2.5 bg-gradient-to-r from-violet-600 to-indigo-600 hover:from-violet-500 hover:to-indigo-500 text-white font-semibold rounded-xl transition-all duration-200 shadow-lg shadow-violet-500/20 text-sm"
                >
                    <svg
                        className="w-4 h-4"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                    >
                        <path
                            strokeLinecap="round"
                            strokeLinejoin="round"
                            strokeWidth={2.5}
                            d="M12 4v16m8-8H4"
                        />
                    </svg>
                    New Todo
                </Link>
            </div>

            <div className="glass-card rounded-2xl p-5">
                <div className="flex items-center justify-between mb-3">
                    <span className="text-sm font-medium text-gray-300">
                        Progress
                    </span>
                    <span className="text-sm text-gray-500">
                        {Math.round((completed / todos.length) * 100)}% complete
                    </span>
                </div>
                <div className="h-2 bg-gray-800 rounded-full overflow-hidden">
                    <div
                        className="h-full bg-gradient-to-r from-violet-600 to-emerald-500 rounded-full transition-all duration-700"
                        style={{ width: `${(completed / todos.length) * 100}%` }}
                    />
                </div>
                <div className="flex items-center gap-4 mt-3 text-xs text-gray-600">
                    <span className="flex items-center gap-1.5">
                        <span className="w-2 h-2 bg-emerald-500 rounded-full" />
                        {completed} completed
                    </span>
                    <span className="flex items-center gap-1.5">
                        <span className="w-2 h-2 bg-amber-500 rounded-full" />
                        {pending} pending
                    </span>
                </div>
            </div>

            <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-4">
                {todos.map((todo) => (
                    <TodoCard
                        key={todo.id}
                        todo={todo}
                    />
                ))}
            </div>
        </div>
    );
}
