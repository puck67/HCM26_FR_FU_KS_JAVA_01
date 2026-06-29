export interface Identifiable {
  id: string;
}

export type CrudAction<T extends Identifiable> =
  | { type: 'SET_ITEMS'; payload: T[] }
  | { type: 'ADD_ITEM'; payload: T }
  | { type: 'UPDATE_ITEM'; payload: T }
  | { type: 'DELETE_ITEM'; payload: string };

export interface CrudToolbarProps {
  title: string;
  entityName: string;
  createLabel?: string;
  searchTerm: string;
  onSearchChange: (val: string) => void;
  statusFilter?: string;
  onStatusChange?: (val: string) => void;
  statusOptions?: { label: string; value: string }[];
  onOpenCreate: () => void;
}
