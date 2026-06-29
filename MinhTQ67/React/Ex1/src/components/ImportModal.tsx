import { UploadCloud } from "lucide-react";
import { Modal } from "./Modal";
import { Button } from "./Button";

export function ImportModal({ isOpen, onClose, onImport }: {
  isOpen: boolean;
  onClose: () => void;
  onImport: () => void;
}) {
  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Import Data">
      <div className="flex flex-col items-center justify-center border-2 border-dashed border-slate-200 rounded-xl p-8 bg-slate-50 hover:bg-slate-100 transition-colors cursor-pointer">
        <UploadCloud size={32} className="text-blue-500 mb-3" />
        <p className="text-slate-700 font-medium">Click to upload CSV or Excel</p>
        <p className="text-slate-400 text-sm mt-1">Maximum file size 5MB</p>
      </div>
      <div className="flex gap-3 justify-end mt-6 pt-4 border-t border-slate-100">
        <Button title="Cancel" action={onClose} style="px-4 py-2 bg-slate-100 text-slate-700 hover:bg-slate-200" />
        <Button title="Upload File" action={() => { onImport(); onClose(); }} style="px-4 py-2 bg-blue-500 text-white shadow-md shadow-blue-500/30 hover:bg-blue-600" />
      </div>
    </Modal>
  );
}
