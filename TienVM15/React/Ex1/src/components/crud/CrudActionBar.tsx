import { ButtonList } from "../ui/ButtonList";
import type { ButtonConfig } from "../ui/ButtonList";

export interface CrudActionBarProps {
  onAdd?: () => void;
  onImport?: () => void;
  onExport?: () => void;
  extraActions?: ButtonConfig[];
  title?: string;
}

export function CrudActionBar({
  onAdd,
  onImport,
  onExport,
  extraActions = [],
  title,
}: CrudActionBarProps) {
  const buttons: ButtonConfig[] = [
    ...(onAdd
      ? [
          {
            title: "Add",
            action: onAdd,
            variant: "primary" as const,
            icon: (
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5">
                <line x1="12" y1="5" x2="12" y2="19" />
                <line x1="5" y1="12" x2="19" y2="12" />
              </svg>
            ),
          },
        ]
      : []),
    ...(onImport
      ? [
          {
            title: "Import",
            action: onImport,
            variant: "secondary" as const,
            icon: (
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
                <polyline points="17 8 12 3 7 8" />
                <line x1="12" y1="3" x2="12" y2="15" />
              </svg>
            ),
          },
        ]
      : []),
    ...(onExport
      ? [
          {
            title: "Export",
            action: onExport,
            variant: "ghost" as const,
            icon: (
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
                <polyline points="7 10 12 15 17 10" />
                <line x1="12" y1="15" x2="12" y2="3" />
              </svg>
            ),
          },
        ]
      : []),
    ...extraActions,
  ];

  return (
    <div className="crud-action-bar">
      {title && <h2 className="crud-action-bar__title">{title}</h2>}
      <div className="crud-action-bar__actions">
        <ButtonList items={buttons} gap="md" />
      </div>
    </div>
  );
}
