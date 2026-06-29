import type { Metadata } from 'next';
import { Inter } from 'next/font/google';
import './globals.css';
import Navbar from '@/components/Navbar';

const inter = Inter({ subsets: ['latin'] });

export const metadata: Metadata = {
  title: 'Task manager — Stay organized',
  description:
    'A fast, server-rendered task management app built with Next.js 14 App Router and Server Actions.',
};

interface RootLayoutProps {
  children: React.ReactNode;
}

export default function RootLayout({ children }: RootLayoutProps) {
  return (
    <html lang="en">
      <body className={`${inter.className} bg-slate-950 text-slate-100 min-h-screen`}>
        <Navbar />
        <main className="pt-16">
          <div className="max-w-5xl mx-auto px-4 sm:px-6 py-8 sm:py-12">
            {children}
          </div>
        </main>
      </body>
    </html>
  );
}
