package com.softserve.academy.service.impl;

import com.softserve.academy.dto.MarathonDto;
import com.softserve.academy.dto.UserDto;
import com.softserve.academy.exception.MarathonNotFoundException;
import com.softserve.academy.model.Marathon;
import com.softserve.academy.model.User;
import com.softserve.academy.repository.MarathonRepository;
import com.softserve.academy.repository.UserRepository;
import com.softserve.academy.service.MarathonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Validation;
import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.by;

@Transactional
@Service
public class MarathonServiceImpl implements MarathonService {
    @Autowired
    MarathonRepository marathonRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<MarathonDto> getAll() {
        return marathonRepository.findAll(by(ASC, "title"))
                .stream()
                .map(this::mapModelToDto)
                .collect(toList());
    }

    @Override
    public MarathonDto getMarathonById(Long id) {
        return mapModelToDto(marathonRepository.findById(id)
                .orElseThrow(MarathonNotFoundException::new));
    }

    @Override
    public MarathonDto createOrUpdate(MarathonDto marathonDto) {
        Marathon marathon = marathonRepository.findById(marathonDto.getId()).orElse(new Marathon());
        marathon.getUsers().clear();
        mapDtoToModel(marathonDto, marathon);

        var validator = Validation.buildDefaultValidatorFactory().getValidator();
        var violations = validator.validate(marathon);
        if (!violations.isEmpty()) {
            throw new RuntimeException(violations.toString());
        }

        return mapModelToDto(marathonRepository.save(marathon));
    }

    @Override
    public void deleteMarathonById(Long id) {
        var marathon = marathonRepository.findById(id).orElseThrow(MarathonNotFoundException::new);
        marathonRepository.delete(marathon);
    }

    @Override
    public void removeFromMarathon(Long userId, Long marathonId) {
        var marathon = marathonRepository.findById(marathonId).orElseThrow(MarathonNotFoundException::new);
        marathon.getUsers().removeIf(u -> u.getId() == userId);
        userRepository.getOne(userId).getMarathons().remove(marathon);
        marathonRepository.save(marathon);
    }

    private MarathonDto mapModelToDto(Marathon marathon) {
        MarathonDto marathonDto = new MarathonDto();
        marathonDto.setId(marathon.getId());
        marathonDto.setTitle(marathon.getTitle());
        marathonDto.setUsers(marathon.getUsers().stream()
                .map(u -> new UserDto(
                        u.getId(), u.getEmail(), u.getFirstName(), u.getLastName(), u.getPassword(), u.getRole(), new ArrayList<>()
                ))
                .sorted(comparing(UserDto::getFirstName).thenComparing(UserDto::getLastName))
                .collect(toList())
        );
        return marathonDto;
    }

    private void mapDtoToModel(MarathonDto marathonDto, Marathon marathon) {
        marathon.setId(marathonDto.getId());
        marathon.setTitle(marathonDto.getTitle());
        marathonDto.getUsers().forEach(userDto -> {
            User user = userRepository.getOne(userDto.getId());
            marathon.getUsers().add(user);
        });
    }
}
