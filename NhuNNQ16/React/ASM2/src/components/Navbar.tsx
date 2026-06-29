'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';

export default function Navbar() {
  const pathname = usePathname();

  const navLinks = [
    { href: '/', label: 'Dashboard' },
    { href: '/tasks', label: 'Tasks List' },
    { href: '/tasks/new', label: 'Create Task' },
  ];

  return (
    <header className="sticky top-0 z-50 w-full border-b border-slate-200/80 bg-white/80 backdrop-blur-md transition-all duration-300">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        <div className="flex items-center gap-8">
          <Link href="/" className="flex items-center gap-2.5 font-extrabold text-xl bg-gradient-to-r from-indigo-600 via-purple-600 to-pink-600 bg-clip-text text-transparent hover:opacity-90 transition-opacity">
            <span className="text-2xl animate-bounce">⚡</span> ZenTask
          </Link>
          <nav className="hidden md:flex items-center gap-6">
            {navLinks.map((link) => {
              const isActive = link.href === '/' 
                ? pathname === '/' 
                : pathname.startsWith(link.href);
              return (
                <Link
                  key={link.href}
                  href={link.href}
                  className={`text-sm font-medium transition-all duration-200 hover:text-indigo-600 relative py-1 ${
                    isActive ? 'text-indigo-600 font-semibold' : 'text-slate-600'
                  }`}
                >
                  {link.label}
                  {isActive && (
                    <span className="absolute bottom-0 left-0 right-0 h-0.5 bg-gradient-to-r from-indigo-500 to-purple-500 rounded-full" />
                  )}
                </Link>
              );
            })}
          </nav>
        </div>

        <div className="flex items-center gap-3">
          <Link
            href="/tasks/new"
            className="relative group overflow-hidden px-4 py-2 rounded-lg bg-gradient-to-r from-indigo-600 to-purple-600 text-white font-medium text-sm transition-all duration-300 hover:shadow-lg hover:shadow-indigo-500/20 active:scale-95"
          >
            <span className="relative z-10 flex items-center gap-1.5">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2.5} stroke="currentColor" className="w-4 h-4">
                <path strokeLinecap="round" strokeLinejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
              </svg>
              Quick Task
            </span>
            <div className="absolute inset-0 bg-gradient-to-r from-purple-600 to-indigo-600 opacity-0 group-hover:opacity-100 transition-opacity duration-300" />
          </Link>
        </div>
      </div>
      {/* Mobile nav indicator bar */}
      <div className="md:hidden flex justify-around border-t border-slate-200/80 bg-white/90 backdrop-blur-md py-3 text-xs text-slate-500">
        {navLinks.map((link) => {
          const isActive = link.href === '/' 
            ? pathname === '/' 
            : pathname.startsWith(link.href);
          return (
            <Link
              key={link.href}
              href={link.href}
              className={`flex flex-col items-center gap-1 transition-colors ${
                isActive ? 'text-indigo-600 font-semibold' : 'text-slate-500'
              }`}
            >
              {link.label === 'Dashboard' && <span>🏠</span>}
              {link.label === 'Tasks List' && <span>📋</span>}
              {link.label === 'Create Task' && <span>✨</span>}
              <span>{link.label}</span>
            </Link>
          );
        })}
      </div>
    </header>
  );

}
