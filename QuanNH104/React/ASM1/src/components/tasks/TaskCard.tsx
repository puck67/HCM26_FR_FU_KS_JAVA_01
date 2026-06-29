import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Edit2, Trash2, Eye, Calendar, ArrowRight } from 'lucide-react';
import type { Task } from '../../types';

interface TaskCardProps {
  task: Task;
  onEdit: (task: Task) => void;
  onDelete: (id: string) => void;
}

export const TaskCard: React.FC<TaskCardProps> = ({ task, onEdit, onDelete }) => {
  const navigate = useNavigate();

  const getStatusStyle = (status: Task['status']) => {
    switch (status) {
      case 'Completed':
        return 'bg-cyber-success/10 text-cyber-success border-cyber-success/20';
      case 'In Progress':
        return 'bg-cyber-cyan/10 text-cyber-cyan border-cyber-cyan/20';
      default:
        return 'bg-cyber-warning/10 text-cyber-warning border-cyber-warning/20';
    }
  };

  const getPriorityStyle = (priority: Task['priority']) => {
    switch (priority) {
      case 'High':
        return 'bg-cyber-danger/10 text-cyber-danger border-cyber-danger/20';
      case 'Medium':
        return 'bg-cyber-purple/10 text-cyber-purple border-cyber-purple/20';
      default:
        return 'bg-gray-400/10 text-gray-400 border-gray-400/20';
    }
  };

  const formatDate = (dateStr: string) => {
    const d = new Date(dateStr);
    return d.toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
    });
  };

  return (
    <div className="glassmorphism rounded-2xl p-5 hover:bg-white/[0.04] transition-all-300 relative group overflow-hidden flex flex-col justify-between border border-white/5 hover:border-white/10 shadow-lg hover:shadow-cyan-900/5">
      {/* Decorative top border for High priority */}
      {task.priority === 'High' && (
        <div className="absolute top-0 left-0 w-full h-[2px] bg-gradient-to-r from-cyber-danger to-cyber-pink" />
      )}
      
      <div>
        {/* Header tags */}
        <div className="flex justify-between items-center gap-2 mb-4">
          <span className={`text-[10px] font-bold uppercase tracking-wider px-2 py-0.5 rounded border ${getStatusStyle(task.status)}`}>
            {task.status}
          </span>
          <span className={`text-[10px] font-bold uppercase tracking-wider px-2 py-0.5 rounded border ${getPriorityStyle(task.priority)}`}>
            {task.priority} Priority
          </span>
        </div>

        {/* Task Name */}
        <h3 className="font-orbitron font-bold text-lg text-white mb-2 line-clamp-1 group-hover:text-cyber-cyan transition-colors duration-200">
          {task.name}
        </h3>

        {/* Task Description */}
        <p className="text-[#94a3b8] text-sm mb-4 line-clamp-2 leading-relaxed h-10">
          {task.description || <span className="italic text-gray-600">No description provided.</span>}
        </p>
      </div>

      {/* Footer */}
      <div className="border-t border-white/5 pt-4 mt-2 flex justify-between items-center">
        <div className="flex items-center gap-1.5 text-xs text-gray-500">
          <Calendar className="h-3.5 w-3.5" />
          <span>{formatDate(task.createdAt)}</span>
        </div>

        <div className="flex items-center gap-1">
          {/* View Details Button */}
          <button
            onClick={() => navigate(`/tasks/${task.id}`)}
            className="p-1.5 text-gray-400 hover:text-cyber-cyan hover:bg-cyber-cyan/5 rounded-lg transition-all duration-200"
            title="View Details"
          >
            <Eye className="h-4 w-4" />
          </button>
          
          {/* Edit Button */}
          <button
            onClick={() => onEdit(task)}
            className="p-1.5 text-gray-400 hover:text-cyber-purple hover:bg-cyber-purple/5 rounded-lg transition-all duration-200"
            title="Edit Task"
          >
            <Edit2 className="h-4 w-4" />
          </button>

          {/* Delete Button */}
          <button
            onClick={() => onDelete(task.id)}
            className="p-1.5 text-gray-400 hover:text-cyber-danger hover:bg-cyber-danger/5 rounded-lg transition-all duration-200"
            title="Delete Task"
          >
            <Trash2 className="h-4 w-4" />
          </button>
        </div>
      </div>
    </div>
  );
};
