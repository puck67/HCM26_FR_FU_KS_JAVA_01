import { AlertCircle } from 'lucide-react';
import { Modal } from './Modal';
import { Button } from './Button';

export function ConfirmModal({ 
  isOpen, 
  onClose, 
  onConfirm, 
  title, 
  message 
}: {
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => void;
  title: string;
  message: string;
}) {
  return (
    <Modal isOpen={isOpen} onClose={onClose} title={title}>
      <div className="flex flex-col items-center text-center gap-4 mb-6 mt-2">
        <div className="w-12 h-12 rounded-full bg-red-100 text-red-600 flex items-center justify-center">
          <AlertCircle size={24} />
        </div>
        <p className="text-slate-600 leading-relaxed">
          {message}
        </p>
      </div>
      <div className="flex gap-3 justify-end w-full">
        <Button 
          title="Cancel" 
          action={onClose} 
          style="px-5 py-2 bg-slate-100 text-slate-700 hover:bg-slate-200" 
        />
        <Button 
          title="Confirm Delete" 
          action={() => {
            onConfirm();
            onClose();
          }} 
          style="px-5 py-2 bg-red-500 text-white shadow-md shadow-red-500/30 hover:bg-red-600 hover:-translate-y-[1px]" 
        />
      </div>
    </Modal>
  );
}
