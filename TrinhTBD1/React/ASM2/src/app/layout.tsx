import type { Metadata } from "next";
import Link from "next/link";
import "./globals.css";

export const metadata: Metadata = {
  title: "Task System",
  description: "Next.js Task Application",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" className="h-full antialiased dark">
      <body className="min-h-full flex flex-col bg-[#0f1115] text-zinc-100 font-sans">
        <header className="bg-zinc-900 border-b border-zinc-800">
          <div className="max-w-6xl mx-auto px-4 sm:px-6 h-14 flex items-center justify-between">
            <Link href="/" className="text-lg font-bold tracking-tight text-white hover:text-red-400 transition-colors flex items-center gap-2">
              <span className="w-2.5 h-2.5 rounded-full bg-[#800020] inline-block"></span>
              <span>TaskManager</span>
            </Link>
            <nav className="flex items-center gap-2">
              <Link href="/" className="px-3 py-1.5 rounded text-sm font-medium text-zinc-300 hover:text-white hover:bg-zinc-800 transition-colors">
                Home
              </Link>
              <Link href="/tasks" className="px-3 py-1.5 rounded text-sm font-medium text-zinc-300 hover:text-white hover:bg-zinc-800 transition-colors">
                Tasks
              </Link>
              <Link href="/tasks/new" className="px-3 py-1.5 rounded text-sm font-medium bg-[#800020] text-white hover:bg-[#991032] transition-colors ml-2">
                New Task
              </Link>
            </nav>
          </div>
        </header>
        <main className="flex-1 max-w-6xl w-full mx-auto px-4 sm:px-6 py-6">
          {children}
        </main>
        <footer className="border-t border-zinc-800/80 py-4 text-center text-xs text-zinc-500">
          Task Management System
        </footer>
      </body>
    </html>
  );
}
