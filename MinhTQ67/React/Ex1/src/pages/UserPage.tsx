import { useState } from "react";
import { Users, UserCheck, ShieldAlert, KeyRound } from "lucide-react";
import { CrudTable } from "../components/CrudTable";
import { CrudActionBar } from "../components/CrudActionBar";
import { ConfirmModal } from "../components/ConfirmModal";
import { ImportModal } from "../components/ImportModal";
import { CrudFormModal, type FieldConfig } from "../components/CrudFormModal";
import { ViewModal } from "../components/ViewModal";
import { StatCard } from "../components/StatCard";

type User = { username: string; email: string; role: string; status: string };

export default function UserPage() {
  const [users, setUsers] = useState<User[]>([
    { username: "admin", email: "admin@example.com", role: "Admin", status: "Active" },
    { username: "johndoe", email: "john@example.com", role: "Editor", status: "Active" },
    { username: "janedoe", email: "jane@example.com", role: "Viewer", status: "Inactive" },
  ]);

  const [isAddOpen, setIsAddOpen] = useState(false);
  const [isImportOpen, setIsImportOpen] = useState(false);
  const [viewingUser, setViewingUser] = useState<User | null>(null);
  const [editingUser, setEditingUser] = useState<User | null>(null);
  const [deletingUser, setDeletingUser] = useState<User | null>(null);

  const totalUsers = users.length;
  const activeUsers = users.filter(u => u.status === "Active").length;
  const admins = users.filter(u => u.role === "Admin").length;

  const fields: FieldConfig[] = [
    { name: "username", label: "Username", type: "text" },
    { name: "email", label: "Email Address", type: "text" },
    { name: "role", label: "Role", type: "select", options: ["Admin", "Editor", "Viewer"] },
    { name: "status", label: "Status", type: "select", options: ["Active", "Inactive"] }
  ];

  const handleExport = () => {
    const csvContent = "data:text/csv;charset=utf-8," 
      + ["username", "email", "role", "status"].join(",") + "\n"
      + users.map(u => `${u.username},${u.email},${u.role},${u.status}`).join("\n");
    const link = document.createElement("a");
    link.setAttribute("href", encodeURI(csvContent));
    link.setAttribute("download", "users_export.csv");
    link.click();
  };

  const handleSaveAdd = (data: any) => {
    setUsers([...users, data as User]);
    setIsAddOpen(false);
  };

  const handleSaveEdit = (data: any) => {
    setUsers(users.map(u => u.username === data.username ? data : u));
    setEditingUser(null);
  };

  const handleDelete = () => {
    if (!deletingUser) return;
    setUsers(users.filter(u => u.username !== deletingUser.username));
    setDeletingUser(null);
  };

  return (
    <div className="w-full max-w-[1600px] mx-auto animate-fadeIn flex flex-col gap-5">
      <CrudActionBar
        onAdd={() => setIsAddOpen(true)}
        onImport={() => setIsImportOpen(true)}
        onExport={handleExport}
      />
      
      <div className="bg-white border border-slate-200 rounded-2xl shadow-sm overflow-hidden">
        <CrudTable
          data={users}
          onView={(u) => setViewingUser(u as User)}
          onEdit={(u) => setEditingUser(u as User)}
          onDelete={(u) => setDeletingUser(u as User)}
          extraActions={(u) => [
            {
              title: <KeyRound size={16} />,
              action: () => alert(`Password reset email sent to ${(u as User).email}`),
              style: "w-8 h-8 bg-amber-50 text-amber-600 hover:bg-amber-100 rounded-lg transition-colors"
            }
          ]}
        />
      </div>

      <ViewModal isOpen={!!viewingUser} onClose={() => setViewingUser(null)} title="User Details" data={viewingUser} />
      <CrudFormModal isOpen={isAddOpen} onClose={() => setIsAddOpen(false)} onSave={handleSaveAdd} title="Add New User" fields={fields} />
      <CrudFormModal isOpen={!!editingUser} onClose={() => setEditingUser(null)} onSave={handleSaveEdit} title="Edit User" fields={fields} initialData={editingUser} />
      <ImportModal isOpen={isImportOpen} onClose={() => setIsImportOpen(false)} onImport={() => alert("File imported!")} />
      <ConfirmModal isOpen={!!deletingUser} onClose={() => setDeletingUser(null)} onConfirm={handleDelete} title="Delete User" message={`Are you sure you want to disable user "${deletingUser?.username}"?`} />
    </div>
  );
}
