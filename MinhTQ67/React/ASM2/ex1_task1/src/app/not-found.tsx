import Link from "next/link";

export default function NotFound() {
  return (
    <div className="flex flex-col items-center justify-center py-32 text-center">
      <div className="text-6xl font-extrabold text-gray-200 mb-4">404</div>
      <h2 className="text-xl font-bold text-gray-700 mb-2">Page Not Found</h2>
      <p className="text-gray-500 text-sm mb-6">The page you are looking for does not exist.</p>
      <Link
        href="/"
        className="px-5 py-2.5 bg-indigo-600 text-white rounded-xl text-sm font-semibold hover:bg-indigo-700 transition-colors"
      >
        Go Home
      </Link>
    </div>
  );
}
