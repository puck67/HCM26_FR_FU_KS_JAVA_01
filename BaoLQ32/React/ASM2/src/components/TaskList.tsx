'use client';

import { useTransition, useState } from 'react';
import Link from 'next/link';
import { Edit2, Trash2, CheckCircle2, Circle, AlertCircle, Calendar, Search, Filter } from 'lucide-react';
import { toggleTaskCompleted, deleteTask } from '@/app/actions';
import { Task } from '@/lib/db';

interface TaskListProps {
  initialTasks: Task[];
}

export default function TaskList({ initialTasks }: TaskListProps) {
  const [isPending, startTransition] = useTransition();
  const [actionTaskId, setActionTaskId] = useState<string | null>(null);
  
  // Search and filter state
  const [searchQuery, setSearchQuery] = useState('');
  const [filter, setFilter] = useState<'all' | 'active' | 'completed'>('all');

  const handleToggle = (id: string) => {
    setActionTaskId(id);
    startTransition(async () => {
      try {
        await toggleTaskCompleted(id);
      } catch (err) {
        alert(err instanceof Error ? err.message : 'Failed to update task');
      } finally {
        setActionTaskId(null);
      }
    });
  };

  const handleDelete = (id: string) => {
    if (!confirm('Are you sure you want to delete this task?')) return;
    setActionTaskId(id);
    startTransition(async () => {
      try {
        await deleteTask(id);
      } catch (err) {
        alert(err instanceof Error ? err.message : 'Failed to delete task');
      } finally {
        setActionTaskId(null);
      }
    });
  };

  // Filter tasks based on query and filter choice
  const filteredTasks = initialTasks.filter(task => {
    const matchesSearch = task.name.toLowerCase().includes(searchQuery.toLowerCase()) || 
      (task.description || '').toLowerCase().includes(searchQuery.toLowerCase());
    
    if (filter === 'completed') return matchesSearch && task.completed;
    if (filter === 'active') return matchesSearch && !task.completed;
    return matchesSearch;
  });

  return (
    <div className="space-y-6">
      {/* Controls Bar */}
      <div className="flex flex-col md:flex-row gap-4 items-center justify-between bg-slate-900/40 p-4 rounded-2xl border border-slate-800">
        {/* Search */}
        <div className="relative w-full md:w-72">
          <Search className="absolute left-3 top-2.5 h-4 w-4 text-slate-400" />
          <input
            type="text"
            placeholder="Search tasks..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full bg-slate-950 border border-slate-800 rounded-xl py-2 pl-10 pr-4 text-sm text-slate-200 placeholder-slate-500 focus:outline-none focus:border-indigo-500 transition-colors"
          />
        </div>

        {/* Filter Tabs */}
        <div className="flex items-center gap-1.5 bg-slate-950 p-1 rounded-xl border border-slate-800">
          {(['all', 'active', 'completed'] as const).map((type) => (
            <button
              key={type}
              onClick={() => setFilter(type)}
              className={`px-4 py-1.5 rounded-lg text-xs font-semibold uppercase tracking-wider transition-all ${
                filter === type
                  ? 'bg-indigo-600 text-white shadow-sm'
                  : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              {type}
            </button>
          ))}
        </div>
      </div>

      {/* Task List Container */}
      {filteredTasks.length === 0 ? (
        <div className="flex flex-col items-center justify-center py-16 text-center border border-dashed border-slate-800 rounded-3xl bg-slate-900/10">
          <AlertCircle className="h-10 w-10 text-slate-500 mb-3" />
          <h3 className="text-lg font-bold text-slate-300">No tasks found</h3>
          <p className="text-sm text-slate-500 mt-1 max-w-xs">
            {searchQuery || filter !== 'all' 
              ? 'Try adjusting your search query or filter criteria.' 
              : 'Get started by creating a new task today.'}
          </p>
          {(!searchQuery && filter === 'all') && (
            <Link
              href="/tasks/new"
              className="mt-4 inline-flex items-center gap-1.5 rounded-xl bg-indigo-600/10 px-4 py-2 text-xs font-semibold text-indigo-400 border border-indigo-500/20 hover:bg-indigo-600 hover:text-white transition-all"
            >
              Create Your First Task
            </Link>
          )}
        </div>
      ) : (
        <div className="grid grid-cols-1 gap-4">
          {filteredTasks.map((task) => {
            const isLoading = isPending && actionTaskId === task.id;
            return (
              <div
                key={task.id}
                className={`relative overflow-hidden rounded-2xl border transition-all duration-300 ${
                  task.completed
                    ? 'border-emerald-500/20 bg-emerald-950/5 shadow-inner'
                    : 'border-slate-800 bg-slate-900/30 hover:border-slate-700 hover:bg-slate-900/50 shadow-md'
                } ${isLoading ? 'opacity-50 pointer-events-none' : ''}`}
              >
                {/* Visual completion side-bar indicator */}
                <div className={`absolute left-0 top-0 bottom-0 w-1 ${
                  task.completed ? 'bg-emerald-500' : 'bg-indigo-500'
                }`} />

                <div className="p-5 flex flex-col sm:flex-row sm:items-center justify-between gap-4 ml-1">
                  {/* Task details */}
                  <div className="space-y-1.5 max-w-xl">
                    <div className="flex items-center gap-2.5">
                      <button
                        onClick={() => handleToggle(task.id)}
                        disabled={isLoading}
                        className="text-slate-400 hover:text-indigo-400 transition-colors"
                        title={task.completed ? "Mark as Active" : "Mark as Completed"}
                      >
                        {task.completed ? (
                          <CheckCircle2 className="h-5.5 w-5.5 text-emerald-400 fill-emerald-500/10" />
                        ) : (
                          <Circle className="h-5.5 w-5.5 text-slate-500 hover:text-indigo-400" />
                        )}
                      </button>
                      <h3 className={`text-base font-semibold leading-6 transition-all ${
                        task.completed 
                          ? 'text-slate-500 line-through' 
                          : 'text-slate-100'
                      }`}>
                        {task.name}
                      </h3>
                      {task.completed && (
                        <span className="inline-flex items-center rounded-full bg-emerald-500/10 px-2 py-0.5 text-2xs font-semibold text-emerald-400 border border-emerald-500/20 uppercase tracking-wide">
                          Completed
                        </span>
                      )}
                    </div>
                    {task.description && (
                      <p className={`text-sm leading-relaxed sm:pl-8 ${
                        task.completed ? 'text-slate-600' : 'text-slate-400'
                      }`}>
                        {task.description}
                      </p>
                    )}
                    <div className="flex items-center gap-2.5 sm:pl-8 text-3xs font-medium text-slate-500">
                      <Calendar className="h-3 w-3" />
                      <span>
                        Created on {new Date(task.createdAt).toLocaleDateString(undefined, {
                          month: 'short',
                          day: 'numeric',
                          year: 'numeric',
                          hour: '2-digit',
                          minute: '2-digit'
                        })}
                      </span>
                    </div>
                  </div>

                  {/* Actions */}
                  <div className="flex items-center gap-2.5 self-end sm:self-center">
                    <Link
                      href={`/tasks/${task.id}`}
                      className="inline-flex items-center justify-center p-2 rounded-xl bg-slate-800/80 text-slate-300 border border-slate-700 hover:bg-slate-700 hover:text-indigo-400 hover:border-slate-600 transition-all shadow-sm"
                      title="Edit Task"
                    >
                      <Edit2 className="h-4.5 w-4.5" />
                    </Link>
                    <button
                      onClick={() => handleDelete(task.id)}
                      disabled={isLoading}
                      className="inline-flex items-center justify-center p-2 rounded-xl bg-red-950/20 text-red-400 border border-red-900/30 hover:bg-red-900/30 hover:text-red-300 hover:border-red-800/50 transition-all shadow-sm"
                      title="Delete Task"
                    >
                      <Trash2 className="h-4.5 w-4.5" />
                    </button>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}
