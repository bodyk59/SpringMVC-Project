package com.softserve.academy.controller;

import com.softserve.academy.dto.MarathonDto;
import com.softserve.academy.dto.UserDto;
import com.softserve.academy.exception.MarathonNotFoundException;
import com.softserve.academy.service.MarathonService;
import com.softserve.academy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = {"/", "/marathons"})
public class MarathonController {
    @Autowired
    MarathonService marathonService;

    @Autowired
    UserService userService;

    @GetMapping()
    public String showAllMarathons(Model model) {
        model.addAttribute("marathons", marathonService.getAll());
        return "marathons";
    }

    @GetMapping("/add")
    public String showCreateMarathonPage(Model model) {
        if (!model.containsAttribute("marathon")) {
            model.addAttribute("marathon", new MarathonDto());
        }
        model.addAttribute("allStudents", userService.getAll());
        return "marathonEdit";
    }

    @PostMapping("/add")
    public String addMarathon(@Valid @ModelAttribute("marathon") MarathonDto marathon,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.marathon", bindingResult);
            redirectAttributes.addFlashAttribute("marathon", marathon);
            return "redirect:/marathons" + (marathon.getId() > 0 ? "/edit/" + marathon.getId() : "/add");
        }

        marathonService.createOrUpdate(marathon);
        return "redirect:/marathons";
    }

    @GetMapping("/edit/{id}")
    public String editCurrentMarathon(@PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        if (!model.containsAttribute("marathon")) {
            try {
                model.addAttribute("marathon", marathonService.getMarathonById(id));
            } catch (MarathonNotFoundException e) {
                redirectAttributes.addFlashAttribute("error", "Marathon with given id doesn`t exist!");
                return "redirect:/marathons";
            }
        }
        model.addAttribute("allStudents", userService.getAll());
        return "marathonEdit";
    }

    @GetMapping("/delete/{id}")
    public String deleteMarathonById(@PathVariable("id") long id) {
        marathonService.deleteMarathonById(id);
        return "redirect:/marathons";
    }
}
