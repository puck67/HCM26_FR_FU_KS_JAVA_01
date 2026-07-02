import { Formik, Form, Field, ErrorMessage } from "formik";
import { useTasks, type Task } from "../context/TaskContext";

export function TaskForm() {
  const { selectedTask, setSelectedTask, setIsFormOpen, dispatch } = useTasks();

  const initialValues = selectedTask || { name: "", description: "" };

  const validate = (values: { name: string; description: string }) => {
    const errors: Record<string, string> = {};
    if (!values.name.trim()) {
      errors.name = "Task name is required";
    } else if (values.name.length > 40) {
      errors.name = "Must be 40 characters or less";
    }

    if (values.description && values.description.length > 200) {
      errors.description = "Must be 200 characters or less";
    }
    return errors;
  };

  return (
    <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-xl shadow-xl p-6 w-full max-w-md border border-slate-100">
        <h3 className="text-xl font-bold text-slate-800 mb-4">
          {selectedTask ? "Modify Existing Task" : "Create New Assignment"}
        </h3>

        <Formik
          initialValues={initialValues}
          validate={validate}
          onSubmit={(values, { resetForm }) => {
            if (selectedTask) {
              dispatch({ type: "UPDATE_TASK", payload: { ...selectedTask, ...values } as Task });
            } else {
              dispatch({
                type: "ADD_TASK",
                payload: { id: Date.now().toString(), ...values } as Task,
              });
            }
            resetForm();
            setSelectedTask(null);
            setIsFormOpen(false);
          }}
        >
          {({ isSubmitting }) => (
            <Form className="space-y-4">
              <div>
                <label className="block text-sm font-semibold text-slate-700 mb-1">Task Title *</label>
                <Field
                  name="name"
                  type="text"
                  className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                <ErrorMessage name="name" component="div" className="text-rose-500 text-xs mt-1 font-medium" />
              </div>

              <div>
                <label className="block text-sm font-semibold text-slate-700 mb-1">Description</label>
                <Field
                  name="description"
                  as="textarea"
                  rows={4}
                  className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                <ErrorMessage name="description" component="div" className="text-rose-500 text-xs mt-1 font-medium" />
              </div>

              <div className="flex justify-end gap-2 pt-2">
                <button
                  type="button"
                  onClick={() => {
                    setSelectedTask(null);
                    setIsFormOpen(false);
                  }}
                  className="px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 font-medium rounded-lg transition-colors text-sm"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={isSubmitting}
                  className="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg transition-colors text-sm shadow-sm"
                >
                  Save
                </button>
              </div>
            </Form>
          )}
        </Formik>
      </div>
    </div>
  );
}
