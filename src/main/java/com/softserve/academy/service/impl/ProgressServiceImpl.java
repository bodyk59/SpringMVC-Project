package com.softserve.academy.service.impl;

import com.softserve.academy.exception.ProgressNotFoundException;
import com.softserve.academy.model.Progress;
import com.softserve.academy.model.Task;
import com.softserve.academy.model.User;
import com.softserve.academy.repository.ProgressRepository;
import com.softserve.academy.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ProgressServiceImpl implements ProgressService {
    @Autowired
    ProgressRepository repository;

    @Override
    public Progress getProgressById(Long id) {
        return repository.findById(id).orElseThrow(ProgressNotFoundException::new);
    }

    @Override
    public Progress addTaskForStudent(Task task, User user) {
        var progress = new Progress(0, LocalDate.now(), LocalDate.now(), Task.TaskStatus.NEW, task, user);
        return repository.save(progress);
    }

    @Override
    public Progress addOrUpdateProgress(Progress progress) {
        return repository.save(progress);
    }

    @Override
    public boolean setStatus(Task.TaskStatus status, Progress progress) {
        var optional = repository.findById(progress.getId());
        if (optional.isPresent()) {
            var foundProgress = optional.get();
            foundProgress.setStatus(status);
            repository.save(foundProgress);
            return true;
        }
        return false;
    }

    @Override
    public List<Progress> allProgressByUserIdAndMarathonId(Long userId, Long marathonId) {
        return repository.findAllByUserIdAndMarathonId(userId, marathonId);
    }

    @Override
    public List<Progress> allProgressByUserIdAndSprintId(Long userId, Long sprintId) {
        return repository.findAllProgressByUserIdAndSprintId(userId, sprintId);
    }
}
