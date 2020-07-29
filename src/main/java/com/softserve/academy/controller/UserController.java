package com.softserve.academy.controller;

import com.softserve.academy.dto.MarathonDto;
import com.softserve.academy.dto.UserDto;
import com.softserve.academy.exception.UserNotFoundException;
import com.softserve.academy.model.User;
import com.softserve.academy.service.MarathonService;
import com.softserve.academy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    MarathonService marathonService;

    @GetMapping("/students")
    public String showAllUsersPage(Model model) {
        sortStudentsAndAddToModel(model, userService.getAllByRole(User.Role.TRAINEE).stream());
        return "students";
    }

    @GetMapping("/students/{id}")
    public String showMarathonUsersPage(@PathVariable("id") long id, Model model) {
        var marathon = marathonService.getMarathonById(id);
        sortStudentsAndAddToModel(model, marathon.getUsers().stream());
        model.addAttribute("marathon", marathon);
        return "students";
    }

    @GetMapping("/students/add")
    public String showCreateUserPage(Model model) {
        if (!model.containsAttribute("user")) {
            var user = new UserDto();
            user.setRole(User.Role.TRAINEE);
            model.addAttribute("user", user);
        }
        sortMarathonsAddAddToModel(model);
        return "studentEdit";
    }

    @GetMapping("/students/edit/{id}")
    public String showEditUserPage(@PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        if (!model.containsAttribute("user")) {
            try {
                model.addAttribute("user", userService.getUserByIdFetchMarathons(id));
            } catch (UserNotFoundException e) {
                redirectAttributes.addFlashAttribute("error", "User with given id not found!");
                return "redirect:/students";
            }
        }
        sortMarathonsAddAddToModel(model);
        return "studentEdit";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteUserById(@PathVariable("id") long id) {
        userService.userDelete(id);
        return "redirect:/students";
    }

    @GetMapping("/students/{mid}/delete/{id}")
    public String removeUserFromMarathon(@PathVariable("id") long id, @PathVariable("mid") long mid) {
        marathonService.removeFromMarathon(id, mid);
        return "redirect:/students/" + mid;
    }

    @PostMapping("/students/update")
    public String createOrUpdateUser(@Valid @ModelAttribute("user") UserDto user, BindingResult result,
                                     RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/students/" + (user.getId() > 0 ? "edit/" + user.getId() : "add");
        }
        userService.createOrUpdateUser(user);
        return "redirect:/students";
    }

    private void sortMarathonsAddAddToModel(Model model) {
        model.addAttribute("allMarathons", marathonService.getAll().stream()
                .sorted(comparing(MarathonDto::getTitle))
                .collect(toList()));
    }

    private void sortStudentsAndAddToModel(Model model, Stream<UserDto> userDtoStream) {
        model.addAttribute("students", userDtoStream
                .sorted(comparing(UserDto::getFirstName).thenComparing(UserDto::getLastName))
                .collect(toList()));
    }
}
