import type { Metadata } from "next";
import { Geist } from "next/font/google";
import "./globals.css";
import Link from "next/link";

const geist = Geist({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "ASM2 - Task Manager",
  description: "A powerful task management application built with Next.js App Router, TypeScript, Tailwind CSS, and Formik.",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en" className="dark">
      <body className={`${geist.className} bg-gray-950 text-gray-100 min-h-screen`}>
        {/* Navbar */}
        <nav className="sticky top-0 z-50 border-b border-gray-800/60 backdrop-blur-xl bg-gray-950/80">
          <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex items-center justify-between h-16">
              <Link href="/" className="flex items-center gap-2.5 group">
                <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-violet-500 to-indigo-600 flex items-center justify-center shadow-lg shadow-violet-500/25">
                  <svg className="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2.5} d="M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z" />
                  </svg>
                </div>
                <span className="font-bold text-lg tracking-tight text-white group-hover:text-violet-300 transition-colors">TaskFlow</span>
              </Link>

              <div className="flex items-center gap-1">
                <Link
                  href="/"
                  className="px-3 py-1.5 text-sm text-gray-400 hover:text-white hover:bg-gray-800/60 rounded-lg transition-all duration-200"
                >
                  Home
                </Link>
                <Link
                  href="/tasks"
                  className="px-3 py-1.5 text-sm text-gray-400 hover:text-white hover:bg-gray-800/60 rounded-lg transition-all duration-200"
                >
                  Tasks
                </Link>
                <Link
                  href="/tasks/new"
                  className="ml-2 px-4 py-1.5 text-sm font-medium text-white bg-gradient-to-r from-violet-600 to-indigo-600 hover:from-violet-500 hover:to-indigo-500 rounded-lg transition-all duration-200 shadow-lg shadow-violet-500/20"
                >
                  + New Task
                </Link>
              </div>
            </div>
          </div>
        </nav>

        <main className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          {children}
        </main>

        <footer className="mt-16 border-t border-gray-800/60 py-6 text-center text-gray-600 text-sm">
          © 2026 TaskFlow · Built with Next.js + TypeScript + Tailwind CSS
        </footer>
      </body>
    </html>
  );
}
