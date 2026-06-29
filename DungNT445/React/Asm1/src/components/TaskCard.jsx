import React from 'react';
import { Link } from 'react-router-dom';
import { CheckCircle2, Circle, Edit3, Eye, Trash2 } from 'lucide-react';

export const TaskCard = ({ task, onToggleStatus, onDelete }) => {
  const getPriorityStyle = (priority) => {
    switch (priority) {
      case 'high':
        return 'bg-rose-50 text-rose-700 border-rose-200';
      case 'medium':
        return 'bg-amber-50 text-amber-700 border-amber-200';
      case 'low':
      default:
        return 'bg-slate-50 text-slate-700 border-slate-200';
    }
  };

  return (
    <div className={`group relative bg-white border rounded-2xl p-5 shadow-sm transition-all duration-300 hover:shadow-md hover:border-slate-300 flex flex-col justify-between h-full ${task.completed ? 'border-emerald-100 bg-emerald-50/10' : 'border-slate-200'}`}>
      <div>
        <div className="flex items-start justify-between gap-4 mb-3">
          {/* Priority & Status Badges */}
          <div className="flex flex-wrap gap-2">
            <span className={`text-xs font-semibold px-2.5 py-1 rounded-full border uppercase tracking-wider ${getPriorityStyle(task.priority)}`}>
              {task.priority}
            </span>
            <span className={`text-xs font-semibold px-2.5 py-1 rounded-full border uppercase tracking-wider ${task.completed ? 'bg-emerald-50 text-emerald-700 border-emerald-200' : 'bg-blue-50 text-blue-700 border-blue-200'}`}>
              {task.completed ? 'completed' : 'pending'}
            </span>
          </div>
          
          {/* Toggle status checkbox/button */}
          <button
            onClick={() => onToggleStatus(task.id)}
            className={`p-1 rounded-full hover:bg-slate-100 transition-colors text-slate-400 hover:text-indigo-600 cursor-pointer`}
            title={task.completed ? 'Mark as Pending' : 'Mark as Completed'}
          >
            {task.completed ? (
              <CheckCircle2 className="w-6 h-6 text-emerald-500 fill-emerald-50" />
            ) : (
              <Circle className="w-6 h-6" />
            )}
          </button>
        </div>

        {/* Title */}
        <h3 className={`text-lg font-bold mb-2 line-clamp-1 transition-all ${task.completed ? 'line-through text-slate-400' : 'text-slate-800'}`}>
          {task.name}
        </h3>

        {/* Description */}
        <p className={`text-sm mb-4 line-clamp-3 ${task.completed ? 'text-slate-400' : 'text-slate-500'}`}>
          {task.description || <span className="italic text-slate-400">No description provided.</span>}
        </p>
      </div>

      {/* Card Footer Actions */}
      <div className="mt-4 pt-4 border-t border-slate-100 flex items-center justify-between text-slate-500">
        <span className="text-xs text-slate-400">
          {new Date(task.createdAt).toLocaleDateString(undefined, {
            month: 'short',
            day: 'numeric',
          })}
        </span>
        
        <div className="flex gap-1">
          <Link
            to={`/tasks/${task.id}`}
            className="p-2 hover:bg-slate-100 rounded-lg hover:text-slate-800 transition-colors"
            title="View Details"
          >
            <Eye className="w-4 h-4" />
          </Link>
          <Link
            to={`/tasks/edit/${task.id}`}
            className="p-2 hover:bg-slate-100 rounded-lg hover:text-indigo-600 transition-colors"
            title="Edit Task"
          >
            <Edit3 className="w-4 h-4" />
          </Link>
          <button
            onClick={() => {
              if (window.confirm(`Are you sure you want to delete "${task.name}"?`)) {
                onDelete(task.id);
              }
            }}
            className="p-2 hover:bg-rose-50 rounded-lg hover:text-rose-600 transition-colors cursor-pointer"
            title="Delete Task"
          >
            <Trash2 className="w-4 h-4" />
          </button>
        </div>
      </div>
    </div>
  );
};
