import { ButtonList } from "./ButtonList";
import type { ButtonConfig } from "./ButtonList";

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
    ...(onAdd ? [{ title: "Add", action: onAdd, style: "btn-primary" }] : []),
    ...(onImport ? [{ title: "Import", action: onImport, style: "btn-secondary" }] : []),
    ...(onExport ? [{ title: "Export", action: onExport, style: "btn-secondary" }] : []),
  ];

  return (
    <div className="crud-action-bar">
      <ButtonList items={buttons} />
    </div>
  );
}
