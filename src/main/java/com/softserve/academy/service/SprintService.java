package com.softserve.academy.service;

import com.softserve.academy.model.Marathon;
import com.softserve.academy.model.Sprint;

import java.util.List;

public interface SprintService {
    List<Sprint> getSprintsByMarathonId(Long id);

    boolean addSprintToMarathon(Sprint sprint, Marathon marathon);

    boolean updateSprint(Sprint sprint);

    Sprint getSprintById(Long id);
}
