import React from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { useTasks } from '../context/TaskContext';
import { ArrowLeft, Save, PlusCircle, Sparkles } from 'lucide-react';

export const TaskForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { tasks, addTask, updateTask } = useTasks();

  const isEditMode = !!id;
  const existingTask = isEditMode ? tasks.find((t) => t.id === id) : null;

  // Form validation schema
  const validationSchema = Yup.object({
    name: Yup.string()
      .max(40, 'Task name cannot exceed 40 characters')
      .required('Task name is required'),
    description: Yup.string()
      .max(200, 'Description cannot exceed 200 characters')
      .optional(),
    priority: Yup.string().oneOf(['low', 'medium', 'high']).required(),
    completed: Yup.boolean(),
  });

  // Formik configuration
  const formik = useFormik({
    initialValues: {
      name: existingTask?.name || '',
      description: existingTask?.description || '',
      priority: existingTask?.priority || 'medium',
      completed: existingTask?.completed || false,
    },
    enableReinitialize: true,
    validationSchema: validationSchema,
    onSubmit: (values) => {
      if (isEditMode && existingTask) {
        updateTask({
          ...existingTask,
          name: values.name.trim(),
          description: values.description.trim(),
          priority: values.priority,
          completed: values.completed,
        });
        navigate(`/tasks/${existingTask.id}`);
      } else {
        addTask(
          values.name.trim(),
          values.description.trim(),
          values.priority
        );
        navigate('/tasks');
      }
    },
  });

  return (
    <div className="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      {/* Back button */}
      <Link
        to={isEditMode ? `/tasks/${id}` : '/tasks'}
        className="inline-flex items-center gap-2 text-sm font-semibold text-slate-500 hover:text-indigo-600 transition-colors mb-6 group"
      >
        <ArrowLeft className="w-4 h-4 transition-transform group-hover:-translate-x-1" />
        <span>Cancel and Go Back</span>
      </Link>

      {/* Form Card */}
      <div className="bg-white border border-slate-200 rounded-3xl p-6 md:p-8 shadow-sm">
        <div className="flex items-center gap-3 pb-6 border-b border-slate-100 mb-6">
          <div className="p-2.5 bg-indigo-50 text-indigo-600 rounded-xl">
            <Sparkles className="w-5 h-5" />
          </div>
          <div>
            <h2 className="text-xl font-bold text-slate-900 my-0">
              {isEditMode ? 'Edit Task Details' : 'Create New Task'}
            </h2>
            <p className="text-sm text-slate-400 mt-1">
              {isEditMode ? 'Modify task parameters and save changes.' : 'Add a new item to your task dashboard.'}
            </p>
          </div>
        </div>

        <form onSubmit={formik.handleSubmit} className="space-y-6">
          {/* Name Field */}
          <div>
            <div className="flex items-center justify-between mb-1.5">
              <label htmlFor="name" className="block text-sm font-semibold text-slate-700">
                Task Name <span className="text-rose-500">*</span>
              </label>
              <span className={`text-[11px] font-medium ${formik.values.name.length > 40 ? 'text-rose-500 font-bold' : 'text-slate-400'}`}>
                {formik.values.name.length} / 40 characters
              </span>
            </div>
            <input
              type="text"
              id="name"
              name="name"
              placeholder="e.g. Design homepage wireframe"
              value={formik.values.name}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              className={`w-full px-4 py-2.5 bg-slate-50 border focus:bg-white focus:ring-2 rounded-xl text-sm outline-none transition-all ${
                formik.touched.name && formik.errors.name
                  ? 'border-rose-300 focus:border-rose-500 focus:ring-rose-500/20'
                  : 'border-slate-200 focus:border-indigo-500 focus:ring-indigo-500/20'
              }`}
            />
            {formik.touched.name && formik.errors.name ? (
              <p className="mt-1.5 text-xs font-semibold text-rose-500">{formik.errors.name}</p>
            ) : null}
          </div>

          {/* Description Field */}
          <div>
            <div className="flex items-center justify-between mb-1.5">
              <label htmlFor="description" className="block text-sm font-semibold text-slate-700">
                Description <span className="text-slate-400 font-normal">(optional)</span>
              </label>
              <span className={`text-[11px] font-medium ${formik.values.description.length > 200 ? 'text-rose-500 font-bold' : 'text-slate-400'}`}>
                {formik.values.description.length} / 200 characters
              </span>
            </div>
            <textarea
              id="description"
              name="description"
              rows={4}
              placeholder="Add more details about this task..."
              value={formik.values.description}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              className={`w-full px-4 py-2.5 bg-slate-50 border focus:bg-white focus:ring-2 rounded-xl text-sm outline-none transition-all resize-none ${
                formik.touched.description && formik.errors.description
                  ? 'border-rose-300 focus:border-rose-500 focus:ring-rose-500/20'
                  : 'border-slate-200 focus:border-indigo-500 focus:ring-indigo-500/20'
              }`}
            />
            {formik.touched.description && formik.errors.description ? (
              <p className="mt-1.5 text-xs font-semibold text-rose-500">{formik.errors.description}</p>
            ) : null}
          </div>

          {/* Grid for Priority and Status */}
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
            {/* Priority level */}
            <div>
              <label htmlFor="priority" className="block text-sm font-semibold text-slate-700 mb-1.5">
                Priority Level
              </label>
              <select
                id="priority"
                name="priority"
                value={formik.values.priority}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
                className="w-full px-4 py-2.5 bg-slate-50 border border-slate-200 focus:bg-white focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 rounded-xl text-sm outline-none transition-all cursor-pointer font-medium text-slate-700"
              >
                <option value="low">Low Priority</option>
                <option value="medium">Medium Priority</option>
                <option value="high">High Priority</option>
              </select>
            </div>

            {/* Status (Only in Edit Mode) */}
            {isEditMode ? (
              <div>
                <label className="block text-sm font-semibold text-slate-700 mb-2">
                  Status
                </label>
                <label className="inline-flex items-center gap-3 cursor-pointer select-none">
                  <div className="relative">
                    <input
                      type="checkbox"
                      name="completed"
                      checked={formik.values.completed}
                      onChange={formik.handleChange}
                      className="sr-only peer"
                    />
                    <div className="w-10 h-6 bg-slate-200 peer-focus:outline-none peer-focus:ring-2 peer-focus:ring-indigo-500/20 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-slate-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-emerald-500"></div>
                  </div>
                  <span className={`text-sm font-semibold transition-colors ${formik.values.completed ? 'text-emerald-600' : 'text-slate-500'}`}>
                    {formik.values.completed ? 'Completed' : 'Pending Action'}
                  </span>
                </label>
              </div>
            ) : null}
          </div>

          {/* Form Actions */}
          <div className="flex gap-3 pt-6 border-t border-slate-100">
            <button
              type="submit"
              disabled={!formik.isValid || formik.isSubmitting}
              className="flex-1 flex items-center justify-center gap-2 px-4 py-3 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl text-sm font-semibold shadow-md transition-all hover:scale-[1.01] cursor-pointer disabled:opacity-50 disabled:pointer-events-none"
            >
              {isEditMode ? <Save className="w-4 h-4" /> : <PlusCircle className="w-4 h-4" />}
              <span>{isEditMode ? 'Save Changes' : 'Create Task'}</span>
            </button>
            <Link
              to={isEditMode ? `/tasks/${id}` : '/tasks'}
              className="px-6 py-3 bg-slate-50 hover:bg-slate-100 text-slate-700 border border-slate-200 rounded-xl text-sm font-semibold transition-all"
            >
              Cancel
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
};
