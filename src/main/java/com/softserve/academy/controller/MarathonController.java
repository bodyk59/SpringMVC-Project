package com.softserve.academy.controller;

import com.softserve.academy.dto.MarathonDto;
import com.softserve.academy.exception.MarathonNotFoundException;
import com.softserve.academy.service.MarathonService;
import com.softserve.academy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping(value = {"/", "/marathons"})
public class MarathonController {
    @Autowired
    MarathonService marathonService;

    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(MarathonController.class);

    @GetMapping
    public String showAllMarathons(Model model) {
        logger.info("Show all marathons was called.");
        model.addAttribute("marathons", marathonService.getAll());
        return "marathons";
    }

    @GetMapping("/add")
    public String showCreateMarathonPage(Model model) {
        logger.info("Show Create marathon page was called.");
        if (!model.containsAttribute("marathon")) {
            model.addAttribute("marathon", new MarathonDto());
            logger.info("New marathon was created.");
        }
        model.addAttribute("allStudents", userService.getAll());
        return "marathonEdit";
    }

    @PostMapping("/add")
    public String addMarathon(@Valid @ModelAttribute MarathonDto marathon,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        logger.info("Add marathon was called.");
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.marathon", bindingResult);
            redirectAttributes.addFlashAttribute("marathon", marathon);
            logger.warn("Validation failed, redirection to another page.");
            return "redirect:/marathons" + (marathon.getId() > 0 ? "/edit/" + marathon.getId() : "/add");
        }
        marathonService.createOrUpdate(marathon);
        return "redirect:/marathons";
    }

    @GetMapping("/edit/{id}")
    public String editCurrentMarathon(@PathVariable long id, Model model, RedirectAttributes redirectAttributes) {
        logger.info("Edit current marathon was called!");
        if (!model.containsAttribute("marathon")) {
            try {
                model.addAttribute("marathon", marathonService.getMarathonById(id));
            } catch (MarathonNotFoundException e) {
                redirectAttributes.addFlashAttribute("error", "Marathon with given id doesn`t exist!");
                logger.error("Marathon with given id doesn`t exist, redirection to another page.");
                return "redirect:/marathons";
            }
        }
        model.addAttribute("allStudents", userService.getAll());
        return "marathonEdit";
    }

    @GetMapping("/delete/{id}")
    public String deleteMarathonById(@PathVariable long id, RedirectAttributes redirectAttributes) {
        logger.info("Delete marathon by id was called!");
        try {
            marathonService.deleteMarathonById(id);
        } catch (MarathonNotFoundException e) {
            logger.error("Marathon with given id doesn`t exist, redirection to another page.");
            redirectAttributes.addFlashAttribute("error", String.format("No marathon with %d exists!", id));
        }
        return "redirect:/marathons";
    }
}
