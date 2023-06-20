package com.example.TourManager.Controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.example.TourManager.Models.ClassModels.Destination;
import com.example.TourManager.Models.EntityModels.Landmark;
import com.example.TourManager.Models.EntityModels.Privilege;
import com.example.TourManager.Models.EntityModels.Role;
import com.example.TourManager.Models.EntityModels.User;
import com.example.TourManager.Models.Enums.TypesOfLandmark;
import com.example.TourManager.Repositories.AuthRepositories.RoleRepo;
import com.example.TourManager.Repositories.AuthRepositories.UserRepo;
import com.example.TourManager.Services.LandmarkService;
import com.example.TourManager.Services.AuthServices.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private LandmarkService _landmarkService;

    @Autowired
    private UserService _userService;

    @Autowired
    private UserRepo _userRepository;

    @Autowired
    private RoleRepo _roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String mainControllerUrl = "http://localhost:8080";

    public AdminController() {
        
    }

    @GetMapping()
    public String adminPage(Model model){

        model.addAttribute("historicalEntities", _landmarkService.GetAll()
                                                                 .stream()
                                                                 .filter(landmark -> landmark.type.equals(TypesOfLandmark.Historical))
                                                                 .collect(Collectors.toList()));

        model.addAttribute("naturalEntities", _landmarkService.GetAll()
                                                                 .stream()
                                                                 .filter(landmark -> landmark.type.equals(TypesOfLandmark.Natural))
                                                                 .collect(Collectors.toList()));
        model.addAttribute("isAdmin", _userService.isAdmin());
        model.addAttribute("users", _userService.getAllUsers());
        model.addAttribute("admin", _userService.getAdminUser());
        model.addAttribute("hasAnyFavorites", !_userService.getCurrentUser().favoriteLandmarks.isEmpty());
        model.addAttribute("wantToVisitDestinations", _userService.getCurrentUser().destinations.stream().filter(dest -> dest.userWantToVisit == true).findAny().isPresent());
        model.addAttribute("wasOnDestinations", _userService.getCurrentUser().destinations.stream().filter(dest -> dest.userWasHere == true).findAny().isPresent());
        return "/auth_pages/admin_page.html";
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RedirectView processRegister(String username, String email, String password, HttpServletRequest request) {

        Privilege readPrivilege
          = _userService.createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
          = _userService.createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
        _userService.createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        _userService.createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        Role adminRole = _roleRepository.findByName("ROLE_ADMIN");
        User user = new User();
        user.username = username;
        user.password = passwordEncoder.encode(password);
        user.email = email;
        user.roles = Arrays.asList(adminRole);
        user.favoriteLandmarks = new ArrayList<Landmark>();
        user.destinations = new ArrayList<Destination>();
        user.enabled = true;
        _userRepository.save(user);

        return redirectView(mainControllerUrl + "/user/login");
    }

    public RedirectView redirectView(String url){
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        return redirectView;
    }
}
