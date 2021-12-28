package by.nosevich.carrental.controllers;

import by.nosevich.carrental.entities.Car;
import by.nosevich.carrental.service.car.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {

    @Autowired
    private CarService carService;

    @GetMapping("/home")
    public String getHomePage(Model model) {
        Car latestCar = carService.getLatest();
        model.addAttribute("latestCar", latestCar);
        return "home";
    }

    @GetMapping("")
    public String getRedirectOnHomePage(Model model) {
        return "redirect:/home";
    }
}
