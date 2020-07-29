package com.softserve.academy.service;

import com.softserve.academy.model.Sprint;
import com.softserve.academy.model.Task;

public interface TaskService {
    Task addTaskToSprint(Task task, Sprint sprint);

    Task getTaskById(Long id);
}
