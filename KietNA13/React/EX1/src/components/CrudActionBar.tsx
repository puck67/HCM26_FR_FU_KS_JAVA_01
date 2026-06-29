import React from 'react';
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
    ...(onAdd ? [{ title: "Add New", action: onAdd, style: "btn btn-primary" }] : []),
    ...(onImport ? [{ title: "Import", action: onImport, style: "btn btn-outline" }] : []),
    ...(onExport ? [{ title: "Export", action: onExport, style: "btn btn-outline" }] : []),
  ];

  return (
    <div className="action-bar">
      <ButtonList items={buttons} />
    </div>
  );
}
