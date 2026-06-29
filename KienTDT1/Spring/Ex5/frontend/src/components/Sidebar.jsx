import { Link } from "react-router-dom";
import { getUserInfo } from "../utils/auth";

export default function Sidebar() {
  const user = getUserInfo();

  const canManageCourses = user?.role === "ADMIN" || user?.role === "TRAINER";
  const canManageUsers = user?.role === "ADMIN";

  return (
    <aside className="w-64 bg-gray-900 text-white min-h-screen p-5">
      <h1 className="text-2xl font-bold mb-8">Training System</h1>

      <nav className="space-y-3">
        <Link className="block hover:bg-gray-700 p-3 rounded" to="/dashboard">
          Dashboard
        </Link>

        <Link className="block hover:bg-gray-700 p-3 rounded" to="/courses">
          Courses
        </Link>

        {canManageCourses && (
          <Link className="block hover:bg-gray-700 p-3 rounded text-blue-300" to="/courses/manage">
            Manage Courses
          </Link>
        )}

        {canManageUsers && (
          <Link className="block hover:bg-gray-700 p-3 rounded" to="/users">
            Users
          </Link>
        )}
      </nav>

      
    </aside>
  );
}
