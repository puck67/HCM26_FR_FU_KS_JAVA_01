import type { Metadata } from 'next';
import { Outfit } from 'next/font/google';
import './globals.css';
import Navbar from '@/components/Navbar';

const outfit = Outfit({
  subsets: ['latin'],
  weight: ['300', '400', '500', '600', '700', '800'],
  variable: '--font-outfit',
});

export const metadata: Metadata = {
  title: 'ZenTask — Premium Task Dashboard',
  description: 'Manage your tasks efficiently with Next.js App Router and Server Actions',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en" className={`${outfit.variable} scroll-smooth`}>
      <body className="font-sans bg-slate-50 text-slate-800 min-h-screen selection:bg-indigo-500 selection:text-white flex flex-col">
        <Navbar />
        <main className="flex-1 w-full max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 md:py-12">
          {children}
        </main>
        
        <footer className="border-t border-slate-200 bg-white py-6 text-center text-xs text-slate-500">
          <p>© {new Date().getFullYear()} ZenTask. Next.js Assignment 02. Crafted with ❤️.</p>
        </footer>
      </body>
    </html>
  );
}

