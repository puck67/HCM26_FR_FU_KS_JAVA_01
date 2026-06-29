'use client';

import { useRouter, usePathname, useSearchParams } from 'next/navigation';
import { useTransition, useEffect, useState } from 'react';

export default function TaskFilters() {
  const router = useRouter();
  const pathname = usePathname();
  const searchParams = useSearchParams();
  const [isPending, startTransition] = useTransition();

  const currentSearch = searchParams.get('q') || '';
  const currentStatus = searchParams.get('status') || 'all';

  const [searchVal, setSearchVal] = useState(currentSearch);

  // Sync state with URL parameter (e.g. if back button clicked)
  useEffect(() => {
    setSearchVal(currentSearch);
  }, [currentSearch]);

  const updateSearch = (value: string) => {
    const params = new URLSearchParams(searchParams.toString());
    if (value) {
      params.set('q', value);
    } else {
      params.delete('q');
    }
    
    startTransition(() => {
      router.push(`${pathname}?${params.toString()}`);
    });
  };

  // Debounced search trigger
  useEffect(() => {
    const timer = setTimeout(() => {
      if (searchVal !== currentSearch) {
        updateSearch(searchVal);
      }
    }, 300);
    return () => clearTimeout(timer);
  }, [searchVal]);

  const setStatus = (status: string) => {
    const params = new URLSearchParams(searchParams.toString());
    if (status && status !== 'all') {
      params.set('status', status);
    } else {
      params.delete('status');
    }
    
    startTransition(() => {
      router.push(`${pathname}?${params.toString()}`);
    });
  };

  return (
    <div className="flex flex-col sm:flex-row gap-4 justify-between items-center bg-white border border-slate-200 p-4 rounded-2xl shadow-sm">
      {/* Search Input */}
      <div className="relative w-full sm:w-72">
        <span className="absolute inset-y-0 left-0 flex items-center pl-3 text-slate-400 text-sm">
          🔍
        </span>
        <input
          type="text"
          placeholder="Search tasks..."
          value={searchVal}
          onChange={(e) => setSearchVal(e.target.value)}
          className="w-full bg-slate-50 border border-slate-200 rounded-xl pl-9 pr-4 py-2.5 text-sm text-slate-800 placeholder-slate-400 focus:outline-none focus:bg-white focus:border-indigo-600 focus:ring-1 focus:ring-indigo-600 transition-all"
        />
        {isPending && (
          <span className="absolute inset-y-0 right-3 flex items-center text-xs text-indigo-600 animate-pulse">
            Syncing...
          </span>
        )}
      </div>

      {/* Filter Tabs */}
      <div className="flex items-center gap-1 p-1 bg-slate-100 border border-slate-200/60 rounded-xl w-full sm:w-auto">
        {(['all', 'active', 'completed'] as const).map((status) => (
          <button
            key={status}
            onClick={() => setStatus(status)}
            className={`flex-1 sm:flex-initial px-4 py-1.5 rounded-lg text-[11px] font-bold uppercase tracking-wider transition-all duration-200 ${
              currentStatus === status
                ? 'bg-white text-indigo-600 shadow-sm border border-slate-200/40'
                : 'text-slate-500 hover:text-slate-800 hover:bg-white/40'
            }`}
          >
            {status}
          </button>
        ))}
      </div>
    </div>
  );

}
