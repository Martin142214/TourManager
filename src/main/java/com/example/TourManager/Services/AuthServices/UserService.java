package com.example.TourManager.Services.AuthServices;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.TourManager.Models.EntityModels.Privilege;
import com.example.TourManager.Models.EntityModels.Role;
import com.example.TourManager.Models.EntityModels.User;
import com.example.TourManager.Repositories.AuthRepositories.PrivilegeRepo;
import com.example.TourManager.Repositories.AuthRepositories.RoleRepo;
import com.example.TourManager.Repositories.AuthRepositories.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo _userRepository;

    @Autowired
    private PrivilegeRepo _privilegeRepository;

    @Autowired
    private RoleRepo _roleRepository;

    @Autowired
    public UserService() {
        
    }

    public boolean emailExist(String email){
        User user = _userRepository.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }

    public boolean usernameExist(String username){
        User user = _userRepository.findByUsername(username);
        if (user != null) {
            return true;
        }
        return false;
    }

    public Privilege createPrivilegeIfNotFound(String name) {
 
        Privilege privilege = _privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            _privilegeRepository.save(privilege);
        }
        return privilege;
    }

    public Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
 
        Role role = _roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            _roleRepository.save(role);
        }
        return role;
    }

    public User getCurrentUser(){
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = _userRepository.findByEmail(user.getName());
        return currentUser;
    }

    public boolean hasUserLoggedIn(){
        if (this.getCurrentUser() != null) {
            return true;
        }
        return false;
    }


    public User getAdminUser() {
        var adminUserEntity = _userRepository.findAll()
                                             .stream()
                                             .filter(user -> user.roles.stream().allMatch(role -> role.name.equals("ROLE_ADMIN")))
                                             .findAny().get();
        return adminUserEntity;                                     
    }

    public Boolean isAdmin(){
        User userEntity = this.getCurrentUser();
        if (userEntity == null) {
            return false;
        }
        else {
            for (Role role : userEntity.roles) {
                if (role.name.equals("ROLE_ADMIN")) {
                    return true;
                }
                else if (role.name.equals("ROLE_USER")){
                    return false;
                }
            }
        }
        return false;
    }

    public Collection<User> getAllUsers(){
        var users = _userRepository.findAll()
                                   .stream()
                                   .filter(user -> user.roles.stream().allMatch(role -> role.name.equals("ROLE_USER")))
                                   .collect(Collectors.toList()); 
        return users;                                  
    }
}
