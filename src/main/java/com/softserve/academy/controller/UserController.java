package com.softserve.academy.controller;

import com.softserve.academy.dto.UserDto;
import com.softserve.academy.exception.UserNotFoundException;
import com.softserve.academy.model.User;
import com.softserve.academy.service.MarathonService;
import com.softserve.academy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    MarathonService marathonService;

    @GetMapping("/students")
    public String showAllUsersPage(Model model) {
        model.addAttribute(
                "students",
                userService.getAllByRole(User.Role.TRAINEE)
        );
        return "students";
    }

    @GetMapping("/students/{id}")
    public String showMarathonUsersPage(@PathVariable long id, Model model) {
        var marathon = marathonService.getMarathonById(id);
        model.addAttribute("students", marathon.getUsers());
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
        model.addAttribute("allMarathons", marathonService.getAll());
        return "studentEdit";
    }

    @GetMapping("/students/edit/{id}")
    public String showEditUserPage(@PathVariable long id, Model model, RedirectAttributes redirectAttributes) {
        if (!model.containsAttribute("user")) {
            try {
                model.addAttribute("user", userService.getUserByIdFetchMarathons(id));
            } catch (UserNotFoundException e) {
                redirectAttributes.addFlashAttribute("error", "User with given id not found!");
                return "redirect:/students";
            }
        }
        model.addAttribute("allMarathons", marathonService.getAll());
        return "studentEdit";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteUserById(@PathVariable long id, RedirectAttributes redirectAttributes) {
        try {
            userService.userDelete(id);
        } catch (EmptyResultDataAccessException e) {
            redirectAttributes.addFlashAttribute("error", "No user with id " + id + " exists!");
        }
        return "redirect:/students";
    }

    @GetMapping("/students/{mid}/delete/{id}")
    public String removeUserFromMarathon(@PathVariable long id, @PathVariable long mid) {
        marathonService.removeFromMarathon(id, mid);
        return "redirect:/students/" + mid;
    }

    @PostMapping("/students/update")
    public String createOrUpdateUser(@Valid @ModelAttribute UserDto user, BindingResult result,
                                     RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/students/" + (user.getId() > 0 ? "edit/" + user.getId() : "add");
        }
        userService.createOrUpdateUser(user);
        return "redirect:/students";
    }
}
