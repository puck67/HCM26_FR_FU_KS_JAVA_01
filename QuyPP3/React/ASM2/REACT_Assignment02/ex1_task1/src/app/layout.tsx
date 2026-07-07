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
  title: "NextJS Architecture Dashboard",
  description: "NextJS App Router CRUD",
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en" className={`${geistSans.variable} ${geistMono.variable} h-full antialiased`}>
      <body className="min-h-screen bg-slate-50/50 text-slate-900 smooth-antialiasing flex flex-col font-sans">
        <nav className="sticky top-0 bg-white/80 backdrop-blur-md border-b border-slate-200 z-40">
          <div className="max-w-4xl mx-auto px-4 h-16 flex items-center justify-between">
            <Link href="/" className="font-black tracking-tight text-slate-900 text-base">
              Next<span className="text-blue-600 font-medium">Dashboard</span>
            </Link>
            <div className="flex gap-6 text-sm font-semibold text-slate-600">
              <Link href="/" className="hover:text-blue-600 transition">Home</Link>
              <Link href="/tasks" className="hover:text-blue-600 transition">Workspace</Link>
            </div>
          </div>
        </nav>
        <main className="max-w-4xl mx-auto px-4 py-8 flex-1 w-full font-sans">
          {children}
        </main>
      </body>
    </html>
  );
}
