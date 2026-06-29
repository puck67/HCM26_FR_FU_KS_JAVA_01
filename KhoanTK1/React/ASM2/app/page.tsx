import Link from "next/link";
import type { Metadata } from "next";

export const metadata: Metadata = {
    title: "Home | ASM2 TodoFlow",
    description: "Manage your todos efficiently with ASM2 TodoFlow - a Next.js powered todo manager.",
};

const features = [
    {
        icon: (
            <svg
                className="w-6 h-6"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
            >
                <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4"
                />
            </svg>
        ),
        title: "Todo Management",
        desc: "Create, update, and delete todos with full CRUD operations powered by Next.js Server Actions.",
        color: "from-violet-500 to-purple-600",
        shadow: "shadow-violet-500/20",
    },
    {
        icon: (
            <svg
                className="w-6 h-6"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
            >
                <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M13 10V3L4 14h7v7l9-11h-7z"
                />
            </svg>
        ),
        title: "Server Actions",
        desc: "Lightning-fast mutations with Next.js Server Actions and automatic cache revalidation.",
        color: "from-indigo-500 to-blue-600",
        shadow: "shadow-indigo-500/20",
    },
    {
        icon: (
            <svg
                className="w-6 h-6"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
            >
                <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
                />
            </svg>
        ),
        title: "Form Validation",
        desc: "Robust client-side validation with Formik + Yup for a smooth user experience.",
        color: "from-emerald-500 to-teal-600",
        shadow: "shadow-emerald-500/20",
    },
    {
        icon: (
            <svg
                className="w-6 h-6"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
            >
                <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M4 5a1 1 0 011-1h14a1 1 0 011 1v2a1 1 0 01-1 1H5a1 1 0 01-1-1V5zM4 13a1 1 0 011-1h6a1 1 0 011 1v6a1 1 0 01-1 1H5a1 1 0 01-1-1v-6zM16 13a1 1 0 011-1h2a1 1 0 011 1v6a1 1 0 01-1 1h-2a1 1 0 01-1-1v-6z"
                />
            </svg>
        ),
        title: "App Router",
        desc: "File-based routing with Server and Client components — mixing the best of both worlds.",
        color: "from-rose-500 to-pink-600",
        shadow: "shadow-rose-500/20",
    },
];

export default function HomePage() {
    return (
        <div className="space-y-20">
            <section className="relative text-center pt-12 pb-8">
                <div className="absolute inset-0 flex items-center justify-center pointer-events-none">
                    <div className="w-[600px] h-[400px] bg-violet-600/10 blur-[100px] rounded-full" />
                </div>

                <div className="relative space-y-6 fade-in-up">
                    <div className="inline-flex items-center gap-2 px-3 py-1.5 rounded-full border border-violet-500/30 bg-violet-500/10 text-violet-300 text-xs font-medium mb-2">
                        <span className="w-1.5 h-1.5 bg-violet-400 rounded-full animate-pulse" />
                        Next.js App Router · TypeScript · Tailwind CSS
                    </div>

                    <h1 className="text-5xl sm:text-6xl lg:text-7xl font-bold tracking-tight">
                        <span className="text-white">Manage Todos</span>
                        <br />
                        <span className="gradient-text">Effortlessly.</span>
                    </h1>

                    <p className="text-gray-400 text-lg sm:text-xl max-w-2xl mx-auto leading-relaxed">
                        A modern todo management application built with Next.js 14, featuring
                        Server Actions, file-based routing, and real-time form validation.
                    </p>

                    <div className="flex flex-col sm:flex-row items-center justify-center gap-3 pt-2">
                        <Link
                            href="/todos"
                            id="hero-view-todos"
                            className="px-8 py-3 bg-gradient-to-r from-violet-600 to-indigo-600 hover:from-violet-500 hover:to-indigo-500 text-white font-semibold rounded-xl transition-all duration-200 shadow-lg shadow-violet-500/25 hover:shadow-violet-500/40 hover:scale-105"
                        >
                            View All Todos
                        </Link>
                        <Link
                            href="/todos/new"
                            id="hero-create-todo"
                            className="px-8 py-3 bg-gray-800/80 hover:bg-gray-700/80 text-gray-200 font-semibold rounded-xl border border-gray-700/60 hover:border-gray-600 transition-all duration-200 hover:scale-105"
                        >
                            Create Todo
                        </Link>
                    </div>
                </div>
            </section>

            <section className="grid grid-cols-3 gap-4 max-w-2xl mx-auto">
                {[
                    { label: "Pages", value: "4" },
                    { label: "Server Actions", value: "4" },
                    { label: "Components", value: "6+" },
                ].map((stat) => (
                    <div
                        key={stat.label}
                        className="glass-card rounded-2xl p-6 text-center"
                    >
                        <div className="text-3xl font-bold text-white mb-1">
                            {stat.value}
                        </div>
                        <div className="text-sm text-gray-500">
                            {stat.label}
                        </div>
                    </div>
                ))}
            </section>

            <section>
                <h2 className="text-2xl font-bold text-white text-center mb-8">
                    What&apos;s inside
                </h2>
                <div className="grid sm:grid-cols-2 lg:grid-cols-4 gap-4">
                    {features.map((f, i) => (
                        <div
                            key={f.title}
                            className="glass-card rounded-2xl p-6 hover:border-gray-600/60 transition-all duration-300 group hover:-translate-y-1"
                            style={{ animationDelay: `${i * 80}ms` }}
                        >
                            <div
                                className={`w-12 h-12 rounded-xl bg-gradient-to-br ${f.color} flex items-center justify-center text-white mb-4 shadow-lg ${f.shadow} group-hover:scale-110 transition-transform duration-300`}
                            >
                                {f.icon}
                            </div>
                            <h3 className="font-semibold text-white mb-2">
                                {f.title}
                            </h3>
                            <p className="text-sm text-gray-400 leading-relaxed">
                                {f.desc}
                            </p>
                        </div>
                    ))}
                </div>
            </section>

            <section className="glass-card rounded-3xl p-10 text-center relative overflow-hidden">
                <div className="absolute inset-0 bg-gradient-to-br from-violet-900/20 to-indigo-900/20 pointer-events-none" />
                <div className="relative">
                    <h2 className="text-3xl font-bold text-white mb-3">
                        Ready to get started?
                    </h2>
                    <p className="text-gray-400 mb-6">
                        Create your first todo and start managing your work efficiently.
                    </p>
                    <Link
                        href="/todos/new"
                        id="cta-create-todo"
                        className="inline-flex items-center gap-2 px-8 py-3 bg-gradient-to-r from-violet-600 to-indigo-600 hover:from-violet-500 hover:to-indigo-500 text-white font-semibold rounded-xl transition-all duration-200 shadow-lg shadow-violet-500/25 pulse-glow"
                    >
                        <svg
                            className="w-5 h-5"
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
                        Create Your First Todo
                    </Link>
                </div>
            </section>
        </div>
    );
}
