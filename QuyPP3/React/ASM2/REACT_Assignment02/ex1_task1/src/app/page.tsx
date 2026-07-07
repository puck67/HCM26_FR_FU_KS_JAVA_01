import Link from "next/link";

export default function HomePage() {
  return (
    <div className="flex flex-col items-center justify-center min-h-[65vh] text-center px-4">
      <h1 className="text-4xl font-extrabold text-slate-900 tracking-tight mb-4">
        Next.js Task Architecture
      </h1>
      <p className="text-md text-slate-600 max-w-md mb-8">
        An advanced dashboard utilizing Server Components for high-speed data delivery, and server actions for state mutations.
      </p>
      <Link
        href="/tasks"
        className="px-6 py-3 bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-xl shadow-md shadow-blue-200 transition-all transform hover:-translate-y-0.5"
      >
        Open Workspace Dashboard &rarr;
      </Link>
    </div>
  );
}
