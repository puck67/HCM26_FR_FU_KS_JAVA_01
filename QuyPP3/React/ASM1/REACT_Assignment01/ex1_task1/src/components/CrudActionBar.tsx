import { Button } from "./ButtonList";

export function CrudActionBar({
  onAdd
}: {
  onAdd?: () => void;
}) {
  return (
    <div className="flex gap-2">
      {onAdd && (
        <Button
          title="+ Add Task"
          action={onAdd}
          style="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-lg text-sm shadow-sm transition"
        />
      )}
    </div>
  );
}
