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
    ...(onAdd ? [{ title: "+ Add Record", action: onAdd, variant: "primary" as const }] : []),
    ...(onImport ? [{ title: "Import Data", action: onImport, variant: "secondary" as const }] : []),
    ...(onExport ? [{ title: "Export CSV", action: onExport, variant: "success" as const }] : []),
  ];

  return (
    <div className="crud-action-bar">
      <ButtonList items={buttons} />
    </div>
  );
}
