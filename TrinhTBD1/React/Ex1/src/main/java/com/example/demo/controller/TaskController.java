package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tasks")
public class TaskController extends GenericCrudController<Task, Long> {
    public TaskController(TaskService service) {
        super(service, Task.class, "tasks", "Task Management", "Add Task");
    }
}
