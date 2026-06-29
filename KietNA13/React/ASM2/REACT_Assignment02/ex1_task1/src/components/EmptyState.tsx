interface EmptyStateProps {
  message: string;
}

export default function EmptyState({ message }: EmptyStateProps) {
  return (
    <div className="flex flex-col items-center justify-center py-24 text-center">
      <div className="text-6xl mb-4 select-none">📭</div>
      <p className="text-slate-400 text-lg font-medium">{message}</p>
      <p className="text-slate-600 text-sm mt-1">Add a new task to get started</p>
    </div>
  );
}
