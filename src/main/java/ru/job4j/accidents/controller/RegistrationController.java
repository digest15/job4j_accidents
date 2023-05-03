package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.accidents.model.User;
import ru.job4j.accidents.service.RegistrationService;

@Controller
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/registration")
    public String regSave(@ModelAttribute User user, Model model) {
        if (registrationService.userExist(user)) {
            String errorMessage = String.format("User with username - %s already exist!", user.getUsername());
            model.addAttribute("errorMessage", errorMessage);

            return "users/create";
        }

        registrationService.createUser(user);
        return "redirect:/login";
    }

    @GetMapping("/registration")
    public String regPage() {
        return "users/create";
    }

}
