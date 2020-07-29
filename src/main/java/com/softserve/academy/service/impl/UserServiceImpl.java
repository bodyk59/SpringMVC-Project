package com.softserve.academy.service.impl;

import com.softserve.academy.dto.MarathonDto;
import com.softserve.academy.dto.UserDto;
import com.softserve.academy.exception.UserNotFoundException;
import com.softserve.academy.model.Marathon;
import com.softserve.academy.model.User;
import com.softserve.academy.repository.MarathonRepository;
import com.softserve.academy.repository.UserRepository;
import com.softserve.academy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Validation;
import java.util.HashSet;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository repository;

    @Autowired
    MarathonRepository marathonRepository;

    @Override
    public List<UserDto> getAll() {
        return repository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(toList());
    }

    @Override
    public List<UserDto> getAllFetchMarathons() {
        return null;
    }

    @Override
    public UserDto getUserByIdFetchMarathons(Long id) {
        return mapEntityToDtoFetchMarathons(repository.findById(id).orElseThrow(UserNotFoundException::new));
    }

    @Override
    public UserDto createOrUpdateUser(UserDto dto) {
        var entity = repository.findById(dto.getId()).orElse(new User());
        entity.getMarathons().clear();
        mapDtoToEntity(dto, entity);

        var validator = Validation.buildDefaultValidatorFactory().getValidator();
        var violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new RuntimeException(violations.toString());
        }

        return mapEntityToDto(repository.save(entity));
    }

    @Override
    public List<UserDto> getAllByRole(User.Role role) {
        return repository.findAllByRole(role).stream()
                .map(this::mapEntityToDto)
                .collect(toList());
    }

    @Override
    public boolean addUserToMarathon(UserDto user, Marathon marathon) {
        var optional = repository.findById(user.getId());
        if (optional.isEmpty()) {
            return false;
        }
        var foundUser = optional.get();

        foundUser.getMarathons().add(marathon);
        var foundMarathon = marathonRepository.getOne(marathon.getId());
        foundMarathon.getUsers().add(foundUser);
        repository.save(foundUser);
        return true;
    }

    @Override
    public void userDelete(Long id) {
        repository.deleteById(id);
    }

    private UserDto mapEntityToDtoFetchMarathons(User entity) {
        return new UserDto(entity.getId(), entity.getEmail(), entity.getFirstName(), entity.getLastName(), entity.getPassword(),
                entity.getRole(),
                entity.getMarathons().stream().map(m -> new MarathonDto(m.getId(), m.getTitle(), new HashSet<>())).collect(toSet())
        );
    }

    private UserDto mapEntityToDto(User entity) {
        return new UserDto(entity.getId(), entity.getEmail(), entity.getFirstName(), entity.getLastName(), entity.getPassword(),
                entity.getRole(),
                new HashSet<>()
        );
    }

    private void mapDtoToEntity(UserDto dto, User entity) {
        entity.setEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());
        entity.getMarathons().clear();
        dto.getMarathons().forEach(m -> {
            Marathon marathon = marathonRepository.getOne(m.getId());
            entity.getMarathons().add(marathon);
        });
    }
}
