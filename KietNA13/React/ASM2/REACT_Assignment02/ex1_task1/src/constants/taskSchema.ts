import * as Yup from 'yup';
import { TASK_NAME_MAX, TASK_DESCRIPTION_MAX } from './validation';

export const TASK_SCHEMA = Yup.object({
  name: Yup.string()
    .required('Task name is required')
    .max(TASK_NAME_MAX, `Name must be ${TASK_NAME_MAX} characters or fewer`),
  description: Yup.string().max(
    TASK_DESCRIPTION_MAX,
    `Description must be ${TASK_DESCRIPTION_MAX} characters or fewer`,
  ),
});
