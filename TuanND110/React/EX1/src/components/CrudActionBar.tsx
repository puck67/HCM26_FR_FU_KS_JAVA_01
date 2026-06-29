
import { ButtonList } from "./ButtonList";
import type { ButtonConfig } from "./ButtonList";

export function CrudActionBar({
                                  onAdd,
                                  onImport,
                                  onExport,
                                  searchQuery,
                                  onSearchChange
                              }: {
    onAdd?: () => void;
    onImport?: () => void;
    onExport?: () => void;
    searchQuery?: string;
    onSearchChange?: (val: string) => void;
}) {
    const buttons: ButtonConfig[] = [
        ...(onImport ? [{
            key: "import-excel",
            title: (
                <>
                    <span className="material-symbols-outlined" style={{ fontSize: '18px' }}>upload</span>
                    Import
                </>
            ),
            action: onImport,
            style: "btn btn-secondary"
        }] : []),
        ...(onExport ? [{
            key: "export-csv",
            title: (
                <>
                    <span className="material-symbols-outlined" style={{ fontSize: '18px' }}>download</span>
                    Export
                </>
            ),
            action: onExport,
            style: "btn btn-secondary"
        }] : []),
        ...(onAdd ? [{
            key: "add-new",
            title: (
                <>
                    <span className="material-symbols-outlined" style={{ fontSize: '18px' }}>add</span>
                    Thêm mới
                </>
            ),
            action: onAdd,
            style: "btn btn-success"
        }] : []),
    ];

    return (
        <div className="actions-container">
            <div className="actions-bar">
                {onSearchChange !== undefined && (
                    <div className="search-wrapper">
                        <span className="material-symbols-outlined">search</span>
                        <input
                            type="text"
                            placeholder="Tìm kiếm..."
                            aria-label="Tìm kiếm"
                            value={searchQuery || ''}
                            onChange={(e) => onSearchChange(e.target.value)}
                            className="search-input"
                        />
                    </div>
                )}
                <div className="btn-group" style={{ marginLeft: 'auto' }}>
                    <button type="button" className="btn btn-icon-only">
                        <span className="material-symbols-outlined">filter_list</span>
                    </button>
                    <ButtonList items={buttons} />
                </div>
            </div>
        </div>
    );
}