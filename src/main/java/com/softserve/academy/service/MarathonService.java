package com.softserve.academy.service;

import com.softserve.academy.dto.MarathonDto;

import java.util.List;

public interface MarathonService {
    List<MarathonDto> getAll();

    MarathonDto getMarathonById(Long id);

    MarathonDto createOrUpdate(MarathonDto marathonDto);

    void deleteMarathonById(Long id);

    void removeFromMarathon(Long userId, Long marathonId);
}
