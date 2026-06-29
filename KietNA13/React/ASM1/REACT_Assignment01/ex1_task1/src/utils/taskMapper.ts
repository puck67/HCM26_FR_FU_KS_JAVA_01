import type { JsonPlaceholderTodo, Task } from '../types/task';

export const mapTodoToTask = (todo: JsonPlaceholderTodo): Task => ({
  id: todo.id,
  name: todo.title,
  completed: todo.completed,
  userId: todo.userId,
});

export const mapTodosToTasks = (todos: JsonPlaceholderTodo[]): Task[] =>
  todos.map(mapTodoToTask);
