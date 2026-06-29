import React, { createContext, useContext, useReducer, useEffect, useCallback, useMemo } from 'react';
import { api } from '../services/api';
import type { Todo } from '../services/api';

interface TodoState {
    todos: Todo[];
    loading: boolean;
    error: string | null;
}

type TodoAction =
    | { type: 'FETCH_START' }
    | { type: 'FETCH_SUCCESS'; payload: Todo[] }
    | { type: 'FETCH_FAILURE'; payload: string }
    | { type: 'ADD_TODO'; payload: Todo }
    | { type: 'UPDATE_TODO'; payload: Todo }
    | { type: 'DELETE_TODO'; payload: string };

const initialState: TodoState = {
    todos: [],
    loading: false,
    error: null,
};

interface TodoContextType {
    state: TodoState;
    fetchTodos: () => Promise<void>;
    addTodo: (name: string, description: string) => Promise<void>;
    updateTodo: (id: string, fields: Partial<Omit<Todo, 'id'>>) => Promise<void>;
    deleteTodo: (id: string) => Promise<void>;
    toggleTodoCompletion: (id: string, completed: boolean) => Promise<void>;
}

const TodoContext = createContext<TodoContextType | undefined>(undefined);

export function TodoProvider({ children }: { children: React.ReactNode }) {
    const [state, dispatch] = useReducer(todoReducer, initialState);

    const fetchTodos = useCallback(async () => {
        dispatch({ type: 'FETCH_START' });
        try {
            const data = await api.getTodos();
            dispatch({ type: 'FETCH_SUCCESS', payload: data });
        } catch (err: any) {
            dispatch({
                type: 'FETCH_FAILURE',
                payload: err.message || 'An error occurred while fetching todos'
            });
        }
    }, []);

    const addTodo = useCallback(async (name: string, description: string) => {
        try {
            const newTodo = await api.createTodo({ name, description });
            dispatch({ type: 'ADD_TODO', payload: newTodo });
        } catch (err: any) {
            throw new Error(err.message || 'Failed to create todo');
        }
    }, []);

    const updateTodo = useCallback(async (id: string, fields: Partial<Omit<Todo, 'id'>>) => {
        try {
            const updatedTodo = await api.updateTodo(id, fields);
            dispatch({ type: 'UPDATE_TODO', payload: updatedTodo });
        } catch (err: any) {
            throw new Error(err.message || 'Failed to update todo');
        }
    }, []);

    const deleteTodo = useCallback(async (id: string) => {
        try {
            const deletedId = await api.deleteTodo(id);
            dispatch({ type: 'DELETE_TODO', payload: deletedId });
        } catch (err: any) {
            throw new Error(err.message || 'Failed to delete todo');
        }
    }, []);

    const toggleTodoCompletion = useCallback(async (id: string, completed: boolean) => {
        await updateTodo(id, { completed });
    }, [updateTodo]);

    useEffect(() => {
        fetchTodos();
    }, [fetchTodos]);

    const contextValue = useMemo(() => ({
        state,
        fetchTodos,
        addTodo,
        updateTodo,
        deleteTodo,
        toggleTodoCompletion
    }), [state, fetchTodos, addTodo, updateTodo, deleteTodo, toggleTodoCompletion]);

    return (
        <TodoContext.Provider value={contextValue}>
            {children}
        </TodoContext.Provider>
    );
}

export const useTodos = (): TodoContextType => {
    const context = useContext(TodoContext);
    if (!context) {
        throw new Error('useTodos must be used within a TodoProvider');
    }
    return context;
};

function todoReducer(state: TodoState, action: TodoAction): TodoState {
    switch (action.type) {
        case 'FETCH_START': {
            return {
                ...state,
                loading: true,
                error: null
            };
        }
        case 'FETCH_SUCCESS': {
            return {
                ...state,
                loading: false,
                todos: action.payload,
                error: null
            };
        }
        case 'FETCH_FAILURE': {
            return {
                ...state,
                loading: false,
                error: action.payload
            };
        }
        case 'ADD_TODO': {
            return {
                ...state,
                todos: [...state.todos, action.payload]
            };
        }
        case 'UPDATE_TODO': {
            return {
                ...state,
                todos: state.todos.map((todo) =>
                    todo.id === action.payload.id ? action.payload : todo
                )
            };
        }
        case 'DELETE_TODO': {
            return {
                ...state,
                todos: state.todos.filter((todo) => todo.id !== action.payload)
            };
        }
        default:
            return state;
    }
}
