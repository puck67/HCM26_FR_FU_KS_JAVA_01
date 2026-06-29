import Link from "next/link";

export default function NotFound() {
  return (
    <div className="min-h-[60vh] flex items-center justify-center">
      <div className="text-center space-y-6">
        <div className="text-8xl font-bold gradient-text">404</div>
        <h1 className="text-2xl font-bold text-white">Task Not Found</h1>
        <p className="text-gray-400">The task you are looking for does not exist or has been deleted.</p>
        <Link
          href="/tasks"
          className="inline-flex items-center gap-2 px-6 py-2.5 bg-gradient-to-r from-violet-600 to-indigo-600 text-white font-semibold rounded-xl transition-all duration-200"
        >
          <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 19l-7-7m0 0l7-7m-7 7h18" />
          </svg>
          Back to Tasks
        </Link>
      </div>
    </div>
  );
}
