import { Outlet, useLocation } from 'react-router-dom';
import Header from './Header';
import Sidebar from './Sidebar';
import Footer from './Footer';
import { useLmsStore } from '../../store/useLmsStore';

export default function Layout() {
  const location = useLocation();
  const isHomePage = location.pathname === '/';
  const { toast, hideToast } = useLmsStore();

  return (
    <div className={isHomePage ? 'main-viewport' : 'main-viewport-no-footer'}>
      {/* Top Header */}
      <Header />

      {/* Left Sidebar */}
      <Sidebar />

      {/* Main Content Viewport */}
      <main className="p-8 flex flex-col min-h-[calc(100vh-64px-140px)] bg-surface">
        <Outlet />
      </main>

      {/* Page Footer */}
      {isHomePage && <Footer />}

      {/* Floating Toast Notification */}
      {toast && (
        <div 
          onClick={hideToast}
          className={`fixed bottom-6 right-6 z-50 flex items-center gap-3 px-4 py-3 rounded-lg shadow-lg cursor-pointer transition-all duration-300 transform translate-y-0 animate-fade-in border ${
            toast.type === 'success' 
              ? 'bg-emerald-50 border-emerald-200 text-emerald-800' 
              : toast.type === 'error'
                ? 'bg-red-50 border-red-200 text-red-800'
                : 'bg-blue-50 border-blue-200 text-blue-800'
          }`}
        >
          <span className="material-icons text-xl">
            {toast.type === 'success' ? 'check_circle' : toast.type === 'error' ? 'error' : 'info'}
          </span>
          <span className="text-sm font-semibold">{toast.message}</span>
          <span className="material-icons text-base ml-2 opacity-50 hover:opacity-100 transition-opacity">close</span>
        </div>
      )}
    </div>
  );
}
