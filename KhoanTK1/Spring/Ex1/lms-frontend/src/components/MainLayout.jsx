import React from 'react';
import { NavLink, Outlet } from 'react-router-dom';

function MainLayout() {
  return (
    <div className="layout-container">
      <header className="banner">
        LMS MANAGEMENT SYSTEM
      </header>
      <div className="layout-main">
        <aside className="sidebar">
          <nav>
            <ul>
              <li>
                <NavLink 
                  to="/courses/new" 
                  className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}
                >
                  Add a new Course
                </NavLink>
              </li>
              <li>
                <NavLink 
                  to="/courses" 
                  className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}
                  end
                >
                  Courses Management
                </NavLink>
              </li>
              <li>
                <NavLink 
                  to="/lessons" 
                  className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}
                >
                  Lessons Management
                </NavLink>
              </li>
            </ul>
          </nav>
        </aside>
        <main className="content">
          <Outlet />
        </main>
      </div>
      <footer className="footer">
        © 2023 - All rights reserved. Built with Spring Framework
      </footer>
    </div>
  );
}

export default MainLayout;
