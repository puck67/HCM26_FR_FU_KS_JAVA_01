import { Modal } from './Modal';
import { Button } from './Button';
import { FileText } from 'lucide-react';

export function ViewModal({ isOpen, onClose, title, data }: {
  isOpen: boolean;
  onClose: () => void;
  title: string;
  data: any;
}) {
  if (!data) return null;
  
  return (
    <Modal isOpen={isOpen} onClose={onClose} title={title}>
      {/* Decorative Header */}
      <div className="flex items-center gap-4 mb-6 p-4 rounded-xl bg-gradient-to-r from-blue-50 to-indigo-50 border border-blue-100/50">
        <div className="w-12 h-12 rounded-full bg-blue-600 text-white flex items-center justify-center shadow-md shadow-blue-500/20">
          <FileText size={24} />
        </div>
        <div>
          <h4 className="font-semibold text-slate-900 text-lg tracking-tight">Record Details</h4>
          <p className="text-sm text-slate-500 font-medium">Complete view of the selected item</p>
        </div>
      </div>

      {/* Content Grid */}
      <div className="grid grid-cols-2 gap-x-4 gap-y-4">
        {Object.entries(data).map(([key, value]) => (
          <div key={key} className="flex flex-col bg-slate-50 rounded-xl p-4 border border-slate-100 shadow-[inset_0_1px_2px_rgba(0,0,0,0.01)] hover:bg-white hover:border-blue-100 hover:shadow-sm transition-all duration-300">
            <span className="text-[0.7rem] font-bold text-slate-400 uppercase tracking-wider mb-1.5 flex items-center gap-2">
              <span className="w-1.5 h-1.5 rounded-full bg-blue-400"></span>
              {key.replace(/([A-Z])/g, ' $1').trim()}
            </span>
            <span className="text-[0.95rem] font-semibold text-slate-800 break-words pl-3 border-l-2 border-slate-200">{String(value)}</span>
          </div>
        ))}
      </div>

      <div className="mt-8 flex justify-end">
        <Button variant="secondary" onClick={onClose}>Close Window</Button>
      </div>
    </Modal>
  );
}
