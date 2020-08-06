package com.softserve.academy.service.impl;

import com.softserve.academy.dto.MarathonDto;
import com.softserve.academy.dto.UserDto;
import com.softserve.academy.exception.UserNotFoundException;
import com.softserve.academy.model.Marathon;
import com.softserve.academy.model.User;
import com.softserve.academy.repository.MarathonRepository;
import com.softserve.academy.repository.UserRepository;
import com.softserve.academy.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Validation;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.by;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository repository;

    @Autowired
    MarathonRepository marathonRepository;

    @Override
    public List<UserDto> getAll() {
        return repository.findAll(by(ASC, "firstName").and(by(ASC, "lastName"))).stream()
                .map(this::mapEntityToDto)
                .collect(toList());
    }

    @Override
    public List<UserDto> getAllFetchMarathons() {
        return null;
    }

    @Override
    public UserDto getUserByIdFetchMarathons(Long id) {
        return mapEntityToDtoFetchMarathons(repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with given id is not found!")));
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
        return repository.findAllByRoleOrderByFirstNameAscLastNameAsc(role).stream()
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
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new UserNotFoundException("User with given id is not found!");
        }

    }

    private UserDto mapEntityToDtoFetchMarathons(User entity) {
        var dto = new UserDto();
        BeanUtils.copyProperties(entity, dto);
        entity.getMarathons().forEach(
                m -> dto.getMarathons().add(new MarathonDto(m.getId(), m.getTitle(), new ArrayList<>()))
        );
        return dto;
    }

    private UserDto mapEntityToDto(User entity) {
        var dto = new UserDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    private void mapDtoToEntity(UserDto dto, User entity) {
        BeanUtils.copyProperties(dto, entity);
        entity.getMarathons().clear();
        dto.getMarathons().forEach(m -> {
            Marathon marathon = marathonRepository.getOne(m.getId());
            entity.getMarathons().add(marathon);
        });
    }
}
