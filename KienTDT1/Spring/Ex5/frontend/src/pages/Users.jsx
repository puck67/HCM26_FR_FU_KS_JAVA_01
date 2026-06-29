import { useState, useEffect } from "react";
import { getUsers } from "../api/userApi";
import { getUserInfo } from "../utils/auth";
import Layout from "../components/Layout";

export default function Users() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const user = getUserInfo();

  useEffect(() => {
    getUsers().then((res) => {
      setUsers(res.data);
      setLoading(false);
    });
  }, []);

  if (user?.role !== "ADMIN") {
    return (
      <Layout>
        <div>
          <h1 className="dashboard-heading">Users</h1>
          <p className="text-slate-600 mt-4">Only ADMIN users can view the user list.</p>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <div>
        <h1 className="dashboard-heading">Users</h1>

        {loading ? (
          <p className="text-slate-600">Loading...</p>
        ) : (
          <div className="dashboard-grid">
            {users.map((item) => (
              <div key={item.id} className="metric-card">
                <h2 className="metric-title">{item.name}</h2>
                <p className="text-lg font-semibold text-slate-900">{item.role}</p>
                <p className="mt-3 text-sm text-slate-600">{item.email}</p>
              </div>
            ))}
          </div>
        )}
      </div>
    </Layout>
  );
}
