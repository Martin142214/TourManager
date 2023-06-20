package com.example.TourManager.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.TourManager.Services.AuthServices.UserService;

@Controller
@RequestMapping(value = "/areas")
public class AreasController {

    @Autowired
    private UserService _userService;

    @Autowired
    public AreasController() {
        
    }

    @GetMapping("/historical")
    public String returnHistoricalAreasPage(Model model){
        model.addAttribute("isSignedIn", _userService.hasUserLoggedIn());
        if (_userService.getCurrentUser() == null) {
            model.addAttribute("hasAnyFavorites", false);
        }
        else {
            model.addAttribute("hasAnyFavorites", !_userService.getCurrentUser().favoriteLandmarks.isEmpty());
            model.addAttribute("isAdmin", _userService.isAdmin());
            model.addAttribute("wantToVisitDestinations", _userService.getCurrentUser().destinations.stream().filter(dest -> dest.userWantToVisit == true).findAny().isPresent());
            model.addAttribute("wasOnDestinations", _userService.getCurrentUser().destinations.stream().filter(dest -> dest.userWasHere == true).findAny().isPresent());
        }
        return "bulgarian_historical_areas_page.html";
    }

    @GetMapping("/natural")
    public String returnNaturalAreasPage(Model model){
        model.addAttribute("isSignedIn", _userService.hasUserLoggedIn());
        if (_userService.getCurrentUser() == null) {
            model.addAttribute("hasAnyFavorites", false);
        }
        else {
            model.addAttribute("hasAnyFavorites", !_userService.getCurrentUser().favoriteLandmarks.isEmpty());
            model.addAttribute("isAdmin", _userService.isAdmin());
            model.addAttribute("wantToVisitDestinations", _userService.getCurrentUser().destinations.stream().filter(dest -> dest.userWantToVisit == true).findAny().isPresent());
            model.addAttribute("wasOnDestinations", _userService.getCurrentUser().destinations.stream().filter(dest -> dest.userWasHere == true).findAny().isPresent());
        }
        return "bulgarian_natural_areas_page.html";
    }
}
