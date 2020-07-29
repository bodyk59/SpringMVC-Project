package com.softserve.academy.service.impl;

import com.softserve.academy.exception.TaskNotFoundException;
import com.softserve.academy.model.Sprint;
import com.softserve.academy.model.Task;
import com.softserve.academy.repository.TaskRepository;
import com.softserve.academy.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Validation;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskRepository repository;

    @Override
    public Task addTaskToSprint(Task task, Sprint sprint) {
        var validator = Validation.buildDefaultValidatorFactory().getValidator();
        var violations = validator.validate(task);
        if (!violations.isEmpty()) {
            throw new RuntimeException(violations.toString());
        }
        task.setSprint(sprint);
        return repository.save(task);
    }

    @Override
    public Task getTaskById(Long id) {
        return repository.findById(id).orElseThrow(TaskNotFoundException::new);
    }
}
