package by.nosevich.carrental.controllers;

import by.nosevich.carrental.dto.CarDto;
import by.nosevich.carrental.service.car.CarService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomePageController {

    public static final String HOME_PAGE = "home";
    public static final String REDIRECT_ON_HOME_PAGE = "redirect:/home";
    public static final String REDIRECT_WITH_LANG_FORMAT = "redirect:%s?lang=%s";
    public static final String REFERER_HEADER_NAME = "Referer";
    public static final String LATEST_CAR_ATTRIBUTE = "latestCar";
    public static final String RU_LANG = "ru";
    public static final String RENTAL_TERMS_RU_PAGE = "rentTerms/rentTermsRu";
    public static final String RENTAL_TERMS_PAGE = "rentTerms/rentTerms";

    private final CarService carService;
    private final LocaleResolver localeResolver;

    @Autowired
    public HomePageController(CarService carService, LocaleResolver localeResolver) {
        this.carService = carService;
        this.localeResolver = localeResolver;
    }

    @GetMapping("/home")
    public String getHomePage(Model model) {
        CarDto latestCar = carService.getLatestAddedCar();
        model.addAttribute(LATEST_CAR_ATTRIBUTE, latestCar);
        return HOME_PAGE;
    }

    @GetMapping("/changeLang")
    public String changeLanguage(HttpServletRequest req, @RequestParam(name = "lang") String lang) {
        String referer = req.getHeader(REFERER_HEADER_NAME);
        String urlForRedirect = referer.contains("?") ? referer.substring(0, referer.indexOf('?')) : referer;
        return String.format(REDIRECT_WITH_LANG_FORMAT, urlForRedirect, lang);
    }

    @GetMapping("")
    public String getRedirectOnHomePage() {
        return REDIRECT_ON_HOME_PAGE;
    }

    @GetMapping("/rentterms")
    public String getRentTermsPage(HttpServletRequest request) {
        if (localeResolver.resolveLocale(request).getLanguage().equals(RU_LANG)) {
            return RENTAL_TERMS_RU_PAGE;
        }
        return RENTAL_TERMS_PAGE;
    }
}
