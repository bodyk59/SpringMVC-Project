package com.softserve.academy.service.impl;

import com.softserve.academy.model.Marathon;
import com.softserve.academy.model.Sprint;
import com.softserve.academy.repository.SprintRepository;
import com.softserve.academy.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Validation;
import java.util.List;

@Service
@Transactional
public class SprintServiceImpl implements SprintService {
    @Autowired
    SprintRepository repository;

    @Override
    public List<Sprint> getSprintsByMarathonId(Long id) {
        return repository.findAllByMarathonId(id);
    }

    @Override
    public boolean addSprintToMarathon(Sprint sprint, Marathon marathon) {
        var validator = Validation.buildDefaultValidatorFactory().getValidator();
        var violations = validator.validate(sprint);
        if (!violations.isEmpty()) {
            throw new RuntimeException(violations.toString());
        }
        marathon.getSprints().add(sprint);
        sprint.setMarathon(marathon);
        repository.save(sprint);
        return true;
    }

    @Override
    public boolean updateSprint(Sprint sprint) {
        if (repository.findById(sprint.getId()).isPresent()) {
            repository.save(sprint);
            return true;
        }
        return true;
    }

    @Override
    public Sprint getSprintById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
