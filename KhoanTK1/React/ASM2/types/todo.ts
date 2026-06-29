export interface Todo {
    id: string;
    name: string;
    description: string;
    completed: boolean;
    createdAt: string;
    updatedAt: string;
}

export interface TodoFormValues {
    name: string;
    description: string;
}
