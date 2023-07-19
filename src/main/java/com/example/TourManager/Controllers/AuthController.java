package com.example.TourManager.Controllers;

import java.util.ArrayList;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.example.TourManager.Models.ClassModels.Destination;
import com.example.TourManager.Models.EntityModels.Landmark;
import com.example.TourManager.Models.EntityModels.User;
import com.example.TourManager.Repositories.AuthRepositories.RoleRepo;
import com.example.TourManager.Repositories.AuthRepositories.UserRepo;
import com.example.TourManager.Services.AuthServices.UserService;

@Controller
@RequestMapping(value = "/user")
public class AuthController {

    @Autowired
    private UserService _userService;

    @Autowired
    private UserRepo _userRepository;

    @Autowired
    private RoleRepo _roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String mainControllerUrl = "http://localhost:8080";

    @Autowired
    public AuthController() {

    }

    /* 
    @GetMapping("/login")
    public String login(){
        return "/auth_pages/login.html";
    }

    @GetMapping("/register")
    public String register(){
        return "/auth_pages/register.html";
    } */

    @GetMapping("/register")
    /*@PreAuthorize("!hasRole('ROLE_ADMIN') AND !hasRole('ROLE_USER')")*/
    public String showRegistrationForm(Model model) {
     
        return "/auth_pages/register.html";
    }  
    
    @PostMapping("/register")
    public RedirectView registerNewUserAccount(String username, String email, String password) throws Exception {
 
        if (_userService.emailExist(email)) {
            throw new Exception
              ("There is an account with that email adress: " + email);
        }
        else if(_userService.usernameExist(username)){
            throw new Exception
              ("There is an account with that username: " + username);
        }
        User user = new User(username, email, passwordEncoder.encode(password), new ArrayList<Destination>(), new ArrayList<Landmark>(), true, Arrays.asList(_roleRepository.findByName("ROLE_USER")));
 
        _userRepository.save(user);
        return redirectView(mainControllerUrl + "/user/login");
    }

    
    @GetMapping("/login")
    public String showLoginForm(Model model) {
     
        return "/auth_pages/login.html";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RedirectView deleteUser(@PathVariable String id) {
        var userEntity = _userRepository.findById(id);
        if (userEntity.isPresent()) {
            //_userRepository.delete(userEntity.get());
        }

        return redirectView(mainControllerUrl + "/admin");
    }

    public RedirectView redirectView(String url){
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        return redirectView;
    }
}
