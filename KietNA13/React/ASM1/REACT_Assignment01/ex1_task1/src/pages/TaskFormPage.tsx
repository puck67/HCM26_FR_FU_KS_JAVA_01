import { useParams, useNavigate, Link } from 'react-router-dom';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import { useTask } from '../hooks/useTask';
import { useTaskMutation } from '../hooks/useTaskMutation';
import { Skeleton } from '../components/Skeleton';
import { Button } from '../components/Button';
import {
  TASK_SCHEMA,
  MAX_NAME_LENGTH,
  MAX_DESCRIPTION_LENGTH,
} from '../constants/taskSchema';
import { ROUTES } from '../constants/routes';
import type { TaskFormValues, Task } from '../types/task';

const EMPTY_FORM_VALUES: TaskFormValues = {
  name: '',
  description: '',
};

const TaskFormPage = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const isEdit = id !== undefined;
  const taskId = Number(id);

  const { data: task, isLoading } = useTask(isEdit ? taskId : 0);
  const { createMutation, updateMutation } = useTaskMutation();

  const initialValues: TaskFormValues =
    isEdit && task
      ? { name: task.name, description: task.description ?? '' }
      : EMPTY_FORM_VALUES;

  const handleSubmit = (values: TaskFormValues) => {
    if (isEdit && task) {
      const updated: Task = { ...task, ...values, description: values.description || undefined };
      updateMutation.mutate(updated, {
        onSuccess: () => navigate(ROUTES.TASK_DETAIL(task.id)),
      });
    } else {
      createMutation.mutate(values, {
        onSuccess: () => navigate(ROUTES.TASKS),
      });
    }
  };

  if (isEdit && isLoading) {
    return (
      <main className="mx-auto max-w-2xl px-4 py-8 sm:px-6 lg:px-8">
        <Skeleton className="h-4 w-24" />
        <Skeleton className="mt-4 h-8 w-40" />
        <div className="mt-6 rounded-xl border border-gray-200 bg-white p-6 shadow-sm space-y-4">
          <Skeleton className="h-4 w-16" />
          <Skeleton className="h-10 w-full rounded-lg" />
          <Skeleton className="h-4 w-24" />
          <Skeleton className="h-24 w-full rounded-lg" />
          <div className="flex gap-3 pt-2">
            <Skeleton className="h-10 w-32 rounded-lg" />
            <Skeleton className="h-10 w-24 rounded-lg" />
          </div>
        </div>
      </main>
    );
  }

  return (
    <main className="mx-auto max-w-2xl px-4 py-8 sm:px-6 lg:px-8">
      <Link
        to={isEdit ? ROUTES.TASK_DETAIL(taskId) : ROUTES.TASKS}
        className="text-sm font-medium text-indigo-600 hover:underline focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-indigo-500 rounded"
      >
        ← {isEdit ? 'Back to Task' : 'Back to Tasks'}
      </Link>

      <h1 className="mt-4 text-2xl font-bold text-gray-900">
        {isEdit ? 'Edit Task' : 'New Task'}
      </h1>

      <div className="mt-6 rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
        <Formik
          initialValues={initialValues}
          validationSchema={TASK_SCHEMA}
          onSubmit={handleSubmit}
          enableReinitialize
        >
          {({ values }) => (
            <Form noValidate className="space-y-5">
              <div>
                <label
                  htmlFor="task-name"
                  className="block text-sm font-medium text-gray-700"
                >
                  Name{' '}
                  <span className="text-red-500" aria-hidden="true">
                    *
                  </span>
                </label>
                <div className="relative mt-1">
                  <Field
                    id="task-name"
                    name="name"
                    type="text"
                    placeholder="Enter task name"
                    maxLength={MAX_NAME_LENGTH}
                    className="block w-full rounded-lg border border-gray-300 px-3 py-2 pr-16 text-sm shadow-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500"
                    aria-required="true"
                    aria-describedby="name-error name-count"
                  />
                  <span
                    id="name-count"
                    className="pointer-events-none absolute right-3 top-2 text-xs text-gray-400"
                    aria-live="polite"
                  >
                    {values.name.length}/{MAX_NAME_LENGTH}
                  </span>
                </div>
                <ErrorMessage
                  id="name-error"
                  name="name"
                  component="p"
                  className="mt-1 text-xs text-red-600"
                />
              </div>

              <div>
                <label
                  htmlFor="task-description"
                  className="block text-sm font-medium text-gray-700"
                >
                  Description{' '}
                  <span className="text-xs font-normal text-gray-400">
                    (optional)
                  </span>
                </label>
                <div className="relative mt-1">
                  <Field
                    id="task-description"
                    name="description"
                    as="textarea"
                    rows={4}
                    placeholder="Enter task description"
                    maxLength={MAX_DESCRIPTION_LENGTH}
                    className="block w-full rounded-lg border border-gray-300 px-3 py-2 pb-6 text-sm shadow-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500 resize-none"
                    aria-describedby="description-error description-count"
                  />
                  <span
                    id="description-count"
                    className="pointer-events-none absolute bottom-2 right-3 text-xs text-gray-400"
                    aria-live="polite"
                  >
                    {(values.description ?? '').length}/{MAX_DESCRIPTION_LENGTH}
                  </span>
                </div>
                <ErrorMessage
                  id="description-error"
                  name="description"
                  component="p"
                  className="mt-1 text-xs text-red-600"
                />
              </div>

              <div className="flex gap-3 pt-2">
                <Button
                  type="submit"
                  isLoading={createMutation.isPending || updateMutation.isPending}
                  aria-label={isEdit ? 'Save task changes' : 'Create new task'}
                >
                  {isEdit ? 'Save Changes' : 'Create Task'}
                </Button>
                <Button
                  type="button"
                  variant="secondary"
                  onClick={() =>
                    navigate(
                      isEdit ? ROUTES.TASK_DETAIL(taskId) : ROUTES.TASKS,
                    )
                  }
                >
                  Cancel
                </Button>
              </div>
            </Form>
          )}
        </Formik>
      </div>
    </main>
  );
};

export default TaskFormPage;
