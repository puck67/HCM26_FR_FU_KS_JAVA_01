package com.example.demo.service;

import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl extends GenericServiceImpl<Task, Long, TaskRepository> implements TaskService {
    public TaskServiceImpl(TaskRepository repository) {
        super(repository);
    }
}
