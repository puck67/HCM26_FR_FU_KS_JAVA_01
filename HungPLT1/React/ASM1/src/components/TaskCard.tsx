import React from 'react';
import { Link } from 'react-router-dom';
import type { Task, TaskStatus } from '../types';
import { Calendar, ChevronRight, Clock, CheckCircle2, AlertCircle } from 'lucide-react';
import { useTasks } from '../context/TaskContext';

interface TaskCardProps {
  task: Task;
}

export const TaskCard: React.FC<TaskCardProps> = ({ task }) => {
  const { updateTaskStatus } = useTasks();

  const handleStatusChange = async (e: React.ChangeEvent<HTMLSelectElement>) => {
    try {
      await updateTaskStatus(task.id, e.target.value as TaskStatus);
    } catch (err) {
      console.error(err);
    }
  };

  const getStatusStyles = (status: TaskStatus) => {
    switch (status) {
      case 'completed':
        return {
          bg: 'bg-emerald-500/10 border-emerald-500/20 text-emerald-400',
          dot: 'bg-emerald-400',
          icon: <CheckCircle2 className="w-4 h-4 text-emerald-400" />,
        };
      case 'in_progress':
        return {
          bg: 'bg-sky-500/10 border-sky-500/20 text-sky-400',
          dot: 'bg-sky-400',
          icon: <Clock className="w-4 h-4 text-sky-400" />,
        };
      default:
        return {
          bg: 'bg-amber-500/10 border-amber-500/20 text-amber-400',
          dot: 'bg-amber-400',
          icon: <AlertCircle className="w-4 h-4 text-amber-400" />,
        };
    }
  };

  const styles = getStatusStyles(task.status);
  const formattedDate = new Date(task.createdAt).toLocaleDateString('en-US', {
    month: 'short',
    day: 'numeric',
    year: 'numeric',
  });

  return (
    <div className="bg-slate-900 border border-slate-800 rounded-xl p-5 hover:border-slate-700 transition-all duration-300 shadow-lg hover:shadow-slate-950/50 flex flex-col justify-between">
      <div>
        <div className="flex justify-between items-start gap-4 mb-3">
          <h3 className="font-semibold text-slate-100 text-lg leading-tight line-clamp-1">
            {task.name}
          </h3>
          <span className={`inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-medium border ${styles.bg}`}>
            {styles.icon}
            {task.status.replace('_', ' ')}
          </span>
        </div>

        <p className="text-slate-400 text-sm mb-4 line-clamp-2 leading-relaxed h-10">
          {task.description || 'No description provided.'}
        </p>
      </div>

      <div className="flex justify-between items-center pt-4 border-t border-slate-800/60 mt-2">
        <div className="flex items-center gap-1.5 text-slate-500 text-xs">
          <Calendar className="w-3.5 h-3.5" />
          <span>{formattedDate}</span>
        </div>

        <div className="flex items-center gap-3">
          <select
            value={task.status}
            onChange={handleStatusChange}
            className="bg-slate-950 text-slate-300 border border-slate-800 rounded-lg text-xs py-1 px-2.5 outline-none focus:border-indigo-500 transition-colors"
          >
            <option value="pending">Pending</option>
            <option value="in_progress">In Progress</option>
            <option value="completed">Completed</option>
          </select>

          <Link
            to={`/tasks/${task.id}`}
            className="inline-flex items-center gap-1 text-xs font-semibold text-indigo-400 hover:text-indigo-300 transition-colors"
          >
            Details
            <ChevronRight className="w-3.5 h-3.5" />
          </Link>
        </div>
      </div>
    </div>
  );
};
