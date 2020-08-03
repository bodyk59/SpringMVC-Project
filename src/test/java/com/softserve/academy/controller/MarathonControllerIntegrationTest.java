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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MarathonControllerIntegrationTest {
    private final MockMvc mockMvc;
    private final MarathonService marathonService;
    private final UserService userService;

    private static UserDto userDto = new UserDto(0, "test@test.com", "test", "test", "123456", User.Role.TRAINEE, new ArrayList<>());
    private static MarathonDto marathonDto = new MarathonDto(0, "Test Marathon", new ArrayList<>());

    @Autowired
    public MarathonControllerIntegrationTest(MockMvc mockMvc,
                                             MarathonService marathonService,
                                             UserService userService) {
        this.mockMvc = mockMvc;
        this.marathonService = marathonService;
        this.userService = userService;
    }

    @BeforeAll
    public void setUp() {
        userDto = userService.createOrUpdateUser(userDto);
        marathonDto = marathonService.createOrUpdate(marathonDto);
    }

    @Test
    public void showAllMarathonsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/marathons"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("marathons"))
                .andExpect(MockMvcResultMatchers.model().attribute("marathons", marathonService.getAll()));
    }

    @Test
    public void showCreateMarathonPageTest() throws Exception {
        MarathonDto marathon = new MarathonDto();
        marathon.setTitle("test");
        mockMvc.perform(MockMvcRequestBuilders.get("/marathons/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("marathonEdit"))
                .andExpect(MockMvcResultMatchers.model().attribute("marathon", marathon))
                .andExpect(MockMvcResultMatchers.model().attribute("allStudents", userService.getAll()));
    }

    @Test
    public void addMarathonTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/marathons/add")
                .param("title", "marathon"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    public void editCurrentMarathonIfItDoesNotExistTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/marathons/edit/-1"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("marathon"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("error"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/marathons"));

    }

    @Test
    public void editCurrentMarathonIfIttExistTest() throws Exception {
        mockMvc.perform(get("/marathons/edit/{id}", marathonDto.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("marathon", marathonService.getMarathonById(marathonDto.getId())))
                .andExpect(model().attribute("allStudents", userService.getAll()))
                .andExpect(view().name("marathonEdit"));

    }

    @Test
    public void deleteMarathonByIdWhenMarathonDoesNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/marathons/delete/-1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("error"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/marathons"));
    }

    @Test
    public void deleteMarathonById() throws Exception {
        MarathonDto marathon = new MarathonDto();
        BeanUtils.copyProperties(marathonDto, marathon, "id");
        mockMvc.perform(MockMvcRequestBuilders.get("/marathons/delete/{id}", marathonService.createOrUpdate(marathon).getId()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/marathons"));
    }
}
