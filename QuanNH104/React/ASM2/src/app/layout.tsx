import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import Link from "next/link";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "TaskFlow - Premium Task Management",
  description: "Next.js + Tailwind + Formik Task Manager",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html
      lang="vi"
      className={`${geistSans.variable} ${geistMono.variable} h-full antialiased`}
    >
      <body className="min-h-full flex flex-col bg-zinc-950 text-zinc-100 font-sans">
        {/* Navigation */}
        <header className="sticky top-0 z-40 w-full border-b border-zinc-850/80 bg-zinc-950/80 backdrop-blur-md">
          <div className="mx-auto flex h-16 max-w-7xl items-center justify-between px-4 sm:px-6 lg:px-8">
            <div className="flex items-center gap-8">
              <Link href="/" className="flex items-center gap-2 group">
                <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-gradient-to-tr from-indigo-500 to-violet-500 text-white shadow-lg shadow-indigo-500/20 group-hover:scale-105 transition-transform duration-200">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    strokeWidth={2.5}
                    stroke="currentColor"
                    className="w-5 h-5"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      d="M9 12.75 11.25 15 15 9.75M21 12c0 1.268-.63 2.39-1.593 3.068a3.745 3.745 0 0 1-1.043 3.296 3.745 3.745 0 0 1-3.296 1.043A3.745 3.745 0 0 1 12 21c-1.268 0-2.39-.63-3.068-1.593a3.746 3.746 0 0 1-3.296-1.043 3.745 3.745 0 0 1-1.043-3.296A3.745 3.745 0 0 1 3 12c0-1.268.63-2.39 1.593-3.068a3.745 3.745 0 0 1 1.043-3.296 3.746 3.746 0 0 1 3.296-1.043A3.746 3.746 0 0 1 12 3c1.268 0 2.39.63 3.068 1.593a3.746 3.746 0 0 1 3.296 1.043 3.746 3.746 0 0 1 1.043 3.296A3.745 3.745 0 0 1 21 12Z"
                    />
                  </svg>
                </div>
                <span className="text-xl font-bold tracking-tight bg-gradient-to-r from-white via-zinc-200 to-zinc-400 bg-clip-text text-transparent">
                  TaskFlow
                </span>
              </Link>

              <nav className="hidden md:flex items-center gap-1">
                <Link
                  href="/"
                  className="rounded-lg px-3 py-2 text-sm font-medium text-zinc-400 hover:text-white hover:bg-zinc-900 transition-colors"
                >
                  Trang chủ
                </Link>
                <Link
                  href="/tasks"
                  className="rounded-lg px-3 py-2 text-sm font-medium text-zinc-400 hover:text-white hover:bg-zinc-900 transition-colors"
                >
                  Danh sách công việc
                </Link>
                <Link
                  href="/tasks/new"
                  className="rounded-lg px-3 py-2 text-sm font-medium text-zinc-400 hover:text-white hover:bg-zinc-900 transition-colors"
                >
                  Tạo công việc mới
                </Link>
              </nav>
            </div>

            <div className="flex items-center gap-3">
              <Link
                href="/tasks/new"
                className="hidden sm:inline-flex items-center justify-center rounded-xl bg-indigo-600 px-4 py-2 text-sm font-semibold text-white shadow-md shadow-indigo-600/10 hover:bg-indigo-500 hover:shadow-indigo-500/20 hover:scale-[1.02] active:scale-[0.98] transition-all duration-200"
              >
                + Thêm mới
              </Link>
            </div>
          </div>
        </header>

        {/* Mobile Navigation Bar */}
        <div className="md:hidden sticky top-[64px] z-30 flex items-center justify-around bg-zinc-950 border-b border-zinc-900 py-3 text-xs text-zinc-400">
          <Link href="/" className="flex flex-col items-center gap-1 hover:text-white font-medium">
            Trang chủ
          </Link>
          <Link href="/tasks" className="flex flex-col items-center gap-1 hover:text-white font-medium">
            Công việc
          </Link>
          <Link href="/tasks/new" className="flex flex-col items-center gap-1 hover:text-white font-medium">
            Tạo mới
          </Link>
        </div>

        {/* Main Content */}
        <main className="flex-1 w-full max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 sm:py-12">
          {children}
        </main>

        {/* Footer */}
        <footer className="border-t border-zinc-900 py-6 text-center text-xs text-zinc-600 bg-zinc-950">
          <p>© {new Date().getFullYear()} TaskFlow. Thử thách ASM2 React + Next.js Server Actions.</p>
        </footer>
      </body>
    </html>
  );
}
