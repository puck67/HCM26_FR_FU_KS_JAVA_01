import React, { useEffect, useState, useMemo } from 'react';
import { useTasks } from '../context/TaskContext';
import { TaskCard } from '../components/TaskCard';
import { SkeletonLoader } from '../components/SkeletonLoader';
import type { TaskStatus } from '../types';
import { PlusCircle, Search, Filter, SortAsc, RefreshCw } from 'lucide-react';
import { Link } from 'react-router-dom';

export const Tasks: React.FC = () => {
  const { tasks, loading, error, fetchTasks } = useTasks();
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedStatus, setSelectedStatus] = useState<TaskStatus | 'all'>('all');
  const [sortBy, setSortBy] = useState<'newest' | 'oldest' | 'alphabetical'>('newest');

  useEffect(() => {
    fetchTasks();
  }, [fetchTasks]);

  const filteredAndSortedTasks = useMemo(() => {
    let result = [...tasks];

    if (selectedStatus !== 'all') {
      result = result.filter(task => task.status === selectedStatus);
    }

    if (searchQuery.trim() !== '') {
      const query = searchQuery.toLowerCase();
      result = result.filter(
        task =>
          task.name.toLowerCase().includes(query) ||
          task.description.toLowerCase().includes(query)
      );
    }

    if (sortBy === 'newest') {
      result.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
    } else if (sortBy === 'oldest') {
      result.sort((a, b) => new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime());
    } else if (sortBy === 'alphabetical') {
      result.sort((a, b) => a.name.localeCompare(b.name));
    }

    return result;
  }, [tasks, selectedStatus, searchQuery, sortBy]);

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">Tasks Manager</h1>
          <p className="text-slate-400 text-xs mt-0.5">Filter, search, organize, and manage your tasks list.</p>
        </div>
        <Link
          to="/tasks/new"
          className="inline-flex items-center gap-2 px-4 py-2.5 bg-indigo-600 hover:bg-indigo-500 active:bg-indigo-700 text-white text-sm font-semibold rounded-xl shadow-lg shadow-indigo-950/20 hover:shadow-indigo-500/15 transition-all duration-200 cursor-pointer"
        >
          <PlusCircle className="w-4 h-4" /> Create Task
        </Link>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-12 gap-4">
        <div className="md:col-span-6 relative">
          <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" />
          <input
            type="text"
            placeholder="Search tasks..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full bg-slate-900 border border-slate-800 text-slate-100 placeholder-slate-500 rounded-xl pl-10 pr-4 py-2.5 text-sm outline-none focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500/20 transition-all duration-200"
          />
        </div>

        <div className="md:col-span-3 relative">
          <Filter className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" />
          <select
            value={selectedStatus}
            onChange={(e) => setSelectedStatus(e.target.value as TaskStatus | 'all')}
            className="w-full bg-slate-900 border border-slate-800 text-slate-100 rounded-xl pl-10 pr-4 py-2.5 text-sm outline-none focus:border-indigo-500 transition-all duration-200 appearance-none cursor-pointer"
          >
            <option value="all">All Statuses</option>
            <option value="pending">Pending</option>
            <option value="in_progress">In Progress</option>
            <option value="completed">Completed</option>
          </select>
        </div>

        <div className="md:col-span-3 relative">
          <SortAsc className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" />
          <select
            value={sortBy}
            onChange={(e) => setSortBy(e.target.value as 'newest' | 'oldest' | 'alphabetical')}
            className="w-full bg-slate-900 border border-slate-800 text-slate-100 rounded-xl pl-10 pr-4 py-2.5 text-sm outline-none focus:border-indigo-500 transition-all duration-200 appearance-none cursor-pointer"
          >
            <option value="newest">Newest First</option>
            <option value="oldest">Oldest First</option>
            <option value="alphabetical">Alphabetical (A-Z)</option>
          </select>
        </div>
      </div>

      {loading ? (
        <SkeletonLoader />
      ) : error ? (
        <div className="p-8 bg-rose-500/10 border border-rose-500/25 rounded-2xl text-center max-w-lg mx-auto space-y-4">
          <p className="text-rose-400 font-medium">{error}</p>
          <button
            onClick={() => fetchTasks()}
            className="inline-flex items-center gap-2 px-4 py-2 bg-rose-600 hover:bg-rose-500 text-white text-xs font-semibold rounded-lg transition-colors cursor-pointer"
          >
            <RefreshCw className="w-3.5 h-3.5" /> Try Again
          </button>
        </div>
      ) : filteredAndSortedTasks.length === 0 ? (
        <div className="text-center py-16 bg-slate-900/40 border border-dashed border-slate-800 rounded-2xl">
          <p className="text-slate-500 text-sm mb-4">No tasks match your criteria.</p>
          <button
            onClick={() => {
              setSearchQuery('');
              setSelectedStatus('all');
              setSortBy('newest');
            }}
            className="text-indigo-400 hover:text-indigo-300 text-xs font-semibold underline cursor-pointer"
          >
            Clear all filters
          </button>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
          {filteredAndSortedTasks.map((task) => (
            <TaskCard key={task.id} task={task} />
          ))}
        </div>
      )}
    </div>
  );
};
