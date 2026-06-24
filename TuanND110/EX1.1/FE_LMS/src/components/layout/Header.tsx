import { Link } from 'react-router-dom';

export default function Header() {
  return (
    <header className="fixed top-0 left-0 w-full h-16 z-50 bg-primary text-on-primary flex items-center justify-between px-6 shadow-md" data-purpose="top-navigation">
      {/* Logo Section */}
      <Link to="/" className="flex items-center gap-3 hover:opacity-90 transition-opacity text-white no-underline">
        <span className="material-icons text-3xl">menu_book</span>
        <h1 className="text-xl font-bold tracking-tight">LMS MANAGEMENT SYSTEM</h1>
      </Link>

      {/* Search and Actions Section */}
      <div className="flex items-center gap-6 flex-1 justify-end">
        {/* Search Bar */}
        <div className="relative w-full max-w-md">
          <span className="material-icons absolute left-3 top-1/2 -translate-y-1/2 text-on-primary/70">search</span>
          <input 
            className="w-full bg-on-primary/10 border-none rounded-md py-1.5 pl-10 pr-4 text-sm placeholder-on-primary/60 focus:ring-2 focus:ring-on-primary/30 focus:bg-on-primary/20 transition-all outline-none" 
            placeholder="Search" 
            type="text" 
          />
        </div>

        {/* User Profile Action */}
        <button aria-label="User Profile" className="hover:bg-on-primary/10 p-2 rounded-full transition-colors flex items-center justify-center">
          <span className="material-icons text-3xl">account_circle</span>
        </button>
      </div>
    </header>
  );
}
