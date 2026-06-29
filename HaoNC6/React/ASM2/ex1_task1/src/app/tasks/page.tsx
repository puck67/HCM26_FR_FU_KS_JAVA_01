import Link from 'next/link';
import { getTasks } from '../../lib/tasks';
import TaskCard from '../../components/TaskCard';
import { PlusCircle, Search, ListTodo, CheckCircle2, Circle } from 'lucide-react';

export const dynamic = 'force-dynamic';

interface PageProps {
  searchParams: Promise<{
    search?: string;
    status?: string;
  }>;
}

export default async function TasksPage({ searchParams }: PageProps) {
  const resolvedParams = await searchParams;
  const searchQuery = (resolvedParams.search || '').trim().toLowerCase();
  const statusFilter = resolvedParams.status || 'all';

  // Fetch tasks directly from server-side JSON storage
  const allTasks = getTasks();

  // Filter tasks based on query params
  const filteredTasks = allTasks.filter((task) => {
    const matchesSearch = 
      task.name.toLowerCase().includes(searchQuery) || 
      task.description.toLowerCase().includes(searchQuery);
    
    if (statusFilter === 'completed') {
      return matchesSearch && task.completed;
    }
    if (statusFilter === 'pending') {
      return matchesSearch && !task.completed;
    }
    return matchesSearch;
  });

  // Calculate statistics for the filter tabs
  const totalCount = allTasks.length;
  const completedCount = allTasks.filter((t) => t.completed).length;
  const pendingCount = totalCount - completedCount;

  return (
    <div className="space-y-8">
      {/* Top Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-3xl font-extrabold text-white flex items-center gap-2">
            <ListTodo className="w-8 h-8 text-indigo-400" /> Tasks Dashboard
          </h1>
          <p className="text-sm text-gray-400 mt-1">
            Manage your daily tasks and keep track of your progress.
          </p>
        </div>
        <Link
          href="/tasks/new"
          className="flex items-center justify-center gap-2 px-5 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-semibold shadow-lg hover:shadow-indigo-500/20 transition-all cursor-pointer self-start sm:self-center"
        >
          <PlusCircle className="w-5 h-5" /> Add New Task
        </Link>
      </div>

      {/* Filter & Search Bar */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 p-4 rounded-2xl bg-white/2 border border-white/5">
        {/* Filter Tabs */}
        <div className="flex gap-1 overflow-x-auto pb-1 md:pb-0">
          <Link
            href="/tasks?status=all"
            className={`px-4 py-2 rounded-xl text-xs font-semibold border transition-all whitespace-nowrap ${
              statusFilter === 'all'
                ? 'bg-indigo-500/10 text-indigo-300 border-indigo-500/30'
                : 'text-gray-400 hover:text-white border-transparent hover:bg-white/5'
            }`}
          >
            All Tasks ({totalCount})
          </Link>
          <Link
            href="/tasks?status=pending"
            className={`px-4 py-2 rounded-xl text-xs font-semibold border transition-all whitespace-nowrap flex items-center gap-1.5 ${
              statusFilter === 'pending'
                ? 'bg-yellow-500/10 text-yellow-300 border-yellow-500/30'
                : 'text-gray-400 hover:text-white border-transparent hover:bg-white/5'
            }`}
          >
            <Circle className="w-3.5 h-3.5" /> Pending ({pendingCount})
          </Link>
          <Link
            href="/tasks?status=completed"
            className={`px-4 py-2 rounded-xl text-xs font-semibold border transition-all whitespace-nowrap flex items-center gap-1.5 ${
              statusFilter === 'completed'
                ? 'bg-emerald-500/10 text-emerald-300 border-emerald-500/30'
                : 'text-gray-400 hover:text-white border-transparent hover:bg-white/5'
            }`}
          >
            <CheckCircle2 className="w-3.5 h-3.5" /> Completed ({completedCount})
          </Link>
        </div>

        {/* Search Input (pure HTML Form submitting via GET parameters) */}
        <form action="/tasks" method="GET" className="relative flex-1 max-w-md w-full">
          <input type="hidden" name="status" value={statusFilter} />
          <input
            type="text"
            name="search"
            defaultValue={resolvedParams.search || ''}
            placeholder="Search tasks..."
            className="w-full pl-10 pr-4 py-2 rounded-xl bg-slate-950/40 border border-white/5 focus:border-indigo-500/50 text-white placeholder-gray-500 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500/20 transition-all"
          />
          <Search className="w-4 h-4 text-gray-500 absolute left-3.5 top-3" />
        </form>
      </div>

      {/* Task List Grid */}
      <div className="space-y-4">
        {filteredTasks.length === 0 ? (
          <div className="text-center py-20 glass-panel rounded-2xl p-8">
            <ListTodo className="w-12 h-12 text-gray-600 mx-auto mb-3" />
            <h3 className="text-lg font-bold text-white">No tasks found</h3>
            <p className="text-sm text-gray-500 mt-1 max-w-sm mx-auto">
              {searchQuery 
                ? `No tasks matched your search query "${searchQuery}". Try clearing the search filter.`
                : `No tasks found in the "${statusFilter}" category.`}
            </p>
            {searchQuery && (
              <Link 
                href={`/tasks?status=${statusFilter}`}
                className="mt-4 inline-block text-xs font-semibold text-indigo-400 hover:text-indigo-300"
              >
                Clear Search Filter
              </Link>
            )}
          </div>
        ) : (
          filteredTasks.map((task) => (
            <TaskCard key={task.id} task={task} />
          ))
        )}
      </div>
    </div>
  );
}
