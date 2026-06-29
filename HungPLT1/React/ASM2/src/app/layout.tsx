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
  title: "TaskManager - Professional Task Manager",
  description: "Next.js 15 Task Management Application with App Router, Formik, and Server Actions",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html
      lang="en"
      className={`${geistSans.variable} ${geistMono.variable} h-full antialiased dark`}
    >
      <body className="min-h-full flex flex-col bg-slate-950 text-slate-100 font-sans selection:bg-violet-500 selection:text-white">
        <header className="sticky top-0 z-40 w-full border-b border-slate-800 bg-slate-950/80 backdrop-blur-md">
          <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
            <div className="flex items-center gap-2">
              <div className="w-8 h-8 rounded-lg bg-gradient-to-tr from-violet-600 to-indigo-600 flex items-center justify-center font-bold text-white shadow-lg shadow-violet-500/30">
                T
              </div>
              <span className="font-extrabold text-xl tracking-tight bg-gradient-to-r from-violet-400 to-indigo-300 bg-clip-text text-transparent">
                TaskManager
              </span>
            </div>
            <nav className="flex items-center gap-6">
              <Link
                href="/"
                className="text-sm font-semibold text-slate-300 hover:text-white transition duration-150"
              >
                Home
              </Link>
              <Link
                href="/tasks"
                className="text-sm font-semibold text-slate-300 hover:text-white transition duration-150"
              >
                Tasks
              </Link>
              <Link
                href="/tasks/new"
                className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-violet-600 hover:bg-violet-700 text-sm font-semibold text-white shadow-lg shadow-violet-600/25 hover:shadow-violet-600/35 transition duration-150"
              >
                New Task
              </Link>
            </nav>
          </div>
        </header>

        <main className="flex-grow flex flex-col">
          {children}
        </main>

        <footer className="w-full border-t border-slate-900 bg-slate-950 py-8">
          <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 text-center text-sm text-slate-500 flex flex-col sm:flex-row items-center justify-between gap-4">
            <p>&copy; {new Date().getFullYear()} TaskManager app. All rights reserved.</p>
            <p className="font-medium bg-gradient-to-r from-slate-400 to-slate-500 bg-clip-text text-transparent">
              FPT Software Academy
            </p>
          </div>
        </footer>
      </body>
    </html>
  );
}
