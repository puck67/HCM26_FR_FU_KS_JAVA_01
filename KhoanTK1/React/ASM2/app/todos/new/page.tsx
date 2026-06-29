"use client";

import TodoForm from "@/components/TodoForm";
import { createTodo } from "@/lib/actions";
import Link from "next/link";

export default function NewTodoPage() {
    return (
        <div className="max-w-2xl mx-auto space-y-8">
            <div className="space-y-1">
                <div className="flex items-center gap-2 text-gray-500 text-sm mb-4">
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
                    <span className="text-gray-400">New</span>
                </div>
                <h1 className="text-3xl font-bold text-white">Create Todo</h1>
                <p className="text-gray-400 text-sm">
                    Fill in the details below to create a new todo.
                </p>
            </div>

            <div className="glass-card rounded-3xl p-8">
                <div className="flex items-center gap-3 mb-6 pb-6 border-b border-gray-800/60">
                    <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-violet-500 to-indigo-600 flex items-center justify-center shadow-lg shadow-violet-500/25">
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
                                d="M12 4v16m8-8H4"
                            />
                        </svg>
                    </div>
                    <div>
                        <h2 className="font-semibold text-white">New Todo</h2>
                        <p className="text-xs text-gray-500">Complete all required fields</p>
                    </div>
                </div>

                <TodoForm
                    mode="create"
                    onSubmit={createTodo}
                />
            </div>

            <div className="glass-card rounded-2xl p-5 flex items-start gap-3">
                <div className="w-8 h-8 rounded-lg bg-indigo-900/40 border border-indigo-800/40 flex items-center justify-center flex-shrink-0 mt-0.5">
                    <svg
                        className="w-4 h-4 text-indigo-400"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                    >
                        <path
                            strokeLinecap="round"
                            strokeLinejoin="round"
                            strokeWidth={2}
                            d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
                        />
                    </svg>
                </div>
                <div>
                    <p className="text-sm font-medium text-gray-300 mb-1">Tips</p>
                    <ul className="text-xs text-gray-500 space-y-1">
                        <li>• Todo name is required and must be at most 40 characters</li>
                        <li>• Description is optional but helps track context (max 200 chars)</li>
                        <li>• After creating, you can mark todos as complete from the todo list</li>
                    </ul>
                </div>
            </div>
        </div>
    );
}
