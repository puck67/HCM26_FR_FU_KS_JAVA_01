import { useState } from "react";
import { createCourse, updateCourse, deleteCourse, getCourses } from "../api/courseApi";
import { getUserInfo } from "../utils/auth";
import Layout from "../components/Layout";

export default function CoursesManage() {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    id: null,
    courseName: "",
    duration: "",
    instructor: "",
  });

  const user = getUserInfo();

  const loadCourses = () => {
    getCourses().then((res) => {
      setCourses(res.data);
      setLoading(false);
    });
  };

  const handleAddCourse = async () => {
    if (!formData.courseName || !formData.duration || !formData.instructor) {
      alert("Please fill in all fields");
      return;
    }

    await createCourse({
      courseName: formData.courseName,
      duration: formData.duration,
      instructor: formData.instructor,
    });

    setFormData({ id: null, courseName: "", duration: "", instructor: "" });
    setShowForm(false);
    loadCourses();
  };

  const handleEditCourse = (course) => {
    setFormData(course);
    setShowForm(true);
  };

  const handleUpdateCourse = async () => {
    if (!formData.courseName || !formData.duration || !formData.instructor) {
      alert("Please fill in all fields");
      return;
    }

    await updateCourse(formData.id, {
      courseName: formData.courseName,
      duration: formData.duration,
      instructor: formData.instructor,
    });

    setFormData({ id: null, courseName: "", duration: "", instructor: "" });
    setShowForm(false);
    loadCourses();
  };

  const handleDeleteCourse = async (id) => {
    if (window.confirm("Are you sure you want to delete this course?")) {
      await deleteCourse(id);
      loadCourses();
    }
  };

  if (user?.role !== "ADMIN" && user?.role !== "TRAINER") {
    return (
      <Layout>
        <div>
          <h1 className="dashboard-heading">Manage Courses</h1>
          <p className="text-slate-600 mt-4">Only ADMIN and TRAINER can manage courses.</p>
        </div>
      </Layout>
    );
  }

  if (loading) {
    loadCourses();
  }

  return (
    <Layout>
      <div>
        <div className="flex justify-between items-center mb-8">
          <h1 className="dashboard-heading">Manage Courses</h1>
          <button
            onClick={() => {
              setFormData({ id: null, courseName: "", duration: "", instructor: "" });
              setShowForm(!showForm);
            }}
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          >
            {showForm ? "Cancel" : "+ Add Course"}
          </button>
        </div>

        {showForm && (
          <div className="bg-white p-6 rounded-lg shadow mb-8">
            <h2 className="text-xl font-bold mb-4">
              {formData.id ? "Edit Course" : "Add New Course"}
            </h2>

            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-slate-700">Course Name</label>
                <input
                  type="text"
                  value={formData.courseName}
                  onChange={(e) => setFormData({ ...formData, courseName: e.target.value })}
                  className="login-input mt-2"
                  placeholder="Enter course name"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-slate-700">Duration</label>
                <input
                  type="text"
                  value={formData.duration}
                  onChange={(e) => setFormData({ ...formData, duration: e.target.value })}
                  className="login-input mt-2"
                  placeholder="e.g., 4 weeks"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-slate-700">Instructor</label>
                <input
                  type="text"
                  value={formData.instructor}
                  onChange={(e) => setFormData({ ...formData, instructor: e.target.value })}
                  className="login-input mt-2"
                  placeholder="Enter instructor name"
                />
              </div>

              <div className="flex gap-3 pt-4">
                <button
                  onClick={formData.id ? handleUpdateCourse : handleAddCourse}
                  className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
                >
                  {formData.id ? "Update Course" : "Add Course"}
                </button>
                <button
                  onClick={() => {
                    setShowForm(false);
                    setFormData({ id: null, courseName: "", duration: "", instructor: "" });
                  }}
                  className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
                >
                  Clear
                </button>
              </div>
            </div>
          </div>
        )}

        <div className="overflow-x-auto">
          <table className="w-full border-collapse border border-gray-300">
            <thead>
              <tr className="bg-gray-200">
                <th className="border border-gray-300 p-3 text-left">ID</th>
                <th className="border border-gray-300 p-3 text-left">Course Name</th>
                <th className="border border-gray-300 p-3 text-left">Duration</th>
                <th className="border border-gray-300 p-3 text-left">Instructor</th>
                <th className="border border-gray-300 p-3 text-center">Actions</th>
              </tr>
            </thead>
            <tbody>
              {courses.map((course) => (
                <tr key={course.id} className="hover:bg-gray-50">
                  <td className="border border-gray-300 p-3">{course.id}</td>
                  <td className="border border-gray-300 p-3">{course.courseName}</td>
                  <td className="border border-gray-300 p-3">{course.duration}</td>
                  <td className="border border-gray-300 p-3">{course.instructor}</td>
                  <td className="border border-gray-300 p-3 text-center space-x-2">
                    <button
                      onClick={() => handleEditCourse(course)}
                      className="bg-yellow-500 text-white px-3 py-1 rounded hover:bg-yellow-600 text-sm"
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => handleDeleteCourse(course.id)}
                      className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 text-sm"
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {courses.length === 0 && !loading && (
          <p className="text-slate-600 mt-4">No courses found.</p>
        )}
      </div>
    </Layout>
  );
}
