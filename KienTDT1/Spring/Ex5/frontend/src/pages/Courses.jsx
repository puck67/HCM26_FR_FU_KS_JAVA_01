import { useState, useEffect } from "react";
import { getCourses } from "../api/courseApi";
import { getUserInfo } from "../utils/auth";
import { Link } from "react-router-dom";
import Layout from "../components/Layout";

export default function Courses() {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const user = getUserInfo();
  const canManage = user?.role === "ADMIN" || user?.role === "TRAINER";

  useEffect(() => {
    getCourses().then((res) => {
      setCourses(res.data);
      setLoading(false);
    });
  }, []);

  return (
    <Layout>
      <div>
        <div className="flex justify-between items-center mb-8">
          <h1 className="dashboard-heading">Courses</h1>
          {canManage && (
            <Link
              to="/courses/manage"
              className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
            >
              Manage Courses
            </Link>
          )}
        </div>

        {loading ? (
          <p className="text-slate-600">Loading...</p>
        ) : (
          <div className="dashboard-grid">
            {courses.map((course) => (
              <div key={course.id} className="metric-card">
                <h2 className="metric-title">{course.courseName}</h2>
                <p className="text-lg font-semibold text-slate-900">{course.duration}</p>
                <p className="mt-3 text-sm text-slate-600">Instructor: {course.instructor}</p>
              </div>
            ))}
          </div>
        )}

        {!loading && courses.length === 0 && (
          <p className="text-slate-600">No courses available.</p>
        )}
      </div>
    </Layout>
  );
}
