package com.example.TourManager.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.TourManager.Services.LandmarkService;
import com.example.TourManager.Services.AuthServices.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MainController {

    @Autowired
    private UserService _userService;

    @Autowired
    private LandmarkService _landmarkService;
    
    public MainController() {
        
    }

    @GetMapping("/")
    //@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public String viewHomePage(HttpServletRequest request, Model model) {
        
        model.addAttribute("isSignedIn", _userService.hasUserLoggedIn());
        model.addAttribute("landmarks", _landmarkService.GetAll());

        if (_userService.hasUserLoggedIn()) {
            model.addAttribute("isAdmin", _userService.isAdmin()); 
            model.addAttribute("hasAnyFavorites", !_userService.getCurrentUser().favoriteLandmarks.isEmpty());
            model.addAttribute("wantToVisitDestinations", _userService.getCurrentUser().destinations.stream().filter(dest -> dest.userWantToVisit == true).findAny().isPresent());
            model.addAttribute("wasOnDestinations", _userService.getCurrentUser().destinations.stream().filter(dest -> dest.userWasHere == true).findAny().isPresent());     
        }
        
        return "main_page.html";
    }

    
}
