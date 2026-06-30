import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import Link from "next/link";
import "./globals.css";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "Taskify - Task Management System",
  description: "Next.js App Router Task Management System",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html
      lang="en"
      className={`${geistSans.variable} ${geistMono.variable} h-full antialiased`}
    >
      <body className="min-h-full flex flex-col bg-zinc-950 text-zinc-50 font-sans">
        {/* Navigation Bar */}
        <header className="border-b border-zinc-800 bg-zinc-900/50 backdrop-blur sticky top-0 z-50">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
            <div className="flex items-center gap-8">
              <Link href="/" className="flex items-center gap-2 font-bold text-xl text-teal-400">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                  <polyline points="22 12 18 12 15 21 9 3 6 12 2 12" />
                </svg>
                Taskify
              </Link>
              <nav className="hidden sm:flex items-center gap-6">
                <Link href="/tasks" className="text-zinc-300 hover:text-white transition-colors text-sm font-medium">
                  Tasks List
                </Link>
                <Link href="/tasks/new" className="text-zinc-300 hover:text-white transition-colors text-sm font-medium">
                  Create Task
                </Link>
              </nav>
            </div>
            <div>
              <Link href="/tasks/new" className="inline-flex items-center justify-center rounded-lg bg-teal-500 px-4 py-2 text-sm font-semibold text-zinc-950 shadow-sm hover:bg-teal-400 transition-colors">
                New Task
              </Link>
            </div>
          </div>
        </header>

        {/* Main Workspace */}
        <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8">
          {children}
        </main>

        {/* Footer */}
        <footer className="border-t border-zinc-800 py-6 bg-zinc-900/20">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex flex-col sm:flex-row items-center justify-between gap-4 text-xs text-zinc-500">
            <span>Next.js App Router Task Manager (REACT_Assignment02)</span>
            <span>Created by TienVM15</span>
          </div>
        </footer>
      </body>
    </html>
  );
}
