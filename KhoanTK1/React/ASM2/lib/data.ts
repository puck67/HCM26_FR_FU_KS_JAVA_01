import { Todo } from "@/types/todo";

// Danh sách todos lưu tạm trong RAM
let todos: Todo[] = [
    {
        id: "1",
        name: "Design system architecture",
        description: "Plan the overall architecture for the new microservices platform.",
        completed: true,
        createdAt: new Date("2026-06-25T08:00:00").toISOString(),
        updatedAt: new Date("2026-06-26T10:00:00").toISOString(),
    },
    {
        id: "2",
        name: "Implement authentication module",
        description: "Build JWT-based authentication with refresh tokens and role-based access control.",
        completed: false,
        createdAt: new Date("2026-06-26T09:00:00").toISOString(),
        updatedAt: new Date("2026-06-26T09:00:00").toISOString(),
    },
    {
        id: "3",
        name: "Write unit tests",
        description: "Cover all service layer methods with Jest unit tests.",
        completed: false,
        createdAt: new Date("2026-06-27T10:00:00").toISOString(),
        updatedAt: new Date("2026-06-27T10:00:00").toISOString(),
    },
    {
        id: "4",
        name: "Setup CI/CD pipeline",
        description: "Configure GitHub Actions for automated testing and deployment.",
        completed: true,
        createdAt: new Date("2026-06-28T08:30:00").toISOString(),
        updatedAt: new Date("2026-06-28T11:00:00").toISOString(),
    },
];

export const getAllTodos = (): Todo[] => [...todos];

export const getTodoById = (id: string): Todo | undefined => todos.find((t) => t.id === id);

export function createTodoInStore(name: string, description: string): Todo {
    const newTodo: Todo = {
        id: Date.now().toString(),
        name,
        description,
        completed: false,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
    };
    todos = [...todos, newTodo];
    return newTodo;
}

export function updateTodoInStore(
    id: string,
    name: string,
    description: string
): Todo | null {
    const idx = todos.findIndex((t) => t.id === id);
    if (idx === -1) {
        return null;
    }
    todos[idx] = { 
        ...todos[idx], 
        name, 
        description, 
        updatedAt: new Date().toISOString() 
    };
    return todos[idx];
}

export function deleteTodoFromStore(id: string): boolean {
    const before = todos.length;
    todos = todos.filter((t) => t.id !== id);
    return todos.length < before;
}

export function toggleTodoCompletedInStore(id: string): Todo | null {
    const found = todos.find((t) => t.id === id);
    if (!found) {
        return null;
    }
    found.completed = !found.completed;
    found.updatedAt = new Date().toISOString();
    todos = [...todos];
    return found;
}
