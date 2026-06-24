import { ButtonList, type ButtonConfig } from "./ButtonList";

export function CrudActionBar({
  onAdd,
  onImport,
  onExport
}: {
  onAdd?: () => void;
  onImport?: () => void;
  onExport?: () => void;
}) {
  const buttons: ButtonConfig[] = [
    ...(onAdd ? [{ title: "Add", action: onAdd, style: "bg-blue-600 hover:bg-blue-700 text-white shadow" }] : []),
    ...(onImport ? [{ title: "Import", action: onImport, style: "bg-gray-200 hover:bg-gray-300 text-gray-800" }] : []),
    ...(onExport ? [{ title: "Export", action: onExport, style: "bg-gray-200 hover:bg-gray-300 text-gray-800" }] : []),
  ];

  return (
    <div className="mb-4 flex items-center justify-between">
      <h2 className="text-xl font-bold text-gray-800">Actions</h2>
      <ButtonList items={buttons} />
    </div>
  );
}
