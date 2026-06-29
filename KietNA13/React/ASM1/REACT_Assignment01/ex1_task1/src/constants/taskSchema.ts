import * as Yup from 'yup';

export const MAX_NAME_LENGTH = 40;
export const MAX_DESCRIPTION_LENGTH = 200;

export const TASK_SCHEMA = Yup.object({
  name: Yup.string()
    .required('Name is required')
    .max(MAX_NAME_LENGTH, `Name must be at most ${MAX_NAME_LENGTH} characters`),
  description: Yup.string().max(
    MAX_DESCRIPTION_LENGTH,
    `Description must be at most ${MAX_DESCRIPTION_LENGTH} characters`,
  ),
});
