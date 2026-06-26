import type { Metadata } from "next";
import { Outfit } from "next/font/google";
import Link from "next/link";
import "./globals.css";

const outfit = Outfit({ subsets: ["latin"], variable: "--font-outfit" });

export const metadata: Metadata = {
  title: "TaskFlow - Next.js Task Dashboard",
  description: "A task management app built with Next.js App Router, Server Actions, and Tailwind CSS",
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en" className={outfit.variable}>
      <body className="font-[family-name:var(--font-outfit)] min-h-[100dvh] flex flex-col">
        {/* Navbar */}
        <header className="sticky top-0 z-50 bg-white/80 backdrop-blur-md border-b border-slate-200/60">
          <nav className="max-w-5xl mx-auto px-4 sm:px-6 h-16 flex items-center justify-between">
            <Link href="/" className="flex items-center gap-2 group">
              <div className="w-8 h-8 rounded-(--radius-btn) bg-accent flex items-center justify-center">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                  <path d="M9 11l3 3L22 4" />
                  <path d="M21 12v7a2 2 0 01-2 2H5a2 2 0 01-2-2V5a2 2 0 012-2h11" />
                </svg>
              </div>
              <span className="text-lg font-semibold tracking-tight text-slate-900 group-hover:text-accent transition-colors">
                TaskFlow
              </span>
            </Link>

            <div className="flex items-center gap-1">
              <NavLink href="/">Home</NavLink>
              <NavLink href="/tasks">Tasks</NavLink>
              <Link
                href="/tasks/new"
                className="ml-2 px-4 py-2 rounded-(--radius-btn) text-sm font-medium bg-accent text-white hover:bg-accent-dark transition-colors active:scale-[0.98]"
              >
                + New Task
              </Link>
            </div>
          </nav>
        </header>

        {/* Main */}
        <main className="flex-1 max-w-5xl mx-auto w-full px-4 sm:px-6 py-8">
          {children}
        </main>

        {/* Footer */}
        <footer className="border-t border-slate-200/60 py-6 text-center text-sm text-slate-400">
          TaskFlow - Next.js App Router + Server Actions
        </footer>
      </body>
    </html>
  );
}

function NavLink({ href, children }: { href: string; children: React.ReactNode }) {
  return (
    <Link
      href={href}
      className="px-4 py-2 rounded-(--radius-btn) text-sm font-medium text-slate-600 hover:text-slate-900 hover:bg-slate-100 transition-colors"
    >
      {children}
    </Link>
  );
}
