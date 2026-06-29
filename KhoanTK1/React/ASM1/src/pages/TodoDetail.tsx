import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useTodos } from '../context/TodoContext';
import { TodoForm } from '../components/TodoForm';
import { api } from '../services/api';
import type { Todo } from '../services/api';
import { 
    ArrowLeft, 
    Calendar, 
    CheckCircle2, 
    Clock, 
    Trash2, 
    Edit3, 
    AlertTriangle,
    Loader2
} from 'lucide-react';

export function TodoDetail() {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const { state, deleteTodo, toggleTodoCompletion } = useTodos();
    
    const [todo, setTodo] = useState<Todo | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [isFormOpen, setIsFormOpen] = useState(false);

    useEffect(() => {
        let active = true;

        const loadTodo = async () => {
            if (!id) return;
            
            const todoFromContext = state.todos.find(t => t.id === id);
            if (todoFromContext) {
                if (active) {
                    setTodo(todoFromContext);
                    setLoading(false);
                    setError(null);
                }
                return;
            }

            try {
                if (active) setLoading(true);
                const todoData = await api.getTodoById(id);
                if (active) {
                    setTodo(todoData);
                    setError(null);
                }
            } catch (err: any) {
                if (active) {
                    setError(err.message || 'Todo not found');
                }
            } finally {
                if (active) setLoading(false);
            }
        };

        loadTodo();

        return () => {
            active = false;
        };
    }, [id, state.todos]);

    const onToggleCompletion = async () => {
        if (!todo) return;
        try {
            await toggleTodoCompletion(todo.id, !todo.completed);
        } catch (err: any) {
            alert(err.message || 'Failed to update todo status');
        }
    };

    const onDeletePress = async () => {
        if (!todo) return;
        if (window.confirm('Are you sure you want to delete this todo?')) {
            try {
                await deleteTodo(todo.id);
                navigate('/todos');
            } catch (err: any) {
                alert(err.message || 'Failed to delete todo');
            }
        }
    };

    const formatDate = (isoString: string) => {
        const date = new Date(isoString);
        return date.toLocaleDateString('en-US', {
            weekday: 'long',
            month: 'long',
            day: 'numeric',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    if (loading) {
        return (
            <div className="container mx-auto px-6 py-10 max-w-3xl">
                <Link
                    to="/todos"
                    className="inline-flex items-center gap-2 text-slate-400 hover:text-white mb-6 group transition-colors text-sm font-semibold"
                >
                    <ArrowLeft className="w-4 h-4 group-hover:-translate-x-1 transition-transform" />
                    <span>Back to Todos</span>
                </Link>
                <div className="glass-card p-12 text-center rounded-3xl flex flex-col items-center">
                    <Loader2 className="w-10 h-10 text-indigo-500 animate-spin mb-4" />
                    <p className="text-slate-400 font-medium">Retrieving todo details...</p>
                </div>
            </div>
        );
    }

    if (error || !todo) {
        return (
            <div className="container mx-auto px-6 py-10 max-w-3xl">
                <Link
                    to="/todos"
                    className="inline-flex items-center gap-2 text-slate-400 hover:text-white mb-6 group transition-colors text-sm font-semibold"
                >
                    <ArrowLeft className="w-4 h-4 group-hover:-translate-x-1 transition-transform" />
                    <span>Back to Todos</span>
                </Link>
                <div className="glass-card p-10 text-center rounded-3xl border-rose-500/20 bg-rose-950/10">
                    <AlertTriangle className="w-12 h-12 text-rose-500 mx-auto mb-4 animate-bounce" />
                    <h3 className="text-xl font-bold text-white mb-2">Todo Not Found</h3>
                    <p className="text-slate-400 text-sm mb-6 max-w-sm mx-auto">{error || 'Todo is unavailable.'}</p>
                    <Link
                        to="/todos"
                        className="px-5 py-2.5 bg-slate-800 hover:bg-slate-700 text-white font-semibold rounded-xl text-sm transition-all"
                    >
                        Return to list
                    </Link>
                </div>
            </div>
        );
    }

    return (
        <div className="container mx-auto px-6 py-10 max-w-3xl">
            <Link
                to="/todos"
                className="inline-flex items-center gap-2 text-slate-400 hover:text-white mb-6 group transition-colors text-sm font-semibold"
            >
                <ArrowLeft className="w-4 h-4 group-hover:-translate-x-1 transition-transform" />
                <span>Back to Todos</span>
            </Link>

            <div className="glass-card rounded-3xl overflow-hidden shadow-2xl relative">
                <div className={`h-2 w-full ${todo.completed ? 'bg-emerald-500' : 'bg-indigo-500'}`} />

                <div className="p-8 md:p-10 space-y-6">
                    <div className="flex flex-wrap items-center justify-between gap-4">
                        <div className="flex items-center gap-1.5 text-xs text-slate-400 font-medium">
                            <Calendar className="w-4 h-4 text-indigo-400" />
                            <span>Created on {formatDate(todo.createdAt)}</span>
                        </div>
                        
                        <button
                            onClick={onToggleCompletion}
                            className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold border transition-all ${
                                todo.completed
                                    ? 'bg-emerald-500/10 text-emerald-400 border-emerald-500/20 hover:bg-emerald-500/20'
                                    : 'bg-indigo-500/10 text-indigo-400 border-indigo-500/20 hover:bg-indigo-500/20'
                            }`}
                        >
                            {todo.completed ? (
                                <>
                                    <CheckCircle2 className="w-3.5 h-3.5" />
                                    <span>Completed</span>
                                </>
                            ) : (
                                <>
                                    <Clock className="w-3.5 h-3.5" />
                                    <span>In Progress</span>
                                </>
                            )}
                        </button>
                    </div>

                    <div className="space-y-4">
                        <h2 className="text-2xl md:text-3xl font-extrabold text-white leading-tight">
                            {todo.name}
                        </h2>
                        <div className="bg-slate-950/30 p-6 rounded-2xl border border-white/5 min-h-[120px]">
                            <p className="text-slate-300 text-base leading-relaxed whitespace-pre-wrap">
                                {todo.description || (
                                    <span className="italic text-slate-500">
                                        No description provided for this todo.
                                    </span>
                                )}
                            </p>
                        </div>
                    </div>

                    <div className="flex flex-wrap justify-between items-center gap-4 pt-6 border-t border-white/10">
                        <button
                            onClick={onToggleCompletion}
                            className={`px-5 py-2.5 rounded-xl font-semibold text-sm transition-all duration-200 border ${
                                todo.completed
                                    ? 'bg-slate-800 hover:bg-slate-700 text-slate-300 border-white/5'
                                    : 'bg-emerald-600 hover:bg-emerald-500 active:bg-emerald-700 text-white border-transparent shadow-lg shadow-emerald-600/25'
                            }`}
                        >
                            {todo.completed ? 'Mark Active' : 'Mark Completed'}
                        </button>

                        <div className="flex items-center gap-3">
                            <button
                                onClick={() => setIsFormOpen(true)}
                                className="inline-flex items-center gap-2 px-4 py-2.5 bg-white/5 hover:bg-indigo-600/15 border border-white/10 hover:border-indigo-500/20 text-slate-300 hover:text-indigo-400 font-semibold rounded-xl text-sm transition-all duration-200"
                            >
                                <Edit3 className="w-4 h-4" />
                                <span>Edit</span>
                            </button>
                            <button
                                onClick={onDeletePress}
                                className="inline-flex items-center gap-2 px-4 py-2.5 bg-rose-500/10 hover:bg-rose-500 active:bg-rose-700 border border-rose-500/20 hover:border-transparent text-rose-400 hover:text-white font-semibold rounded-xl text-sm transition-all duration-200"
                            >
                                <Trash2 className="w-4 h-4" />
                                <span>Delete</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <TodoForm
                isOpen={isFormOpen}
                onClose={() => setIsFormOpen(false)}
                todoToEdit={todo}
            />
        </div>
    );
}
