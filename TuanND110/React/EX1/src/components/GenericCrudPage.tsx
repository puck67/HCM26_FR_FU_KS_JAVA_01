import { useState } from "react";
import { CrudActionBar } from "./CrudActionBar";
import { CrudTable } from "./CrudTable";
import type { ButtonConfig } from "./ButtonList";

interface GenericCrudPageProps<T extends object> {
    data: T[];
    onAdd: () => void;
    onEdit: (item: T) => void;
    onDelete: (item: T) => void;
    onImport?: () => void;
    onExport?: () => void;
    extraActions?: (item: T) => ButtonConfig[];
    filterFn: (item: T, query: string) => boolean;
}

export function GenericCrudPage<T extends object>({
                                                      data,
                                                      onAdd,
                                                      onEdit,
                                                      onDelete,
                                                      onImport,
                                                      onExport,
                                                      extraActions,
                                                      filterFn,
                                                  }: GenericCrudPageProps<T>) {
    const [searchQuery, setSearchQuery] = useState("");

    const filteredData = data.filter((item) => filterFn(item, searchQuery));

    return (
        <div style={{ animation: "fadeIn 0.3s ease-out" }}>
            <CrudActionBar
                onAdd={onAdd}
                onImport={onImport}
                onExport={onExport}
                searchQuery={searchQuery}
                onSearchChange={setSearchQuery}
            />
            <CrudTable
                data={filteredData}
                onEdit={onEdit}
                onDelete={onDelete}
                extraActions={extraActions}
            />
        </div>
    );
}
