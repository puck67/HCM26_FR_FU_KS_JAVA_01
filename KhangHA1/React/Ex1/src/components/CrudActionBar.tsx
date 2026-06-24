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
    ...(onAdd ? [{ title: "Add", action: onAdd }] : []),
    ...(onImport ? [{ title: "Import", action: onImport }] : []),
    ...(onExport ? [{ title: "Export", action: onExport }] : []),
  ];

  return <ButtonList items={buttons} />;
}
