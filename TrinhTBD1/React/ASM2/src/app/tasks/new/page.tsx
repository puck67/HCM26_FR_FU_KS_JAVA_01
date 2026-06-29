'use client';

import React, { useState } from 'react';
import { useFormik } from 'formik';
import Link from 'next/link';
import { createTask } from '@/app/actions/taskActions';
import { GenericCard } from '@/components/generic/GenericCard';
import { GenericFormField } from '@/components/generic/GenericFormField';
import { GenericButton } from '@/components/generic/GenericButton';
import { GenericToolbar } from '@/components/generic/GenericToolbar';

interface FormValues {
  name: string;
  description: string;
}

export default function NewTaskPage() {
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
      name: '',
      description: '',
    },
    validate,
    onSubmit: async (values) => {
      setServerError(null);
      setIsSubmitting(true);
      try {
        const formData = new FormData();
        formData.append('name', values.name.trim());
        formData.append('description', values.description ? values.description.trim() : '');

        const result = await createTask(formData);
        if (result && !result.success && result.error) {
          setServerError(result.error);
          setIsSubmitting(false);
        }
      } catch (err) {
        if (err && typeof err === 'object' && 'digest' in err && String(err.digest).startsWith('NEXT_REDIRECT')) {
          throw err;
        }
        console.error('Submit error:', err);
        setServerError('An unexpected error occurred. Please try again.');
        setIsSubmitting(false);
      }
    },
  });

  return (
    <div className="max-w-2xl mx-auto space-y-4">
      <GenericToolbar
        title="New Task"
        extraActions={
          <Link href="/tasks">
            <GenericButton variant="outline" size="md" label="Back to Tasks" />
          </Link>
        }
      />

      <GenericCard>
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
            placeholder="Enter task name"
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
            placeholder="Enter optional description..."
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
              label="Save Task"
              loading={isSubmitting}
            />
          </div>
        </form>
      </GenericCard>
    </div>
  );
}
