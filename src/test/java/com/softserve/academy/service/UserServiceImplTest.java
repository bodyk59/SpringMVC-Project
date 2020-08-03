package com.softserve.academy.service;

import com.softserve.academy.dto.UserDto;
import com.softserve.academy.exception.UserNotFoundException;
import com.softserve.academy.model.Marathon;
import com.softserve.academy.model.User;
import com.softserve.academy.repository.MarathonRepository;
import com.softserve.academy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserService service;

    @MockBean
    UserRepository userRepository;

    @MockBean
    MarathonRepository marathonRepository;

    @Test
    public void whenGetAll_ThanShouldBeReturnedListOfAllUsersDto() {
        var user = new User();
        user.setId(1);
        var user2 = new User();
        user2.setId(2);

        when(userRepository.findAll(any(Sort.class))).thenReturn(List.of(user, user2));

        var userDto = new UserDto();
        userDto.setId(1);
        var userDto2 = new UserDto();
        userDto2.setId(2);

        assertThat(service.getAll(), is(List.of(userDto, userDto2)));
    }

    @Test
    public void whenUserNotPresent_ThanGetUserByIdFetchMarathonsShouldThrowException() {
        assertThrows(UserNotFoundException.class, () -> service.getUserByIdFetchMarathons(100L));
    }

    @Test
    public void whenUserIsPresent_ThanGetUserByIdFetchMarathonsShouldReturnUserDto() {
        var user = new User();
        user.setId(1);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        var userDto = new UserDto();
        userDto.setId(1);

        assertThat(service.getUserByIdFetchMarathons(1L), is(userDto));
    }

    @Test
    public void whenUserDataNotValid_ThanCreateOrUpdateUserShouldThrowRuntimeException() {
        assertThrows(RuntimeException.class, () -> service.createOrUpdateUser(new UserDto()));
    }

    @Test
    public void whenUserDataValid_ThanRepositorySaveMethodShouldBeInvoked() {
        var user = new User(0, "pratsundv@gmail.com", "Dmitry", "Pratsun", "123456", User.Role.TRAINEE, new HashSet<>(), new HashSet<>());
        when(userRepository.save(any(User.class))).thenReturn(user);

        service.createOrUpdateUser(new UserDto(0, "pratsundv@gmail.com", "Dmitry", "Pratsun", "123456", User.Role.TRAINEE, new ArrayList<>()));

        verify(userRepository, atLeastOnce()).save(user);
    }

    @Test
    public void whenGetAllByRole_ThanFindAllByRoleOrderByFirstNameAscLastNameAscShouldBeInvoked() {
        service.getAllByRole(User.Role.TRAINEE);

        verify(userRepository, atLeastOnce()).findAllByRoleOrderByFirstNameAscLastNameAsc(User.Role.TRAINEE);
    }

    @Test
    public void whenUserDelete_ThanDeleteByIdShouldBeInvoked() {
        service.userDelete(1L);

        verify(userRepository, atLeastOnce()).deleteById(1L);
    }

    @Test
    public void whenAddUserToMarathon_ThanSaveShouldBeInvoked() {
        var user = new User();
        var marathon = new Marathon(1, "Test", new HashSet<>(), new ArrayList<>());
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(marathonRepository.getOne(marathon.getId())).thenReturn(marathon);

        service.addUserToMarathon(new UserDto(), marathon);

        verify(marathonRepository, atLeastOnce()).getOne(marathon.getId());
        verify(userRepository, atLeastOnce()).save(user);
        assertTrue(marathon.getUsers().contains(user));
        assertTrue(user.getMarathons().contains(marathon));
    }
}