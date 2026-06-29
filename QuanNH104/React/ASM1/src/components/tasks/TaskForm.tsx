import React from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import type { Task } from '../../types';
import { Button } from '../shared/Button';

interface TaskFormProps {
  initialValues?: Omit<Task, 'id' | 'createdAt'>;
  onSubmit: (values: Omit<Task, 'id' | 'createdAt'>) => Promise<void>;
  onCancel: () => void;
  submitLabel?: string;
}

const validationSchema = Yup.object().shape({
  name: Yup.string()
    .required('Task name is required')
    .max(40, 'Task name must be 40 characters or less'),
  description: Yup.string()
    .max(200, 'Description must be 200 characters or less')
    .optional(),
  status: Yup.string()
    .oneOf(['Pending', 'In Progress', 'Completed'])
    .required('Status is required'),
  priority: Yup.string()
    .oneOf(['Low', 'Medium', 'High'])
    .required('Priority is required'),
});

const defaultInitialValues: Omit<Task, 'id' | 'createdAt'> = {
  name: '',
  description: '',
  status: 'Pending',
  priority: 'Medium',
};

export const TaskForm: React.FC<TaskFormProps> = ({
  initialValues = defaultInitialValues,
  onSubmit,
  onCancel,
  submitLabel = 'Save Task',
}) => {
  return (
    <Formik
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={async (values, { setSubmitting }) => {
        try {
          await onSubmit(values);
        } catch (error) {
          // Handle submission error if any (handled in page context)
        } finally {
          setSubmitting(false);
        }
      }}
    >
      {({ isSubmitting, errors, touched }) => (
        <Form className="space-y-5">
          {/* Task Name */}
          <div>
            <label htmlFor="name" className="block text-xs font-semibold text-[#94a3b8] uppercase tracking-wider mb-2">
              Task Name <span className="text-cyber-danger">*</span>
            </label>
            <Field
              type="text"
              name="name"
              id="name"
              placeholder="e.g. Design Landing Page"
              className={`w-full bg-white/5 border rounded-lg px-4 py-2.5 text-white placeholder-gray-500 focus:outline-none transition-all duration-200 ${
                errors.name && touched.name
                  ? 'border-cyber-danger focus:ring-1 focus:ring-cyber-danger/30'
                  : 'border-white/10 focus:border-cyber-cyan focus:ring-1 focus:ring-cyber-cyan/30'
              }`}
            />
            <ErrorMessage name="name" component="div" className="text-cyber-danger text-xs mt-1.5 font-medium" />
          </div>

          {/* Description */}
          <div>
            <label htmlFor="description" className="block text-xs font-semibold text-[#94a3b8] uppercase tracking-wider mb-2">
              Description <span className="text-gray-500">(Optional)</span>
            </label>
            <Field
              as="textarea"
              name="description"
              id="description"
              placeholder="Provide a brief task description..."
              rows={3}
              className={`w-full bg-white/5 border rounded-lg px-4 py-2.5 text-white placeholder-gray-500 focus:outline-none transition-all duration-200 resize-none ${
                errors.description && touched.description
                  ? 'border-cyber-danger focus:ring-1 focus:ring-cyber-danger/30'
                  : 'border-white/10 focus:border-cyber-cyan focus:ring-1 focus:ring-cyber-cyan/30'
              }`}
            />
            <ErrorMessage name="description" component="div" className="text-cyber-danger text-xs mt-1.5 font-medium" />
          </div>

          {/* Status & Priority Row */}
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            {/* Status */}
            <div>
              <label htmlFor="status" className="block text-xs font-semibold text-[#94a3b8] uppercase tracking-wider mb-2">
                Status
              </label>
              <Field
                as="select"
                name="status"
                id="status"
                className="w-full bg-[#11141c] border border-white/10 rounded-lg px-4 py-2.5 text-white focus:outline-none focus:border-cyber-cyan focus:ring-1 focus:ring-cyber-cyan/30 transition-all duration-200"
              >
                <option value="Pending">Pending</option>
                <option value="In Progress">In Progress</option>
                <option value="Completed">Completed</option>
              </Field>
            </div>

            {/* Priority */}
            <div>
              <label htmlFor="priority" className="block text-xs font-semibold text-[#94a3b8] uppercase tracking-wider mb-2">
                Priority
              </label>
              <Field
                as="select"
                name="priority"
                id="priority"
                className="w-full bg-[#11141c] border border-white/10 rounded-lg px-4 py-2.5 text-white focus:outline-none focus:border-cyber-cyan focus:ring-1 focus:ring-cyber-cyan/30 transition-all duration-200"
              >
                <option value="Low">Low</option>
                <option value="Medium">Medium</option>
                <option value="High">High</option>
              </Field>
            </div>
          </div>

          {/* Form Actions */}
          <div className="flex justify-end gap-3 pt-4 border-t border-white/5">
            <Button
              type="button"
              variant="secondary"
              onClick={onCancel}
              disabled={isSubmitting}
            >
              Cancel
            </Button>
            <Button
              type="submit"
              variant="primary"
              isLoading={isSubmitting}
            >
              {submitLabel}
            </Button>
          </div>
        </Form>
      )}
    </Formik>
  );
};
