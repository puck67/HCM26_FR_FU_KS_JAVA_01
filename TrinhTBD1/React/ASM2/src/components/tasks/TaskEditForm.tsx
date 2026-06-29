'use client';

import React, { useState } from 'react';
import { useFormik } from 'formik';
import Link from 'next/link';
import { Task } from '@/types/task';
import { updateTask } from '@/app/actions/taskActions';
import { GenericCard } from '@/components/generic/GenericCard';
import { GenericFormField } from '@/components/generic/GenericFormField';
import { GenericButton } from '@/components/generic/GenericButton';
import { GenericBadge } from '@/components/generic/GenericBadge';

interface TaskEditFormProps {
  task: Task;
}

interface FormValues {
  name: string;
  description: string;
}

export const TaskEditForm: React.FC<TaskEditFormProps> = ({ task }) => {
  const [serverError, setServerError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const validate = (values: FormValues) => {
    const errors: Partial<FormValues> = {};

    const trimmedName = values.name ? values.name.trim() : '';
    if (!trimmedName) {
      errors.name = 'Task name is required';
    } else if (trimmedName.length > 40) {
      errors.name = 'Task name cannot exceed 40 characters';
    }

    if (values.description && values.description.length > 200) {
      errors.description = 'Description cannot exceed 200 characters';
    }

    return errors;
  };

  const formik = useFormik<FormValues>({
    initialValues: {
      name: task.name || '',
      description: task.description || '',
    },
    validate,
    onSubmit: async (values) => {
      setServerError(null);
      setIsSubmitting(true);
      try {
        const formData = new FormData();
        formData.append('name', values.name.trim());
        formData.append('description', values.description ? values.description.trim() : '');

        const result = await updateTask(task.id, formData);
        if (result && !result.success && result.error) {
          setServerError(result.error);
          setIsSubmitting(false);
        }
      } catch (err) {
        if (err && typeof err === 'object' && 'digest' in err && String(err.digest).startsWith('NEXT_REDIRECT')) {
          throw err;
        }
        console.error('Update error:', err);
        setServerError('An unexpected error occurred while updating.');
        setIsSubmitting(false);
      }
    },
  });

  return (
    <GenericCard title="Edit Task">
      <div className="mb-5 pb-3 border-b border-zinc-800 flex items-center justify-between">
        <span className="text-xs text-zinc-400 font-mono">ID: {task.id}</span>
        <GenericBadge
          label={task.completed ? 'Completed' : 'Pending'}
          variant={task.completed ? 'completed' : 'pending'}
        />
      </div>

      {serverError && (
        <div className="mb-4 p-3 bg-red-950/60 border border-red-800 text-red-300 rounded text-sm">
          {serverError}
        </div>
      )}

      <form onSubmit={formik.handleSubmit} noValidate>
        <GenericFormField
          id="name"
          name="name"
          label="Task Name"
          value={formik.values.name}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.errors.name}
          touched={formik.touched.name}
          required
          maxLength={40}
        />

        <GenericFormField
          id="description"
          name="description"
          label="Description"
          type="textarea"
          value={formik.values.description}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.errors.description}
          touched={formik.touched.description}
          maxLength={200}
        />

        <div className="mt-6 flex items-center justify-end gap-3 pt-3 border-t border-zinc-800">
          <Link href="/tasks">
            <GenericButton type="button" variant="outline" label="Cancel" disabled={isSubmitting} />
          </Link>
          <GenericButton
            type="submit"
            variant="primary"
            label="Update Task"
            loading={isSubmitting}
          />
        </div>
      </form>
    </GenericCard>
  );
};
