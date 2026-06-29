import type { Metadata } from 'next';
import './globals.css';
import Navbar from '../components/Navbar';

export const metadata: Metadata = {
  title: 'TaskSphere — Next.js Task Dashboard',
  description: 'Manage tasks and projects seamlessly using a Next.js App Router dashboard with Server Actions.',
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" className="h-full antialiased">
      <body className="min-h-full flex flex-col bg-[#070a13] text-gray-100">
        {/* Navigation Bar */}
        <Navbar />

        {/* Main Content */}
        <main className="flex-1 w-full max-w-6xl mx-auto px-4 md:px-8 py-8 flex flex-col justify-start">
          {children}
        </main>

        {/* Footer */}
        <footer className="border-t border-white/5 py-8 text-center text-xs text-gray-600 bg-slate-950/20">
          <p>© {new Date().getFullYear()} TaskSphere. Developed for JSFW Assignment 2 (Next.js).</p>
        </footer>
      </body>
    </html>
  );
}
