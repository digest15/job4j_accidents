package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.AccidentService;

@Controller
@RequestMapping("/accident")
@AllArgsConstructor
public class AccidentController {

    private final AccidentService accidentService;

    @GetMapping("/create")
    public String viewCreateAccident(Model model) {
        model.addAttribute("types", accidentService.listTypes());
        return "accident/createAccident";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Accident accident) {
        accidentService.add(accident);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String edit(Model model, @PathVariable int id) {
        model.addAttribute("accident", accidentService.findById(id));
        model.addAttribute("types", accidentService.listTypes());
        return "accident/editAccident";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Accident accident) {
        accidentService.update(accident);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        accidentService.delete(id);
        return "redirect:/";
    }
}
