import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import Link from 'next/link';

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Next.js Task Dashboard",
  description: "ASM2 Task Dashboard built with Next.js App Router",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={`${inter.className} bg-gray-50 text-gray-900 min-h-screen flex flex-col`}>
        <nav className="bg-blue-800 text-white shadow-md">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex items-center justify-between h-16">
              <div className="flex items-center">
                <span className="font-bold text-xl mr-8">Task Dashboard ASM2</span>
                <div className="hidden md:block">
                  <div className="flex items-baseline space-x-4">
                    <Link href="/" className="px-3 py-2 rounded-md text-sm font-medium hover:bg-blue-700">Home</Link>
                    <Link href="/tasks" className="px-3 py-2 rounded-md text-sm font-medium hover:bg-blue-700">Tasks</Link>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </nav>
        <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8">
          {children}
        </main>
        <footer className="bg-gray-800 text-gray-300 text-center py-4">
          &copy; {new Date().getFullYear()} Next.js Assignment 2
        </footer>
      </body>
    </html>
  );
}
