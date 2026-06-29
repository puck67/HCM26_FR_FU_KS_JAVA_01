import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { ClipboardList, CheckCircle2, Clock, AlertCircle, PlusCircle, ArrowRight } from 'lucide-react';
import { useTasks } from '../context/TaskContext.tsx';
import { Card } from '../components/generic/Card.tsx';
import { Badge, statusToBadgeProps } from '../components/generic/Badge.tsx';
import { Button } from '../components/generic/Button.tsx';
import { Spinner } from '../components/generic/Spinner.tsx';

/**
 * HomePage — dashboard showing task summary statistics and recent tasks.
 * Route: /
 */
export const HomePage: React.FC = () => {
  const { tasks, loading, fetchTasks } = useTasks();
  const navigate = useNavigate();

  useEffect(() => { fetchTasks(); }, [fetchTasks]);

  const total      = tasks.length;
  const todo       = tasks.filter((t) => t.status === 'todo').length;
  const inProgress = tasks.filter((t) => t.status === 'in_progress').length;
  const completed  = tasks.filter((t) => t.status === 'completed').length;

  const stats = [
    { label: 'Total',       value: total,      icon: <ClipboardList size={20} />, color: 'text-indigo-600', bg: 'bg-indigo-50 border-indigo-100' },
    { label: 'To Do',       value: todo,       icon: <AlertCircle   size={20} />, color: 'text-amber-600',  bg: 'bg-amber-50  border-amber-100'  },
    { label: 'In Progress', value: inProgress, icon: <Clock         size={20} />, color: 'text-blue-600',   bg: 'bg-blue-50   border-blue-100'   },
    { label: 'Completed',   value: completed,  icon: <CheckCircle2  size={20} />, color: 'text-emerald-600',bg: 'bg-emerald-50 border-emerald-100'},
  ];

  const recentTasks = tasks.slice(0, 5);

  return (
    <div className="space-y-8">
      {/* Hero */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-extrabold text-slate-900 tracking-tight">
            Dashboard
          </h1>
          <p className="text-sm text-slate-500 mt-1">
            Manage and track all your tasks in one place.
          </p>
        </div>
        <Button
          variant="primary"
          leadingIcon={<PlusCircle size={16} />}
          onClick={() => navigate('/tasks/new')}
        >
          New Task
        </Button>
      </div>

      {/* Stats */}
      {loading ? (
        <div className="flex justify-center py-10"><Spinner size="lg" /></div>
      ) : (
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          {stats.map((s) => (
            <Card key={s.label} className="flex flex-col gap-3">
              <div className={`self-start p-2 rounded-lg border ${s.bg} ${s.color}`}>
                {s.icon}
              </div>
              <div>
                <p className="text-2xl font-extrabold text-slate-900">{s.value}</p>
                <p className="text-xs text-slate-500 font-medium mt-0.5">{s.label}</p>
              </div>
            </Card>
          ))}
        </div>
      )}

      {/* Recent tasks */}
      {!loading && recentTasks.length > 0 && (
        <div className="space-y-3">
          <div className="flex items-center justify-between">
            <h2 className="text-sm font-bold text-slate-700 uppercase tracking-wider">
              Recent Tasks
            </h2>
            <button
              onClick={() => navigate('/tasks')}
              className="flex items-center gap-1 text-xs text-blue-600 hover:text-blue-700 font-semibold transition-colors"
            >
              View all <ArrowRight size={13} />
            </button>
          </div>

          <div className="flex flex-col gap-2">
            {recentTasks.map((task) => (
              <Card
                key={task.id}
                onClick={() => navigate(`/tasks/${task.id}`)}
                className={[
                  'flex items-center justify-between gap-4',
                  task.status === 'completed' ? 'bg-emerald-50/40 border-emerald-100' : '',
                ].join(' ')}
              >
                <div className="flex-1 min-w-0">
                  <p
                    className={[
                      'text-sm font-semibold truncate',
                      task.status === 'completed'
                        ? 'line-through text-slate-400'
                        : 'text-slate-900',
                    ].join(' ')}
                  >
                    {task.name}
                  </p>
                  {task.description && (
                    <p className="text-xs text-slate-500 truncate mt-0.5">
                      {task.description}
                    </p>
                  )}
                </div>
                <Badge {...statusToBadgeProps(task.status)} />
              </Card>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};
