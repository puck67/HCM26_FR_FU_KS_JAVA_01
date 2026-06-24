import { useState, useEffect } from "react";
import TaskPage from "./pages/TaskPage";
import StudentPage from "./pages/StudentPage";
import TeacherPage from "./pages/TeacherPage";
import { ToastProvider } from "./components/ToastContext";
import { ParticlesBackground } from "./components/ParticlesBackground";
import { AppstoreOutlined, TeamOutlined, ScheduleOutlined, SettingOutlined } from '@ant-design/icons';
import "./index.css";

const THUMB_IMAGES = [
  "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe",
  "https://images.unsplash.com/photo-1550684848-fac1c5b4e853",
  "https://images.unsplash.com/photo-1541701494587-cb58502866ab",
  "https://images.unsplash.com/photo-1579546929518-9e396f3cc809",
  "https://images.unsplash.com/photo-1614850523459-c2f4c699c52e",
  "https://images.unsplash.com/photo-1620641788421-7a1c342ea42e",
  "https://images.unsplash.com/photo-1614850715649-1d0106293bd1",
  "https://images.unsplash.com/photo-1506744012022-28d159be4161",
  "https://images.unsplash.com/photo-1533134486753-c833f0ed4866"
];

function AppContent() {
  const [activeTab, setActiveTab] = useState("task");
  const [activeTheme, setActiveTheme] = useState(1);
  const [isDarkMode, setIsDarkMode] = useState(true);
  const [themeStartIndex, setThemeStartIndex] = useState(0);
  
  useEffect(() => {
    if (isDarkMode) {
      document.body.classList.remove('light-mode');
    } else {
      document.body.classList.add('light-mode');
    }
  }, [isDarkMode]);
  
  // Mouse parallax effect
  const [mousePos, setMousePos] = useState({ x: 0, y: 0 });
  
  useEffect(() => {
    const handleMouseMove = (e: MouseEvent) => {
      // Calculate normalized mouse position from -1 to 1
      const x = (e.clientX / window.innerWidth) * 2 - 1;
      const y = (e.clientY / window.innerHeight) * 2 - 1;
      setMousePos({ x, y });
    };
    
    window.addEventListener('mousemove', handleMouseMove);
    return () => window.removeEventListener('mousemove', handleMouseMove);
  }, []);

  // Calculate dynamic rotation based on mouse (subtle movement, base is flat)
  const rotateY = (mousePos.x * 2); 
  const rotateX = -(mousePos.y * 2); 

  return (
    <div className="perspective-container">
      <ParticlesBackground />
      <div 
        className="vr-panel"
        style={{ transform: `rotateY(${rotateY}deg) rotateX(${rotateX}deg) translateZ(0px)` }}
      >
        <div className="vr-sidebar">
          <div className="vr-header" style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
            <video 
              autoPlay 
              loop 
              muted 
              playsInline 
              src="https://v1.pinimg.com/videos/mc/720p/ed/2d/89/ed2d891ab77c823f878eff553e35eb17.mp4"
              style={{
                width: '96px',
                height: '96px',
                borderRadius: '20px',
                objectFit: 'cover',
                boxShadow: '0 8px 16px rgba(0,0,0,0.4)',
                border: '1px solid rgba(255,255,255,0.1)'
              }}
            />
            <div>
              <h2>SYSTEM</h2>
              <p>DASHBOARD</p>
            </div>
          </div>

          <div className="vr-nav">
            <div 
              className={`vr-nav-item ${activeTab === "task" ? "active" : ""}`}
              onClick={() => setActiveTab("task")}
            >
              <div className="vr-nav-icon"><AppstoreOutlined style={{ fontSize: '24px' }} /></div>
              <div className="vr-nav-text">
                <h3>Task Management</h3>
                {activeTab === "task" && (
                  <>
                    <p>Assignments</p>
                    <p>Project Progress</p>
                  </>
                )}
              </div>
            </div>

            <div 
              className={`vr-nav-item ${activeTab === "student" ? "active" : ""}`}
              onClick={() => setActiveTab("student")}
            >
              <div className="vr-nav-icon"><TeamOutlined style={{ fontSize: '24px' }} /></div>
              <div className="vr-nav-text">
                <h3>Student Records</h3>
                {activeTab === "student" && (
                  <>
                    <p>Profiles & Data</p>
                    <p>Grades & Conduct</p>
                  </>
                )}
              </div>
            </div>

            <div 
              className={`vr-nav-item ${activeTab === "teacher" ? "active" : ""}`}
              onClick={() => setActiveTab("teacher")}
            >
              <div className="vr-nav-icon"><ScheduleOutlined style={{ fontSize: '24px' }} /></div>
              <div className="vr-nav-text">
                <h3>Teacher Roster</h3>
                {activeTab === "teacher" && (
                  <>
                    <p>Staff</p>
                    <p>Teaching Schedule</p>
                  </>
                )}
              </div>
            </div>
          </div>

          <div className="vr-theme-toggle" onClick={() => setIsDarkMode(!isDarkMode)}>
            <div className="vr-nav-icon">
              {isDarkMode ? <SettingOutlined style={{ fontSize: '24px' }} /> : <SettingOutlined style={{ fontSize: '24px', color: '#111' }} />}
            </div>
            <div className="vr-nav-text">
              <h3>{isDarkMode ? "Dark Mode" : "Light Mode"}</h3>
              <p>Theme Setting</p>
            </div>
          </div>
        </div>

        <div 
          className="vr-main-content"
          style={{ "--bg-image": `url('${THUMB_IMAGES[activeTheme]}?q=80&w=2000&auto=format&fit=crop')` } as React.CSSProperties}
        >
          <div className="vr-content-glass">
            {activeTab === "task" && <TaskPage />}
            {activeTab === "student" && <StudentPage />}
            {activeTab === "teacher" && <TeacherPage />}
          </div>
          
          <div className="vr-bottom-carousel-container">
            <div className="carousel-glass-pill">
              <div className="carousel-thumbnails">
                {THUMB_IMAGES.slice(themeStartIndex, themeStartIndex + 8).map((img, idx) => {
                  const actualIndex = themeStartIndex + idx;
                  return (
                    <div 
                      key={actualIndex}
                      className={`thumb ${activeTheme === actualIndex ? 'active' : ''}`} 
                      style={{ backgroundImage: `url('${img}?w=100&fit=crop')` }}
                      onClick={() => setActiveTheme(actualIndex)}
                    ></div>
                  );
                })}
              </div>
              <div className="carousel-divider"></div>
              <button 
                className="carousel-next"
                onClick={() => {
                  if (themeStartIndex + 8 >= THUMB_IMAGES.length) {
                    setThemeStartIndex(0);
                  } else {
                    setThemeStartIndex(themeStartIndex + 1);
                  }
                }}
              >
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><polyline points="9 18 15 12 9 6"></polyline></svg>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default function App() {
  return (
    <ToastProvider>
      <AppContent />
    </ToastProvider>
  );
}
