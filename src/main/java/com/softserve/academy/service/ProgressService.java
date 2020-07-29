package com.softserve.academy.service;

import com.softserve.academy.model.Progress;
import com.softserve.academy.model.Task;
import com.softserve.academy.model.User;

import java.util.List;

public interface ProgressService {
    Progress getProgressById(Long id);

    Progress addTaskForStudent(Task task, User user);

    Progress addOrUpdateProgress(Progress progress);

    boolean setStatus(Task.TaskStatus status, Progress progress);

    List<Progress> allProgressByUserIdAndMarathonId(Long userId, Long marathonId);

    List<Progress> allProgressByUserIdAndSprintId(Long userId, Long sprintId);
}
