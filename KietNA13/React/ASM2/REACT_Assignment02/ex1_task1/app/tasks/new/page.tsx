import { createTask } from '@/lib/actions';
import TaskForm from '@/components/TaskForm';
import { ROUTES } from '@/constants/routes';

export default function NewTaskPage() {
  return <TaskForm onSubmit={createTask} cancelHref={ROUTES.TASKS} />;
}
