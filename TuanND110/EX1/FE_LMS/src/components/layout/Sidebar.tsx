import { NavLink } from 'react-router-dom';

export default function Sidebar() {
  const menuItems = [
    {
      label: 'Thêm mới Khóa học',
      subLabel: 'Add a new Course',
      icon: 'add_circle',
      to: '/courses/add',
      iconClass: 'text-primary',
    },
    {
      label: 'Quản lý Khóa học',
      subLabel: 'Courses Management',
      icon: 'library_books',
      to: '/courses',
      iconClass: 'text-on-surface-variant group-hover:text-primary',
    },
    {
      label: 'Quản lý Học viên',
      subLabel: 'Students Management',
      icon: 'group',
      to: '/students',
      iconClass: 'text-on-surface-variant group-hover:text-primary',
    },
  ];

  return (
    <aside className="fixed left-0 top-16 bottom-0 w-sidebar-width bg-white border-r border-outline-variant flex flex-col pt-4 overflow-y-auto" data-purpose="sidebar-navigation">
      <nav className="flex flex-col gap-1 px-3">
        {menuItems.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            className={({ isActive }) => 
              `group flex items-center gap-4 py-3 transition-all duration-200 active:scale-95 ${
                isActive 
                  ? 'px-[12px] bg-secondary-container text-on-secondary-container border-l-4 border-primary font-bold shadow-sm rounded-r-md'
                  : 'px-4 rounded-md hover:bg-surface-container-highest text-on-surface'
              }`
            }
          >
            <span className={`material-icons ${item.iconClass}`}>
              {item.icon}
            </span>
            <div className="flex flex-col">
              <span className="text-sm">{item.label}</span>
              <span className="text-xs font-normal italic opacity-80">{item.subLabel}</span>
            </div>
          </NavLink>
        ))}
      </nav>
      {/* Spacer matching mt-auto in wireframe */}
      <div className="mt-auto p-8 opacity-20 select-none" />
    </aside>
  );
}
