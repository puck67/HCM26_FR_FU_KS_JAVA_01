import { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { TodoProvider } from './context/TodoContext';
import { Navbar } from './components/Navbar';
import { Home } from './pages/Home';
import { Todos } from './pages/Todos';
import { TodoDetail } from './pages/TodoDetail';
import { TodoForm } from './components/TodoForm';

function App() {
    const [isCreateOpen, setIsCreateOpen] = useState(false);

    return (
        <TodoProvider>
            <Router>
                <div className="min-h-screen bg-[#0b111e] text-slate-100 flex flex-col">
                    <Navbar
                        onOpenCreateModal={() => setIsCreateOpen(true)}
                    />
                    
                    <main className="flex-grow">
                        <Routes>
                            <Route
                                path="/"
                                element={<Home />}
                            />
                            <Route
                                path="/todos"
                                element={<Todos />}
                            />
                            <Route
                                path="/todos/:id"
                                element={<TodoDetail />}
                            />
                        </Routes>
                    </main>

                    <footer className="py-6 border-t border-white/5 bg-[#070b14] text-center text-xs text-slate-500 font-semibold tracking-wider">
                        <div>
                            &copy; 2026 ASM1 Todo Management. Built with React, TypeScript & Tailwind CSS.
                        </div>
                    </footer>

                    <TodoForm
                        isOpen={isCreateOpen}
                        onClose={() => setIsCreateOpen(false)}
                    />
                </div>
            </Router>
        </TodoProvider>
    );
}

export default App;
