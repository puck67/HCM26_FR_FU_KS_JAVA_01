export interface Todo {
    id: string;
    name: string;
    description: string;
    completed: boolean;
    createdAt: string;
}

const STORAGE_KEY = 'asm1_todos';

const defaultTodos: Todo[] = [
    {
        id: '1',
        name: 'Research Target Audience',
        description: 'Conduct surveys and interview users to define user personas for the new landing page.',
        completed: true,
        createdAt: new Date(Date.now() - 86400000 * 3).toISOString(),
    },
    {
        id: '2',
        name: 'Design High-Fidelity Mockups',
        description: 'Create responsive UI designs in Figma using our premium color palette and glassmorphism guidelines.',
        completed: false,
        createdAt: new Date(Date.now() - 86400000 * 2).toISOString(),
    },
    {
        id: '3',
        name: 'Configure State Management',
        description: 'Implement React context and useReducer to handle todo data globally in the application.',
        completed: false,
        createdAt: new Date(Date.now() - 86400000).toISOString(),
    },
    {
        id: '4',
        name: 'Setup Form Validation',
        description: 'Build todo forms using Formik and schema validation via Yup, handling limits and error displays.',
        completed: false,
        createdAt: new Date().toISOString(),
    }
];

function fetchLocalStorage(): Todo[] {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(defaultTodos));
        return defaultTodos;
    }
    return JSON.parse(raw) as Todo[];
}

function saveLocalStorage(todos: Todo[]): void {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(todos));
}

const sleep = (ms: number) => new Promise((resolve) => setTimeout(resolve, ms));

export const api = {
    async getTodos(): Promise<Todo[]> {
        await sleep(800);
        if (Math.random() < 0.02) {
            throw new Error('Failed to fetch todos from server. Please try again.');
        }
        return fetchLocalStorage();
    },

    async getTodoById(id: string): Promise<Todo> {
        await sleep(600);
        const list = fetchLocalStorage();
        const found = list.find(t => t.id === id);
        if (!found) {
            throw new Error(`Todo with ID ${id} not found.`);
        }
        return found;
    },

    async createTodo(todoData: Omit<Todo, 'id' | 'createdAt' | 'completed'>): Promise<Todo> {
        await sleep(800);
        const list = fetchLocalStorage();
        const newTodo: Todo = {
            id: Math.random().toString(36).substring(2, 9),
            name: todoData.name,
            description: todoData.description || '',
            completed: false,
            createdAt: new Date().toISOString()
        };
        list.push(newTodo);
        saveLocalStorage(list);
        return newTodo;
    },

    async updateTodo(id: string, updatedFields: Partial<Omit<Todo, 'id'>>): Promise<Todo> {
        await sleep(800);
        const list = fetchLocalStorage();
        const idx = list.findIndex(t => t.id === id);
        if (idx === -1) {
            throw new Error(`Todo with ID ${id} not found.`);
        }
        const updatedTodo = { ...list[idx], ...updatedFields };
        list[idx] = updatedTodo;
        saveLocalStorage(list);
        return updatedTodo;
    },

    async deleteTodo(id: string): Promise<string> {
        await sleep(800);
        const list = fetchLocalStorage();
        const filtered = list.filter(t => t.id !== id);
        if (filtered.length === list.length) {
            throw new Error(`Todo with ID ${id} not found.`);
        }
        saveLocalStorage(filtered);
        return id;
    }
};
