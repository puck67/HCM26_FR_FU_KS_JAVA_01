import { ButtonConfig } from './ButtonList';
import { ButtonList } from './ButtonList';

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
        ...(onAdd ? [{ title: "Add Student", action: onAdd, style: "btn btn-primary" }] : []),
        ...(onImport ? [{ title: "Import", action: onImport, style: "btn btn-outline" }] : []),
        ...(onExport ? [{ title: "Export", action: onExport, style: "btn btn-outline" }] : []),
    ];

    return (
        <div className="crud-action-bar">
            <ButtonList items={buttons} />
        </div>
    );
}