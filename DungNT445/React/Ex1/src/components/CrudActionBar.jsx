import React from 'react';
import { ButtonList } from './ButtonList';
import { PlusIcon, ImportIcon, ExportIcon } from './Icons';

export function CrudActionBar({
  onAdd,
  onImport,
  onExport
}) {
  const buttons = [
    ...(onAdd ? [{ title: "Add New", action: onAdd, variant: "primary", icon: <PlusIcon /> }] : []),
    ...(onImport ? [{ title: "Import Data", action: onImport, variant: "secondary", icon: <ImportIcon /> }] : []),
    ...(onExport ? [{ title: "Export Data", action: onExport, variant: "secondary", icon: <ExportIcon /> }] : []),
  ];

  return (
    <div className="action-bar-buttons">
      <ButtonList items={buttons} />
    </div>
  );
}
