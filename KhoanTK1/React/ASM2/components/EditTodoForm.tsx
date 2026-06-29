"use client";

import TodoForm from "@/components/TodoForm";
import { updateTodo } from "@/lib/actions";
import { Todo } from "@/types/todo";

interface EditTodoFormProps {
    todo: Todo;
}

const EditTodoForm = ({ todo }: EditTodoFormProps) => {
    const handleSubmit = async (formData: FormData) => {
        await updateTodo(todo.id, formData);
    };

    return (
        <TodoForm
            mode="edit"
            initialValues={{ name: todo.name, description: todo.description }}
            onSubmit={handleSubmit}
        />
    );
};

export default EditTodoForm;
