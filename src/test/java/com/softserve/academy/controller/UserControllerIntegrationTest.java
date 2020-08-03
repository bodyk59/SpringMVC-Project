package com.softserve.academy.controller;

import com.softserve.academy.dto.MarathonDto;
import com.softserve.academy.dto.UserDto;
import com.softserve.academy.model.User;
import com.softserve.academy.service.MarathonService;
import com.softserve.academy.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerIntegrationTest {
    private final MockMvc mockMvc;

    private final UserService userService;

    private final MarathonService marathonService;

    private static UserDto userDto = new UserDto(0, "test@test.com", "test", "test", "123456", User.Role.TRAINEE, new ArrayList<>());
    private static MarathonDto marathonDto = new MarathonDto(0, "Test Marathon", new ArrayList<>());

    @Autowired
    public UserControllerIntegrationTest(MockMvc mockMvc, UserService userService, MarathonService marathonService) {
        this.mockMvc = mockMvc;
        this.userService = userService;
        this.marathonService = marathonService;
    }

    @BeforeAll
    public void setUp() {
        userDto = userService.createOrUpdateUser(userDto);
        marathonDto = marathonService.createOrUpdate(marathonDto);
    }

    @Test
    public void showAllUsersPageTest() throws Exception {
        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students"))
                .andExpect(model().attribute("students", userService.getAllByRole(User.Role.TRAINEE)));
    }

    @Test
    public void showMarathonUsersPage() throws Exception {
        mockMvc.perform(get("/students/{id}", marathonDto.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("students"))
                .andExpect(model().attribute("students", marathonDto.getUsers()))
                .andExpect(model().attribute("marathon", marathonDto));
    }

    @Test
    public void showCreateUserPage() throws Exception {
        var user = new UserDto();
        user.setRole(User.Role.TRAINEE);
        mockMvc.perform(get("/students/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("studentEdit"))
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("allMarathons", marathonService.getAll()));
    }

    @Test
    public void deleteUserById() throws Exception {
        var deleteDto = new UserDto();
        BeanUtils.copyProperties(userDto, deleteDto, "id");
        mockMvc.perform(get("/students/delete/{id}", userService.createOrUpdateUser(deleteDto).getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/students"));
    }

    @Test
    public void deleteUserByIdWhenUserNotExist() throws Exception {
        mockMvc.perform(get("/students/delete/-1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("error"))
                .andExpect(view().name("redirect:/students"));
    }

    @Test
    public void showEditUserPageWhenUserNotExist() throws Exception {
        mockMvc.perform(get("/students/edit/-1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("error"))
                .andExpect(view().name("redirect:/students"));
    }

    @Test
    public void showEditUserPageWhenUserExist() throws Exception {
        mockMvc.perform(get("/students/edit/{id}", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", userService.getUserByIdFetchMarathons(userDto.getId())))
                .andExpect(model().attribute("allMarathons", marathonService.getAll()))
                .andExpect(view().name("studentEdit"));
    }

    public void createUserWithInputErrors() throws Exception {
        mockMvc.perform(
                post("/students/update")
                        .param("email", "")
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("password", "")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeHasFieldErrors(
                        "org.springframework.validation.BindingResult.user", "email"
                ))
                .andExpect(view().name("redirect:/students/add"));
    }
}