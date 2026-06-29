import React from 'react';
import { Link } from 'react-router-dom';
import { Eye, Edit3, Trash2, Calendar, CheckCircle2, Circle, Clock } from 'lucide-react';
import type { Task } from '../types';

interface TaskCardProps {
  task: Task;
  onEdit: (task: Task) => void;
  onDelete: (id: string) => void;
}

export const TaskCard: React.FC<TaskCardProps> = ({ task, onEdit, onDelete }) => {
  const getStatusDetails = (status: Task['status']) => {
    switch (status) {
      case 'completed':
        return {
          icon: <CheckCircle2 size={16} className="text-emerald-400" />,
          label: 'Completed',
          classes: 'bg-emerald-500/10 border-emerald-500/20 text-emerald-400 shadow-[0_0_15px_rgba(16,185,129,0.08)]',
        };
      case 'in_progress':
        return {
          icon: <Clock size={16} className="text-amber-400 animate-pulse" />,
          label: 'In Progress',
          classes: 'bg-amber-500/10 border-amber-500/20 text-amber-400 shadow-[0_0_15px_rgba(245,158,11,0.08)]',
        };
      default:
        return {
          icon: <Circle size={16} className="text-blue-400" />,
          label: 'To Do',
          classes: 'bg-blue-500/10 border-blue-500/20 text-blue-400 shadow-[0_0_15px_rgba(59,130,246,0.08)]',
        };
    }
  };

  const statusInfo = getStatusDetails(task.status);
  const formattedDate = new Date(task.createdAt).toLocaleDateString(undefined, {
    month: 'short',
    day: 'numeric',
    year: 'numeric',
  });

  return (
    <div className="glass-card p-6 rounded-2xl border border-white/5 relative overflow-hidden flex flex-col justify-between h-full group">
      {/* Decorative inner gradient border */}
      <div className="absolute -inset-px bg-gradient-to-r from-violet-500/0 via-violet-500/5 to-indigo-500/0 rounded-2xl pointer-events-none group-hover:from-violet-500/10 group-hover:to-indigo-500/10 transition-all duration-500" />
      
      <div>
        <div className="flex items-start justify-between gap-4 mb-3">
          <h3 className="font-semibold text-lg text-white group-hover:text-violet-300 transition-colors duration-300 line-clamp-1">
            {task.name}
          </h3>
          <span className={`flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-semibold border ${statusInfo.classes}`}>
            {statusInfo.icon}
            <span>{statusInfo.label}</span>
          </span>
        </div>

        <p className="text-sm text-gray-400 leading-relaxed mb-6 line-clamp-2 h-10">
          {task.description || <span className="text-gray-600 italic">No description provided.</span>}
        </p>
      </div>

      <div className="pt-4 border-t border-white/5 flex items-center justify-between mt-auto">
        <div className="flex items-center gap-1.5 text-xs text-gray-500">
          <Calendar size={13} />
          <span>{formattedDate}</span>
        </div>

        <div className="flex items-center gap-1">
          <Link
            to={`/tasks/${task.id}`}
            title="View Task Details"
            className="p-2 text-gray-400 hover:text-white hover:bg-white/5 rounded-lg border border-transparent hover:border-white/5 transition-all active:scale-90"
          >
            <Eye size={16} />
          </Link>
          <button
            onClick={() => onEdit(task)}
            title="Edit Task"
            className="p-2 text-gray-400 hover:text-violet-400 hover:bg-violet-500/10 rounded-lg border border-transparent hover:border-violet-500/10 transition-all active:scale-90 cursor-pointer"
          >
            <Edit3 size={16} />
          </button>
          <button
            onClick={() => onDelete(task.id)}
            title="Delete Task"
            className="p-2 text-gray-400 hover:text-rose-400 hover:bg-rose-500/10 rounded-lg border border-transparent hover:border-rose-500/10 transition-all active:scale-90 cursor-pointer"
          >
            <Trash2 size={16} />
          </button>
        </div>
      </div>
    </div>
  );
};
