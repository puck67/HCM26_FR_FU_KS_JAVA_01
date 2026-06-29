import React from 'react';
import { useNavigate } from 'react-router-dom';
import { CheckSquare, ListTodo, Activity, Award, ArrowRight, Zap, CheckCircle2 } from 'lucide-react';
import { useTasks } from '../context/TaskContext';
import { Button } from '../components/shared/Button';

export const Home: React.FC = () => {
  const navigate = useNavigate();
  const { tasks, loading } = useTasks();

  // Calculate dynamic stats
  const total = tasks.length;
  const completed = tasks.filter(t => t.status === 'Completed').length;
  const inProgress = tasks.filter(t => t.status === 'In Progress').length;
  const pending = tasks.filter(t => t.status === 'Pending').length;
  const highPriority = tasks.filter(t => t.priority === 'High').length;
  
  const completionRate = total > 0 ? Math.round((completed / total) * 100) : 0;

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-float">
      {/* Hero Banner */}
      <div className="relative glassmorphism rounded-3xl p-8 sm:p-12 mb-8 overflow-hidden border border-white/5 shadow-2xl">
        <div className="absolute top-0 right-0 w-[400px] h-[400px] bg-cyber-purple/10 rounded-full blur-[100px] -z-10" />
        <div className="absolute bottom-0 left-0 w-[300px] h-[300px] bg-cyber-cyan/10 rounded-full blur-[80px] -z-10" />
        
        <div className="max-w-3xl">
          <div className="inline-flex items-center gap-2 px-3 py-1.5 rounded-full bg-cyber-purple/10 border border-cyber-purple/20 text-cyber-purple text-xs font-bold uppercase tracking-wider mb-6">
            <Zap className="h-3.5 w-3.5" /> Next-Gen Task Management
          </div>
          
          <h1 className="font-orbitron font-black text-4xl sm:text-6xl text-white mb-6 leading-tight tracking-tight">
            Elevate Your Workflow with{' '}
            <span className="bg-gradient-to-r from-cyber-pink via-cyber-purple to-cyber-cyan bg-clip-text text-transparent text-glow-purple">
              CYBER_TASK
            </span>
          </h1>
          
          <p className="text-[#94a3b8] text-base sm:text-lg mb-8 leading-relaxed">
            A beautiful, reactive React application built with TypeScript, React Router, Hooks, Formik, and styled using Tailwind CSS. Manage tasks, simulate async APIs, and visualize progress in real-time.
          </p>
          
          <div className="flex flex-wrap gap-4">
            <Button
              variant="pink"
              size="lg"
              onClick={() => navigate('/tasks')}
              className="font-orbitron tracking-wider text-xs"
            >
              <span>Access Tasks Dashboard</span>
              <ArrowRight className="h-4 w-4" />
            </Button>
            
            <a
              href="https://github.com"
              target="_blank"
              rel="noreferrer"
              className="inline-flex items-center justify-center font-semibold rounded-lg text-sm px-6 py-3 bg-white/5 border border-white/10 hover:bg-white/10 text-white transition-all-300"
            >
              Documentation
            </a>
          </div>
        </div>
      </div>

      {/* Statistics Section */}
      <div className="mb-8">
        <h2 className="font-orbitron font-bold text-xl uppercase tracking-wider text-white mb-6 flex items-center gap-2">
          <Activity className="h-5 w-5 text-cyber-cyan" /> System Statistics
        </h2>
        
        {loading ? (
          <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
            {[...Array(4)].map((_, i) => (
              <div key={i} className="glassmorphism rounded-2xl p-6 h-28 animate-pulse border border-white/5" />
            ))}
          </div>
        ) : (
          <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
            {/* Total Tasks */}
            <div className="glassmorphism rounded-2xl p-6 border border-white/5 hover:border-white/10 transition-all duration-300 relative group overflow-hidden">
              <div className="absolute top-0 left-0 w-1 h-full bg-cyber-purple" />
              <div className="flex justify-between items-start mb-4">
                <span className="text-gray-500 font-bold uppercase text-[10px] tracking-wider">Total Tasks</span>
                <ListTodo className="h-5 w-5 text-cyber-purple" />
              </div>
              <p className="font-orbitron font-bold text-3xl text-white text-glow-purple">{total}</p>
            </div>

            {/* In Progress */}
            <div className="glassmorphism rounded-2xl p-6 border border-white/5 hover:border-white/10 transition-all duration-300 relative group overflow-hidden">
              <div className="absolute top-0 left-0 w-1 h-full bg-cyber-cyan" />
              <div className="flex justify-between items-start mb-4">
                <span className="text-gray-500 font-bold uppercase text-[10px] tracking-wider">In Progress</span>
                <Activity className="h-5 w-5 text-cyber-cyan" />
              </div>
              <p className="font-orbitron font-bold text-3xl text-white text-glow-cyan">{inProgress}</p>
            </div>

            {/* Completed */}
            <div className="glassmorphism rounded-2xl p-6 border border-white/5 hover:border-white/10 transition-all duration-300 relative group overflow-hidden">
              <div className="absolute top-0 left-0 w-1 h-full bg-cyber-success" />
              <div className="flex justify-between items-start mb-4">
                <span className="text-gray-500 font-bold uppercase text-[10px] tracking-wider">Completed</span>
                <CheckCircle2 className="h-5 w-5 text-cyber-success" />
              </div>
              <p className="font-orbitron font-bold text-3xl text-white text-glow-success">{completed}</p>
            </div>

            {/* High Priority */}
            <div className="glassmorphism rounded-2xl p-6 border border-white/5 hover:border-white/10 transition-all duration-300 relative group overflow-hidden">
              <div className="absolute top-0 left-0 w-1 h-full bg-cyber-danger" />
              <div className="flex justify-between items-start mb-4">
                <span className="text-gray-500 font-bold uppercase text-[10px] tracking-wider">High Priority</span>
                <Award className="h-5 w-5 text-cyber-danger" />
              </div>
              <p className="font-orbitron font-bold text-3xl text-white text-glow-danger">{highPriority}</p>
            </div>
          </div>
        )}
      </div>

      {/* Progress Chart Visualization */}
      {!loading && total > 0 && (
        <div className="glassmorphism rounded-3xl p-6 sm:p-8 border border-white/5">
          <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-6">
            <div>
              <h3 className="font-orbitron font-bold text-lg text-white uppercase tracking-wider">Task Completion Status</h3>
              <p className="text-xs text-gray-500 mt-1">Real-time status of your task distribution</p>
            </div>
            <span className="font-orbitron font-black text-2xl text-cyber-cyan">{completionRate}% Done</span>
          </div>

          {/* Progress Bar */}
          <div className="w-full bg-white/5 rounded-full h-4 mb-8 overflow-hidden p-[2px] border border-white/10">
            <div
              className="bg-gradient-to-r from-cyber-purple via-cyber-pink to-cyber-cyan h-full rounded-full transition-all duration-500 border-glow-cyan"
              style={{ width: `${completionRate}%` }}
            />
          </div>

          {/* Details breakdown */}
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
            <div className="flex items-center gap-4 bg-white/[0.02] border border-white/5 rounded-xl p-4">
              <div className="h-3 w-3 rounded-full bg-cyber-warning" />
              <div>
                <p className="text-xs text-gray-500 uppercase tracking-wider font-bold">Pending</p>
                <p className="font-orbitron text-lg font-bold text-white mt-0.5">
                  {pending} <span className="text-xs text-gray-500 font-normal">({total > 0 ? Math.round((pending / total) * 100) : 0}%)</span>
                </p>
              </div>
            </div>
            
            <div className="flex items-center gap-4 bg-white/[0.02] border border-white/5 rounded-xl p-4">
              <div className="h-3 w-3 rounded-full bg-cyber-cyan" />
              <div>
                <p className="text-xs text-gray-500 uppercase tracking-wider font-bold">In Progress</p>
                <p className="font-orbitron text-lg font-bold text-white mt-0.5">
                  {inProgress} <span className="text-xs text-gray-500 font-normal">({total > 0 ? Math.round((inProgress / total) * 100) : 0}%)</span>
                </p>
              </div>
            </div>

            <div className="flex items-center gap-4 bg-white/[0.02] border border-white/5 rounded-xl p-4">
              <div className="h-3 w-3 rounded-full bg-cyber-success" />
              <div>
                <p className="text-xs text-gray-500 uppercase tracking-wider font-bold">Completed</p>
                <p className="font-orbitron text-lg font-bold text-white mt-0.5">
                  {completed} <span className="text-xs text-gray-500 font-normal">({total > 0 ? Math.round((completed / total) * 100) : 0}%)</span>
                </p>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
