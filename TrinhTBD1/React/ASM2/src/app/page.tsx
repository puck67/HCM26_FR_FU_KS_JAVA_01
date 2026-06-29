import Link from 'next/link';
import { getTasks } from '@/lib/tasks';
import { GenericCard } from '@/components/generic/GenericCard';
import { GenericButton } from '@/components/generic/GenericButton';

export const revalidate = 0;

export default async function HomePage() {
  const tasks = await getTasks();
  const totalTasks = tasks.length;
  const completedTasks = tasks.filter(t => t.completed).length;
  const pendingTasks = totalTasks - completedTasks;

  return (
    <div className="space-y-6 max-w-4xl mx-auto">
      <div className="bg-zinc-900 border border-zinc-800 rounded-lg p-6 flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-xl font-bold text-white">Dashboard Overview</h1>
        </div>
        <div className="flex gap-2">
          <Link href="/tasks">
            <GenericButton variant="primary" size="md" label="View Tasks" />
          </Link>
          <Link href="/tasks/new">
            <GenericButton variant="outline" size="md" label="New Task" />
          </Link>
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <GenericCard className="text-center">
          <div className="text-xs font-semibold text-zinc-400 uppercase tracking-wider">Total</div>
          <div className="mt-1 text-3xl font-bold text-white">{totalTasks}</div>
        </GenericCard>

        <GenericCard className="text-center">
          <div className="text-xs font-semibold text-zinc-400 uppercase tracking-wider">Completed</div>
          <div className="mt-1 text-3xl font-bold text-red-400">{completedTasks}</div>
        </GenericCard>

        <GenericCard className="text-center">
          <div className="text-xs font-semibold text-zinc-400 uppercase tracking-wider">Pending</div>
          <div className="mt-1 text-3xl font-bold text-zinc-300">{pendingTasks}</div>
        </GenericCard>
      </div>

      <GenericCard title="Recent Tasks">
        {tasks.length === 0 ? (
          <p className="text-sm text-zinc-400 py-2">No tasks available.</p>
        ) : (
          <div className="divide-y divide-zinc-800">
            {tasks.slice(0, 5).map((task) => (
              <div key={task.id} className="py-2.5 flex items-center justify-between text-sm">
                <div>
                  <span className={`font-medium ${task.completed ? 'line-through text-zinc-500' : 'text-zinc-200'}`}>
                    {task.name}
                  </span>
                </div>
                <Link href={`/tasks/${task.id}`} className="text-xs text-red-400 hover:underline">
                  View →
                </Link>
              </div>
            ))}
          </div>
        )}
      </GenericCard>
    </div>
  );
}
