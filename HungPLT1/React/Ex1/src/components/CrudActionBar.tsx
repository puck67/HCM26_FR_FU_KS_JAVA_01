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
    ...(onAdd ? [{ title: "+ Thêm mới", action: onAdd, style: "btn-add" }] : []),
    ...(onImport ? [{ title: "Import", action: onImport, style: "btn-import" }] : []),
    ...(onExport ? [{ title: "Export", action: onExport, style: "btn-export" }] : []),
  ];

  return (
    <div className="crud-action-bar">
      <ButtonList items={buttons} />
    </div>
  );
}
