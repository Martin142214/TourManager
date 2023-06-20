package com.example.TourManager.Services.AuthServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.TourManager.Models.EntityModels.Privilege;
import com.example.TourManager.Models.EntityModels.Role;
import com.example.TourManager.Models.EntityModels.User;
import com.example.TourManager.Repositories.AuthRepositories.RoleRepo;
import com.example.TourManager.Repositories.AuthRepositories.UserRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepo _userRepository;
 
    @Autowired
    private RoleRepo _roleRepository;

   public UserDetailsServiceImpl() {
    
   }

    @Override
    public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
 
        User user = _userRepository.findByUsername(username);
        if (user == null) {
            return new org.springframework.security.core.userdetails.User(
              " ", " ", true, true, true, true, 
              getAuthorities(Arrays.asList(
                _roleRepository.findByName("ROLE_USER"))));
        }
        
        return new org.springframework.security.core.userdetails.User(
          user.email, user.password, user.isEnabled(), true, true, 
          true, getAuthorities(user.roles));
    }

    public Collection<? extends GrantedAuthority> getAuthorities(
      Collection<Role> roles) {
 
        return getGrantedAuthorities(getPrivileges(roles));
    }

    public List<String> getPrivileges(Collection<Role> roles) {
 
        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            privileges.add(role.name);
            collection.addAll(role.privileges);
        }
        for (Privilege item : collection) {
            privileges.add(item.name);
        }
        return privileges;
    }

    public List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
