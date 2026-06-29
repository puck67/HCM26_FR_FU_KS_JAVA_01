import Link from "next/link";

export default function Navbar() {
  return (
    <nav className="bg-gray-900 shadow-lg">
      <div className="max-w-5xl mx-auto px-4">
        <div className="flex items-center justify-between h-16">
          <Link href="/" className="flex items-center gap-2">
            <span className="text-2xl">✅</span>
            <span className="text-white font-bold text-lg tracking-tight">TaskBoard</span>
          </Link>
          <div className="flex items-center gap-1">
            {[
              { href: "/", label: "Home" },
              { href: "/tasks", label: "Tasks" },
              { href: "/tasks/new", label: "+ New Task" },
            ].map((link) => (
              <Link
                key={link.href}
                href={link.href}
                className="px-4 py-2 rounded-md text-sm font-medium text-gray-300 hover:bg-gray-700 hover:text-white transition-colors"
              >
                {link.label}
              </Link>
            ))}
          </div>
        </div>
      </div>
    </nav>
  );
}
