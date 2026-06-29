import React from 'react';
import { AlertCircle, RefreshCw } from 'lucide-react';

interface AlertProps {
  title?: string;
  message: string;
  onRetry?: () => void;
}

export const Alert: React.FC<AlertProps> = ({ title = 'Something went wrong', message, onRetry }) => {
  return (
    <div className="glass-card max-w-xl mx-auto border-rose-500/20 bg-rose-950/10 p-6 rounded-2xl flex flex-col md:flex-row items-center md:items-start gap-4 relative overflow-hidden">
      {/* Background soft red glow */}
      <div className="absolute -left-12 -top-12 w-24 h-24 bg-rose-500/10 blur-2xl pointer-events-none rounded-full" />
      
      <div className="p-3 bg-rose-500/20 text-rose-400 rounded-xl border border-rose-500/30">
        <AlertCircle size={24} />
      </div>

      <div className="flex-1 text-center md:text-left">
        <h3 className="text-lg font-semibold text-rose-200 mb-1">{title}</h3>
        <p className="text-sm text-gray-400 leading-relaxed">{message}</p>
        
        {onRetry && (
          <button
            onClick={onRetry}
            className="mt-4 inline-flex items-center gap-2 px-4 py-2 text-xs font-semibold rounded-lg bg-rose-500/20 hover:bg-rose-500/30 text-rose-300 border border-rose-500/30 active:scale-95 transition-all duration-200 cursor-pointer"
          >
            <RefreshCw size={14} />
            <span>Try Connection Again</span>
          </button>
        )}
      </div>
    </div>
  );
};
