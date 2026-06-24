import { ButtonList } from './ButtonList';
import type { ButtonConfig } from './ButtonList';

interface CrudActionBarProps {
  onAdd?: () => void;
  onImport?: () => void;
  onExport?: () => void;
  searchQuery?: string;
  onSearchChange?: (val: string) => void;
  entityName?: string;
}

export function CrudActionBar({
  onAdd,
  onImport,
  onExport,
  searchQuery,
  onSearchChange,
  entityName = 'item',
}: CrudActionBarProps) {
  const buttons: ButtonConfig[] = [
    ...(onAdd
      ? [
          {
            title: 'Add New',
            action: onAdd,
            style: 'btn-add-action btn-primary',
            icon: (
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
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
            title: 'Import',
            action: onImport,
            style: 'btn-import-action btn-secondary',
            icon: (
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
                <polyline points="14 10 12 12 10 10" />
                <line x1="12" y1="12" x2="12" y2="3" />
              </svg>
            ),
          },
        ]
      : []),
    ...(onExport
      ? [
          {
            title: 'Export CSV',
            action: onExport,
            style: 'btn-export-action btn-secondary',
            icon: (
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
                <polyline points="10 9 12 7 14 9" />
                <line x1="12" y1="7" x2="12" y2="17" />
              </svg>
            ),
          },
        ]
      : []),
  ];

  return (
    <div className="crud-action-bar">
      {onSearchChange !== undefined && (
        <div className="action-bar-search">
          <svg className="search-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <circle cx="11" cy="11" r="8" />
            <line x1="21" y1="21" x2="16.65" y2="16.65" />
          </svg>
          <input
            type="text"
            className="search-input"
            placeholder={`Search ${entityName.toLowerCase()}s...`}
            value={searchQuery || ''}
            onChange={(e) => onSearchChange(e.target.value)}
          />
          {searchQuery && (
            <button className="clear-search-btn" onClick={() => onSearchChange('')}>
              ✕
            </button>
          )}
        </div>
      )}
      
      <div className="action-bar-buttons">
        <ButtonList items={buttons} />
      </div>
    </div>
  );
}
