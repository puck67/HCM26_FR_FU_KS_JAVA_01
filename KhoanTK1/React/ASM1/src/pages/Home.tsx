import { Link } from 'react-router-dom';
import { useTodos } from '../context/TodoContext';
import { ListTodo, CheckCircle2, Clock, BarChart3, ArrowRight } from 'lucide-react';

const SPECIFICATIONS = [
    {
        title: 'useReducer',
        text: 'Robust local data structure management for adds, edits, and deletes.',
        color: 'text-indigo-400'
    },
    {
        title: 'Formik & Yup',
        text: 'Strict criteria schema validations for safe form submissions.',
        color: 'text-purple-400'
    },
    {
        title: 'Tailwind CSS',
        text: 'Fully responsive, grid-driven interface optimized for mobile and desktop.',
        color: 'text-pink-400'
    },
    {
        title: 'REST Simulation',
        text: 'Asynchronous HTTP mock API calling with real-world state feedback.',
        color: 'text-emerald-400'
    }
];

export const Home = () => {
    const { todos, loading } = useTodos().state;

    const totalTodos = todos.length;
    const completedTodos = todos.filter((t) => t.completed).length;
    const pendingTodos = totalTodos - completedTodos;
    const completionRate = totalTodos > 0 ? Math.round((completedTodos / totalTodos) * 100) : 0;

    return (
        <div className="container mx-auto px-6 py-10 max-w-6xl">
            <div className="relative overflow-hidden rounded-3xl bg-gradient-to-r from-indigo-900/60 via-purple-900/40 to-slate-900/80 p-8 md:p-12 border border-indigo-500/10 shadow-2xl mb-10">
                <div className="absolute -right-16 -top-16 w-64 h-64 bg-indigo-500/10 rounded-full blur-3xl"></div>
                <div className="absolute -left-16 -bottom-16 w-64 h-64 bg-purple-500/10 rounded-full blur-3xl"></div>
                
                <div className="relative z-10 max-w-2xl">
                    <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold bg-indigo-500/15 text-indigo-400 border border-indigo-500/20 mb-4">
                        <span className="w-1.5 h-1.5 rounded-full bg-indigo-400 animate-ping"></span>
                        Version 1.0 (Vite + Hooks)
                    </span>
                    <h1 className="text-4xl md:text-5xl font-extrabold text-white tracking-tight leading-tight mb-4">
                        Simplify Your Workflow, <br />
                        <span className="bg-gradient-to-r from-indigo-400 via-purple-400 to-pink-400 bg-clip-text text-transparent">
                            Amplify Productivity.
                        </span>
                    </h1>
                    <p className="text-slate-300 text-lg mb-8 leading-relaxed">
                        Welcome to ASM1 Todo Dashboard. Keep track of your assignments, manage sub-todos with useReducer, and validate your data dynamically using Formik & Tailwind CSS.
                    </p>
                    <div className="flex flex-wrap gap-4">
                        <Link
                            to="/todos"
                            className="inline-flex items-center gap-2 px-6 py-3 bg-indigo-600 hover:bg-indigo-500 active:bg-indigo-700 text-white font-semibold rounded-xl shadow-lg shadow-indigo-600/30 transition-all duration-200"
                        >
                            <span>Manage Todos</span>
                            <ArrowRight className="w-5 h-5" />
                        </Link>
                    </div>
                </div>
            </div>

            <h2 className="text-2xl font-bold text-white mb-6 flex items-center gap-2">
                <BarChart3 className="w-6 h-6 text-indigo-400" />
                <span>Dashboard Overview</span>
            </h2>

            {loading && totalTodos === 0 ? (
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-10">
                    {[1, 2, 3].map((i) => (
                        <div
                            key={i}
                            className="glass-card p-6 rounded-2xl animate-pulse"
                        >
                            <div className="w-12 h-12 bg-white/5 rounded-xl mb-4"></div>
                            <div className="h-4 bg-white/10 w-24 rounded mb-2"></div>
                            <div className="h-8 bg-white/10 w-16 rounded"></div>
                        </div>
                    ))}
                </div>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-10">
                    <div className="glass-card p-6 rounded-2xl relative overflow-hidden">
                        <div className="absolute top-0 right-0 w-24 h-24 bg-blue-500/5 rounded-full blur-xl -mr-4 -mt-4"></div>
                        <div className="flex items-center justify-between mb-4">
                            <span className="text-slate-400 font-semibold text-sm uppercase tracking-wider">
                                Total Todos
                            </span>
                            <div className="bg-blue-500/10 p-2.5 rounded-xl text-blue-400">
                                <ListTodo className="w-6 h-6" />
                            </div>
                        </div>
                        <div className="text-3xl font-extrabold text-white mb-2">
                            {totalTodos}
                        </div>
                        <p className="text-xs text-slate-400 font-medium">
                            All todos in current backlog
                        </p>
                    </div>

                    <div className="glass-card p-6 rounded-2xl relative overflow-hidden">
                        <div className="absolute top-0 right-0 w-24 h-24 bg-emerald-500/5 rounded-full blur-xl -mr-4 -mt-4"></div>
                        <div className="flex items-center justify-between mb-4">
                            <span className="text-slate-400 font-semibold text-sm uppercase tracking-wider">
                                Completed
                            </span>
                            <div className="bg-emerald-500/10 p-2.5 rounded-xl text-emerald-400">
                                <CheckCircle2 className="w-6 h-6" />
                            </div>
                        </div>
                        <div className="text-3xl font-extrabold text-white mb-2">
                            {completedTodos}
                        </div>
                        <div className="w-full bg-slate-800 rounded-full h-1.5 mt-3 mb-1 overflow-hidden">
                            <div
                                className="bg-emerald-500 h-1.5 rounded-full transition-all duration-500"
                                style={{ width: `${completionRate}%` }}
                            ></div>
                        </div>
                        <div className="flex justify-between text-xs text-slate-400 font-medium">
                            <span>{completionRate}% Complete</span>
                            <span>{pendingTodos} remaining</span>
                        </div>
                    </div>

                    <div className="glass-card p-6 rounded-2xl relative overflow-hidden">
                        <div className="absolute top-0 right-0 w-24 h-24 bg-amber-500/5 rounded-full blur-xl -mr-4 -mt-4"></div>
                        <div className="flex items-center justify-between mb-4">
                            <span className="text-slate-400 font-semibold text-sm uppercase tracking-wider">
                                Pending
                            </span>
                            <div className="bg-amber-500/10 p-2.5 rounded-xl text-amber-400">
                                <Clock className="w-6 h-6" />
                            </div>
                        </div>
                        <div className="text-3xl font-extrabold text-white mb-2">
                            {pendingTodos}
                        </div>
                        <p className="text-xs text-slate-400 font-medium">
                            Require attention and processing
                        </p>
                    </div>
                </div>
            )}

            <div className="glass-card p-8 rounded-3xl">
                <h3 className="text-xl font-bold text-white mb-6">
                    Key Specifications Applied
                </h3>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                    {SPECIFICATIONS.map((spec) => (
                        <div
                            key={spec.title}
                            className="p-4 rounded-xl bg-white/5 border border-white/5"
                        >
                            <div className={`font-bold mb-1 ${spec.color}`}>
                                {spec.title}
                            </div>
                            <p className="text-xs text-slate-400">
                                {spec.text}
                            </p>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};
