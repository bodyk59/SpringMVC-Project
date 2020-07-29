package com.softserve.academy.service;

import com.softserve.academy.dto.UserDto;
import com.softserve.academy.model.Marathon;
import com.softserve.academy.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();
    List<UserDto> getAllFetchMarathons();

    UserDto getUserByIdFetchMarathons(Long id);

    UserDto createOrUpdateUser(UserDto dto);

    List<UserDto> getAllByRole(User.Role role);

    boolean addUserToMarathon(UserDto user, Marathon marathon);

    void userDelete(Long id);
}
