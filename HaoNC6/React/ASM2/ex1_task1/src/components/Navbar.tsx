'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { CheckSquare, Home, ListTodo, PlusCircle } from 'lucide-react';

export default function Navbar() {
  const pathname = usePathname();

  const links = [
    { href: '/', label: 'Home', icon: Home },
    { href: '/tasks', label: 'Tasks', icon: ListTodo },
    { href: '/tasks/new', label: 'Add Task', icon: PlusCircle },
  ];

  return (
    <nav className="glass-nav sticky top-0 z-50 px-4 md:px-8 py-4">
      <div className="max-w-6xl mx-auto flex items-center justify-between">
        <Link href="/" className="flex items-center gap-2 text-white font-bold text-xl tracking-tight">
          <CheckSquare className="w-6 h-6 text-indigo-400" />
          <span>Task<span className="text-purple-400">Sphere</span></span>
        </Link>
        <div className="flex gap-1 md:gap-2">
          {links.map((link) => {
            const Icon = link.icon;
            // Check if active: Exact match for '/', or starts-with match for others
            const isActive = link.href === '/' ? pathname === '/' : pathname.startsWith(link.href);
            return (
              <Link
                key={link.href}
                href={link.href}
                className={`flex items-center gap-2 px-3 py-1.5 rounded-lg text-sm font-medium transition-all ${
                  isActive
                    ? 'bg-white/10 text-white shadow-sm border border-white/10'
                    : 'text-gray-400 hover:text-white hover:bg-white/5'
                }`}
              >
                <Icon className={`w-4 h-4 ${isActive ? 'text-indigo-400' : 'text-gray-500'}`} />
                <span>{link.label}</span>
              </Link>
            );
          })}
        </div>
      </div>
    </nav>
  );
}
