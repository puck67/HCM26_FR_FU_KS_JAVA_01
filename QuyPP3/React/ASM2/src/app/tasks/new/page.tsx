"use client";

import TaskFormClient from "@/components/TaskFormClient";
import { createTask } from "@/app/actions/taskActions";

export default function NewTaskPage() {
  return (
    <TaskFormClient 
      title="Create New Assignment" 
      onSubmitAction={createTask} 
    />
  );
}
